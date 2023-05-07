package main;

/**
 * Template for the different task types.
 */
public interface Task {
  // Type Identifier.
  public static final int RECURRING_TASK = 1;
  public static final int TRANSIENT_TASK = 2;
  public static final int ANTI_TASK = 3;

  // Common variables.
  String name = "";
  int type = 0;
  int startDate = 0;
  int startTime = 0;
  int duration = 0;


  // Common setter methods.
  public void setName(String newName);

  public void setStartTime(int newTime);

  public void setDuration(int newDuration);

  public void setDate(int newDate);


  // Common getter methods.
  public String getName();

  public int getType();

  public int getStartTime();

  public int getDuration();

  public int getStartDate();
}
