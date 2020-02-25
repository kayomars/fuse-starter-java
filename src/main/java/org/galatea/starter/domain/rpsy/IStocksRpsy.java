package org.galatea.starter.domain.rpsy;

import java.util.List;
import java.util.Optional;
import org.galatea.starter.domain.DailyPrices;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStocksRpsy extends JpaRepository<DailyPrices, Long> {

  List<DailyPrices> findAllByStockSymbol(String stockSymbol);

  List<DailyPrices> findAllByStockSymbol(String stockSymbol, Sort sort);

  List<DailyPrices> findByStockSymbolOrderByRelatedDateDesc(String stockSymbol);
}
