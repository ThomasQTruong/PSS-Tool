package main;
import java.util.ArrayList;
/**
 * Placeholder javadoc.
 */
public class PSS {
  private Schedule schedule;

  // public static void main(String[] args) {
  //   int test = ConsoleInput.getIntRange("Enter a number 10 - 20.", 10, 20);
  //   System.out.println(test);
  // }



  //assuming the user is given option to input recurring, transient tasks, and anti task -- Brian Kang
  public void enterTask(int taskType, String name, float startTime, float duration, 
                      int startDate, int endDate, int frequency) {
    Task newTask;
    switch(taskType) {
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




  //user given option to delete task by name --Brian Kang
  public void deleteTask(String taskName) {
    //call the deleteTaskByName method of the Schedule object
    schedule.deleteTaskByName(taskName);
}

  //get schedule from Schedule class -- Brian Kang
  public ArrayList<Task> getSchedule() {
    return schedule.generateSchedule();
  }


}