package main;

/**
 * Placeholder javadoc.
 */
public class AntiTask implements Task {
  // Setter methods.
  public static final int TASK_TYPE = ANTI_TASK;
  private String name = "";
  private String type = "";
  private int startDate = 0;
  private float startTime = 0;
  private float duration = 0;
  //

  @Override
  public void setName(String newName) {
    this.name = newName;
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

  @Override
  public void setType(String newType) {
    this.type = newType;
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
}


