package main;

import java.util.HashSet;

/**
 * Placeholder javadoc.
 */
public class RecurringTask implements Task {
  // Informational constants.
  public static final int TASK_IDENTITY = RECURRING_TASK;
  public static final String[] TASK_TYPES = {"Class", "Study", "Sleep", "Exercise", "Work", "Meal"};

  // Fields.
  private AntiTask antiTask = null;
  private String name = "";
  private String type = "";
  private int startDate = 0;
  private float startTime = 0;
  private float duration = 0;
  private int endDate = 0;
  private int frequency = 0;


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
   * Sets the end date to a new one.
   *
   * @param endDate - the new end date.
   */
  public void setEndDate(int endDate) {
    this.endDate = endDate;
  }

  /**
   * Sets the frequency to a new one.
   *
   * @param frequency - the new frequency.
   */
  public void setFrequency(int frequency) {
    this.frequency = frequency;
  }

  /**
   * Sets the antiTask to a new one.
   *
   * @param antiTask - the new Anti-Task.
   */
  public void setAntiTask(AntiTask antiTask) {
    this.antiTask = antiTask;
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
   * Retrieves the end date.
   *
   * @return int - the end date.
   */
  public int getEndDate() {
    return endDate;
  }

  /**
   * Retrieves the frequency.
   *
   * @return int - the frequency.
   */
  public int getFrequency() {
    return frequency;
  }

  /**
   * Retrieves the antiTask.
   *
   * @return antiTask - the Anti-Task.
   */
  public AntiTask getAntiTask() {
    return antiTask;
  }

  /**
   * Retrieves the dates for a recurring task with AntiTask applied.
   *
   * @return HashSet(Integer) - the container for the dates.
   */
  public HashSet<Integer> getDates() {
    HashSet<Integer> dates = new HashSet<>();

    // While the end date has not been reached.
    int currentDate = startDate;
    while (currentDate < endDate) {
      if (currentDate != antiTask.getStartDate()) {
        // Add current date to the set.
        dates.add(currentDate);
        // Increase date by frequency.
        currentDate = DateAndTime.increaseDayForYYYYMMDD(currentDate, frequency);
      }
    }

    return dates;
  }

  /**
   * Retrieves all the dates for a recurring task.
   *
   * @return HashSet(Integer) - the container for all of the dates.
   */
  public HashSet<Integer> getDatesIgnoreAnti() {
    HashSet<Integer> dates = new HashSet<>();

    // While the end date has not been reached.
    int currentDate = startDate;
    while (currentDate < endDate) {
      // Add current date to the set.
      dates.add(currentDate);
      // Increase date by frequency.
      currentDate = DateAndTime.increaseDayForYYYYMMDD(currentDate, frequency);
    }

    return dates;
  }


  // Other methods.
  @Override
  public boolean isConflictingWith(Task otherTask) {
    return false;
  }
}
