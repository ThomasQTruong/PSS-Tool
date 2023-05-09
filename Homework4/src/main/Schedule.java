package main;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

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


  //apply antiTask to listOfTask to remove recurring task from schedule when antiTask is entered through PSS class. --Brian Kang
  public void applyAntiTask(AntiTask antiTask) {
    List<Task> tasksToRemove = new ArrayList<>();
    for (Task task : listOfTasks) {
        if (task instanceof RecurringTask) {
            RecurringTask recurringTask = (RecurringTask) task;
            //check if recurring and antiTask overlap in schedule
            if (antiTask.getStartDate() >= recurringTask.getStartDate() &&
                antiTask.getStartDate() <= recurringTask.getEndDate() &&
                (antiTask.getStartDate() - recurringTask.getStartDate()) % recurringTask.getFrequency() == 0 &&
                antiTask.getStartTime() == recurringTask.getStartTime() &&
                antiTask.getDuration() == recurringTask.getDuration() &&
                antiTask.getName().equals(recurringTask.getName())) {
                tasksToRemove.add(recurringTask);
            }
        }
    }
    for (Task taskToRemove : tasksToRemove) {
        removeTask(taskToRemove);
    }
}

//remove task by its name  --Brian Kang
  public void deleteTaskByName(String taskName) {
    Task taskToDelete = null;
    for (Task task : listOfTasks) {
        if (task.getName().equals(taskName)) {
            taskToDelete = task;
            break;
        }
  }
    if (taskToDelete != null) {
        removeTask(taskToDelete);
    } else {
        System.out.println("Task with name " + taskName + " not found.");
    }
  }




  // function for generating schedule
  //added just spaceholder to avoid errors in PSS class -- Brian Kang
  public ArrayList<Task> generateSchedule() {
    ArrayList<Task> schedule = new ArrayList<>();
    return schedule;
  }

  /**
   * Loads tasks from a JSON file.
   *
   * @param json - the json to extract data from.
   * @return boolean - whether loading was successful or not.
   */
  public boolean loadFromJson(JsonObject json) {
    return true;
  }
}
