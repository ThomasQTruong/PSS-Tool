package test;

import main.Task;
import main.Schedule;
import main.AntiTask;
import main.RecurringTask;

/**
 * Used for unit testing the Task class.
 */
public class TestTask {
  public static void main(String[] args) {

    testLinking();
  
    if(testLinking()){
      System.out.println("testLinking() is successful!");
    }

    
  }

  public static boolean testLinking(){
    //create recurring task
    //create antitask
    //adds them to list of schedule
    //uses getAntiTask / getRecurringTask to find their respective linked task/

    boolean success = true;
    Schedule schedule = new Schedule();

    Task running = schedule.createTask(1, "Running" , "Visit", 1.25f, 1.25f , 20230609 , 20230709 , 4);
    Task noRunning = schedule.createTask(0, "I Hate Running " , "Visit" , 1.25f, 1.25f , 20230609, 20230609, 1);

    schedule.addTask(running);
    schedule.addTask(noRunning);

    if(((RecurringTask) running).getAntiTask() == null || ((AntiTask) noRunning).getRecurringTask() == null){
      success = false;
    }

    schedule.deleteTask(noRunning);


    if(((RecurringTask) running).getAntiTask() != null){
      success = false;
    }

    return success;
  }

}
 