package org.ctoader.learn.grpc;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TradeModelJson {
    private Long refid;
    private String sourceSystem;
    private String tradeId;
    private Integer version;
    private String productType;
    private String productSubType;
    private byte[] body;
}

