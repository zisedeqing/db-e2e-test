package framework;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

// Thread-safe file writer
public class ResultWriter {
    private String filePath_;
    private OutputStreamWriter writer_;

    public ResultWriter(@NotNull String filePath) {
        this.filePath_ = filePath;
    }

    public void writeLine(@NotNull String str) throws ExecException {
        write(str + "\n");
    }

    public void writeNewLine(@NotNull String str) throws ExecException {
        write("\n" + str);
    }

    private FileOutputStream createFileOutputStream() throws IOException {
        while (true) {
            try {
                return new FileOutputStream(filePath_);
            } catch (FileNotFoundException e) {
                File file = new File(filePath_);
                if (file.getParentFile() != null && !file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                    continue;
                }
                throw e;
            }
        }
    }

    public synchronized void write(@NotNull String str) throws ExecException {
        try {
            if (writer_ == null) {
                FileOutputStream fos = createFileOutputStream();
                writer_ = new OutputStreamWriter(fos, "UTF-8");
            }
            if (str == null || str.isEmpty()) return;

            writer_.write(str);
            writer_.flush();
        } catch (IOException e) {
            throw new ExecException(e);
        }
    }

    public void close() throws ExecException {
        try {
            if (writer_ != null)
                writer_.close();
        } catch (IOException e) {
            throw new ExecException(e);
        }
    }
}
