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
}
