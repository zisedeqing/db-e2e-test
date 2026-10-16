package framework.statement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractHookStatement extends IHookStatement {
    public static String EXTRACT_IDENTIFIER = "-- extract";
    private String extractRegexp_;

    public ExtractHookStatement(int lineNumber) {
        super(lineNumber, HookType.HT_RESULT);
    }

    public static boolean isExtractHookStatement(String comment) {
        return comment.startsWith(EXTRACT_IDENTIFIER);
    }

    @Override
    protected String getHookIndent() {
        return EXTRACT_IDENTIFIER;
    }

    @Override
    protected String getHookRule() {
        return extractRegexp_;
    }

    @Override
    public String apply(String line) {
        if (line == null) return null;
        Pattern pattern = Pattern.compile(extractRegexp_);
        Matcher matcher = pattern.matcher(line);

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        while (matcher.find()) {
            if (!first)
                sb.append("\n");
            else
                first = false;
            sb.append(matcher.group());
        }
        return sb.toString();
    }

    @Override
    public IHookStatement parserHook(String comment) {
        String tmp = comment.substring(EXTRACT_IDENTIFIER.length()).trim();
        tmp = initName(tmp);
        extractRegexp_ = tmp.substring(1);
        return this;
    }
}
