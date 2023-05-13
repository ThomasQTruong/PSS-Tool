package main;

import java.util.HashSet;

/**
 * A task that re-occurs daily or weekly until a certain date.
 */
public class RecurringTask implements Task {
  // Informational constants.
  public static final int TASK_IDENTITY = RECURRING_TASK;
  public static final String[] TASK_TYPES = {"Class", "Study", "Sleep", "Exercise", "Work", "Meal"};

  // Fields.
  private AntiTask antiTask;
  private String name;
  private String type;
  private int startDate;
  private float startTime;
  private float duration;
  private int endDate;
  private int frequency;


  /**
   * Default constructor.
   */
  public RecurringTask() {
    this.antiTask = null;
    this.name = "";
    this.type = "";
    this.startDate = 0;
    this.startTime = 0;
    this.duration = 0;
    this.endDate = 0;
    this.frequency = 0;
  }

  /**
   * Copy Constructor: initializes with the data of another recurring task.
   *
   * @param task - the recurring task to copy data from.
   */
  public RecurringTask(RecurringTask task) {
    this.antiTask = task.antiTask;
    this.name = task.name;
    this.type = task.type;
    this.startDate = task.startDate;
    this.startTime = task.startTime;
    this.duration = task.duration;
    this.endDate = task.endDate;
    this.frequency = task.frequency;
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
    // AntiTask doesnt exist, just use getDatesIgnoreAnti().
    if (antiTask == null) {
      return getDatesIgnoreAnti();
    }

    HashSet<Integer> dates = new HashSet<>();

    // While the end date has not been reached.
    int currentDate = startDate;
    while (currentDate < endDate) {
      if (currentDate != antiTask.getStartDate()) {
        // Add current date to the set.
        dates.add(currentDate);
      }
      // Increase date by frequency.
      currentDate = DateAndTime.increaseDayForYYYYMMDD(currentDate, frequency);
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
    // Checking itself.
    if (this == otherTask) {
      return false;
    }

    HashSet<Integer> thisTaskDates = getDates();

    // otherTask is also a recurring task.
    if (otherTask.getIdentity() == Task.RECURRING_TASK) {
      HashSet<Integer> otherTaskDates = ((RecurringTask) otherTask).getDates();

      // Check for same date.
      boolean hasSameDate = false; 
      for (int otherDate : otherTaskDates) {
        if (thisTaskDates.contains(otherDate)) {
          hasSameDate = true;
          break;
        }
      }

      // If they do not have same date, no confliction.
      if (!hasSameDate) {
        return false;
      }
    } else if (!thisTaskDates.contains(otherTask.getStartDate())) {
      // Does not contain the same start date, no confliction.
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
