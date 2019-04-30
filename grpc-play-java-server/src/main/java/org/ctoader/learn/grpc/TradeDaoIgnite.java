package org.ctoader.learn.grpc;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.ctoader.learn.grpc.api.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.cache.Cache;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Repository
public class TradeDaoIgnite implements TradeDao {

    private final Ignite igniteClient;

    @Autowired
    public TradeDaoIgnite(Ignite igniteClient) {
        this.igniteClient = igniteClient;
    }

    @Override
    public Stream<Trade> findAllTrades() {
        CacheConfiguration<Long, Trade> cacheConfiguration = makeCacheConfiguration();
        IgniteCache<Long, Trade> tradesCache = igniteClient.getOrCreateCache(cacheConfiguration);

        Iterator<Cache.Entry<Long, Trade>> iterator = tradesCache.iterator();
        Iterable<Cache.Entry<Long, Trade>> iterable = () -> iterator;

        return StreamSupport.stream(iterable.spliterator(), false)
                            .map(entry -> entry.getValue());
    }

    private CacheConfiguration<Long, Trade> makeCacheConfiguration() {
        CacheConfiguration<Long, Trade> cacheConfiguration = new CacheConfiguration<>();
        cacheConfiguration.setBackups(0);
        cacheConfiguration.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        cacheConfiguration.setName("trades");
        return cacheConfiguration;
    }

    public void addTrade(Trade trade) {
        CacheConfiguration<Long, Trade> cacheConfiguration = makeCacheConfiguration();
        IgniteCache<Long, Trade> tradesCache = igniteClient.getOrCreateCache(cacheConfiguration);

        tradesCache.put(trade.getRefid().getValue(), trade);
    }
}
