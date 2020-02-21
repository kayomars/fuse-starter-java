package org.galatea.starter.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * A Feign Declarative REST Client to access endpoints from the AlphaVantage API to get stock prices.
 * See https://www.alphavantage.co/documentation/
 */
@FeignClient(name = "AV", url = "${spring.rest.avBasePath}")
public interface AlphaVantageClient {

  /**
   * Get a list of all historical stock prices for a specific stock from AlphaVantage
   * @return a list of all of the historical prices of a stock
   * */
  @GetMapping("query?function=TIME_SERIES_DAILY&symbol={symbol}&apikey=${keys.AVKey}")
  AlphaVantageResponse getAllPricesOfStock(@PathVariable("symbol") String stockSymbol);

  /**
   * Get a list of stock prices for a specific stock for <= 100 days
   * @return a list of prices of a stock for <= 100 days
   * */
  @GetMapping("query?function=TIME_SERIES_DAILY&symbol={symbol}&outputsize=compact&apikey=${keys.AVKey}")
  AlphaVantageResponse getPricesOfStockForDays(@PathVariable("symbol") String stockSymbol);

}
