package org.galatea.starter.translators;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.domain.DailyPrices;
import org.galatea.starter.service.AlphaVantageObjects.AlphaVantageDailyPrices;
import org.galatea.starter.service.AlphaVantageObjects.AlphaVantageMetaData;
import org.galatea.starter.service.AlphaVantageResponse;
import org.springframework.stereotype.Service;

/**
 * A layer for transformation, aggregation, and business required when retrieving stock prices from
 * AlphaVantage.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlphaVantageResponseTranslator {

  /**
   * This function is responsible for creating instances of domain object DailyPrices.
   */
  public List<DailyPrices> createAllDailyPricesObjects(AlphaVantageResponse avResp) {

    List<DailyPrices> allDailyPrices = new ArrayList<DailyPrices>();
    String stockSymbol = avResp.getAllMetaData().getStockSymbol();
    String freshnessDate = avResp.getAllMetaData().getFreshnessDate();

    for (Map.Entry mapElement : avResp.getAllTimeSeriesData().entrySet()) {
      String relatedDate = (String)mapElement.getKey();
      AlphaVantageDailyPrices relatedPrices = (AlphaVantageDailyPrices)mapElement.getValue();

      // Build out instances of entity DailyPrices
      DailyPrices curDailyPrice = DailyPrices.builder()
          .stockSymbol(stockSymbol)
          .freshnessDate(freshnessDate)
          .relatedDate(relatedDate)
          .open(relatedPrices.getOpen())
          .high(relatedPrices.getHigh())
          .low(relatedPrices.getLow())
          .close(relatedPrices.getClose())
          .volume(relatedPrices.getVolume())
          .build();

      log.info(curDailyPrice.toString());
      allDailyPrices.add(curDailyPrice);

    }
    return allDailyPrices;
  }
}
