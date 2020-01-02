package com.github.lapth.greeting.client;

import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

import javax.net.ssl.SSLException;
import java.io.File;

public class GreetingClient {

    public static void main(String[] args) throws SSLException {
        System.out.println("Hello gRPC Client");

        System.out.println("Creating channel");

        // Create a secure channel to connect to server
        ManagedChannel secureChannel = NettyChannelBuilder.forAddress("localhost", 30000)
                .sslContext(GrpcSslContexts.forClient().trustManager(new File("ssl/ca.crt")).build())
                .build();

        // Creat new synchronous greet service client
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(secureChannel);

        // Creating greet request
        Greeting greeting = Greeting.newBuilder()
                .setFirstName("Lap")
                .setLastName("Tran")
                .build();
        GreetRequest greetRequest = GreetRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        // Call the server by gRPC
        GreetResponse greetResponse = greetClient.greet(greetRequest);

        // Retrieve result and print it
        System.out.println(greetResponse.getResult());

        System.out.println("Shutdown channel");
        secureChannel.shutdown();
    }

}
