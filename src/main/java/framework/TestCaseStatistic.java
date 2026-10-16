package framework;

public class TestCaseStatistic {
  private long totalCaseCount = 0;
  private long totalCaseSqlCount = 0;
  private long setupSqlCount = 0;
  private long cleanupSqlCount = 0;

  private long totalStatementCount = 0;
  private long totalFinishedStatementCount = 0;

  private boolean inBefore = false;
  private boolean inTestCase = true;

  public void add(TestCaseStatistic statistic) {
    totalCaseCount += statistic.totalCaseCount;
    totalCaseSqlCount += statistic.totalCaseSqlCount;
    setupSqlCount += statistic.setupSqlCount;
    cleanupSqlCount += statistic.cleanupSqlCount;
    totalStatementCount += statistic.totalStatementCount;
  }

  public void increSqlCount() {
    if (inTestCase) totalCaseSqlCount++;
    else if (inBefore) setupSqlCount++;
    else cleanupSqlCount++;
  }

  public void increFinishedStatementCount() {
    totalFinishedStatementCount++;
  }

  public void increTotalStatementCount() {
    totalStatementCount++;
  }

  public void increTotalCaseCount() {
    this.totalCaseCount += 1;
  }

  public void setInBefore() {
    this.inBefore = true;
    inTestCase = false;
  }

  public void setInTestCase() {
    this.inTestCase = true;
    inBefore = false;
  }

  public void setInAfter() {
    this.inTestCase = false;
    inBefore = false;
  }

  public long getTotalCaseCount() {
    return totalCaseCount;
  }


  public long getTotalCaseSqlCount() {
    return totalCaseSqlCount;
  }

  public long getSetupSqlCount() {
    return setupSqlCount;
  }

  public long getCleanupSqlCount() {
    return cleanupSqlCount;
  }

  public long getTotalStatementCount() {
    return totalStatementCount;
  }

  public long getTotalFinishedStatementCount() {
    return totalFinishedStatementCount;
  }
}
