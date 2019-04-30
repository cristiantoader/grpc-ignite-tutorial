package org.ctoader.learn.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@Slf4j
public class GrpcPlayServerApp implements ApplicationRunner {

    @Value("${server.port:8118}")
    private Integer serverPort;

    private final ApplicationContext applicationContext;

    @Autowired
    public GrpcPlayServerApp(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        SpringApplication.run(GrpcPlayServerApp.class, args);
    }

    public void run(ApplicationArguments args) throws Exception {
        Server server = ServerBuilder.forPort(serverPort)
                                     .addService(applicationContext.getBean(TradeGrpcApi.class))
                                     .build()
                                     .start();

        Runtime.getRuntime()
               .addShutdownHook(new Thread(() -> server.shutdownNow()));

        log.info("Server started on port {}.", serverPort);
        server.awaitTermination();
    }
}
