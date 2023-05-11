package test;

import main.Schedule;
import main.TransientTask;
import main.Task;
/**
 * Used for unit testing the Schedule class.
 */
public class TestSchedule {
  public static void main(String[] args) {
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

    //test task is added to schedule
    System.out.println("Testing adding a new task to schedule");
    boolean added = schedule.addTask(transientTask);
    System.out.println("Expected true got " + added);

    //test to see if same task can be added again
    System.out.println("Testing adding same task to schedule");
    boolean addedAgain = schedule.addTask(transientTask);
    System.out.println("Expected false got " + addedAgain);

    //test to see if schedule can remove task
    System.out.println("Testing removing of a task");
    boolean removed = schedule.deleteTask(transientTask);
    System.out.println("Expected true got " + removed);

    //test to see if a schedule contains a task
    schedule.addTask(transientTask);
    schedule.addTask(transientTask2);
    System.out.println("Testing schedule contains a task");
    Task returnedTask = schedule.getByName(transientTask2.getName());
    System.out.println("Expected task to equal task searched. Result: " + (returnedTask.equals(transientTask2)));
  }
}
