package org.galatea.starter.service;

import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.domain.DailyPrices;
import org.galatea.starter.domain.rpsy.IStocksRpsy;
import org.galatea.starter.translators.AlphaVantageResponseTranslator;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
  public AlphaVantageResponse getStockPrices(final String stockSymbol, final List<Integer> numDays) {

    // TODO: Extract numDays from list, default to 100 if not provided

    AlphaVantageResponse thisAlphaResponse = getStockPricesFromAVAll(stockSymbol);

    // Creating all the Daily Prices objects
    List<DailyPrices> allDailyPrices = avTranslator.createAllDailyPricesObjects(thisAlphaResponse);

    //Saving all to DB
    stocksRpsyService.saveAllEntities(allDailyPrices);

    // Testing
    log.info("reached here");
    log.info(stocksRpsyService.findEntriesInDescDateOrder(stockSymbol).toString());

    return thisAlphaResponse;
  }

  /**
   * Get all prices of a specific stock for the last 100 days, or possibly less if the stock is
   * newly listed.
   * @param stockSymbol, a String representing the symbol of the stock
   * @param numDays, an int representing the number of days of prices to fetch
   * @return a list of all prices for that stock, for <= 100 days
   */
  public AlphaVantageResponse getStockPricesFromAVForDays(final String stockSymbol, final int numDays) {
    return alphaVClient.getPricesOfStockForDays(stockSymbol);
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
