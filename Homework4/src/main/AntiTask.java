package main;

/**
 * Placeholder javadoc.
 */
public class AntiTask implements Task {
  // Informational constants.
  public static final int TASK_IDENTITY = ANTI_TASK;
  public static final String[] TASK_TYPES = {"Cancellation"};

  // Fields.
  private RecurringTask recurringTask = null;
  private String name = "";
  private String type = "";
  private int startDate = 0;
  private float startTime = 0;
  private float duration = 0;


  // Setter methods.
  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void setType(String type) {
    this.type = type;
  }

  @Override
  public void setStartTime(float startTime) {
    this.startTime = startTime;
  }

  @Override
  public void setDuration(float duration) {
    this.duration = duration;
  }

  @Override
  public void setStartDate(int startDate) {
    this.startDate = startDate;
  }

  /**
   * Sets the recurringTask to a new one.
   *
   * @param recurringTask - the new Recurring Task.
   */
  public void setRecurringTask(RecurringTask recurringTask) {
    this.recurringTask = recurringTask;
  }


  // Getter methods.
  @Override
  public int getIdentity() {
    return TASK_IDENTITY;
  }

  @Override
  public String[] getTypes() {
    return TASK_TYPES;
  }

  @Override
  public int getTypeLength() {
    return TASK_TYPES.length;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public float getStartTime() {
    return startTime;
  }

  @Override
  public float getDuration() {
    return duration;
  }

  @Override
  public int getStartDate() {
    return startDate;
  }

  /**
   * Retrieves the recurringTask.
   *
   * @return recurringTask - the Recurring Task.
   */
  public RecurringTask getRecurringTask() {
    return recurringTask;
  }


  // Other methods.
  @Override
  public boolean isConflictingWith(Task otherTask) {
    // Checking itself.
    if (this == otherTask) {
      return false;
    }
    // Can only conflict with another AntiTask.
    if (otherTask.getIdentity() == Task.ANTI_TASK) {
      // Check if the two anti-tasks are conflicting.
      return DateAndTime.areTimesOverlapping(startTime, duration,
                                             otherTask.getStartTime(), otherTask.getDuration());
    }
    // Not an AntiTask, cannot conflict.
    return false;
  }
}
