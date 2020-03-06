package org.galatea.starter.domain.rpsy;

import java.util.List;
import org.galatea.starter.domain.DailyPrices;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStocksRpsy extends JpaRepository<DailyPrices, Long> {

  List<DailyPrices> findTop1ByStockSymbolOrderByRelatedDateDesc(String stockSymbol);

  List<DailyPrices> findByStockSymbolAndRelatedDateGreaterThanEqualOrderByRelatedDateDesc(String stockSymbol, String neededDate);
}
