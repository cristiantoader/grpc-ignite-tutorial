package org.ctoader.learn.grpc;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.ctoader.learn.grpc.api.Trade;
import org.ctoader.learn.grpc.api.TradeFilter;
import org.ctoader.learn.grpc.api.TradeServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TradeGrpcApi extends TradeServiceGrpc.TradeServiceImplBase {

    private final TradeService tradeService;

    @Autowired
    public TradeGrpcApi(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @Override
    public void findTrades(TradeFilter filter, StreamObserver<Trade> responseObserver) {
        log.info("Received find trades filed request {}.", filter);
        this.tradeService.findTrades(filter, responseObserver);
        responseObserver.onCompleted();
    }

    @Override
    public void findTradesIgnite(TradeFilter request, StreamObserver<Trade> responseObserver) {
        this.tradeService.findTradesIgnite(request, responseObserver);
    }
}

