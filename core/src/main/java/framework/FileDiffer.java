package framework;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Chunk;
import com.github.difflib.patch.DeltaType;
import com.github.difflib.patch.Patch;

public class FileDiffer {
    private String expectFile_;
    private String targetFile_;

    public FileDiffer(String expectFile, String targetFile) {
        this.expectFile_ = expectFile;
        this.targetFile_ = targetFile;
    }

    public boolean diff(String diffOutput) throws ExecException, DiffException {
        List<String> expectContent = readResultFile(expectFile_);
        List<String> targetContent = readResultFile(targetFile_);

        Patch<String> diff = DiffUtils.diff(expectContent, targetContent);
        List<AbstractDelta<String>> deltas = diff.getDeltas();
        if (deltas.isEmpty()) return true;

        boolean result = true;
        ResultWriter writer = null;
        for (AbstractDelta<String> delta : deltas) {
            if (delta.getType() != DeltaType.EQUAL && writer == null) {
                writer = new ResultWriter(diffOutput);
                writeDiffHeader(writer);
            }
            switch (delta.getType()) {
                case INSERT:
                    // new line
                    result = false;
                    Chunk<String> insert = delta.getTarget();
                    int position = insert.getPosition();
                    writer.writeLine("+ " + (position + 1) + " " + insert.getLines());
                    break;
                case CHANGE:
                    // changed line
                    result = false;
                    Chunk<String> source = delta.getSource();
                    Chunk<String> target1 = delta.getTarget();
                    writer.writeLine("- " + (source.getPosition() + 1) + " " + source.getLines() +
                            "+ " + "" + (target1.getPosition() + 1) + " " + target1.getLines());
                    break;
                case DELETE:
                    // deleted line
                    result = false;
                    Chunk<String> delete = delta.getSource();
                    writer.writeLine("- " + (delete.getPosition() + 1) + " " + delete.getLines());
                    break;
                case EQUAL:
                    break;
            }
        }
        writer.close();
        return result;
    }

    private List<String> readResultFile(String file) throws ExecException {
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader targetReader = new BufferedReader(
                    new InputStreamReader(fis, Charset.forName("UTF-8")));
            return targetReader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new ExecException(e);
        }
    }

    private void writeDiffHeader(ResultWriter writer) throws ExecException {
        writer.writeLine("source file: " + expectFile_);
        writer.writeLine("target file: " + targetFile_);
    }
}
