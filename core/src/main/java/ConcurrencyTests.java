import com.google.common.base.Joiner;
import framework.ConnectionOptionsManager;
import framework.ExecException;
import framework.TestCaseStatistic;
import framework.TestScript;
import framework.cli.ConnectorConfigLoader;
import framework.cli.ConnectorConfiguration;
import framework.cli.EndpointFormatter;
import framework.connection.DbConnector;
import framework.connection.Endpoint;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ConcurrencyTests {
    private static Logger LOGGER = LogManager.getLogger(ConcurrencyTests.class);
    private static String SCRIPT_SUFFIX = "sql";
    private static String RESULT_SUFFIX = "result";
    private static String DIFF_SUFFIX = "diff";

    private static final String ENV_ENDPOINTS = "ENDPOINTS";
    private static final String ENV_WORK_DIR = "WORK_DIR";
    private static final String ENV_DATABASE = "DATABASE";
    private static final String ENV_USER = "USER";
    private static final String ENV_PASSWORD = "PASSWORD";
    private static final String ENV_TEST_CASE = "TEST_CASE";

    private String baseDir_;
    private String scriptBaseDir_;
    private String resultOutputBaseDir_;
    private String expectResultBaseDir_;
    private String resultDiffBaseDir_;
    private String originResultBaseDir_;

    private String globalHookFile;
    private String globalEnvFile;
    private String connectorFile_;

    private String givenTestFile_;
    private List<String> givenTestCases_;

    private String[] testSuiteFileNames_ = null;

    private HashSet<String> skipTestSet_ = new HashSet<>();

    private List<String> customerEnvs_ = new ArrayList<>();

    private List<Endpoint> endpoints_ = new ArrayList<>();
    private DbConnector connector_;

    private boolean skipErrorScripts_ = false;
    private boolean exitFailedScripts_ = false;

    private int failedTestCases_ = 0;
    private boolean printProgress = false;
    private int printProgressInterval = 10; // seconds
    protected Progress progress = null;

    public ConcurrencyTests parseArgs(String args[]) {
        Options options = new Options();

        options.addOption("h", "help", false, "print help info");
        options.addOption("c", "connector", true,
                "connector config file; default is work-dir/connector.properties");

        options.addOption("d", "work-dir", true, "[Required] workspace directory");
        options.addOption("t", "test-case", true, "only run the given test case");
        options.addOption("e", "env", true, "custom environment variables, format: name=value");

        options.addOption("1", "trace", false, "enable log trace");
        options.addOption("2", "debug", false, "enable log debug");

        options.addOption("S", "skip-on-error", false, "skip error test case, default is false");
        options.addOption("E", "exit-on-failed", false, "exit test if found any failed test case, default is false");
        options.addOption("3", "progress", false, "print progress for each script");
        options.addOption("4", "progress-interval", true, "print progress interval, default is 10s");
        options.addOption("f", "file-of-test", true, "test case file, default is all files in the given directory");
        options.addOption("s", "skip-tests-file", true, "skip tests listed in this file");

        HelpFormatter hf = new HelpFormatter();
        hf.setWidth(110);
        CommandLineParser parser = new PosixParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption('h')) {
                hf.printHelp(ConcurrencyTests.class.getName(), options, true);
                System.exit(0);
            }
            if (commandLine.hasOption("c"))
                connectorFile_ = commandLine.getOptionValue("c");

            if (commandLine.hasOption("d"))
                baseDir_ = commandLine.getOptionValue("d");
            if (commandLine.hasOption("t")) {
                givenTestFile_ = commandLine.getOptionValue("t");
            }
            if (commandLine.hasOption("f")){
                testSuiteFileNames_ = commandLine.getOptionValues("f");
            }
            // Positional arguments are treated as test cases to run directly,
            // e.g. `... -d ... dql/test_igs_filter_push test_calcite_velox`.
            List<String> positionalArgs = commandLine.getArgList();
            if (positionalArgs != null && !positionalArgs.isEmpty()) {
                givenTestCases_ = new ArrayList<>(positionalArgs);
            }
            if (commandLine.hasOption("s")) {
                String skipTestsFile = commandLine.getOptionValue("s");
                try {
                    skipTestSet_ = new HashSet<>(Files.readAllLines(Paths.get(skipTestsFile)));
                    LOGGER.info("Skip test set: " + Joiner.on(",").join(skipTestSet_));
                } catch (IOException e) {
                    LOGGER.error("", e);
                }
            }

            if (commandLine.hasOption("trace"))
                Configurator.setLevel(LogManager.getRootLogger(), Level.TRACE);
            if (commandLine.hasOption("debug"))
                Configurator.setLevel(LogManager.getRootLogger(), Level.DEBUG);
            if (commandLine.hasOption("e"))
                customerEnvs_.addAll(Arrays.asList(commandLine.getOptionValues("e")));
            if (commandLine.hasOption("progress"))
                printProgress = true;
            if (commandLine.hasOption("progress-interval"))
                printProgressInterval = Integer.valueOf(commandLine.getOptionValue("progress-interval"));

            if (baseDir_ == null) {
                hf.printHelp(ConcurrencyTests.class.getName(), options, true);
                System.exit(1);
            }

            scriptBaseDir_ = baseDir_ + "/sql";
            resultOutputBaseDir_ = baseDir_ + "/output";
            expectResultBaseDir_ = baseDir_ + "/expect";
            resultDiffBaseDir_ = baseDir_ + "/diff";
            originResultBaseDir_ = baseDir_ + "/origin";
            globalHookFile = baseDir_ + "/hook_file";
            globalEnvFile = baseDir_ + "/env_file";
            if (connectorFile_ == null)
                connectorFile_ = baseDir_ + "/connector.properties";
        } catch (Exception e) {
            LOGGER.error("", e);
            hf.printHelp(ConcurrencyTests.class.getName(), options, true);
            System.exit(1);
        }

        ConnectorConfiguration configuration = loadConnectorConfiguration();
        connector_ = configuration.getConnector();
        endpoints_.addAll(configuration.getEndpoints());
        ConnectionOptionsManager.setGlobalOptions(configuration.getConnectionOptions());
        validateEndpoints();

        LOGGER.info("endpoints: " + Joiner.on(",").join(formatEndpoints()));
        LOGGER.info("workspace dir: " + baseDir_);
        return this;
    }

    public static void main(String[] args) throws Exception {
        System.exit(new ConcurrencyTests().parseArgs(args).run());
    }

    public int run() throws Exception {
        mkdirTempDir();
        TestScript executor = createScriptExecutor();

        // list dir, and run each scripts
        List<String> scripts = getScripts();
        Timer timer = new Timer();
        Timer totalTimer = new Timer();
        TestCaseStatistic statistic = new TestCaseStatistic();
        for (String script : scripts) {
            LOGGER.debug("Start test script: " + script);
            timer.reset();
            String scriptName = removeExtension(script);
            String scriptPath = scriptBaseDir_ + "/" + script;
            String outputResult = resultOutputBaseDir_ + "/" + getResultFileName(scriptName);
            String diffFile = resultDiffBaseDir_ + "/" + getDiffFileName(scriptName);
            String outputOriginResult = originResultBaseDir_ + "/" + getResultFileName(scriptName);
            try {
                deleteFile(outputResult);
                deleteFile(diffFile);
                deleteFile(outputOriginResult);

                startProgress(executor, script);
                executor.setEnv(ENV_TEST_CASE, scriptName);
                TestCaseStatistic caseStatistic = executor.executeScript(scriptPath, outputResult, outputOriginResult);
                boolean diffResult = executor.diffResult(
                        expectResultBaseDir_ + "/" + getResultFileName(scriptName),
                        outputResult, diffFile);
                if (!diffResult) {
                    LOGGER.warn(String.format("%s: [FAILED], finished cases: %d, time: %s(s)",
                            scriptName, caseStatistic.getTotalCaseCount(), timer.getFormattedSecond()));
                    failedTestCases_++;
                    if (exitFailedScripts_) System.exit(-1);
                } else
                    LOGGER.info(String.format("%s: [OK], total cases: %d, time: %s(s), total statements: %d",
                            scriptName, caseStatistic.getTotalCaseCount(), timer.getFormattedSecond(),
                            caseStatistic.getTotalStatementCount()));
                statistic.add(caseStatistic);
            } catch (Exception e) {
                LOGGER.error(scriptPath, e);
                if (!skipErrorScripts_) {
                    stopProgress();
                    throw e;
                }
            }
            stopProgress();
            LOGGER.debug("Finish test script: " + script);
        }
        String summary = String.format("Finished test, Total(Successful) test files: %d(%d), " +
                        "Total test cases: %d, TotalStatements: %d, Total time: %s(s)",
                scripts.size(), scripts.size() - failedTestCases_,
                statistic.getTotalCaseCount(),
                statistic.getTotalStatementCount(), totalTimer.getFormattedSecond());
        if (failedTestCases_ == 0)
            LOGGER.info(summary);
        else
            LOGGER.error(summary);
        return failedTestCases_;
    }

    private List<String> getScriptsFromDir(String dirName, String dirPath) {
        File dir = new File(dirPath);
        // file name of test case: test_xxx.sql
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                File tmp = new File(dir, name);
                if (tmp.isDirectory()) return true;
                return !name.startsWith("__") && /* skip hidden test case */
                        name.startsWith("test_") &&
                        name.endsWith("." + SCRIPT_SUFFIX) &&
                        !skipTestSet_.contains(name.substring(0, name.length() - SCRIPT_SUFFIX.length() - 1));
            }
        };

        List<String> scripts = new ArrayList<>();
        for (File file : dir.listFiles(filenameFilter)) {
            if (file.isDirectory()) {
                scripts.addAll(getScriptsFromDir(file.getName(), file.getAbsolutePath()));
                continue;
            }
            if (file.isHidden()) continue;
            if (dirName != null)
                scripts.add(dirName + "/" + file.getName());
            else
                scripts.add(file.getName());
        }
        return scripts;
    }

    private List<String> getScriptsFromTestSuites() throws Exception {
        List<String> scripts = new ArrayList<>();
        Set<String> scriptSet = new HashSet<>();
        for (String testSuiteFileName : testSuiteFileNames_) {
            List<String> subScripts = getScriptsFromFile(testSuiteFileName);
            for (String subScript : subScripts) {
                if (scriptSet.contains(subScript)) {
                    LOGGER.warn("Skipping duplicate test case: " + subScript);
                    continue;
                }
                scriptSet.add(subScript);
                scripts.add(subScript);
            }
        }
        LOGGER.info("Running tests numbers numbers: " + scripts.size() + ", test cases:");
        scripts.forEach(script -> LOGGER.info(script));
        return scripts;
    }

    private List<String> getScriptsFromFile(String testSuiteFileName) throws Exception {
        File file = new File(baseDir_, testSuiteFileName);
        if (!file.exists()) {
            throw new Exception("test file not found: " + testSuiteFileName + " in " + baseDir_ + " directory");
        }

        List<String> scripts = new ArrayList<>();
        Set<String> scriptSet = new HashSet<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("#")) {
                    String caseName = line.substring(1).trim();
                    if (!caseName.isEmpty())
                        LOGGER.warn("Skipping test case: " + caseName);
                    continue;
                }
                if (!skipTestSet_.isEmpty() && skipTestSet_.contains(line)) {
                    LOGGER.warn("Skipping test case: " + line);
                    continue;
                }
                if (scriptSet.contains(line)) {
                    LOGGER.warn("Skipping duplicate test case: " + line);
                    continue;
                }

                String scriptName = line + "." + SCRIPT_SUFFIX;
//                File scriptFile = new File(scriptBaseDir_ + "/" + scriptName);
//                if (!scriptFile.exists()) {
//                    LOGGER.warn("Skipping not exists test case: " + scriptName);
//                    continue;
//                }

                scriptSet.add(line);
                scripts.add(scriptName);
            }
        } catch (IOException e) {
            LOGGER.error("parse " + testSuiteFileName + " failed", e);
        }
        return scripts;
    }

    private List<String> getScripts() throws Exception {
        List<String> scripts;
        if (givenTestFile_ != null) {
            scripts = Arrays.asList(String.format("%s.%s", givenTestFile_, SCRIPT_SUFFIX));
        } else if (givenTestCases_ != null && !givenTestCases_.isEmpty()) {
            scripts = new ArrayList<>();
            Set<String> scriptSet = new HashSet<>();
            for (String testCase : givenTestCases_) {
                if (!skipTestSet_.isEmpty() && skipTestSet_.contains(testCase)) {
                    LOGGER.warn("Skipping test case: " + testCase);
                    continue;
                }
                if (!scriptSet.add(testCase)) {
                    LOGGER.warn("Skipping duplicate test case: " + testCase);
                    continue;
                }
                scripts.add(testCase + "." + SCRIPT_SUFFIX);
            }
            LOGGER.info("Running tests numbers: " + scripts.size() + ", test cases:");
            scripts.forEach(script -> LOGGER.info(script));
        } else if (testSuiteFileNames_ != null && testSuiteFileNames_.length > 0) {
            scripts = getScriptsFromTestSuites();
        } else
            scripts = getScriptsFromDir(null, scriptBaseDir_);
        return scripts;
    }

    private static String removeExtension(String script) {
        int pos = script.lastIndexOf('.');
        if (pos > -1)
            return script.substring(0, pos);
        return script;
    }
    private static String getResultFileName(String scriptName) {
        return scriptName + "." + RESULT_SUFFIX;
    }

    private static String getDiffFileName(String scriptName) {
        return scriptName + "." + DIFF_SUFFIX;
    }

    private void deleteFile(String file) {
        new File(file).delete();
    }

    private void mkdirTempDir() {
        File file = new File(resultOutputBaseDir_);
        if (!file.exists())
            file.mkdir();
        file = new File(resultDiffBaseDir_);
        if (!file.exists())
            file.mkdir();
        file = new File(originResultBaseDir_);
        if (!file.exists())
            file.mkdir();
    }

    private Map<String, String> setEnvironment() throws ExecException {
        Map<String, String> defaultEnvs = new HashMap<>(System.getenv());
        loadGlobalEnvFile(defaultEnvs);

        // Expose endpoints to shell scripts in the stable order defined by the
        // connector's extraPortNames(), e.g. host:port:hs2=25000:webserver=25001.
        defaultEnvs.put(ENV_ENDPOINTS, Joiner.on(",").join(formatEndpoints()));
        defaultEnvs.put(ENV_WORK_DIR, baseDir_);
        defaultEnvs.put(ENV_DATABASE, valueOrEmpty(
                ConnectionOptionsManager.getGlobalOptions().getDbName()));
        defaultEnvs.put(ENV_USER, valueOrEmpty(
                ConnectionOptionsManager.getGlobalOptions().getUserName()));
        defaultEnvs.put(ENV_PASSWORD, valueOrEmpty(
                ConnectionOptionsManager.getGlobalOptions().getPassword()));

        for (int j = 0; j < customerEnvs_.size(); j++) {
            String pair = customerEnvs_.get(j);
            int idx = pair.indexOf('=');
            if (idx == -1)
                throw new ExecException("invalid environment variable: " + pair);
            defaultEnvs.put(pair.substring(0, idx), pair.substring(idx+1));
        }
        return defaultEnvs;
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    private void loadGlobalEnvFile(Map<String, String> envs) throws ExecException {
        if (!Files.exists(Paths.get(globalEnvFile))) return;

        FileInputStream input = null;
        try {
            input = new FileInputStream(globalEnvFile);
            Properties prop = new Properties();
            prop.load(input);

            for (Map.Entry<Object, Object> entry : prop.entrySet()) {
                envs.put((String)entry.getKey(), (String)entry.getValue());
            }
        } catch (IOException ex) {
            throw new ExecException(ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new ExecException(e);
                }
            }
        }
    }

    private TestScript createScriptExecutor() throws ExecException {
        TestScript executor = new TestScript();
        executor.registerEndpoints(endpoints_);
        executor.setConnector(connector_);
        executor.setEnvs(setEnvironment());

        File hookFile = new File(globalHookFile);
        if (!hookFile.exists() || hookFile.isDirectory()) return executor;
        executor.addGlobalHooks(globalHookFile);
        return executor;
    }

    // Load connector implementation, endpoints, database, credentials, and JDBC
    // options from one properties file.
    private ConnectorConfiguration loadConnectorConfiguration() {
        ConnectorConfiguration configuration = ConnectorConfigLoader.load(connectorFile_);
        LOGGER.info("connector: " + configuration.getConnector().getClass().getName());
        return configuration;
    }

    // Warn if an endpoint carries extra ports unknown to the connector, so
    // typos surface early instead of silently missing port info.
    private void validateEndpoints() {
        List<String> known = connector_.extraPortNames();
        for (Endpoint endpoint : endpoints_) {
            for (String name : endpoint.getExtraPorts().keySet()) {
                if (!known.contains(name))
                    LOGGER.warn("endpoint " + endpoint.getHost() + " has unknown extra port name '"
                            + name + "', known names: " + known);
            }
        }
    }

    // Format endpoints with extra ports ordered by the connector's extraPortNames(),
    // e.g. host:port:hs2=25000:webserver=25001.
    private List<String> formatEndpoints() {
        return EndpointFormatter.format(endpoints_, connector_);
    }

    private void startProgress(TestScript executor, String prefix) throws InterruptedException {
        if (printProgress) {
            stopProgress();
            progress = new Progress(executor, prefix);
            progress.start();
        }
    }

    private void stopProgress() throws InterruptedException {
        if (progress != null) {
            progress.stop_ = true;
            progress.join();
            progress = null;
        }
    }

    class Timer {
        private long startTime_ = System.currentTimeMillis();

        public void reset() {
            startTime_ = System.currentTimeMillis();
        }

        public double getSecond() {
            return getMs()/1000.0;
        }

        public String getFormattedSecond() {
            return String.format("%.3f", getSecond());
        }

        public long getMs() {
            return System.currentTimeMillis() - startTime_;
        }
    }

    class Progress extends Thread {
        TestScript executor_;
        String prefix_;
        boolean stop_ = false;

        public Progress(TestScript executor, String prefix) {
            setName("progress");
            executor_ = executor;
            prefix_ = prefix;
        }

        @Override
        public void run() {
            Timer timer = new Timer();
            while (!stop_) {
                if (timer.getSecond() >= printProgressInterval) {
                    int p = executor_.getProgress();
                    LOGGER.info(String.format("%s [%d%%]", prefix_, p));
                    timer.reset();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.warn("", e);
                }
            }
        }
    }
}
