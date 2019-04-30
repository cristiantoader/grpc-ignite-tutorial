package org.ctoader.learn.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.ctoader.learn.grpc.api.Trade;
import org.ctoader.learn.grpc.api.TradeFilter;
import org.ctoader.learn.grpc.api.TradeServiceGrpc;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Iterator;

@SpringBootApplication
@Slf4j
public class GrpcPlayClientApp implements ApplicationRunner {

    private static final String HOST_NAME = "localhost";
    private static final int HOST_PORT = 8118;

    public static void main(String[] args) {
        SpringApplication.run(GrpcPlayClientApp.class, args);
    }

    public void run(ApplicationArguments args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST_NAME, HOST_PORT)
                .usePlaintext()
                .build();

        TradeServiceGrpc.TradeServiceBlockingStub stub = TradeServiceGrpc.newBlockingStub(channel);

        TradeFilter filter = TradeFilter.newBuilder()
                                        .setProductSubType("PT")
                                        .build();

        Iterator<Trade> trades = stub.findTrades(filter);
        while (trades.hasNext()) {
            Trade trade = trades.next();
            log.info("Received trade {}.", trade);
        }

        channel.shutdown();
    }
}
