package org.ctoader.learn.grpc;

import com.google.protobuf.GeneratedMessageV3;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PerformanceMeasurement {

    private long totalEntries = 0;
    private long totalBytes = 0;
    private long lastTimestamp = System.currentTimeMillis();

    private static final long BATCH_SIZE = 100;

    public <T extends GeneratedMessageV3> void recordEntry(T entry) {
        totalEntries++;
        totalBytes += entry.getSerializedSize();

        if (totalEntries % BATCH_SIZE == 0) {
            long currentTimestamp = System.currentTimeMillis();
            log.info("Processed {} entries / second - total data processed {} GB.",
                    computeEntriesPerSecond(currentTimestamp),
                    computeProcessedGB());

            lastTimestamp = currentTimestamp;
        }
    }

    private double computeProcessedGB() {
        return (totalBytes / 1000) / 1048576D;
    }

    private double computeEntriesPerSecond(long currentTimestamp) {
        return (double) BATCH_SIZE / ((currentTimestamp - lastTimestamp) / 1_000D);
    }
}
