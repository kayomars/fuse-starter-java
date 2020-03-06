package org.galatea.starter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE) // For spring and jackson
@Builder
@Data
@Entity
@IdClass(EntryId.class)
public class DailyPrices {

  @Id
  @JsonProperty("Symbol")
  private String stockSymbol;

  @Id
  @JsonProperty("Date")
  private String relatedDate;

  @JsonProperty("Open")
  private BigDecimal open;

  @JsonProperty("High")
  private BigDecimal high;

  @JsonProperty("Low")
  private BigDecimal low;

  @JsonProperty("Close")
  private BigDecimal close;

}
