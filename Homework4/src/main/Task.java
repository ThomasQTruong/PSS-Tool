package main;
import java.io.*;

/**
 * Placeholder javadoc.
 */

public interface Task{
  String taskType = "";
  String taskName = "";
  int taskDuration = 0;
  boolean isAntiTask = false;
  int taskStartTime = 0;

  // Java Interface Abstract Methods (Don't have bodies)

  // Setter Methods
  public void setTaskStartTime();

  public void setTaskEndTime();
  
  public void setTaskDuration();

  public void setTaskName();

  public void setRecurring();

  // Getter Methods

  public int getTaskTime();

  public int getTaskDuration();

  public String getTaskName();

  public boolean getRecurring();

}

