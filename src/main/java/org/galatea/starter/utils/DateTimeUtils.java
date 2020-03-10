package org.galatea.starter.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.domain.DailyPrices;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DateTimeUtils {


  @Setter
  @Getter
  static Set<String> allHolidays = new HashSet<>();

  /**
   * Used to initialize static list of all holidays that occurred since 100 business
   * dats ago
   * @param allDailyPrices list of all DailyPrices objects used in the initialization
   */
  public static void initializeHolidays(List<DailyPrices> allDailyPrices) {

    for (int i = allDailyPrices.size() - 1; i > 0; i--) {

      // First date needs to be incremented so it isnt included in datesUntil calcs used below
      LocalDate firstDate = LocalDate.parse(allDailyPrices.get(i).getRelatedDate()).plusDays(1);
      LocalDate secondDate = LocalDate.parse(allDailyPrices.get(i - 1).getRelatedDate());

      List<LocalDate> dates = firstDate.datesUntil(secondDate).collect(Collectors.toList());

      for (LocalDate thisDate : dates) {

        DayOfWeek dow = thisDate.getDayOfWeek();
        if (!(dow.equals(DayOfWeek.SATURDAY) || dow.equals(DayOfWeek.SUNDAY))) {
          addToHolidays(thisDate.toString());
        }
      }
    }
  }


  /**
   * Adds a date to set of holidays
   * @param thisDate the date in YYYY-MM-DD format
   */
  public static void addToHolidays(String thisDate) {
    allHolidays.add(thisDate);
  }


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
