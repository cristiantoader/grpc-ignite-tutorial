package org.ctoader.learn.grpc;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IgniteConfig {

    public static final String CONFIG_PATH = "grpc-play-java-server/src/main/resources/ignite-client-config.xml";

    @Bean
    public Ignite makeIgnite() {
        Ignite igniteClient = Ignition.start(CONFIG_PATH);
        igniteClient.cluster()
                    .active(true);

        igniteClient.active(true);

        return igniteClient;
    }
}
