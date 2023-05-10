package main;

import java.util.Calendar;

/**
 * Utility class for Date and Time related methods.
 */
public class DateAndTime {
  /**
   * Rounds the minutes in the time to the nearest 15 minutes.
   *
   * @param toRound - the time to round the minutes for.
   * @return float - the rounded time.
   */
  public static float roundMinutesToNearest15(float toRound) {
    // Already nearest 15 minutes.
    if (toRound % 0.25 == 0) {
      return toRound;
    }

    // Safer float arithmetic compared to (int) (toRound % 1 * 100).
    int minutes = (int) (toRound * 100) - (int) (toRound) * 100;
    // ((Round down to nearest 25)) + 25.
    minutes = ((minutes / 25) * 25) + 25;
    
    // Add to toRound the minutes in float.
    toRound = (int) toRound + (minutes / 100.0f);

    return toRound;
  }


  /**
   * Retrieves the current year.
   *
   * @return int - the current year.
   */
  public static int getYear() {
    return Calendar.getInstance().get(Calendar.YEAR);
  }


  /**
   * Retrieves the current month.
   *
   * @return int - the current month.
   */
  public static int getMonth() {
    return Calendar.getInstance().get(Calendar.MONTH);
  }


  /**
   * Retrieves the current day.
   *
   * @return int - the current day.
   */
  public static int getDay() {
    return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
  }
}
