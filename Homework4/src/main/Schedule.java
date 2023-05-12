package main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.io.File;
import java.io.FileReader;

/**
 * Placeholder javadoc.
 */
public class Schedule {
  static Gson GSON = new GsonBuilder()
                         .disableHtmlEscaping()
                         .setPrettyPrinting()
                         .create();

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
   * @param newTask - the task to add.
   * @return boolean - whether the task was successfully added or not.
   */
  public boolean addTask(Task newTask) {
    // Task name already exists.
    if (containsName(newTask.getName())) {
      return false;
    }
    // Not an antitask and overlaps.
    if (newTask.getIdentity() != Task.ANTI_TASK && reportOverlap(newTask)) {
      return false;
    } else if (newTask.getIdentity() == Task.ANTI_TASK) {
      // newTask is an AntiTask.
      AntiTask antiNewTask = (AntiTask) newTask;

      // Get overlaps.
      ArrayList<Task> collisions = getCollisions(newTask);
      
      // No overlap found, cannot make anti-task.
      if (collisions.size() == 0)  {
        return false;
      }

      // Overlap found, check if it collides with a Recurring task.
      for (Task collidedTask : collisions) {
        if (collidedTask.getIdentity() == Task.RECURRING_TASK) {
          // Recurring task already has an anti task!
          RecurringTask recurringCollided = (RecurringTask) collidedTask;
          if (recurringCollided.getAntiTask() != null) {
            return false;
          } else {
            // Does not have an anti-task yet, link them.
            recurringCollided.setAntiTask(antiNewTask);
            antiNewTask.setRecurringTask(recurringCollided);
          }
        }
      }
    }

    // No overlap and name is free, add task.
    listOfTasks.add(newTask);

    // Sort tasks.
    sortTasks();

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
        if (((AntiTask) toCheck).isOverlappingWith(task)) {
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


  public boolean saveAsJson(String location) {
    return true;
  }


  /**
   * Loads tasks from a JSON file.
   *
   * @param location - location of the json file.
   * @return int - amount of successfully loaded tasks.
   */
  public int loadFromJson(String location) {
    int numberOfSuccessfulLoads = 0;

    // Try to open file.
    File jsonFile = new File(location);
    if (!jsonFile.exists()) {
      return 0;
    }

    try {
      JsonArray taskArray = GSON.fromJson(new FileReader(jsonFile), JsonArray.class);
      // For every task in the json array.
      for (int i = 0; i < taskArray.size(); ++i) {
        JsonObject task = taskArray.get(i).getAsJsonObject();
        Task newTask;
        
        // If Name does not exist, skip; otherwise get Name.
        if (!task.has("Name")) {
          continue;
        }
        String taskName = task.get("Name").getAsString();

        // If Type does not exist, skip; otherwise get Type.
        if (!task.has("Type")) {
          continue;
        }
        String taskType = task.get("Type").getAsString();

        // If StartTime does not exist, skip; otherwise get StartTime.
        if (!task.has("StartTime")) {
          continue;
        }
        float taskStartTime = task.get("StartTime").getAsFloat();
        taskStartTime = DateAndTime.roundMinutesToNearest15(taskStartTime);
        if (taskStartTime < 0 || taskStartTime > 23.75) {
          continue;
        }
        
        // If Duration does not exist, skip; otherwise get Duration.
        if (!task.has("Duration")) {
          continue;
        }
        float taskDuration = task.get("Duration").getAsFloat();
        taskDuration = DateAndTime.roundMinutesToNearest15(taskDuration);
        if (taskDuration < 0.25 || taskDuration > 23.75) {
          continue;
        }

        // Create task/obtain additional information based on taskType.
        int taskStartDate = 0;
        int taskEndDate = 0;
        int taskFrequency = 0;
        if (RecurringTask.taskTypeExist(taskType)) {
          // If StartDate does not exist, skip; otherwise get StartDate.
          if (!task.has("StartDate")) {
            continue;
          }
          taskStartDate = task.get("StartDate").getAsInt();
          if (!DateAndTime.isValidYYYYMMDD(taskStartDate)) {
            continue;
          }

          // If EndDate does not exist, skip; otherwise get EndDate.
          if (!task.has("EndDate")) {
            continue;
          }
          taskEndDate = task.get("EndDate").getAsInt();
          if (!DateAndTime.isValidYYYYMMDD(taskEndDate)) {
            continue;
          }

          // If Frequency does not exist, skip; otherwise get Frequency.
          if (!task.has("Frequency")) {
            continue;
          }
          taskFrequency = task.get("Frequency").getAsInt();
          if (taskFrequency != 1 && taskFrequency != 7) {
            continue;
          }

          newTask = new RecurringTask();
          ((RecurringTask) newTask).setEndDate(taskEndDate);
          ((RecurringTask) newTask).setFrequency(taskFrequency);
        } else if (AntiTask.taskTypeExist(taskType)) {
          // Is an AntiTask.
          newTask = new AntiTask();

          // If Date does not exist, skip; otherwise get Date.
          if (!task.has("Date")) {
            continue;
          }
          taskStartDate = task.get("Date").getAsInt();
          if (!DateAndTime.isValidYYYYMMDD(taskStartDate)) {
            continue;
          }
        } else if (TransientTask.taskTypeExist(taskType)) {
          // Is a transient task.
          newTask = new TransientTask();

          // If Date does not exist, skip; otherwise get Date.
          if (!task.has("Date")) {
            continue;
          }
          taskStartDate = task.get("Date").getAsInt();
          if (!DateAndTime.isValidYYYYMMDD(taskStartDate)) {
            continue;
          }
        } else {
          // Task type is incorrect.
          continue;
        }

        // Set common values of the tasks.
        newTask.setName(taskName);
        newTask.setType(taskType);
        newTask.setStartTime(taskStartTime);
        newTask.setDuration(taskDuration);
        newTask.setStartDate(taskStartDate);

        if (addTask(newTask)) {
          ++numberOfSuccessfulLoads;
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return numberOfSuccessfulLoads;
  }

  /**
   * Sorts listOfTasks by startDate and startTime.
   */
  public void sortTasks() {
    listOfTasks.sort(Comparator.comparingInt(Task::getStartDate)
                               .thenComparingDouble(Task::getStartTime));
  }
}
