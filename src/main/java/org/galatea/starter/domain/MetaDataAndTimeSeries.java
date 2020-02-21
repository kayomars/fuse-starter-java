package org.galatea.starter.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import org.galatea.starter.serializers.AlphaVantageDeserializer;

@Data
@Builder
@JsonDeserialize(using = AlphaVantageDeserializer.class)
public class MetaDataAndTimeSeries {

  @JsonProperty("Meta Data")
  private MetaData allMetaData;

  // Probably should use a custom Date format
  @JsonProperty("Time Series (Daily)")
  private Map<String, DailyPrices> allTimeSeriesData;
}
