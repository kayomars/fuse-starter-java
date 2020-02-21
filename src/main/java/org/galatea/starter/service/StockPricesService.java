package org.galatea.starter.service;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.domain.IexSymbol;
import org.galatea.starter.domain.ListOfDailyPrices;
import org.galatea.starter.domain.MetaDataAndTimeSeries;
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

  // ALL THE LOGIC FOR DATABASE PULLS OR API PULLS WILL GO HERE
  /**
   * Logic is hard
   */
  public ListOfDailyPrices logicChain(final String stockSymbol, final int numDays) {
    return getStockPricesFromAVAll(stockSymbol);
  }

  /**
   * Get all prices of a specific stock for the last 100 days, or possibly less if the stock is
   * newly listed.
   * @param stockSymbol, a String representing the symbol of the stock
   * @param numDays, an int representing the number of days of prices to fetch
   * @return a list of all prices for that stock, for <= 100 days
   */
  public ListOfDailyPrices getStockPricesFromAVForDays(final String stockSymbol, final int numDays) {
    return alphaVClient.getPricesOfStockForDays(stockSymbol);
  }


  // NEED TO KEEP TRACK OF LAST DATE OF KNOWN HISTORY SOMEHOW
  /**
   * Get all prices of a specific stock for the entirety of its history.
   * @param stockSymbol, a String representing the symbol of the stock
   * @return a list of all prices for that stock in all available history
   */
  public ListOfDailyPrices getStockPricesFromAVAll(final String stockSymbol) {
    return alphaVClient.getAllPricesOfStock(stockSymbol);
  }

}
