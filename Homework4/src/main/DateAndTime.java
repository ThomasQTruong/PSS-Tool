package main;

import java.util.Calendar;

/**
 * Utility class for Date and Time related methods.
 */
public class DateAndTime {
  public static final int[] DAYS_IN_MONTHS = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

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
   * Get current date in YYYYMMDD format.
   *
   * @return int - the formatted date.
   */
  public static int getCurrentYYYYMMDD() {
    return (getYear() * 10000) + (getMonth() * 100) + getDay();
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


  /**
   * Checks if the year is a leap year.
   *
   * @param year - the year to check.
   * @return boolean - whether the year is a leap year or not.
   */
  public static boolean isLeapYear(int year) {
    // Divisible by 4.
    if (year % 4 == 0) {
      // Divisible by 100.
      if (year % 100 == 0) {
        // Divisible by 400, is a leap year.
        if (year % 400 == 0) {
          return true;
        }
      // Not divisible by 100, is a leap year.
      } else {
        return true;
      }
    }
    return false;
  }


  /**
   * Get the number of days in a month for a specific year.
   *
   * @param year - the year to check for leap.
   * @param month - the month to get the number of days for.
   * @return int - the number of days.
   */
  public static int getDaysInMonth(int year, int month) {
    int days = DAYS_IN_MONTHS[month - 1];

    // Is February and leap year; +1 day.
    if (month == 2 && isLeapYear(year)) {
      ++days;
    }
    return days;
  }


  /**
   * Checks if a YYYYMMDD formatted date is valid.
   *
   * @param date - the formatted date to check.
   * @return boolean - whether the date is valid or not.
   */
  public static boolean isValidYYYYMMDD(int date) {
    // Extract year, month, and date.
    int year = date / 10000;
    int month = (date % 10000) / 100;
    int day = date % 100;

    // Check if month and day are valid.
    if (month < 1 || month > 12) {
      return false;
    }
    if (day < 1 || day > getDaysInMonth(year, month)) {
      return false;
    }
    
    // Valid month and day.
    return true;
  }
}
