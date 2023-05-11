package main;

import java.util.HashSet;

/**
 * Placeholder javadoc.
 */
public class TransientTask implements Task {
  // Informational constants.
  public static final int TASK_IDENTITY = TRANSIENT_TASK;
  public static final String[] TASK_TYPES = {"Visit", "Shopping", "Appointment"};

  // Fields.
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


  // Other methods.
  @Override
  public boolean isConflictingWith(Task otherTask) {
    // Other task is a recurring task.
    if (otherTask.getIdentity() == Task.RECURRING_TASK) {
      HashSet<Integer> otherTaskDates = ((RecurringTask) otherTask).getDates();

      // Overlapping.
      if (otherTaskDates.contains(startDate)) {
        float otherTaskEndTime = otherTask.getStartTime() + otherTask.getDuration();
        // if ()
        return true;
      }
      // Not overlapping.
      return false;
    }
    // Is anti/transient: if dates are the same.
    if (otherTask.getStartDate() == startDate) {
      return false;  // TEST
    }
    return false;  // TEST
  }
}
