package test;

import java.util.ArrayList;
import main.Schedule;
import main.Task;
import main.TransientTask;

/**
 * Used for unit testing the Schedule class.
 */
public class TestSchedule {
  public static void main(String[] args) {
    // Most of testing for schedule is done in the main program.
    
    // Test_Richard().
    System.out.println("Testing Richard's cases.");
    Test_Richard();
    System.out.println("=Test_Richard() END=\n");


    // Test_getTasksInRange().
    System.out.println("Testing Test_getTasksInRange().");
    if (Test_getTasksInRange()) {
      System.out.println("Test_getTasksInRange() passed all cases.");
    }
  }

  // Richard's test.
  public static void Test_Richard() {
    //initialize task and schedule
    Schedule schedule = new Schedule();
    TransientTask transientTask = new TransientTask();
    TransientTask transientTask2 = new TransientTask();

    transientTask.setName("Doctor Visit");
    transientTask.setType("Appointment");
    transientTask.setStartDate(20230812);
    transientTask.setStartTime(8.00f);
    transientTask.setDuration(1.00f);

    transientTask2.setName("Look at cars");
    transientTask2.setType("Shopping");
    transientTask2.setStartDate(20230627);
    transientTask2.setStartTime(12.35f);
    transientTask2.setDuration(14.35f);

    // test task is added to schedule 
    boolean added = schedule.addTask(transientTask);
    if (!added) {
      System.out.println("Testing adding a new task to schedule");
      System.out.println("Expected true got " + added);
    }

    //test to see if same task can be added again
    boolean addedAgain = schedule.addTask(transientTask);
    if (addedAgain) {
      System.out.println("Testing adding same task to schedule");
      System.out.println("Expected false got " + addedAgain);
    }

    //test to see if schedule can remove task
    boolean removed = schedule.deleteTask(transientTask);
    if (!removed) {
      System.out.println("Testing removing of a task");
      System.out.println("Expected true got " + removed);
    }

    //test to see if a schedule contains a task
    schedule.addTask(transientTask);
    schedule.addTask(transientTask2);
    Task returnedTask = schedule.getByName(transientTask2.getName());
    boolean equalResult = returnedTask.equals(transientTask2);
    if (!equalResult) {
      System.out.println("Testing schedule contains a task");
      System.out.println("Expected task to equal task searched. Result: "
                          + (returnedTask.equals(transientTask2)));
    }
  }

  public static boolean Test_getTasksInRange() {
    Schedule schedule = new Schedule();
    schedule.loadFromJson("../../test/getDatesInRange.json");
    boolean fullSuccess = true;

    ArrayList<Task> tasksInRange1 = schedule.getTasksInRange(20230514, 20230519);
    if (tasksInRange1.size() != 5) {
      System.out.println("tasksInRange1: expected 5, but got " + tasksInRange1.size());
      fullSuccess = false;
    }

    ArrayList<Task> tasksInRange2 = schedule.getTasksInRange(20230514, 20230515);
    if (tasksInRange2.size() != 4) {
      System.out.println("tasksInRange2: expected 4, but got " + tasksInRange2.size());
      fullSuccess = false;
    }

    // Testing 2023 to 2025 (includes everything).
    ArrayList<Task> tasksInRange3 = schedule.getTasksInRange(20230514, 20250515);
    if (tasksInRange3.size() != 6) {
      System.out.println("tasksInRange3: expected 6, but got " + tasksInRange3.size());
      fullSuccess = false;
    }

    return fullSuccess;
  }
}
