package main;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A personal scheduling system where the user
 * could plan out tasks for specific times and dates.
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
          return;
        case 1:
          // Create a task.
          if (!createTask()) {  // Failed to create.
            System.out.println();
            System.out.println("[!!!] Failed to create task: name exists or time conflict.");
          }
          break;
        case 2:
          // View a task.
          if (!viewTaskOrSchedule()) {
            System.out.println();
            System.out.println("[!!!] Failed to view task: task does not exist.");
          }
          break;
        case 3:
          // Delete a task.
          if (!deleteTask()) {
            System.out.println();
            System.out.println("[!!!] Failed to delete task: task does not exist.");
          }
          break;
        case 4:
          // Edit a task.
          if (!editTask()) {
            System.out.println();
            System.out.println("[!!!] Failed to edit task: name or overlapping issue.");
          }
          break;
        case 5:
          // Write the schedule to a file.
          if (!saveToFile()) {
            System.out.println();
            System.out.println("[!!!] Failed to save file: something went wrong.");
          } else {
            System.out.println();
            System.out.println("[*] File saved successfully.");
          }
          break;
        case 6:
          // Read the schedule from a file.
          int loadResult = loadFromFile();

          // Failed to get file.
          System.out.println();
          if (loadResult == -1) {
            System.out.println("[!!!] Failed to load file: could not find file.");
          } else {
            // Loaded tasks.
            System.out.printf("[*] Successfully loaded %d tasks.\n", loadResult);
          }
          break;
        case 7:
          // View or write the schedule for one day.
          if (!viewOrWriteForOneDay()) {
            System.out.println();
            System.out.println("[!!!] Failed to write schedule: something went wrong.");
          }
          break;
        case 8:
          // View or write the schedule for one week.
          if (!viewOrWriteForOneWeek()) {
            System.out.println();
            System.out.println("[!!!] Failed to write schedule: something went wrong.");
          }
          break;
        case 9:
          // View or write the schedule for one month.
          if (!viewOrWriteForOneMonth()) {
            System.out.println();
            System.out.println("[!!!] Failed to write schedule: something went wrong.");
          }
          break;
        default:
          // Some how got out of range?
          break;
      }
      System.out.println();
    } while (userInput != 0);

    // Close scanners.
    ConsoleInput.closeScanners();
  }


  /**
   * Prints the operations menu.
   */
  private static void printMenu() {
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
    System.out.println("-Task Setup-");
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

    // Create the task and try to add it.
    Task newTask = schedule.createTask(taskIdentity, name, taskType, startTime,
                                       duration, startDate, endDate, frequency);
    return schedule.addTask(newTask);
  }

  /**
   * Option 1.2: Asks the user for the task identity.
   *
   * @return int - the identifier of the task (recurring, transient, anti).
   */
  private static int getTaskIdentity() {
    System.out.println("=Task Identifier=");
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
    System.out.println("=Task Types=");
    for (int i = 0; i < amountOfOptions; ++i) {
      System.out.printf("[%d] %s\n", i + 1, taskTypes[i]);
    }
    int userOption = ConsoleInput.getIntRange("Please choose a task type.", 1, amountOfOptions);
    System.out.println();

    return taskTypes[userOption - 1];
  }


  /**
   * Option 2.1: Displays information of a specific task or the whole schedule.
   *
   * @return boolean - whether the task/schedule was viewed successfully or not.
   */
  private static boolean viewTaskOrSchedule() {
    // Get user's view choice.
    int viewChoice = getViewChoice();
    
    // View a task.
    if (viewChoice == 1) {
      System.out.print("Tasks: ");
      displayTaskNames();

      String taskName = ConsoleInput.getString("Enter the name of the task you want to view:");
      Task task = schedule.getByName(taskName);

      System.out.println();
      System.out.println("-Task-");

      return displayTaskInfo(task);
    } else if (viewChoice == 2) {
      displaySchedule(schedule.getTasks());
      return true;
    }
    return true;
  }

  /**
   * Displays a schedule.
   *
   * @param scheduleToView - the schedule to display.
   */
  private static void displaySchedule(ArrayList<Task> scheduleToView) {
    System.out.println("-Schedule-");
    Iterator<Task> taskIterator = scheduleToView.iterator();
    while (taskIterator.hasNext()) {
      displayTaskInfo(taskIterator.next());
      if (taskIterator.hasNext()) {
        System.out.println();
      }
    }
  }

  /**
   * Option 2.2: Displays information of the given task.
   *
   * @param task - the task to display information of.
   * @return boolean - whether the task was successfully displayed.
   */
  private static boolean displayTaskInfo(Task task) {
    // Could not find task.
    if (task == null) {
      return false;
    }

    // Print task name: > Task Name.
    System.out.printf("> %s\n", task.getName());
    // Print based on task type.
    if (task.getIdentity() == Task.RECURRING_TASK) {
      RecurringTask recurringTask = (RecurringTask) task;

      System.out.printf("%14s | %14s | %10s | %8s | %9s | %s\n",
                        "Start Date", "End Date", "Start Time", "Duration",
                        "Frequency", "Anti-Task Linked");
      System.out.printf("%14s | ", DateAndTime.YYYYMMDDToString(task.getStartDate()));
      System.out.printf("%14s | ", DateAndTime.YYYYMMDDToString(recurringTask.getEndDate()));
      System.out.printf("%10s | ", DateAndTime.timeToString(task.getStartTime()));
      System.out.printf("%8s | ",  DateAndTime.durationToString(task.getDuration()));
      System.out.printf("%9s | ",  recurringTask.getFrequency());
      // Print AntiTask name if exists.
      if (recurringTask.getAntiTask() != null) {
        System.out.printf("%s",   recurringTask.getAntiTask().getName());
      } else {
        System.out.printf("%s",   "");
      }
    } else if (task.getIdentity() == Task.ANTI_TASK) {
      System.out.printf("%14s | %10s | %8s | %s\n",
                        "Start Date", "Start Time", "Duration", "Recurring Task Linked");
      System.out.printf("%14s | ", DateAndTime.YYYYMMDDToString(task.getStartDate()));
      System.out.printf("%10s | ", DateAndTime.timeToString(task.getStartTime()));
      System.out.printf("%8s | ",     DateAndTime.durationToString(task.getDuration()));
      // Print RecurringTask name if exists.
      if (((AntiTask) task).getRecurringTask() != null) {
        System.out.printf("%s",      ((AntiTask) task).getRecurringTask().getName());
      } else {
        System.out.printf("%s",   "");
      }
    } else {
      System.out.printf("%14s | %10s | %8s\n",
                        "Start Date", "Start Time", "Duration");
      System.out.printf("%14s | ", DateAndTime.YYYYMMDDToString(task.getStartDate()));
      System.out.printf("%10s | ", DateAndTime.timeToString(task.getStartTime()));
      System.out.printf("%8s",     DateAndTime.durationToString(task.getDuration()));
    }
    System.out.println();

    return true;
  }

  /**
   * Option 2.3: Asks the user for what they want to view.
   *
   * @return int - the user's option.
   */
  private static int getViewChoice() {
    System.out.println("=View Choices=");
    System.out.println("[1] A Task");
    System.out.println("[2] Schedule");
    System.out.println("[0] Cancel");
    int viewChoice = ConsoleInput.getIntRange("Please select what you want to view.",
                                              0, 2);
    System.out.println();

    return viewChoice;
  }


  /**
   * Option 3: deletes task by asking for name of the task.
   *
   * @return boolean - whether the delete was successful or not.
   */
  private static boolean deleteTask() {
    // Displays existing tasks.
    System.out.print("Tasks: ");
    displayTaskNames();

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
    displayTaskNames();

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

      return schedule.editTaskName(taskToEdit, newName);
    } else if (userOption == 2) {
      // 2: Edit type.
      System.out.println();
      String newType = getTaskType(taskToEdit.getIdentity());

      return schedule.editTaskType(taskToEdit, newType);
    } else if (userOption == 3) {
      // 3: Edit start time.
      float startTime = ConsoleInput.getFloatRange("Enter the starting time of task [0 - 23.75].",
                                               0.0f, 23.75f);

      return schedule.editTaskStartTime(taskToEdit, startTime);
    } else if (userOption == 4) {
      // 4: Edit duration.
      float duration = ConsoleInput.getFloatRange("Enter the duration of the task [0.25 - 23.75].",
                                                0.25f, 23.75f);
      
      return schedule.editTaskDuration(taskToEdit, duration);
    } else if (userOption == 5) {
      // 5: Edit start date.
      int startDate = ConsoleInput.getIntMin("Enter the start date of the task [YYYYMMDD].",
                                         10000000);
      // Invalid startDate, keep asking for a valid one.
      while (!DateAndTime.isValidYYYYMMDD(startDate)) {
        startDate = ConsoleInput.getIntMin("Invalid start date. Try again.",
                                       10000000);
      }

      return schedule.editTaskStartDate(taskToEdit, startDate);
    } else if (userOption == 6) {
      // 6: Edit end date.
      int endDate = ConsoleInput.getIntMin("Enter the end date of the task [YYYYMMDD].",
                                           taskToEdit.getStartDate() + 1);
      // Invalid endDate, keep asking for a valid one.
      while (!DateAndTime.isValidYYYYMMDD(endDate)) {
        endDate = ConsoleInput.getIntMin("Invalid end date. Try again.",
                                         taskToEdit.getStartDate() + 1);
      }
      
      return schedule.editTaskEndDate((RecurringTask) taskToEdit, endDate);
    } else if (userOption == 7) {
      // 7: Edit frequency.
      int frequency = 0;
      while (frequency != 1 && frequency != 7) {
        frequency = ConsoleInput.getInt("Enter the task frequency [1 (daily) | 7 (weekly)].");
      }
      
      return schedule.editTaskFrequency((RecurringTask) taskToEdit, frequency);
    }

    return false;
  }

  /**
   * Option 4.2: prints out all of the existing tasks.
   */
  private static void displayTaskNames() {
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


  /**
   * Option 5: Lets the user save the schedule to a file.
   *
   * @return boolean - whether the save was successful or not.
   */
  private static boolean saveToFile() {
    String fileName = ConsoleInput.getString("Enter the name you want the file to be saved as.");

    return schedule.saveAsJson(fileName, schedule.getTasks());
  }


  /**
   * Option 6: Lets the user load a schedule from a file.
   *
   * @return int - amount of successful tasks loaded (-1 = cant find file).
   */
  private static int loadFromFile() {
    String fileName = ConsoleInput.getString("Enter the name of the file you want to load from.");

    return schedule.loadFromJson(fileName);
  }


  /**
   * Option 7.1: Lets the user view or write a schedule for one day.
   *
   * @return boolean - whether the write was successful.
   */
  private static boolean viewOrWriteForOneDay() {
    int viewOrWrite = getViewOrWrite();
    // Cancelled.
    if (viewOrWrite == 0) {
      return true;
    }

    // Get start date.
    System.out.println();
    int startDate = ConsoleInput.getIntMin("Enter the start date of the task [YYYYMMDD].",
                                       10000000);
    // Invalid startDate, keep asking for a valid one.
    while (!DateAndTime.isValidYYYYMMDD(startDate)) {
      startDate = ConsoleInput.getIntMin("Invalid start date. Try again.",
                                         10000000);
    }

    // Get tasks at date.
    ArrayList<Task> tasksInRange = schedule.getTasksInRange(startDate, startDate);

    if (viewOrWrite == 1) {
      // View.
      System.out.println();
      displaySchedule(tasksInRange);
    } else if (viewOrWrite == 2) {
      // Write.
      String fileName = ConsoleInput.getString("Enter the name you want the file to be saved as.");

      return schedule.saveAsJson(fileName, tasksInRange);
    }

    return true;
  }

  /**
   * Option 7.2: Asks the user whether they want to view or write the schedule.
   *
   * @return int - the user's view/write choice.
   */
  private static int getViewOrWrite() {
    System.out.println("=View or Write=");
    System.out.println("[1] View");
    System.out.println("[2] Write");
    System.out.println("[0] Cancel");

    return ConsoleInput.getIntRange("Would you like to view or write?", 0, 2);
  }


  /**
   * Option 8: Lets the user view or write a schedule for one week.
   *
   * @return boolean - whether the write was successful.
   */
  private static boolean viewOrWriteForOneWeek() {
    int viewOrWrite = getViewOrWrite();
    // Cancelled.
    if (viewOrWrite == 0) {
      return true;
    }

    // Get start date.
    System.out.println();
    int startDate = ConsoleInput.getIntMin("Enter the start date of the task [YYYYMMDD].",
                                       10000000);
    // Invalid startDate, keep asking for a valid one.
    while (!DateAndTime.isValidYYYYMMDD(startDate)) {
      startDate = ConsoleInput.getIntMin("Invalid start date. Try again.",
                                         10000000);
    }

    // Get tasks in date range.
    ArrayList<Task> tasksInRange = schedule.getTasksInRange(startDate,
                                DateAndTime.increaseDayForYYYYMMDD(startDate, 7));

    if (viewOrWrite == 1) {
      // View.
      System.out.println();
      displaySchedule(tasksInRange);
    } else if (viewOrWrite == 2) {
      // Write.
      String fileName = ConsoleInput.getString("Enter the name you want the file to be saved as.");

      return schedule.saveAsJson(fileName, tasksInRange);
    }

    return true;
  }


  /**
   * Option 9: Lets the user view or write a schedule for one month.
   * Assuming one month is 30 days.
   *
   * @return boolean - whether the write was successful.
   */
  private static boolean viewOrWriteForOneMonth() {
    int viewOrWrite = getViewOrWrite();
    // Cancelled.
    if (viewOrWrite == 0) {
      return true;
    }

    // Get start date.
    System.out.println();
    int startDate = ConsoleInput.getIntMin("Enter the start date of the task [YYYYMMDD].",
                                       10000000);
    // Invalid startDate, keep asking for a valid one.
    while (!DateAndTime.isValidYYYYMMDD(startDate)) {
      startDate = ConsoleInput.getIntMin("Invalid start date. Try again.",
                                         10000000);
    }

    // Get tasks in date range.
    ArrayList<Task> tasksInRange = schedule.getTasksInRange(startDate,
                                DateAndTime.increaseDayForYYYYMMDD(startDate, 30));

    if (viewOrWrite == 1) {
      // View.
      System.out.println();
      displaySchedule(tasksInRange);
    } else if (viewOrWrite == 2) {
      // Write.
      String fileName = ConsoleInput.getString("Enter the name you want the file to be saved as.");

      return schedule.saveAsJson(fileName, tasksInRange);
    }

    return true;
  }
}
