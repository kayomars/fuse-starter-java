package org.galatea.starter.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DailyPrices {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @JsonIgnore
  private String stockSymbol;

  @JsonIgnore
  private String updateDate;

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
