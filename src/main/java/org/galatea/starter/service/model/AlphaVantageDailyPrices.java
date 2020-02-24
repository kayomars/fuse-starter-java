package org.galatea.starter.service.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.math.BigInteger;
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
public class AlphaVantageDailyPrices {

  @JsonAlias("1. open")
  private BigDecimal open;

  @JsonAlias("2. high")
  private BigDecimal high;

  @JsonAlias("3. low")
  private BigDecimal low;

  @JsonAlias("4. close")
  private BigDecimal close;

  @JsonAlias("5. volume")
  private BigInteger volume;
}
