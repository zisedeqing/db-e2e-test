package framework.statement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplaceHookStatement extends IHookStatement {
    public static String MATCH_IDENTIFIER = "-- match";
    public static String REPLACT_IDENTIFIER = "-- replace:";
    private String matchRegexp_;
    private String replaceString_;

    public ReplaceHookStatement(int line) {
        super(line, HookType.HT_RESULT);
    }

    public String getMatchRegexp() {
        return matchRegexp_;
    }

    public String getReplaceString() {
        return replaceString_;
    }

    public static boolean isMatchStatement(String comment) {
        return comment.startsWith(ReplaceHookStatement.MATCH_IDENTIFIER);
    }

    public static boolean isReplaceStatement(String comment) {
        return comment.startsWith(ReplaceHookStatement.REPLACT_IDENTIFIER);
    }

    @Override
    protected String getHookIndent() {
        return null;
    }

    @Override
    protected String getHookRule() {
        return null;
    }

    @Override
    public String apply(String line) {
        if (line == null) return null;
        Pattern pattern = Pattern.compile(getMatchRegexp());
        Matcher matcher = pattern.matcher(line);
        return matcher.replaceAll(getReplaceString());
    }

    @Override
    public IHookStatement parserHook(String comment) {
        if (isMatchStatement(comment)) {
            String tmp = comment.substring(MATCH_IDENTIFIER.length()).trim();
            tmp = initName(tmp);
            matchRegexp_ = tmp.substring(1);
        } else {
            replaceString_ = comment.substring(REPLACT_IDENTIFIER.length()).trim();
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(MATCH_IDENTIFIER);
        if (name_ != null)
            builder.append("(").append(name_).append(")");
        builder.append(":").append(matchRegexp_).append("\n");
        if (replaceString_ != null)
            builder.append(REPLACT_IDENTIFIER).append(replaceString_);
        builder.append("\n");
        return builder.toString();
    }
}
