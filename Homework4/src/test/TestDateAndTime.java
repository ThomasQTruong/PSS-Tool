package test;

import main.DateAndTime;

public class TestDateAndTime {
  public static void main(String[] args) {
    // Test roundMinutesToNearest15()
    System.out.println("Testing: Test_roundMinutesToNearest15()");
    float[] case1 =     {22.74f, 22.75f, 20.01f, 13.76f, 12.51f, 5.12f};
    float[] expected1 = {22.75f, 22.75f, 20.25f, 14.00f, 12.75f, 5.25f};
    if (Test_roundMinutesToNearest15(case1, expected1)) {
      System.out.println("roundMinutesToNearest15() was successful.");
    }


    // Test isLeapYear()
    System.out.println("Testing: isLeapYear()");
    int[] years = {1900, 2000, 2004, 2008, 1800};
    boolean[] expected2 = {false, true, true, true, false};

    Test_isLeapYear(years, expected2);
    if(Test_isLeapYear(years, expected2)) {
      System.out.println("isLeapYear() was successful.");
    }

    // Test isValidYYYYMMDD()
    System.out.println("Testing: isValidYYYYMMDD()");
    int[] dates = {20230521, 9000521, 20040202, 20220202, 18000808};
    boolean[] expected3 = {true, true, true, true, true};

    if(Test_isValidYYYYMMDD(dates, expected3)) {
      System.out.println("isValidYYYYMMDD() was successful.");
    }


  }

  // Tests roundMinutesToNearest15()
  public static boolean Test_roundMinutesToNearest15(float[] cases, float[] expected) {
    boolean success = true;
    for (int i = 0; i < cases.length; ++i) {
      float result = DateAndTime.roundMinutesToNearest15(cases[i]);
      if (result != expected[i]) {
        System.out.printf("roundMinutesToNearest15(%f): expected: %f, got %f.\n", cases[i], expected[i], result);
        success = false;
      }
    }

    return success;
  }

  // Tests isLeapYear()
  public static boolean Test_isLeapYear(int[] years, boolean[] expected) {
    boolean success = true;

    for (int i = 0; i < years.length; ++i) {
      if(DateAndTime.isLeapYear(years[i]) != expected[i]) {
        System.out.println("Year: " + years[i] + " Result:" + DateAndTime.isLeapYear(years[i]) + " Expected:" + expected[i]);
        return success = false;
      }

    }

    return success;
  }

  // Tests isValidYYYYMMDD()
  public static boolean Test_isValidYYYYMMDD(int[] dates, boolean[] expected) {
    boolean success = true;

    for (int i = 0; i < dates.length; ++i) {
      if(DateAndTime.isValidYYYYMMDD(dates[i]) != expected[i]) {
        System.out.println("Date: " + dates[i] + " Result:" + DateAndTime.isValidYYYYMMDD(dates[i]) + " Expected:" + expected[i]);
        return success = false;
      }

    }

    return success;
  }


}