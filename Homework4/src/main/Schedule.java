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

    // Name is free, add task.
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
    // Is an anti-task, must not overlap with anything else.
    if (task.getIdentity() == Task.ANTI_TASK) {
      ArrayList<Task> collisions = getCollisions(task);

      for (Task collidedTask : collisions) {
        // Cannot collide with a non-recurring task.
        if (collidedTask.getIdentity() != Task.RECURRING_TASK) {
          return false;
        } else if (collidedTask != ((AntiTask) task).getRecurringTask()) {
          // Not the linked recurring task! Still overlapping.
          return false;
        }
      }
      // Unlink the AntiTask from the RecurringTask.
      ((AntiTask) task).getRecurringTask().setAntiTask(null);
    } else if (task.getIdentity() == Task.RECURRING_TASK) {
      // Is a recurring task, cannot be linked to a anti-task.
      if (((RecurringTask) task).getAntiTask() != null) {
        return false;
      }
    }

    // Task exists, remove from listOfTasks.
    listOfTasks.remove(task);
    task = null;
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
    // For every task in listOfTasks.
    for (Task task : listOfTasks) {
      // Not itself.
      if (task != newTask) {
        // Is conflicting with newTask.
        if (task.isConflictingWith(newTask)) {
          return true;
        }
      }
    }

    return false;
  }


  /**
   * Retrives a list of tasks that toCheck overlaps with.
   *
   * @param toCheck - the task to check for overlap.
   * @return ArrayList(Task) - the list of collisions.
   */
  public ArrayList<Task> getCollisions(Task toCheck) {
    ArrayList<Task> collisions = new ArrayList<>();
    
    // For every task.
    for (Task task : listOfTasks) {
      // Not itself.
      if (task != toCheck) {
        // Is conflicting.
        if (task.isConflictingWith(toCheck)) {
          collisions.add(task);
        }
      }
    }

    return collisions;
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
