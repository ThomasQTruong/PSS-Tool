package main;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Placeholder javadoc.
 */
public class PSS {
  private static Schedule schedule = new Schedule();


  /**
   * The main PSS program.
   *
   * @param args - commandline arguments.
   */
  public static void main(String[] args) {
    int userInput;
    
    do {
      // Print and get user's operation choice.
      printMenu();
      userInput = ConsoleInput.getIntRange("Please choose an operation.", 0, 9);
      System.out.println();

      // Check operation choice.
      switch (userInput) {
        case 0:
          // Exit program.
          System.out.println("Good bye!");
          break;
        case 1:
          // Create a task.
          if (!createTask()) {  // Failed to create.
            System.out.println();
            System.out.println("[!!!] Failed to create task: name exists or time conflict.");
          }
          System.out.println();
          break;
        case 2:
          // View a task.
          viewTask();
          break;
        case 3:
          // Delete a task.
          if (!deleteTask()) {
            System.out.println();
            System.out.println("[!!!] Failed to delete task: task does not exist.");
          }
          System.out.println();
          break;
        case 4:
          // Edit a task.
          if (!editTask()) {
            System.out.println("[!!!] Failed to edit task: name or overlapping issue.");
          }
          break;
        case 5:
          // Write the schedule to a file.
          break;
        case 6:
          // Read the schedule from a file.
          schedule.loadFromJson("../../test/Set1.json");
          break;
        case 7:
          // View or write the schedule for one day.
          break;
        case 8:
          // View or write the schedule for one week.
          break;
        case 9:
          // View or write the schedule for one month.
          break;
      }
    } while (userInput != 0);

    // Close scanners.
    ConsoleInput.closeScanners();
  }


  /**
   * Prints the operations menu.
   */
  public static void printMenu() {
    System.out.println("=Operations Menu=");
    System.out.println("[1] Create a task");
    System.out.println("[2] View a task");
    System.out.println("[3] Delete a task");
    System.out.println("[4] Edit a task");
    System.out.println("[5] Write the schedule to a file");
    System.out.println("[6] Read the schedule from a file");
    System.out.println("[7] View or write the schedule for one day");
    System.out.println("[8] View or write the schedule for one week");
    System.out.println("[9] View or write the schedule for one month");
    System.out.println("[0] Exit Program");
  }


  /**
   * Option 1.1: Obtains information and creates the task.
   *
   * @return boolean - whether the task was successfully created or not.
   */
  private static boolean createTask() {
    
    int taskIdentity = getTaskIdentity();

    // Obtain common task values.
    System.out.println("=Task Setup=");
    String name = ConsoleInput.getString("Enter a name for the task.");
    System.out.println();

    String taskType = getTaskType(taskIdentity);

    float startTime = ConsoleInput.getFloatRange("Enter the starting time of task [0 - 23.75].",
                                             0.0f, 23.75f);

    float duration = ConsoleInput.getFloatRange("Enter the duration of the task [0.25 - 23.75].",
                                                0.25f, 23.75f);

    int startDate = ConsoleInput.getIntMin("Enter the start date of the task [YYYYMMDD].",
                                       10000000);
    // Invalid startDate, keep asking for a valid one.
    while (!DateAndTime.isValidYYYYMMDD(startDate)) {
      startDate = ConsoleInput.getIntMin("Invalid start date. Try again.",
                                         10000000);
    }

    // Is a recurring task, ask for 2 more properties.
    int endDate = 0;
    int frequency = 0;
    if (taskIdentity == Task.RECURRING_TASK) {
      endDate = ConsoleInput.getIntMin("Enter the end date of the task [YYYYMMDD].",
                                       startDate + 1);
      // Invalid endDate, keep asking for a valid one.
      while (!DateAndTime.isValidYYYYMMDD(endDate)) {
        endDate = ConsoleInput.getIntMin("Invalid end date. Try again.",
                                         startDate + 1);
      }

      while (frequency != 1 && frequency != 7) {
        frequency = ConsoleInput.getInt("Enter the task frequency [1 (daily) | 7 (weekly)].");
      }
    }

    // call enterTask() and add to schedule list 
    return enterTask(taskIdentity, name, taskType, startTime,
                     duration, startDate, endDate, frequency);
  }

  /**
   * Option 1.2: Asks the user for the task identity.
   *
   * @return int - the identifier of the task (recurring, transient, anti).
   */
  private static int getTaskIdentity() {
    System.out.println("-Task Identifier-");
    System.out.println("[1] Recurring Task");
    System.out.println("[2] Transient Task");
    System.out.println("[3] Anti-Task");
    int taskIdentity = ConsoleInput.getIntRange("Please choose a task identity.", 1, 3);
    System.out.println();

    return taskIdentity;
  }

  /**
   * Option 1.3: Asks the user for the type of task.
   *
   * @return String - the type of task.
   */
  private static String getTaskType(int taskIdentity) {
    // Get task type.
    Task taskType;
    if (taskIdentity == Task.RECURRING_TASK) {
      taskType = new RecurringTask();
    } else if (taskIdentity == Task.TRANSIENT_TASK) {
      taskType = new TransientTask();
    } else {  // Anti-Task.
      taskType = new AntiTask();
    }

    // Get amount of options for the specific task.
    int amountOfOptions = taskType.getTypeLength();

    // If there is no option for some reason, return blank.
    if (amountOfOptions == 0) {
      return "";
    }
    // If there is only one option.
    if (amountOfOptions == 1) {
      return taskType.getTypes()[0];
    }

    // Multiple options, list and get user's choice.
    String[] taskTypes = taskType.getTypes();
    System.out.println("-Task Types-");
    for (int i = 0; i < amountOfOptions; ++i) {
      System.out.printf("[%d] %s\n", i + 1, taskTypes[i]);
    }
    int userOption = ConsoleInput.getIntRange("Please choose a task type.", 1, amountOfOptions);
    System.out.println();

    return taskTypes[userOption - 1];
  }

  /**
   * Option 1.4: Creates a task with the given information and inserts into the schedule.
   *
   * @param taskIdentity - the task identifier (recurring, transient, anti).
   * @param name - the name of the task.
   * @param taskType - the type of the task.
   * @param startTime - the start time of the task.
   * @param duration - the duration of the task.
   * @param startDate - the start date of the task.
   * @param endDate - the end date if it is a RECURRING task.
   * @param frequency - the frequency if it is a RECURRING task.
   * @return boolean - whether the task was entered successfully.
   */
  public static boolean enterTask(int taskIdentity, String name, String taskType, float startTime,
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

    // Try to create task.
    return schedule.addTask(newTask);
  }


  // Option 2: NOT DONE, JUST TESTING OTHER FUNCTIONS.
  private static boolean viewTask() {
    for (Task task : schedule.getTasks()) {
      if (task.getIdentity() == Task.RECURRING_TASK) {
        RecurringTask recurringTask = (RecurringTask) task;
        System.out.printf("%s | %d | %d | %f | %f\n", task.getName(), task.getStartDate(),
                                                      recurringTask.getEndDate(),
                                                      task.getStartTime(), task.getDuration());
      } else {
        System.out.printf("%s | %d | %f | %f\n", task.getName(), task.getStartDate(),
                                               task.getStartTime(), task.getDuration());
      }
    }
    return true;
  }


  /**
   * Option 3: deletes task by asking for name of the task.
   *
   * @return boolean - whether the delete was successful or not.
   */
  private static boolean deleteTask() {
    // Displays existing tasks.
    System.out.print("Tasks: ");
    displayTasks();

    // Get task name to delete.
    String taskToDelete = ConsoleInput.getString("Enter the name of the task to delete.");

    return schedule.deleteTaskByName(taskToDelete);
  }


  /**
   * Option 4: edits the property a task.
   *
   * @return boolean - whether the edit was successful or not.
   */ 
  private static boolean editTask() {
    // Displays existing tasks.
    System.out.print("Tasks: ");
    displayTasks();

    String taskName = ConsoleInput.getString("Enter the name of the task you want to edit.");
    System.out.println();
    Task taskToEdit = schedule.getByName(taskName);
    // Task does not exist.
    if (taskToEdit == null) {
      return false;
    }

    // Get amount of options based on task.
    int amountOfOptions = 5;
    if (taskToEdit.getIdentity() == Task.RECURRING_TASK) {
      amountOfOptions = 7;
    }
    String[] properties = {"Name", "Type", "Start Time", "Duration",
                           "Start Date", "End Date", "Frequency"};

    // Task exists, ask user what they want to change.
    System.out.println("=Editable Properties=");
    for (int i = 0; i < amountOfOptions; ++i) {
      System.out.printf("[%d] %s\n", i + 1, properties[i]);
    }
    int userOption = ConsoleInput.getIntRange("Enter the property you want to edit.",
                                              0, amountOfOptions);

    // Do edit based on chosen option.
    if (userOption == 0) {
      // 0: Cancel operation.
      return true;  // Edit technically successful, just stopped editing.
    } else if (userOption == 1) {
      // 1: Edit name.
      String newName = ConsoleInput.getString("Enter the new name you would like.");

      // Name exists.
      if (schedule.containsName(newName)) {
        return false;
      }

      taskToEdit.setName(newName);
    } else if (userOption == 2) {
      // 2: Edit type.
      System.out.println();
      String newType = getTaskType(taskToEdit.getIdentity());
      taskToEdit.setType(newType);
    } else if (userOption == 3) {
      // 3: Edit start time.
      float startTime = ConsoleInput.getFloatRange("Enter the starting time of task [0 - 23.75].",
                                               0.0f, 23.75f);
      float oldStartTime = taskToEdit.getStartTime();
      taskToEdit.setStartTime(startTime);

      // New time overlaps with another task.
      if (schedule.reportOverlap(taskToEdit)) {
        // Revert changes.
        taskToEdit.setStartTime(oldStartTime);
        return false;
      }
    } else if (userOption == 4) {
      // 4: Edit duration.
      float duration = ConsoleInput.getFloatRange("Enter the duration of the task [0.25 - 23.75].",
                                                0.25f, 23.75f);
      float oldDuration = taskToEdit.getDuration();
      taskToEdit.setDuration(duration);

      // New duration overlaps with another task.
      if (schedule.reportOverlap(taskToEdit)) {
        // Revert changes.
        taskToEdit.setDuration(oldDuration);
        return false;
      }
    } else if (userOption == 5) {
      // 5: Edit start date.
      int startDate = ConsoleInput.getIntMin("Enter the start date of the task [YYYYMMDD].",
                                         10000000);
      // Invalid startDate, keep asking for a valid one.
      while (!DateAndTime.isValidYYYYMMDD(startDate)) {
        startDate = ConsoleInput.getIntMin("Invalid start date. Try again.",
                                       10000000);
      }
      // Save old start date incase overlap issue.
      int oldDate = taskToEdit.getStartDate();
      taskToEdit.setStartDate(startDate);
      
      // New date overlaps with another task.
      if (schedule.reportOverlap(taskToEdit)) {
        // Revert changes.
        taskToEdit.setStartDate(oldDate);
        return false;
      }
    } else if (userOption == 6) {
      // 6: Edit end date.
      int endDate = ConsoleInput.getIntMin("Enter the end date of the task [YYYYMMDD].",
                                           taskToEdit.getStartDate() + 1);
      // Invalid endDate, keep asking for a valid one.
      while (!DateAndTime.isValidYYYYMMDD(endDate)) {
        endDate = ConsoleInput.getIntMin("Invalid end date. Try again.",
                                         taskToEdit.getStartDate() + 1);
      }
      // Save old end date incase overlap issue.
      RecurringTask recurringTaskToEdit = (RecurringTask) taskToEdit;
      int oldDate = recurringTaskToEdit.getEndDate();
      recurringTaskToEdit.setEndDate(endDate);
      
      // New date overlaps with another task.
      if (schedule.reportOverlap(taskToEdit)) {
        // Revert changes.
        recurringTaskToEdit.setEndDate(oldDate);
        return false;
      }
    } else if (userOption == 7) {
      int frequency = 0;
      while (frequency != 1 && frequency != 7) {
        frequency = ConsoleInput.getInt("Enter the task frequency [1 (daily) | 7 (weekly)].");
      }
      // Get old frequency incase overlap.
      RecurringTask recurringTaskToEdit = (RecurringTask) taskToEdit;
      int oldFrequency = recurringTaskToEdit.getFrequency();
      recurringTaskToEdit.setFrequency(frequency);

      // New frequency overlaps with another task.
      if (schedule.reportOverlap(taskToEdit)) {
        // Revert changes.
        recurringTaskToEdit.setFrequency(oldFrequency);
        return false;
      }
    }

    return true;
  }

  /**
   * Option 4.2: prints out all of the existing tasks.
   */
  private static void displayTasks() {
    // For every task.
    Iterator<Task> taskIterator = schedule.getTasks().iterator();
    // First item exists, just print it.
    if (taskIterator.hasNext()) {
      System.out.print(taskIterator.next().getName());
    }
    // More items exist, print with a comma in front.
    while (taskIterator.hasNext()) {
      System.out.print(", " + taskIterator.next().getName());
    }
    System.out.println();
  }


  // Option 5.
  private static boolean saveToFile() {
    String fileName = ConsoleInput.getString("Enter the name you want the file to be saved as.");

    return true;
  }


  // Option 6.
  private static boolean loadFromFile() {
    String fileName = ConsoleInput.getString("Enter the name of the file you want to load from.");

    return true;
  }


  // Option 7 (???).
  private static boolean viewOrWriteForOneDay() {
    return true;
  }


  // Option 8 (???).
  private static boolean viewOrWriteForOneWeek() {
    return true;
  }


  // Option 9 (???).
  private static boolean viewOrWriteForOneMonth() {
    return true;
  }
}
