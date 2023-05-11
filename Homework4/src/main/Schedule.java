package main;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

/**
 * Placeholder javadoc.
 */
public class Schedule {
  private ArrayList<Task> listOfTasks;


  /**
   * Constructor: initalizes new schedule object.
   */
  public Schedule() {
    listOfTasks = new ArrayList<>();
  }


  /**
   * Retrieves the list of tasks.
   *
   * @return ArrayList(Task) - the list of tasks.
   */
  public ArrayList<Task> getTasks() {
    return listOfTasks;
  }


  /**
   * Adds a task to listOfTasks if it is not overlapping.
   *
   * @param task - the task to add.
   * @return boolean - whether the task was successfully added or not.
   */
  public boolean addTask(Task task) {
    // Task name already exists.
    if (containsName(task.getName())) {
      return false;
    }
    // Given task is overlapping with another task.
    if (reportOverlap(task)) {
      return false;
    }
    // Not over lapping, add task.
    listOfTasks.add(task);
    return true;
  }


  /**
   * Deletes a task from listOfTask if it exists.
   *
   * @param task - the task to remove.
   * @return boolean - whether the task was successfully removed or not.
   */
  public boolean deleteTask(Task task) {
    // Task does not exist.
    if (task == null || !listOfTasks.contains(task)) {
      return false;
    }
    // Task exists, remove from listOfTasks.
    listOfTasks.remove(task);
    return true;
  }


  /**
   * Checks if a task in listOfTasks contains the name.
   *
   * @param name - the name to check for.
   * @return boolean - whether a task has the name or not.
   */
  public boolean containsName(String name) {
    for (Task task : listOfTasks) {
      if (task.getName().equals(name)) {
        return true;
      }
    }

    return false;
  }


  /**
   * Gets a task in listOfTasks if exists.
   *
   * @param name - the name of the task to retrieve.
   * @return Task - the task if exists; null otherwise.
   */
  public Task getByName(String name) {
    for (Task task : listOfTasks) {
      if (task.getName().equals(name)) {
        return task;
      }
    }

    return null;
  }


  /**
   * Reports whether newTask overlaps with any other tasks.
   *
   * @param newTask - the task to check for overlapping.
   * @return boolean - whether newTask overlaps or not.
   */
  public boolean reportOverlap(Task newTask) {
    int newTaskIdentity = newTask.getIdentity();
    
    // For every task in listOfTasks.
    for (Task task : listOfTasks) {
      // Not itself.
      if (task != newTask) {
      }
    }

    /*
    // For every task in listOfTasks.
    for (Task currentTask : listOfTasks) {
      // Not itself.
      if (currentTask != task) {
        // Found recurring task in list.
        if (currentTask.getIdentity() == Task.RECURRING_TASK) {
          // Convert to RecurringTask classtype.
          RecurringTask recurringCurrentTask = (RecurringTask) currentTask;
          // task is also a recurring task.
          if (task.getIdentity() == Task.RECURRING_TASK) {
            RecurringTask recurringTask = (RecurringTask) task;
            
            // Check for conflicting time.
            HashSet<Integer> currentTaskDates = recurringCurrentTask.getDates();
            HashSet<Integer> taskDates = recurringTask.getDates();

            for (int i : taskDates) {
              // Confliction!
              if (currentTaskDates.contains(i)) {
                return true;
              }
            }
            // No conflictions found.
            return false;
          // Is a transient/anti.
          } else {
            
          }
        } else {

        }
      }
    }*/

    return false;
  }


  // apply antiTask to listOfTask to remove recurring task from schedule
  // when antiTask is entered through PSS class. --Brian Kang
  public void applyAntiTask(AntiTask antiTask) {
    List<Task> tasksToRemove = new ArrayList<>();
    for (Task task : listOfTasks) {
      if (task instanceof RecurringTask) {
        RecurringTask recurringTask = (RecurringTask) task;
        // check if recurring and antiTask overlap in schedule
        if (antiTask.getStartDate() >= recurringTask.getStartDate()
            && antiTask.getStartDate() <= recurringTask.getEndDate()
            && (antiTask.getStartDate() - recurringTask.getStartDate())
                % recurringTask.getFrequency() == 0
            && antiTask.getStartTime() == recurringTask.getStartTime()
            && antiTask.getDuration() == recurringTask.getDuration()
            && antiTask.getName().equals(recurringTask.getName())) {
          tasksToRemove.add(recurringTask);
        }
      }
    }
    for (Task taskToRemove : tasksToRemove) {
      deleteTask(taskToRemove);
    }
  }


  /**
   * Deletes a task with a specific name.
   *
   * @param taskName - the name of the task to delete.
   * @return boolean - whether the deletion was successful or not.
   */
  public boolean deleteTaskByName(String taskName) {
    Task taskToDelete = null;
    for (Task task : listOfTasks) {
      if (task.getName().equals(taskName)) {
        taskToDelete = task;
        break;
      }
    }
    return deleteTask(taskToDelete);
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
