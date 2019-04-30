package org.ctoader.learn.grpc;

import org.ctoader.learn.grpc.api.Trade;

import java.util.stream.Stream;

public interface TradeDao {
    Stream<Trade> findAllTrades();
}
