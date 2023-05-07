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
  long startDate = 0;
  long startTime = 0;
  long duration = 0;



  // Setter Methods.
  public void setTaskStartTime();

  public void setTaskEndTime();

  public void setTaskDuration();

  public void setTaskName();

  public void setRecurring();


  // Getter Methods.
  public int getTaskTime();

  public int getTaskDuration();

  public String getTaskName();

  public boolean getRecurring();
}
