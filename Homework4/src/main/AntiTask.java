package main;

import java.util.HashSet;

/**
 * A task that cancels out an occurance for a recurring task.
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
  
  
  /**
   * Checks if this task overlaps with the other task (unconflicting case).
   *
   * @param otherTask - the other task to check for overlap.
   * @return boolean - whether it overlaps with any other task.
   */
  public boolean isOverlappingWith(Task otherTask) {
    // Linked to this task, which is overlapping.
    if (recurringTask == otherTask) {
      return true;
    }

    // otherTask is not a recurring task.
    if (otherTask.getIdentity() != Task.RECURRING_TASK) {
      return DateAndTime.areTimesOverlapping(startTime, duration,
                                             otherTask.getStartTime(), otherTask.getDuration());
    } else {
      // otherTask is a recurring task.
      HashSet<Integer> otherTaskDates = ((RecurringTask) otherTask).getDates();

      // For every start date.
      for (int date : otherTaskDates) {
        // Matching date.
        if (date == startDate) {
          return true;
        }
      }
    }

    // No overlaps.
    return false;
  }

  /**
   * Checks whether a task type exists in TASK_TYPES.
   *
   * @param taskType - the task type to check for.
   * @return boolean - whether the type exists in TASK_TYPES or not.
   */
  public static boolean taskTypeExist(String taskType) {
    for (int i = 0; i < TASK_TYPES.length; ++i) {
      if (TASK_TYPES[i].equals(taskType)) {
        return true;
      }
    }
    return false;
  }
}
