package org.ctoader.learn.grpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcPlayConfig {

    @Bean
    public ObjectMapper makeObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ModelMapper makeModelMapper() {
        return new ModelMapper();
    }
}
