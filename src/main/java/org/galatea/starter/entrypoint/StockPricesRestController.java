package org.galatea.starter.entrypoint;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.aspect4log.Log;
import net.sf.aspect4log.Log.Level;
import org.galatea.starter.domain.DailyPrices;
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
   * @param numDays, the integer representation of the number of days
   * @param stockSymbol the String representation of the stock symbol
   * @return a list of all stock prices of that given stock in the last N days.
   */
  @GetMapping(value = "${mvc.stocks.getPricesForDaysPath}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public List<DailyPrices> getPrices(
      @RequestParam(value = "stock")
      @NotNull(message = "Symbol cannot be empty")
      final String stockSymbol,
      @RequestParam(value = "days")
      @PositiveOrZero(message = "N must be a non-negative integer")
      final Integer numDays) {
     return stockPricesService.getStockPrices(stockSymbol, numDays);
  }

}
