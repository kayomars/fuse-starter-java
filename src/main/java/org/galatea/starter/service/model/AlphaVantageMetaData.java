package org.galatea.starter.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * A layer for transformation, aggregation, and business required when retrieving stock prices from
 * AlphaVantage or our database.
 */
@Slf4j
@RequiredArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlphaVantageMetaData {

  @JsonProperty("1. Information")
  private String information;

  @JsonProperty("2. Symbol")
  private String stockSymbol;

  @JsonProperty("3. Last Refreshed")
  private String freshnessDate;

  @JsonProperty("4. Output Size")
  private String outputSize;

  // Could have to do some work to play with time zones
  @JsonProperty("5. Time Zone")
  private String timeZone;

}
