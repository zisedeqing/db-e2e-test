package framework.statement;

public enum StatementType {
    BACKGROUND("&"),  // running in background
    JOIN("<"),  // join an existing session
    QUIT("!");  // quit the given session

    private String flag;

    StatementType(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return flag;
    }

    public static StatementType create(String flag) {
        if (flag.equals(JOIN))
            return JOIN;
        else if (flag.equals(BACKGROUND))
            return BACKGROUND;
        else if (flag.equals(QUIT))
            return QUIT;
        else
            return null;
    }
}
