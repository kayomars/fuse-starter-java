package org.galatea.starter.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.galatea.starter.serializers.AlphaVantageDeserializer;

@Data
@Builder
@JsonDeserialize(using = AlphaVantageDeserializer.class)
public class ListOfDailyPrices {

  private List<DailyPrices> allDailyPrices;

  public void addToDailyPrices(DailyPrices curDailyPrice) {
    allDailyPrices.add(curDailyPrice);
  }

}
