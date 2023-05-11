package main;

import java.util.Calendar;
import java.util.HashSet;
import java.lang.Math;

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
    return (getCurrentYear() * 10000) + (getCurrentMonth() * 100) + getCurrentDay();
  }


  /**
   * Retrieves the current year.
   *
   * @return int - the current year.
   */
  public static int getCurrentYear() {
    return Calendar.getInstance().get(Calendar.YEAR);
  }


  /**
   * Retrieves the current month.
   *
   * @return int - the current month.
   */
  public static int getCurrentMonth() {
    return Calendar.getInstance().get(Calendar.MONTH);
  }


  /**
   * Retrieves the current day.
   *
   * @return int - the current day.
   */
  public static int getCurrentDay() {
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
    int year = getYearFromYYYYMMDD(date);
    int month = getMonthFromYYYYMMDD(date);
    int day = getDayFromYYYYMMDD(date);

    // Check if year, month, and day are valid.
    if (year > 9999) {
      return false;
    }
    if (month < 1 || month > 12) {
      return false;
    }
    if (day < 1 || day > getDaysInMonth(year, month)) {
      return false;
    }
    
    // Valid month and day.
    return true;
  }


  /**
   * Extracts the year from a YYYYMMDD formatted date.
   *
   * @param date - the YYYYMMDD formatted date to extract from.
   * @return int - the year extracted.
   */
  public static int getYearFromYYYYMMDD(int date) {
    return date / 10000;
  }


  /**
   * Extracts the month from a YYYYMMDD formatted date.
   *
   * @param date - the YYYYMMDD formatted date to extract from.
   * @return int - the month extracted.
   */
  public static int getMonthFromYYYYMMDD(int date) {
    return (date % 10000) / 100;
  }


  /**
   * Extracts the day from a YYYYMMDD formatted date.
   *
   * @param date - the YYYYMMDD formatted date to extract from.
   * @return int - the day extracted.
   */
  public static int getDayFromYYYYMMDD(int date) {
    return date % 100;
  }


  /**
   * Increments the year of a YYYYMMDD formatted date by 1.
   *
   * @param date - the YYYYMMDD formatted date.
   * @return int - the incremented date.
   */
  public static int increaseYearForYYYYMMDD(int date) {
    // At max range, dont increase.
    if (getYearFromYYYYMMDD(date) == 9999) {
      return date;
    }
    
    return date += 10000;
  }


  /**
   * Increments the year of a YYYYMMDD formatted date by xNumberOfYears.
   *
   * @param date - the YYYYMMDD formatted date.
   * @param xNumberOfYears - the number of years to increment by.
   * @return int - the incremented date.
   */
  public static int increaseYearForYYYYMMDD(int date, int xNumberOfYears) {
    // At max range, dont increase.
    if (getYearFromYYYYMMDD(date) + xNumberOfYears > 9999) {
      return date;
    }
    
    return date += (xNumberOfYears * 10000);
  }


  /**
   * Decrements the year of a YYYYMMDD formatted date by 1.
   *
   * @param date - the YYYYMMDD formatted date.
   * @return int - the decremented date.
   */
  public static int decreaseYearForYYYYMMDD(int date) {
    // At min range, dont increase.
    if (getYearFromYYYYMMDD(date) == 1000) {
      return date;
    }
    
    return date -= 10000;
  }


  /**
   * Decrements the year of a YYYYMMDD formatted date by xNumberOfYears.
   *
   * @param date - the YYYYMMDD formatted date.
   * @param xNumberOfYears - the number of years to decrement by.
   * @return int - the decremented date.
   */
  public static int decreaseYearForYYYYMMDD(int date, int xNumberOfYears) {
    // At min range, dont increase.
    if (getYearFromYYYYMMDD(date) - xNumberOfYears < 1000) {
      return date;
    }
    
    return date -= (xNumberOfYears * 10000);
  }


  /**
   * Sets the month for a YYYYMMDD formatted date to a specific month.
   *
   * @param date - the date to change the month for.
   * @param month - the month to change to.
   * @return int - the date with the month changed.
   */
  public static int setMonthForYYYYMMDD(int date, int month) {
    // Invalid month given.
    if (month < 1 || month > 12) {
      return -1;
    }

    // Remove current month.
    date -= (getMonthFromYYYYMMDD(month) * 100);

    // Add new month.
    date += (month * 100);

    return date;
  }


  /**
   * Sets the day for a YYYYMMDD formatted date.
   *
   * @param date - the YYYYMMDD formatted date.
   * @param day - the day to set to.
   * @return int - the date with the day set.
   */
  public static int setDayForYYYYMMDD(int date, int day) {
    // Invalid day given.
    if (day < 1 || day > getDaysInMonth(getYearFromYYYYMMDD(date), getMonthFromYYYYMMDD(date))) {
      return -1;
    }

    // Reset day to 0.
    date = date - (date % 100);
    // Add day.
    date += day;

    return date;
  }


  /**
   * Increases the month of a YYYYMMDD formatted date by 1.
   *
   * @param date - the YYYYMMDD formatted date.
   * @return int - the incremented date.
   *//**
   * Increases the month of a YYYYMMDD formatted date by 1.
   *
   * @param date - the YYYYMMDD formatted date.
   * @return int - the incremented date.
   */
  public static int increaseMonthForYYYYMMDD(int date) {
    // Is at the last month.
    if (getMonthFromYYYYMMDD(date) == 12) {
      // Increment year.
      date = increaseYearForYYYYMMDD(date);
      // Set month to first month.
      date = setMonthForYYYYMMDD(date, 1);
    } else {  // Not last month.
      date = increaseMonthForYYYYMMDD(date);
    }

    return date;
  }


  /**
   * Increases the month of a YYYYMMDD formatted date by xNumberOfMonths.
   *
   * @param date - the YYYYMMDD formatted date.
   * @param xNumberOfMonths - the number of months to increase by.
   * @return int - the incremented date.
   */
  public static int increaseMonthForYYYYMMDD(int date, int xNumberOfMonths) {
    int totalMonth = getMonthFromYYYYMMDD(date) + xNumberOfMonths;

    // Over the cap.
    if (totalMonth > 12) {
      // Increment year by amount of 12 months passed.
      date = increaseYearForYYYYMMDD(date, totalMonth / 12);
      // Set month to proper month.
      if (totalMonth % 12 == 0) {
        date = setMonthForYYYYMMDD(date, 12);
      } else {
        date = setMonthForYYYYMMDD(date, totalMonth % 12);
      }
    } else {  // Not last month.
      date = increaseMonthForYYYYMMDD(date, xNumberOfMonths);
    }

    return date;
  }


  /**
   * Decreases the month of a YYYYMMDD formatted date by 1.
   *
   * @param date - the YYYYMMDD formatted date.
   * @return int - the decremented date.
   */
  public static int decreaseMonthForYYYYMMDD(int date) {
    // Is at the first month.
    if (getMonthFromYYYYMMDD(date) == 1) {
      // Decrement year.
      date = decreaseYearForYYYYMMDD(date);
      // Set month to last month.
      date = setMonthForYYYYMMDD(date, 12);
    } else {  // Not first month.
      date = decreaseMonthForYYYYMMDD(date);
    }

    return date;
  }


  /**
   * Decreases the month of a YYYYMMDD formatted date by xNumberOfMonths.
   *
   * @param date - the YYYYMMDD formatted date.
   * @param xNumberOfMonths - the number of months to decrease by.
   * @return int - the decremented date.
   */
  public static int decreaseMonthForYYYYMMDD(int date, int xNumberOfMonths) {
    int totalMonth = getMonthFromYYYYMMDD(date) - xNumberOfMonths;

    // Under the min.
    if (totalMonth < 1) {
      totalMonth = xNumberOfMonths - getMonthFromYYYYMMDD(date);
      // Decrease year by amount of 12 months reverted.
      date = increaseYearForYYYYMMDD(date, 1 + (totalMonth / 12));
      // Set month to proper month.
      if (totalMonth % 12 == 0) {
        date = setMonthForYYYYMMDD(date, 12);
      } else {
        date = setMonthForYYYYMMDD(date, totalMonth % 12);
      }
    } else {  // Not last month.
      date = increaseMonthForYYYYMMDD(date, xNumberOfMonths);
    }

    return date;
  }


  /**
   * Increase the day of a YYYYMMDD formatted date by 1.
   *
   * @param date - the YYYYMMDD formatted date.
   * @return int - the incremented date.
   */
  public static int increaseDayForYYYYMMDD(int date) {
    // Add 1 day to date.
    date += 1;
    
    // No need to fix.
    if (isValidYYYYMMDD(date)) {
      return date;
    }

    // Need to fix; increase month by one.
    date = increaseMonthForYYYYMMDD(date);
    // Set to the first day of the month.
    date = setDayForYYYYMMDD(date, 1);

    return date;
  }


  /**
   * Increase the day of a YYYYMMDD formatted date by xNumberOfDays.
   *
   * @param date - the YYYYMMDD formatted date.
   * @param xNumberOfDays - the number of days to increase by.
   * @return int - the incremented date or -1 if failed.
   */
  public static int increaseDayForYYYYMMDD(int date, int xNumberOfDays) {
    // Out of range.
    if (xNumberOfDays < 1 || xNumberOfDays + getDayFromYYYYMMDD(date) > 99) {
      return -1;
    }

    // Add xNumberOfDays day to date.
    date += xNumberOfDays;
    
    // No need to fix.
    if (isValidYYYYMMDD(date)) {
      return date;
    }

    // While there are still days to change.
    while (xNumberOfDays > 0) {
      // Get values.
      int year = getYearFromYYYYMMDD(date);
      int month = getMonthFromYYYYMMDD(date);
      int day = getDayFromYYYYMMDD(date);
      int totalDays = getDaysInMonth(year, month);
      
      // day goes over to next month.
      if (day > totalDays) {
        date -= totalDays;
        xNumberOfDays -= totalDays;
        date = increaseMonthForYYYYMMDD(date);
      }
    }

    return date;
  }


  /**
   * Decrease the day of a YYYYMMDD formatted date by 1.
   *
   * @param date - the YYYYMMDD formatted date.
   * @return int - the decremented date.
   */
  public static int decreaseDayForYYYYMMDD(int date) {
    // Remove 1 day to date.
    date -= 1;
    
    // No need to fix.
    if (isValidYYYYMMDD(date)) {
      return date;
    }

    // Need to fix; increase month by one.
    date = decreaseMonthForYYYYMMDD(date);
    // Set to the last day of the month.
    int year = getYearFromYYYYMMDD(date);
    int month = getMonthFromYYYYMMDD(date);
    date = setDayForYYYYMMDD(date, getDaysInMonth(year, month));

    return date;
  }


  /**
   * Checks if two times are conflicting.
   *
   * @param startTime1 - the first starting time.
   * @param endTime1 - the first ending time.
   * @param startTime2 - the second starting time.
   * @param endTime2 - the second end time.
   * @return boolean - whether the times are conflicting or not.
   */
  public static boolean areTimesOverlapping(float startTime1, float endTime1,
                                            float startTime2, float endTime2) {
    // Convert to int to try to minimize floating arithmetic errors.
    int start1 = (int) Math.round(startTime1 * 100);
    int end1 = (int) Math.round(endTime1 * 100);
    int start2 = (int) Math.round(startTime2 * 100);
    int end2 = (int) Math.round(endTime2 * 100);

    // Get the possible times for the ranges (15 minute intervals).
    // End times are excluded.
    HashSet<Integer> timeRange1 = new HashSet<>();
    for (int start = start1; start < end1; start += 25) {
      timeRange1.add(start);
    }
    HashSet<Integer> timeRange2 = new HashSet<>();
    for (int start = start2; start < end2; start += 25) {
      timeRange2.add(start);
    }

    // For every time in timeRange2.
    for (int time : timeRange2) {
      // If time exists in timeRange1: time overlaps.
      if (timeRange1.contains(time)) {
        return true;
      }
    }

    // Does not overlap.
    return false;
  }
}
