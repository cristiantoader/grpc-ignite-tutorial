package org.ctoader.learn.grpc;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.ctoader.learn.grpc.api.Trade;
import org.ctoader.learn.grpc.api.TradeFilter;
import org.ctoader.learn.grpc.api.TradeServiceGrpc;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TradeGrpcCApi extends TradeServiceGrpc.TradeServiceImplBase {

    @Override
    public void findTrades(TradeFilter request, StreamObserver<Trade> responseObserver) {
        log.info("Received find trades request {}.", request);
        responseObserver.onCompleted();
    }
}

