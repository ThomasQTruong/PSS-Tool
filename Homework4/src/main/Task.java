package main;

/**
 * Template for the different task types.
 */
public interface Task {
  // Type Identifier Constants.
  public static final int RECURRING_TASK = 1;
  public static final int TRANSIENT_TASK = 2;
  public static final int ANTI_TASK = 3;


  // [SETTERS]
  /**
   * Sets the name of the task with a new one.
   *
   * @param name - the new name.
   */
  public void setName(String name);

  /**
   * Sets the type of the task with a new one.
   *
   * @param type - the new type.
   */
  public void setType(String type);

  /**
   * Sets the start time of the task with a new one.
   *
   * @param startTime - the new start time.
   */
  public void setStartTime(float startTime);

  /**
   * Sets the duration of the task with a new one.
   *
   * @param duration - the new duration.
   */
  public void setDuration(float duration);

  /**
   * Sets the start date of the task with a new one.
   *
   * @param startDate - the new start date.
   */
  public void setStartDate(int startDate);


  // [GETTERS]
  /**
   * Retrieves the identity.
   *
   * @return int - the identity.
   */
  public int getIdentity();

  /**
   * Retrieves the available task types.
   *
   * @return String[] - the task types.
   */
  public String[] getTypes();

  /**
   * Retrieves the length of the available task types.
   *
   * @return int - the length of the available task types.
   */
  public int getTypeLength();

  /**
   * Retrieves the name.
   *
   * @return String - the name.
   */
  public String getName();

  /**
   * Retrieves the type.
   *
   * @return String - the type.
   */
  public String getType();

  /**
   * Retrieves the start time.
   *
   * @return float - the start time.
   */
  public float getStartTime();

  /**
   * Retrieves the duration.
   *
   * @return float - the duration.
   */
  public float getDuration();

  /**
   * Retrieves the start date.
   *
   * @return int - the start date.
   */
  public int getStartDate();
}
