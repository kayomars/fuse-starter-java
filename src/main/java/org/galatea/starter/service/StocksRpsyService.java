package org.galatea.starter.service;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.domain.DailyPrices;
import org.galatea.starter.domain.rpsy.IStocksRpsy;
import org.galatea.starter.entrypoint.exception.EntityNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class StocksRpsyService {

  @NonNull
  IStocksRpsy stocksRepo;

  /**
   * Retrieve entries holding daily prices from the database.
   * @param id, of type Long which is the identifier of the daily price entry
   */
  public Optional<DailyPrices> findEntry(final Long id) {
    log.info("Retrieving daily price of stock with id {}", id);
    return stocksRepo.findById(id);
  }

  /**
   * Retrieve multiple DailyPrice entries with matching stockSymbol from the database.
   * @param stockSymbol a string comprising of the symbol of the stock
   */
  public List<DailyPrices> findEntries(final String stockSymbol) {
    log.info("Retrieving all entries with stock symbol: {}", stockSymbol);

    List<DailyPrices> retrievedEntries = Lists.newArrayList(stocksRepo.findAllByStockSymbol(stockSymbol));

    if (retrievedEntries.isEmpty()) {
      throw new EntityNotFoundException(DailyPrices.class, "missingEntries");
    }
    return retrievedEntries;
  }

  /**
   * Retrieve multiple DailyPrices entries with matching stockSymbol from the database, in a descending data order.
   * This will order the entries so that the most recent dated entry will be first.
   * @param stockSymbol a string comprising of the symbol of the stock
   */
  public List<DailyPrices> findEntriesInDescDateOrder(final String stockSymbol) {
    log.info("Retrieving all entries with stock symbol: {}", stockSymbol);

    List<DailyPrices> retrievedEntries = Lists.newArrayList(
        stocksRepo.findAllByStockSymbol(stockSymbol, Sort.by(
            Direction.DESC, "relatedDate")));

    if (retrievedEntries.isEmpty()) {
      throw new EntityNotFoundException(DailyPrices.class, "missingEntries");
    }
    return retrievedEntries;
  }

  /**
   * Saves all given entities into database. Must be given an iterable consisting of DailyPrices entities
   * @param dailyPricesToSave iterable consisting of DailyPrices objects
   */
  public void saveAllEntities(Iterable<DailyPrices> dailyPricesToSave) {
    stocksRepo.saveAll(dailyPricesToSave);
  }

}
