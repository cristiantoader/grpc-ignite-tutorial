package org.ctoader.learn.grpc;

import org.ctoader.learn.grpc.api.Trade;
import org.ctoader.learn.grpc.api.TradeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class TradeService {

    private final TradeFileSystemDao tradeDao;

    @Autowired
    public TradeService(TradeFileSystemDao tradeDao) {
        this.tradeDao = tradeDao;
    }

    private static boolean isFilteredString(Supplier<String> filterSupplier, Supplier<String> tradeSupplier) {
        return StringUtils.isEmpty(filterSupplier.get()) || Objects.equals(filterSupplier.get(), tradeSupplier.get());
    }

    private static <T> boolean isFiltered(Supplier<T> filterSupplier, Supplier<T> tradeSupplier) {
        return Objects.isNull(filterSupplier.get()) || Objects.equals(filterSupplier.get(), tradeSupplier.get());
    }

    public List<Trade> findTrades(TradeFilter filter) {
        return tradeDao.findAllTrades()
                       .stream()
                       .filter(trade -> isFiltered(trade::getRefid, filter::getRefid))
                       .filter(trade -> isFilteredString(trade::getTradeId, filter::getTradeId))
                       .filter(trade -> isFilteredString(trade::getSourceSystem, filter::getSourceSystem))
                       .filter(trade -> isFilteredString(trade::getTradeId, filter::getTradeId))
                       .filter(trade -> isFiltered(trade::getVersion, filter::getVersion))
                       .filter(trade -> isFilteredString(trade::getProductType, filter::getProductType))
                       .filter(trade -> isFilteredString(trade::getProductSubType, filter::getProductSubType))
                       .collect(Collectors.toList());
    }
}
