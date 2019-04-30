package org.ctoader.learn.grpc;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.ctoader.learn.grpc.api.Trade;
import org.ctoader.learn.grpc.api.TradeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
@Slf4j
public class TradeService {

    private final TradeDaoJsonFileSystem tradeDaoTraditional;
    private final TradeDaoIgnite tradeDaoIgnite;

    @Autowired
    public TradeService(TradeDaoJsonFileSystem tradeDaoTraditional,
                        TradeDaoIgnite tradeDaoIgnite) {

        this.tradeDaoTraditional = tradeDaoTraditional;
        this.tradeDaoIgnite = tradeDaoIgnite;
    }

    private static boolean isFilteredString(Supplier<String> tradeSupplier, Supplier<String> filterSupplier) {
        return StringUtils.isEmpty(filterSupplier.get()) || Objects.equals(filterSupplier.get(), tradeSupplier.get());
    }

    private static <T> boolean isFiltered(Supplier<T> tradeSupplier, Supplier<Boolean> hasFilterValue, Supplier<T> filterSupplier) {
        return !hasFilterValue.get()
                || Objects.equals(filterSupplier.get(), tradeSupplier.get());
    }

    public void findTrades(TradeFilter filter, StreamObserver<Trade> responseObserver) {
        log.info("Finding all file based storage trades.");
        tradeDaoTraditional.findAllTrades()
                .filter(trade -> isFiltered(trade::getRefid, filter::hasRefid, filter::getRefid))
                .filter(trade -> isFiltered(trade::getVersion, filter::hasVersion, filter::getVersion))
                .filter(trade -> isFilteredString(trade::getTradeId, filter::getTradeId))
                .filter(trade -> isFilteredString(trade::getSourceSystem, filter::getSourceSystem))
                .filter(trade -> isFilteredString(trade::getTradeId, filter::getTradeId))
                .filter(trade -> isFilteredString(trade::getProductType, filter::getProductType))
                .filter(trade -> isFilteredString(trade::getProductSubType, filter::getProductSubType))
                .forEach(responseObserver::onNext);
    }

    public void findTradesIgnite(TradeFilter filter, StreamObserver<Trade> responseObserver) {
        boolean isInitialised = tradeDaoIgnite.findAllTrades()
                                              .findAny()
                                              .isPresent();

        if (!isInitialised) {
            log.info("Ignite database is not initialised, importing data.");
            tradeDaoTraditional.findAllTrades()
                               .forEach(tradeDaoIgnite::addTrade);
        }

        log.info("Finding all ignite trades based on filter.");
        tradeDaoIgnite.findAllTrades()
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
