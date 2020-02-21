package org.galatea.starter.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.domain.ListOfDailyPrices;
import org.springframework.stereotype.Service;

/**
 * A layer for transformation, aggregation, and business required when retrieving stock prices from
 * AlphaVantage or our database.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlphaVantageResponseService {

  // injecting in client to make queries
  @NonNull
  private AlphaVantageClient alphaVClient;



}
