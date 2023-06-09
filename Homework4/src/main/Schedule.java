package main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

/**
 * A schedule of tasks.
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
   * Retrieves the list of tasks in a date range.
   *
   * @return ArrayList(Task) - the list of tasks.
   */
  public ArrayList<Task> getTasksInRange(int startDate, int endDate) {
    ArrayList<Task> tasksInRange = new ArrayList<Task>();
    HashSet<Integer> datesInRange = DateAndTime.getDatesInRange(startDate, endDate);

    // For every task.
    for (Task task : getTasks()) {
      // Is a recurring task.
      if (task.getIdentity() == Task.RECURRING_TASK) {
        // Get all dates.
        HashSet<Integer> taskDates = ((RecurringTask) task).getDates();

        // For every date in recurring task.
        for (int date : taskDates) {
          // If the date is in range, add to tasksInRange.
          if (datesInRange.contains(date)) {
            tasksInRange.add(task);
            break;
          }
        }
      } else {
        // Not recurring task: if the start date is in range, add to tasksInRange.
        if (datesInRange.contains(task.getStartDate())) {
          tasksInRange.add(task);
        }
      }
    }

    return tasksInRange;
  }

  /**
   * Creates a task with the given information.
   *
   * @param taskIdentity - the task identifier (recurring, transient, anti).
   * @param name - the name of the task.
   * @param taskType - the type of the task.
   * @param startTime - the start time of the task.
   * @param duration - the duration of the task.
   * @param startDate - the start date of the task.
   * @param endDate - the end date if it is a RECURRING task.
   * @param frequency - the frequency if it is a RECURRING task.
   * @return Task - the created task.
   */
  public Task createTask(int taskIdentity, String name, String taskType, float startTime,
                         float duration, int startDate, int endDate, int frequency) {
    // Create task based on identity.
    Task newTask;
    if (taskIdentity == Task.RECURRING_TASK) {
      newTask = new RecurringTask();
    } else if (taskIdentity == Task.TRANSIENT_TASK) {
      newTask = new TransientTask();
    } else {
      newTask = new AntiTask();
    }

    // Set common values.
    newTask.setName(name);
    newTask.setType(taskType);
    newTask.setStartTime(startTime);
    newTask.setDuration(duration);
    newTask.setStartDate(startDate);

    // If recurring, set end date and frequency.
    if (taskIdentity == Task.RECURRING_TASK) {
      RecurringTask recurringNewTask = (RecurringTask) newTask;
      recurringNewTask.setEndDate(endDate);
      recurringNewTask.setFrequency(frequency);
    }

    return newTask;
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
            // If the start times do not match, cannot link.
            if (recurringCollided.getStartTimeAsInt() != antiNewTask.getStartTimeAsInt()) {
              return false;
            }
            // If the durations do not match, cannot link.
            if (recurringCollided.getDurationAsInt() != antiNewTask.getDurationAsInt()) {
              return false;
            }
            
            // Start time and duration match and does not have an anti-task yet, link.
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
   * Tries to edit the name of a task.
   *
   * @param task - the task to change name of.
   * @param name - the name to change to.
   * @return boolean - whether the operation was successful or not.
   */
  public boolean editTaskName(Task task, String name) {
    // Name exists.
    if (containsName(name)) {
      return false;
    }

    task.setName(name);
    return true;
  }


  /**
   * Tries to edit the type of a task.
   *
   * @param task - the task to change type of.
   * @param type - the type to change to.
   * @return boolean - whether the operation was successful or not.
   */
  public boolean editTaskType(Task task, String type) {
    // Check if the type exists for the respective task identities.
    if (task.getIdentity() == Task.RECURRING_TASK) {
      // Does not exist.
      if (!RecurringTask.taskTypeExist(type)) {
        return false;
      }
    } else if (task.getIdentity() == Task.TRANSIENT_TASK) {
      if (!TransientTask.taskTypeExist(type)) {
        return false;
      }
    } else if (task.getIdentity() == Task.ANTI_TASK) {
      if (!AntiTask.taskTypeExist(type)) {
        return false;
      }
    }

    // Type exists; change to new type.
    task.setType(type);
    return true;
  }

  
  /**
   * Tries to edit the start time of a task.
   *
   * @param task - the task to edit.
   * @param startTime - the new start time to change to.
   * @return boolean - whether the edit was successful or not.
   */
  public boolean editTaskStartTime(Task task, float startTime) {
    // Creates a copy of the original startTime.
    float originalStartTime = task.getStartTime();

    // Remove original task from list.
    listOfTasks.remove(task);

    // Edit task value.
    task.setStartTime(startTime);

    // New start time overlaps with something, revert back time.
    if (!addTask(task)) {
      task.setStartTime(originalStartTime);
      sortTasks();
      return false;
    }

    return true;
  }


  /**
   * Tries to edit the duration of a task.
   *
   * @param task - the task to edit.
   * @param duration - the new duration to change to.
   * @return boolean - whether the edit was successful or not.
   */
  public boolean editTaskDuration(Task task, float duration) {
    // Creates a copy of the original duration.
    float originalDuration = task.getDuration();

    // Remove original task from list.
    listOfTasks.remove(task);

    // Edit task value.
    task.setDuration(duration);

    // New duration overlaps with something, revert back time. 
    if (!addTask(task)) {
      task.setDuration(originalDuration);
      return false;
    }

    return true;
  }


  /**
   * Tries to edit the start date of a task.
   *
   * @param task - the task to edit.
   * @param startDate - the new start date to change to.
   * @return boolean - whether the edit was successful or not.
   */
  public boolean editTaskStartDate(Task task, int startDate) {
    // Creates a copy of the original startDate.
    int originalStartDate = task.getStartDate();

    // Remove original task from list.
    listOfTasks.remove(task);

    // Edit task value.
    task.setStartDate(startDate);

    // New start date overlaps with something, revert back time. 
    if (!addTask(task)) {
      task.setStartDate(originalStartDate);
      sortTasks();
      return false;
    }

    return true;
  }


  /**
   * Tries to edit the end date of a recurring task.
   *
   * @param task - the task to edit.
   * @param endDate - the new end date to change to.
   * @return boolean - whether the edit was successful or not.
   */
  public boolean editTaskEndDate(RecurringTask task, int endDate) {
    // Creates a copy of the original endDate.
    int originalEndDate = task.getEndDate();

    // Remove original task from list.
    listOfTasks.remove(task);

    // Edit task value.
    task.setEndDate(endDate);

    // New end date overlaps with something, revert back time.
    if (!addTask(task)) {
      task.setEndDate(originalEndDate);
      return false;
    }

    return true;
  }


  /**
   * Tries to edit the frequency of a recurring task.
   *
   * @param task - the task to edit.
   * @param frequency - the new frequency to change to.
   * @return boolean - whether the edit was successful or not.
   */
  public boolean editTaskFrequency(RecurringTask task, int frequency) {
    // Creates a copy of the original frequency.
    int originalFrequency = task.getFrequency();

    // Remove original task from list.
    listOfTasks.remove(task);

    // Edit task value.
    task.setFrequency(frequency);

    // New frequency overlaps with something, revert back time.
    if (!addTask(task)) {
      task.setFrequency(originalFrequency);
      return false;
    }

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
   * Saves the schedule as a JSON fole.
   *
   * @param location - the location/name to save the file as.
   * @param scheduleToSave - the schedule to save to a file.
   * @return boolean - whether the save was successful or not.
   */
  public boolean saveAsJson(String location, ArrayList<Task> scheduleToSave) {
    // Open file if exists, otherwise create file.
    File jsonFile = new File(location);
    try {
      // Create new file if possible.
      jsonFile.createNewFile();
      JsonWriter jsonWriter = GSON.newJsonWriter(new FileWriter(jsonFile));

      jsonWriter.beginArray();
      // For every task.
      for (Task task : scheduleToSave) {
        // Write information.
        jsonWriter.beginObject()
                    .name("Name")
                      .value(task.getName())
                    .name("Type")
                      .value(task.getType())
                    // If task is recurring, name = StartDate, otherwise, name = Date.
                    .name((task.getIdentity() == Task.RECURRING_TASK) ? "StartDate" : "Date")
                      .value(task.getStartDate())
                    .name("StartTime")
                      .value(String.format("%.2f", task.getStartTime()))
                    .name("Duration")
                      .value(String.format("%.2f", task.getDuration()));
        
        // If recurring task, write end date and frequency too.
        if (task.getIdentity() == Task.RECURRING_TASK) {
          jsonWriter.name("EndDate")
                    .value(((RecurringTask) task).getEndDate())
                  .name("Frequency")
                    .value(((RecurringTask) task).getFrequency());
        }
        jsonWriter.endObject();
      }
      jsonWriter.endArray();
      jsonWriter.flush();
    } catch (IOException e) {
      return false;
    }

    return true;
  }


  /**
   * Loads tasks from a JSON file.
   *
   * @param location - the location/name of the json file.
   * @return int - amount of successfully loaded tasks.
   */
  public int loadFromJson(String location) {
    int numberOfSuccessfulLoads = 0;

    // Try to open file.
    File jsonFile = new File(location);
    if (!jsonFile.exists()) {
      return -1;
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
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return numberOfSuccessfulLoads;
  }

  /**
   * Sorts listOfTasks by startDate and startTime.
   */
  public void sortTasks() {
    listOfTasks.sort(Comparator.comparingInt(Task::getStartDate)
                               .thenComparingDouble(Task::getStartTimeAsInt));
  }
}
