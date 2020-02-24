package org.galatea.starter.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.service.model.AlphaVantageDailyPrices;
import org.galatea.starter.service.model.AlphaVantageMetaData;
import org.springframework.stereotype.Service;

/**
 * A layer for transformation, aggregation, and business required when retrieving stock prices from
 * AlphaVantage or our database.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlphaVantageResponse {

  @Getter
  @JsonProperty("Meta Data")
  private AlphaVantageMetaData allMetaData;

  // Probably should use a custom Date format
  @Getter
  @JsonProperty("Time Series (Daily)")
  private Map<String, AlphaVantageDailyPrices> allTimeSeriesData;

}
