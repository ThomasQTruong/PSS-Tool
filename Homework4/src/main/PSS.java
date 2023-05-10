package main;

import java.util.ArrayList;

/**
 * Placeholder javadoc.
 */
public class PSS {
  private Schedule schedule;


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
          createTask();
          break;
        case 2:
          // View a task.
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
   * Option 1.1: creates the task.
   *
   * @return boolean - whether the task was successfully created or not.
   */
  private static boolean createTask() {
    PSS pss = new PSS();
    int taskIdentity = getTaskIdentity();
    

    // Obtain common task values.
    System.out.println("=Task Setup=");
    String name = ConsoleInput.getString("Enter a name for the task.");
    System.out.println();

    String taskTypeString = getTaskType(taskIdentity);

    float startTime = ConsoleInput.getFloatRange("Enter the starting time of task [0 - 23.75].",
                                             0.0f, 23.75f);

    float duration = ConsoleInput.getFloatRange("Enter the duration of the task [0.25 - 23.75].",
                                                0.25f, 23.75f);



    int startDate = ConsoleInput.getInt("Enter the start date of the task [YYYYMMDD].");

    int endDate = 0;
    int frequency = 0;

    if(taskTypeString.equals("RECURRING_TASK")) {
      endDate = ConsoleInput.getInt("Enter the end date of the task [YYYY/MM/DD].");
      frequency = ConsoleInput.getInt("Enter the frequency of the task in days.");
    }

     //call enterTask() and add to schedule list 
    pss. enterTask(taskTypeString, name, startTime, duration, startDate, endDate, frequency);

    return true;
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
    System.out.println("[0] Cancel");
    int taskIdentity = ConsoleInput.getIntRange("Please choose a task identity.", 0, 3);
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


  // assuming the user is given option to input recurring,
  // transient tasks, and anti task -- Brian Kang
  public void enterTask(String taskTypeString, String name, float startTime, float duration, 
                      int startDate, int endDate, int frequency) {
    //convert taskType string to int
    int taskType;
    switch(taskTypeString){
        case "TRANSIENT_TASK":
            taskType = Task.TRANSIENT_TASK;
            break;
        case "RECURRING_TASK":
            taskType = Task.RECURRING_TASK;
            break;
        case "ANTI_TASK":
            taskType = Task.ANTI_TASK;
            break;
        default:
            System.out.println("Invalid task type.");
            return;

    }
    //use int taskType to sort which task
    Task newTask;
    switch (taskType) {
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
        antiTask.setStartTime(startTime);
        antiTask.setDuration(duration);
        antiTask.setStartDate(startDate);
        schedule.applyAntiTask(antiTask);
        return;
      default:
        System.out.println("Invalid task type.");
        return;
    }
    newTask.setName(name);
    newTask.setStartTime(startTime);
    newTask.setDuration(duration);
    newTask.setStartDate(startDate);
    this.schedule.addTask(newTask);
  }


  // user given option to delete task by name --Brian Kang
  public void deleteTask(String taskName) {
    //call the deleteTaskByName method of the Schedule object
    schedule.deleteTaskByName(taskName);
  }

  
  // get schedule from Schedule class -- Brian Kang
 // public ArrayList<Task> getSchedule() {
 //   return schedule.getTasks();
 // }
}
