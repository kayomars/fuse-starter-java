package org.galatea.starter.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// NEED ERROR CHECKING FOR WHEN FUTURE DATES ARE PROVIDED
public class DateTimeUtils {

  // Creating all relevant holidays
  static Set<String> allHolidays = new HashSet<>(Arrays.asList("2020-12-25", "2020-11-26",
      "2020-09-07", "2020-07-03", "2020-05-25", "2020-04-10", "2020-02-17", "2020-01-20",
      "2020-01-01",
      "2019-12-25", "2019-11-22", "2019-09-03", "2019-07-04", "2019-05-28", "2019-03-30",
      "2019-02-19", "2019-01-15", "2019-01-01"));

  /**
   * Given the number of days N as an int, it will find the date N days ago and return it
   * in a YYYY-MM-DD format
   * @param n the number of days in Long
   */
  public static String findDateNDaysAgo(final Long n) {

    LocalDate todaysDate = LocalDate.now();
    LocalDate pastDate = todaysDate.minusDays(n);

    return pastDate.toString();
  }

  /**
   * Adds up the number of business days, and returns immediately if it is over 100
   * @param otherDate represents date in YYYY-MM-DD format
   * @return true if number of business days is over 100
   */
  public static boolean isNumOfBusinessDaysOver100(final String otherDate){

    LocalDate todaysDate = LocalDate.now();
    LocalDate prevDate = LocalDate.parse(otherDate);

    // Sanity check
    if (todaysDate.isBefore(prevDate)) { return false;}

    LocalDate curDate = LocalDate.from(todaysDate);
    int totalBusinessDays = 0;

    while (curDate.isAfter(prevDate)) {
      DayOfWeek dow = curDate.getDayOfWeek();
      if (!(dow.equals(DayOfWeek.SATURDAY) || dow.equals(DayOfWeek.SUNDAY) || allHolidays
          .contains(curDate.toString()))) {
        totalBusinessDays += 1;
      }

      if (totalBusinessDays > 100) { return true; }

      curDate = curDate.minusDays(1);
    }

    return false;

  }

  // THINK ABOUT CASE WHERE ITS BEFORE THE MARKET OPENS
  /**
   * Given a date in YYYY-MM-DD format find the number of business days between now and that date.
   * Calculations will not include that date in the count, but will include today.
   * @param otherDate represents date in YYYY-MM-DD format
   * @return the number of business days between now and then
   */
  public static Long numOfBusinessDaysFromToday(final String otherDate) {

    LocalDate todaysDate = LocalDate.now();
    LocalDate prevDate = LocalDate.parse(otherDate);

    // Check to see if it is the same day
    if (todaysDate.isEqual(prevDate)) { return (long) 0;}

    // Sanity check
    if (todaysDate.isBefore(prevDate)) { return (long) 0;}

    long totalBusinessDays = ChronoUnit.DAYS.between(prevDate, todaysDate);

    LocalDate curDate = LocalDate.from(prevDate);

    while (curDate.isBefore(todaysDate)) {
      DayOfWeek dow = curDate.getDayOfWeek();
      if (dow.equals(DayOfWeek.SATURDAY) || dow.equals(DayOfWeek.SUNDAY) || allHolidays
          .contains(curDate.toString())) {
        totalBusinessDays -= 1;
      }
      curDate = curDate.plusDays(1);
    }

    return totalBusinessDays;
  }


}
