package main;

/**
 * Placeholder javadoc.
 */
public class RecurringTask implements Task {
  public static final int TASK_TYPE = RECURRING_TASK;
  private String name = "";
  private String type = "";
  private int startDate = 0;
  private float startTime = 0;
  private float duration = 0;
  private int endDate = 0;
  private int frequency = 0;

  // Setter methods.
  @Override
  public void setName(String newName) {
    this.name = newName;
  }

  @Override
  public void setType(String newType) {
    this.type = newType;
  }

  @Override
  public void setStartTime(float newTime) {
    this.startTime = newTime;
  }

  @Override
  public void setDuration(float newDuration) {
    this.duration = newDuration;
  }

  @Override
  public void setStartDate(int newDate) {
    this.startDate = newDate;
  }

  
  public void setEndDate(int newDate) {
    this.endDate = newDate;
  }

  public void setFrequency(int newFrequency) {
    this.frequency = newFrequency;
  }


  // Getter methods.
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
