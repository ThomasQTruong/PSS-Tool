package main;

/**
 * Placeholder javadoc.
 */
public class RecurringTask implements Task {
  // Informational constants.
  public static final int TASK_IDENTITY = RECURRING_TASK;
  public static final String[] TASK_TYPES = {"Class", "Study", "Sleep", "Exercise", "Work", "Meal"};

  // Fields.
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

  public void setEndDate(int endDate) {
    this.endDate = endDate;
  }

  public void setFrequency(int frequency) {
    this.frequency = frequency;
  }


  // Getter methods.
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

  public int getEndDate() {
    return endDate;
  }

  public int getFrequency() {
    return frequency;
  }
}
