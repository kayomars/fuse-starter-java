package org.galatea.starter.domain;

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
  private String stockSymbol;

  @Id
  private String relatedDate;

  private BigDecimal open;

  private BigDecimal high;

  private BigDecimal low;

  private BigDecimal close;

  private BigInteger volume;
}
