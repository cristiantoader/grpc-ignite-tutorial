package org.ctoader.learn.grpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.google.protobuf.Int32Value;
import com.google.protobuf.Int64Value;
import lombok.extern.slf4j.Slf4j;
import org.ctoader.learn.grpc.api.Trade;
import org.jooq.lambda.Unchecked;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Repository
@Slf4j
public class TradeDaoJsonFileSystem implements TradeDao {

    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    @Value("${trade.persistence.fp}")
    private String tradePersistenceFilePath;

    @Autowired
    public TradeDaoJsonFileSystem(ObjectMapper objectMapper,
                                  ModelMapper modelMapper) {
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public Stream<Trade> findAllTrades() {
        try {
            return Files.lines(Paths.get(tradePersistenceFilePath))
                    .map(Unchecked.function(line -> objectMapper.readValue(line, TradeModelJson.class)))
                    .map(Unchecked.function(tradeFs -> convertToTrade(tradeFs)));

        } catch (IOException e) {
            log.error("", e);
            return Stream.empty();
        }
    }

    private Trade convertToTrade(TradeModelJson tradeFs) throws IOException {
        return Trade.newBuilder()
                    .setRefid(Int64Value.newBuilder().setValue(tradeFs.getRefid()).build())
                    .setSourceSystem(tradeFs.getSourceSystem())
                    .setTradeId(tradeFs.getTradeId())
                    .setVersion(Int32Value.newBuilder().setValue(tradeFs.getVersion()).build())
                    .setProductType(tradeFs.getProductType())
                    .setProductSubType(tradeFs.getProductSubType())
                    .setBody(ByteString.readFrom(new ByteArrayInputStream(tradeFs.getBody())))
                    .build();
    }
}
