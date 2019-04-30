package org.ctoader.learn.grpc;

import org.ctoader.learn.grpc.api.Trade;

import java.util.List;

public interface TradeDao {
    List<Trade> findAllTrades();
}
