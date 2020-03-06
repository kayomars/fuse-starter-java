package org.galatea.starter.testutils;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.galatea.starter.utils.DateTimeUtils;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ActiveProfiles("test")
public class DateTimeUtilsTest {

  final LocalDate localDateToday = LocalDate.now();

  @Test
  public void testFindDateNDaysAgoPosDate() {
    assertEquals(localDateToday.minusDays((long) 1).toString(), DateTimeUtils.findDateNDaysAgo((long) 1));
    assertEquals(localDateToday.minusDays((long) 31).toString(), DateTimeUtils.findDateNDaysAgo((long) 31));
    assertEquals(localDateToday.minusDays((long) 365).toString(), DateTimeUtils.findDateNDaysAgo((long) 365));
    assertEquals(localDateToday.minusDays((long) 1000).toString(), DateTimeUtils.findDateNDaysAgo((long) 1000));
  }

  @Test
  public void testFindDateNDaysAgoZero() {
    assertEquals(localDateToday.toString(), DateTimeUtils.findDateNDaysAgo((long) 0));
  }


  @Test
  public void testFindDateNDaysAgoNegDate() {
    assertEquals(localDateToday.plusDays((long) 1).toString(), DateTimeUtils.findDateNDaysAgo((long) -1));
    assertEquals(localDateToday.plusDays((long) 10).toString(), DateTimeUtils.findDateNDaysAgo((long) -10));
    assertEquals(localDateToday.plusDays((long) 100).toString(), DateTimeUtils.findDateNDaysAgo((long) -100));
  }

  @Test
  public void testNumOfBusinessDaysFromTodaySameDay() {
    LocalDate currentDate = LocalDate.now();
    assertEquals((long) 0, (long) DateTimeUtils.numOfBusinessDaysFromToday(currentDate.toString()));
  }

}
