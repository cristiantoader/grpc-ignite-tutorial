package org.ctoader.learn.grpc;

import org.ctoader.learn.grpc.api.Trade;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TradeFileSystemDao implements TradeDao {

    @Override
    public List<Trade> findAllTrades() {
        return null;
    }
}
