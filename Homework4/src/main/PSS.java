package main;

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
          for (Task task : schedule.getTasks()) {
            System.out.printf("%s | %d | %f | %f\n", task.getName(), task.getStartDate(), task.getStartTime(), task.getDuration());
          }
          break;
        case 3:
          // Delete a task.
          break;
        case 4:
          // Edit a task.
          break;
        case 5:
          // Write the schedule to a file.
          break;
        case 6:
          // Read the schedule from a file.
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
                                         DateAndTime.getCurrentYYYYMMDD());
    // Invalid startDate, keep asking for a valid one.
    while (!DateAndTime.isValidYYYYMMDD(startDate)) {
      startDate = ConsoleInput.getIntMin("Invalid start date. Try again.",
                                         DateAndTime.getCurrentYYYYMMDD());
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

      frequency = ConsoleInput.getIntRange("Enter the frequency of the task in days [1 - 7].",
                                           1, 7);
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

    return taskTypes[userOption];
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
    // use int taskIdentity to sort which task
    Task newTask;
    switch (taskIdentity) {
      case Task.RECURRING_TASK:
        RecurringTask recurringTask = new RecurringTask();
        recurringTask.setEndDate(endDate);
        recurringTask.setFrequency(frequency);
        newTask = recurringTask;
        break;
      case Task.TRANSIENT_TASK:
        newTask = new TransientTask();
        break;
      case Task.ANTI_TASK:
        AntiTask antiTask = new AntiTask();
        antiTask.setName(name);
        antiTask.setType(taskType);
        antiTask.setStartTime(startTime);
        antiTask.setDuration(duration);
        antiTask.setStartDate(startDate);
        schedule.applyAntiTask(antiTask);
        return true;
      default:
        System.out.println("Invalid task identity.");
        return false;
    }
    newTask.setName(name);
    newTask.setType(taskType);
    newTask.setStartTime(startTime);
    newTask.setDuration(duration);
    newTask.setStartDate(startDate);
    return schedule.addTask(newTask);
  }


  // Option 2.
  private static boolean viewTask() {
    return true;
  }


  // Option 3.
  private static boolean deleteTask() {
    return true;
  }


  // Option 4.
  private static boolean editTask() {
    return true;
  }


  // Option 5.
  private static boolean saveToFile() {
    return true;
  }


  // Option 6.
  private static boolean loadFromFile() {
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


  // user given option to delete task by name --Brian Kang
  public void deleteTask(String taskName) {
    // call the deleteTaskByName method of the Schedule object
    schedule.deleteTaskByName(taskName);
  }

  
  // get schedule from Schedule class -- Brian Kang
  // public ArrayList<Task> getSchedule() {
  //   return schedule.getTasks();
  // }
}
