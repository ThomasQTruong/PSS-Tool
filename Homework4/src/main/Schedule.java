package main;

import com.google.gson.JsonObject;
import java.util.ArrayList;

/**
 * Placeholder javadoc.
 */
public class Schedule {
  private ArrayList<Task> listOfTasks;

  // initalize new schedule class
  public Schedule() {
    listOfTasks = new ArrayList<>();
  }

  // function for adding task to list
  public void addTask(Task task) {
    if (reportOverlap(task)) {
      System.out.println("Overlap detected. Task cannot be added.");
      return;
    }
    listOfTasks.add(task);
  }

  // function for removing task from list
  public void removeTask(Task task) {
    listOfTasks.remove(task);
  }

  // function for checking overlaps of tasks
  private boolean reportOverlap(Task newTask) {
    for (Task task : listOfTasks) {
      if (task.getStartDate() == newTask.getStartDate()) {
        if (task.getStartTime() + task.getDuration()
            <= newTask.getStartTime() + newTask.getDuration()) {
          return true;  // Overlap detected
        }
      }
    }
    return false;  // No overlap found
  }

  // function for generating schedule
  private void generateSchedule() {

  }

  /**
   * Loads tasks from a JSON file.
   * 
   * @param 
   */
  public boolean loadFromJson(JsonObject json) {
    return true;
  }
}
