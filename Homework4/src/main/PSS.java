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

  // Option 1.
  private static boolean createTask() {
    return true;
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
  public void enterTask(int taskType, String name, float startTime, float duration, 
                      int startDate, int endDate, int frequency) {
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
  public ArrayList<Task> getSchedule() {
    return schedule.getTasks();
  }
}
