package org.galatea.starter.entrypoint;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.aspect4log.Log;
import net.sf.aspect4log.Log.Level;
import org.galatea.starter.service.AlphaVantageResponse;
import org.galatea.starter.service.StockPricesService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Log(enterLevel = Level.INFO, exitLevel = Level.INFO)
@Validated
@RestController
@RequiredArgsConstructor
public class StockPricesRestController {

  @NonNull
  private StockPricesService stockPricesService;

  /**
   * Exposes an endpoint to get the prices of a given stock in the last N days.
   * @param stockSymbol the String representation of the stock symbol
   * @param numDays, the int representation of the number of days
   * @return a list of all stock prices of that given stock in the last N days.
   */
  @GetMapping(value = "${mvc.stocks.getPricesForDaysPath}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public void getPrices(
      @RequestParam(value = "stock") final String stockSymbol, @RequestParam(value = "days") final int numDays) {
    stockPricesService.getStockPrices(stockSymbol, numDays);
    return;
  }

}
