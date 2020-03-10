package org.galatea.starter.utils;

import java.time.LocalDate;
import java.util.List;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.domain.DailyPrices;
import org.galatea.starter.service.AlphaVantageResponse;
import org.galatea.starter.service.StockPricesService;
import org.galatea.starter.translators.AlphaVantageResponseTranslator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HolidayChecker {

  @NonNull
  private StockPricesService stockPricesService;

  @NonNull
  private AlphaVantageResponseTranslator avTranslator;

  // Fires at 9.30AM every day
  @Scheduled(cron = "0 30 9 * * ?", zone = "EST")
  public void doHolidayRelatedActions() {

    if (isTodayHoliday()) {
      DateTimeUtils.addToHolidays(LocalDate.now().toString());
    }

  }

  /**
   * Checks if today is a holiday by checking if AlphaVantage has a relevant response
   * for today
   * @return boolean indicating whether it is a holiday or not
   */
  public boolean isTodayHoliday() {

    log.info("Checking if today is a holiday from AV using symbol: {} ", "MSFT");
    AlphaVantageResponse thisAlphaResponse = stockPricesService.getStockPricesFromAVCompact( "MSFT");
    List<DailyPrices> allDailyPrices = avTranslator.createAllDailyPricesObjects(thisAlphaResponse);

    String mostRecentDate = allDailyPrices.get(0).getRelatedDate();

    return LocalDate.now().equals(LocalDate.parse(mostRecentDate));

  }

}
