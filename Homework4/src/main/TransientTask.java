package main;

import java.util.HashSet;

/**
 * A regular task that just happens once.
 */
public class TransientTask implements Task {
  // Informational constants.
  public static final int TASK_IDENTITY = TRANSIENT_TASK;
  public static final String[] TASK_TYPES = {"Visit", "Shopping", "Appointment"};

  // Fields.
  private String name;
  private String type;
  private int startDate;
  private float startTime;
  private float duration;


  /**
   * Default constructor.
   */
  public TransientTask() {
    name = "";
    type = "";
    startDate = 0;
    startTime = 0;
    duration = 0;
  }

  /**
   * Copy Constructor: initializes with the data of another transient task.
   *
   * @param task - the transient task to copy data from.
   */
  public TransientTask(TransientTask task) {
    this.name = task.name;
    this.type = task.type;
    this.startDate = task.startDate;
    this.startTime = task.startTime;
    this.duration = task.duration;
  }


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
  public int getStartTimeAsInt() {
    return DateAndTime.floatToInt(startTime);
  }

  @Override
  public float getDuration() {
    return duration;
  }

  @Override
  public int getDurationAsInt() {
    return DateAndTime.floatToInt(duration);
  }

  @Override
  public int getStartDate() {
    return startDate;
  }


  // Other methods.
  @Override
  public boolean isConflictingWith(Task otherTask) {
    // Checking itself.
    if (this == otherTask) {
      return false;
    }
    // Other task is an anti-task, allowed to overlap.
    if (otherTask.getIdentity() == Task.ANTI_TASK) {
      return false;
    }

    // Other task is a recurring task; check if theres a matching date.
    if (otherTask.getIdentity() == Task.RECURRING_TASK) {
      HashSet<Integer> otherTaskDates = ((RecurringTask) otherTask).getDates();

      // Date does not match, cannot overlap.
      if (!otherTaskDates.contains(startDate)) {
        return false;
      }
    } else if (otherTask.getStartDate() != startDate) {
      // Is a transient/anti AND date are not matching.
      return false;
    }

    // Since dates are the matching, check for overlapping time.
    return DateAndTime.areTimesOverlapping(startTime, duration,
                                           otherTask.getStartTime(), otherTask.getDuration());
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
