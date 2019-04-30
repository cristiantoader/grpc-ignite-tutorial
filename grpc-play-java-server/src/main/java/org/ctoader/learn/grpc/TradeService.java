package org.ctoader.learn.grpc;

import io.grpc.stub.StreamObserver;
import org.ctoader.learn.grpc.api.Trade;
import org.ctoader.learn.grpc.api.TradeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.function.Supplier;

@Service
public class TradeService {

    private final TradeDaoJsonFileSystem tradeDao;

    @Autowired
    public TradeService(TradeDaoJsonFileSystem tradeDao) {
        this.tradeDao = tradeDao;
    }

    private static boolean isFilteredString(Supplier<String> tradeSupplier, Supplier<String> filterSupplier) {
        return StringUtils.isEmpty(filterSupplier.get()) || Objects.equals(filterSupplier.get(), tradeSupplier.get());
    }

    private static <T> boolean isFiltered(Supplier<T> tradeSupplier, Supplier<Boolean> hasFilterValue, Supplier<T> filterSupplier) {
        return !hasFilterValue.get()
                || Objects.equals(filterSupplier.get(), tradeSupplier.get());
    }

    public void findTrades(TradeFilter filter, StreamObserver<Trade> responseObserver) {
        tradeDao.findAllTrades()
                .filter(trade -> isFiltered(trade::getRefid, filter::hasRefid, filter::getRefid))
                .filter(trade -> isFiltered(trade::getVersion, filter::hasVersion, filter::getVersion))
                .filter(trade -> isFilteredString(trade::getTradeId, filter::getTradeId))
                .filter(trade -> isFilteredString(trade::getSourceSystem, filter::getSourceSystem))
                .filter(trade -> isFilteredString(trade::getTradeId, filter::getTradeId))
                .filter(trade -> isFilteredString(trade::getProductType, filter::getProductType))
                .filter(trade -> isFilteredString(trade::getProductSubType, filter::getProductSubType))
                .forEach(responseObserver::onNext);
    }
}
