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
public class TradeGrpcCApi extends TradeServiceGrpc.TradeServiceImplBase {

    private final TradeService tradeService;

    @Autowired
    public TradeGrpcCApi(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @Override
    public void findTrades(TradeFilter filter, StreamObserver<Trade> responseObserver) {
        log.info("Received find trades filed request {}.", filter);

        this.tradeService.findTrades(filter)
                .stream()
                .forEach(responseObserver::onNext);

        responseObserver.onCompleted();
    }
}

