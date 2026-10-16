package framework;

public class ExecException extends Exception {
  public ExecException(String msg) {
    super(msg);
  }

  public ExecException(Throwable e) {
    super(e);
  }
}
