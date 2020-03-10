package org.galatea.starter.service;

import org.galatea.starter.domain.rpsy.IStocksRpsy;
import org.galatea.starter.utils.DateTimeUtils;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.domain.DailyPrices;
import org.galatea.starter.translators.AlphaVantageResponseTranslator;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * A layer for transformation, aggregation, and business required when retrieving stock prices from
 * AlphaVantage or our database.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockPricesService {

  // injecting in client to make queries
  @NonNull
  private AlphaVantageClient alphaVClient;

  // injecting in translator to make relevant translations
  @NonNull
  private AlphaVantageResponseTranslator avTranslator;

  // injecting in stocks repository to make relevant DB actions
  @NonNull
  private IStocksRpsy stocksRpsy;

  // Initializes the set of past holidays in the last 100 business days at application ready
  @EventListener(ApplicationReadyEvent.class)
  public void initializeAllHolsOnStartUp() {

    log.info("Initializing allHolidays from AV using symbol: {} ", "MSFT");
    AlphaVantageResponse thisAlphaResponse = getStockPricesFromAVCompact( "MSFT");
    List<DailyPrices> allDailyPrices = avTranslator.createAllDailyPricesObjects(thisAlphaResponse);

    DateTimeUtils.initializeHolidays(allDailyPrices);
  }


  // ALL THE LOGIC FOR DATABASE PULLS OR API PULLS WILL GO HERE
  /**
   * Function that will be the starting point of the logic chain that will
   * be constructed next. Currently queries AV API, stores all to DB and then pulls all from DB
   */
  public List<DailyPrices> getStockPrices(final String stockSymbol, final int numDays) {

    boolean isAllDataThere = false;
    boolean isFullCallNeeded = true;

    List<DailyPrices> mostRecentEntry = stocksRpsy.findTop1ByStockSymbolOrderByRelatedDateDesc(stockSymbol);

    if (!mostRecentEntry.isEmpty()) {

      // Freshness check
      String dateOfRecentEntry = mostRecentEntry.get(0).getRelatedDate();

      isAllDataThere = ( DateTimeUtils.numOfBusinessDaysFromToday(dateOfRecentEntry) <= 0 );
      isFullCallNeeded = DateTimeUtils.isNumOfBusinessDaysOver100(dateOfRecentEntry);
    }

    // Handle pulls from AV
    if (!isAllDataThere) {
      AlphaVantageResponse thisAlphaResponse;
      if (isFullCallNeeded) {
        log.info("Doing a Full Pull from AV for symbol: {}", stockSymbol);
        thisAlphaResponse = getStockPricesFromAVAll(stockSymbol);
      } else {
        log.info("Doing a Compact Pull from AV for symbol: {}", stockSymbol);
        thisAlphaResponse = getStockPricesFromAVCompact(stockSymbol);
      }

      // Creating and saving DailyPrices objects
      List<DailyPrices> allDailyPrices = avTranslator.createAllDailyPricesObjects(thisAlphaResponse);
      stocksRpsy.saveAll(allDailyPrices);
    }

    // Figure out what date numDays reflects and only return entries from that or newer dates
    String dateNDaysAgo = DateTimeUtils.findDateNDaysAgo((long)numDays);

    return stocksRpsy.findByStockSymbolAndRelatedDateGreaterThanEqualOrderByRelatedDateDesc(stockSymbol, dateNDaysAgo);

  }

  /**
   * Get all prices of a specific stock for the last 100 days, or possibly less if the stock is
   * newly listed.
   * @param stockSymbol, a String representing the symbol of the stock
   * @return a list of all prices for that stock, for <= 100 days
   */
  public AlphaVantageResponse getStockPricesFromAVCompact(final String stockSymbol) {
    return alphaVClient.getPricesOfStockCompact(stockSymbol);
  }

  /**
   * Get all prices of a specific stock for the entirety of its history.
   * @param stockSymbol, a String representing the symbol of the stock
   * @return a list of all prices for that stock in all available history
   */
  public AlphaVantageResponse getStockPricesFromAVAll(final String stockSymbol) {
    return alphaVClient.getAllPricesOfStock(stockSymbol);
  }

}
