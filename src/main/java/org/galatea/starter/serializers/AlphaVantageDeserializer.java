package org.galatea.starter.serializers;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.Data;
import org.galatea.starter.domain.DailyPrices;
import org.galatea.starter.domain.ListOfDailyPrices;


public class AlphaVantageDeserializer extends StdDeserializer<ListOfDailyPrices> {

  public AlphaVantageDeserializer() {
    this(null);
  }

  public AlphaVantageDeserializer(Class<?> vc) {
    super(vc);
  }

  // Deserialize into list of POJOs
  @Override
  public ListOfDailyPrices deserialize(JsonParser p, DeserializationContext ctxt) throws IOException,
      JsonProcessingException {

    List<DailyPrices> allDailyPrices = new ArrayList<DailyPrices>();
    //ListOfDailyPrices allDailyPrices = new ListOfDailyPrices();
    TreeNode tn = p.readValueAsTree();

    // All required fields for DailyPrices entity
    String stockSymbol;
    String updateDate;
    String openStr;
    String highStr;
    String lowStr;
    String closeStr;
    String volumeStr;


    // At the Meta Data and TimeSeries Level
    TreeNode metaHolder = tn.get("Meta Data");
    stockSymbol = metaHolder.get("2. Symbol").toString();

    // Removing superfluous "" at both ends
    stockSymbol = stockSymbol.substring(1, stockSymbol.length() - 1);

    TreeNode allDatesAndDataTN = tn.get("Time Series (Daily)");
    Iterator<String> allDates = allDatesAndDataTN.fieldNames();

    TreeNode curDayNode;
    // Iterate through all field names which are dates here and try and pull all required data
    while (allDates.hasNext()) {
      updateDate = allDates.next();

      // Point node to a certain date field
      curDayNode = allDatesAndDataTN.get(updateDate);

      // Get all values while excluding the "" that they come with
      openStr = curDayNode.get("1. open").toString();
      highStr = curDayNode.get("2. high").toString();
      lowStr = curDayNode.get("3. low").toString();
      closeStr = curDayNode.get("4. close").toString();
      volumeStr = curDayNode.get("5. volume").toString();

      // Use Lombok builder with the appropriate types while removing extra apostrophes at both ends
      DailyPrices thisDailyPrice = DailyPrices.builder()
          .stockSymbol(stockSymbol)
          .updateDate(updateDate)
          .open(new BigDecimal( openStr.substring(1, openStr.length() - 1)) )
          .high(new BigDecimal( highStr.substring(1, highStr.length() - 1)) )
          .low(new BigDecimal( lowStr.substring(1, lowStr.length() - 1)) )
          .close(new BigDecimal( closeStr.substring(1, closeStr.length() - 1)) )
          .volume(new BigInteger( volumeStr.substring(1, volumeStr.length() - 1)) )
          .build();

      System.out.println(thisDailyPrice);
      allDailyPrices.add(thisDailyPrice);
      //allDailyPrices.addToDailyPrices(thisDailyPrice);
      }

    //return allDailyPrices;
    ListOfDailyPrices needed = ListOfDailyPrices.builder()
        .allDailyPrices(allDailyPrices)
        .build();
    return needed;
  }

}

