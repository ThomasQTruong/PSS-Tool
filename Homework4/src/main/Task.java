package main;

/**
 * Template for the different task types.
 */
public interface Task {
  // Type Identifier Constants.
  public static final int RECURRING_TASK = 1;
  public static final int TRANSIENT_TASK = 2;
  public static final int ANTI_TASK = 3;


  // Common setter methods.
  public void setName(String name);

  public void setType(String type);

  public void setStartTime(float time);

  public void setDuration(float duration);

  public void setStartDate(int startDate);


  // Common getter methods.
  public String getName();

  public String getType();

  public float getStartTime();

  public float getDuration();

  public int getStartDate();
}
