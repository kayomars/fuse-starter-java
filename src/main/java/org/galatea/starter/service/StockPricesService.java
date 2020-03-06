package org.galatea.starter.service;

import org.galatea.starter.utils.DateTimeUtils;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.domain.DailyPrices;
import org.galatea.starter.translators.AlphaVantageResponseTranslator;
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

  // injecting in stocks repository services to make relevant DB actions
  @NonNull
  private StocksRpsyService stocksRpsyService;

  // ALL THE LOGIC FOR DATABASE PULLS OR API PULLS WILL GO HERE
  /**
   * Function that will be the starting point of the logic chain that will
   * be constructed next. Currently queries AV API, stores all to DB and then pulls all from DB
   */
  public void getStockPrices(final String stockSymbol, final int numDays) {

    boolean isAllDataThere = false;
    boolean isFullCallNeeded = false;

    // First getting info from DB
    List<DailyPrices> retrievedFromDB = stocksRpsyService.findEntriesInDescDateOrder(stockSymbol);

    if (!retrievedFromDB.isEmpty()) {

      // Freshness check
      String dateOfRecentEntry = retrievedFromDB.get(0).getRelatedDate();
      Long businessDaysSinceRecentEntry = DateTimeUtils.numOfBusinessDaysFromToday(dateOfRecentEntry);
      isAllDataThere = (businessDaysSinceRecentEntry <= 0);
      isFullCallNeeded = (businessDaysSinceRecentEntry > 100);

      // Checking date of last entry
      String dateOfLastEntry = retrievedFromDB.get(retrievedFromDB.size() - 1).getRelatedDate();

      // Check how many days ago last entry represents
      Long daysOld = DateTimeUtils.findNumofAllDaysFromDate(dateOfLastEntry);

      // We have all the info in the DB
      if (daysOld >= numDays) {
        log.info("All needed data found in DB");
      } else {
        log.info("Need to possibly pull from AV");
        //TODO insert check for last date overall and then
        isAllDataThere = false;
        isFullCallNeeded = true;
      }
    }

    // Deal with pulls from AV
    if (!isAllDataThere) {
      AlphaVantageResponse thisAlphaResponse;
      if (isFullCallNeeded) {
        log.info("Doing a full pull");
        thisAlphaResponse = getStockPricesFromAVAll(stockSymbol);
      } else {
        log.info("Doing a compact pull");
        thisAlphaResponse = getStockPricesFromAVCompact(stockSymbol);
      }

      // Creating all the Daily Prices objects
      List<DailyPrices> allDailyPrices = avTranslator.createAllDailyPricesObjects(thisAlphaResponse);

      //Saving all to DB
      stocksRpsyService.saveAllEntities(allDailyPrices);
    }

    // Figure out what date n reflects and only return that many entries
    String dateNDaysAgo = DateTimeUtils.findDateNDaysAgo((long)numDays);
    log.info(stocksRpsyService.findEntriesInDescDateOrderWithDateGreaterEqualThan(stockSymbol, dateNDaysAgo).toString());

    return;
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
