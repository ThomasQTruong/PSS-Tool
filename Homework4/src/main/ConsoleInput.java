package main;

import java.util.Scanner;

/**
 * ConsoleInput.java
 * Provides handy methods for accepting inputs in the console.
 *
 * <p>Copyright (c) 2021, Thomas Truong.</p>
 */
public class ConsoleInput {
  private static Scanner input = new Scanner(System.in);
  private static Scanner stringInput = new Scanner(System.in);

  /**
   * Asks the user to input an integer.
   * Invalid inputs--Strings and doubles--will not cause errors.
   *
   * @return int - the integer received from the user.
   */
  public static int getInt() {
    // Prompt user.
    System.out.println("Enter a number.");
    System.out.print("Input: ");
    // Ask for input and loop if not an integer; if valid, skip loop.
    while (!input.hasNextInt()) {
      System.out.print("Input: ");
      // Eat up the invalid input.
      input.nextLine();
    }
    // Return the correct input.
    return input.nextInt();
  }


  /**
   * Asks the user to input an integer with a custom prompt.
   * Invalid inputs--Strings and doubles--will not cause errors.
   *
   * @param prompt - the String used for asking the user for input.
   * @return int - the integer received from the user.
   */
  public static int getInt(String prompt) {
    // Prompt user.
    System.out.println(prompt);
    System.out.print("Input: ");
    // Ask for input and loop if not an integer; if valid, skip loop.
    while (!input.hasNextInt()) {
      System.out.print("Input: ");
      // Eat up the invalid input.
      input.nextLine();
    }
    // Return the correct input.
    return input.nextInt();
  }


  /**
   * Asks the user to input an integer within a range.
   * Returns an integer out of the range if invalid min/max.
   * Invalid inputs--Strings and doubles--will not cause errors.
   *
   * @param min - the minimum value the int can be.
   * @param max - the maximum value the int can be.
   * @return int - the int within the range.
   */
  public static int getIntRange(int min, int max) {
    // Invalid min/max; return an integer out of the range.
    if (min >= max) {
      return min + 1;
    }

    // The user's input.
    int userInput;

    // Prompt user.
    System.out.printf("Enter a number that is between or equal to %d and %d.\n", min, max);

    // Repeat the process if number isnt within the range.
    do {
      System.out.print("Input: ");
      // Ask for input and loop if not an integer; if valid, skip loop.
      while (!input.hasNextInt()) {
        System.out.print("Input: ");
        // Eat up the invalid input.
        input.nextLine();
      }
      // Return the correct input.
      userInput = input.nextInt();
    } while (userInput < min || userInput > max);
    
    return userInput;
  }


  /**
   * Asks the user to input an integer within a range with a custom prompt.
   * Returns an integer out of the range if invalid min/max.
   * Invalid inputs--Strings and doubles--will not cause errors.
   *
   * @param prompt - the String used for asking the user for input.
   * @param min - the minimum value the int can be.
   * @param max - the maximum value the int can be.
   * @return int - the int within the range.
   */
  public static int getIntRange(String prompt, int min, int max) {
    // Invalid min/max; return an integer out of the range.
    if (min >= max) {
      return min + 1;
    }

    // The user's input.
    int userInput;

    // Prompt user.
    System.out.println(prompt);

    // Repeat the process if number isnt within the range.
    do {
      System.out.print("Input: ");
      // Ask for input and loop if not an integer; if valid, skip loop.
      while (!input.hasNextInt()) {
        System.out.print("Input: ");
        // Eat up the invalid input.
        input.nextLine();
      }
      // Return the correct input.
      userInput = input.nextInt();
    } while (userInput < min || userInput > max);
    
    return userInput;
  }


  /**
   * Asks the user to input a character.
   *
   * @return char - the character that the user inputted.
   */
  public static char getChar() {
    // Prompt user.
    System.out.println("Enter a letter.");
    System.out.print("Input: ");

    // Return the first character in the input.
    return input.next().charAt(0);
  }


  /**
   * Asks the user to input a character using a prompt.
   *
   * @param prompt - the prompt to use to ask the user for input.
   * @return char - the character that the user inputted.
   */
  public static char getChar(String prompt) {
    System.out.println(prompt);
    System.out.print("Input: ");

    return input.next().charAt(0);
  }


  /**
   * Asks the user to input a string using a prompt.
   *
   * @param prompt - the prompt to use to ask the user for input.
   * @return String - the string that the user inputted.
   */
  public static String getString(String prompt) {
    System.out.println(prompt);
    System.out.print("Input: ");

    return stringInput.nextLine();
  }


  /**
   * Asks the user to input a float using a prompt.
   *
   * @param prompt - the prompt to use to ask the user for input.
   * @return float - the float that the user inputted.
   */
  public static float getFloat(String prompt) {
    // Prompt user.
    System.out.println(prompt);
    System.out.print("Input: ");
    // Ask for input and loop if not an float; if valid, skip loop.
    while (!input.hasNextFloat()) {
      System.out.print("Input: ");
      // Eat up the invalid input.
      input.nextLine();
    }
    // Return the correct input.
    return input.nextFloat();
  }


  /**
   * Asks the user to input an float within a range with a custom prompt.
   * Returns an float out of the range if invalid min/max.
   * Invalid inputs--Strings--will not cause errors.
   *
   * @param prompt - the String used for asking the user for input.
   * @param min - the minimum value the float can be.
   * @param max - the maximum value the float can be.
   * @return float - the float within the range.
   */
  public static float getFloatRange(String prompt, float min, float max) {
    // Invalid min/max; return an integer out of the range.
    if (min >= max) {
      return min + 1;
    }

    // The user's input.
    float userInput;

    // Prompt user.
    System.out.println(prompt);

    // Repeat the process if number isnt within the range.
    do {
      System.out.print("Input: ");
      // Ask for input and loop if not an integer; if valid, skip loop.
      while (!input.hasNextFloat()) {
        System.out.print("Input: ");
        // Eat up the invalid input.
        input.nextLine();
      }
      // Return the correct input.
      userInput = input.nextFloat();
    } while (userInput < min || userInput > max);
    
    return userInput;
  }


  /**
   * Closes the input scanners.
   */
  public static void closeScanners() {
    input.close();
    stringInput.close();
  }
}
