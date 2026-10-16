package framework.statement;

public class EmptyStatement extends INormalStatement {

  public EmptyStatement(int lineNumber) {
    super(null, lineNumber, null);
  }



  public void endExecute() {}

  @Override
  public String toString() {
    return "\n";
  }

  @Override
  String toOriginString() {
    return toString();
  }
}
