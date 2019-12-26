package com.github.lapth.greeting.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GreetingServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Hello gRPC Server");

        // Start server at port 50051
        Server server = ServerBuilder.forPort(30000)
                // Add Greeting service to this gRPC server
                .addService(new GreetServiceImpl())
                .build();

        // Let start
        server.start();

        // Just add shutdown hook of Java runtime
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Server is shutdowning ...");
            // Call this to shutdown server
            server.shutdown();
            System.out.println("Server has shutdowned");
        }));

        // We have to wait for the Server shutdowned, if not the program will be halted.
        server.awaitTermination();
    }
}
