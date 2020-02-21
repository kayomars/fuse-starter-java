package org.galatea.starter.service.AlphaVantageObjects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
