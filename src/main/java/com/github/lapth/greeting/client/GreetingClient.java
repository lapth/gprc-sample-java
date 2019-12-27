package com.github.lapth.greeting.client;

import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {

    public static void main(String[] args) {
        System.out.println("Hello gRPC Client");

        System.out.println("Creating channel");

        // doUnaryCall();
        doServerStreamingCall();
    }

    private static void doServerStreamingCall() {
        // Creat a channel to connect to server
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 30000)
                .usePlaintext()
                .build();

        // Creat new synchronous greet service client
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        // Creating greet request
        Greeting greeting = Greeting.newBuilder()
                .setFirstName("Lap")
                .setLastName("Tran")
                .build();
        GreetSSRequest greetRequest = GreetSSRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        // Call the server by Server Streaming RPC
        // Print all result once received
        // *** The difference with UnaryRPC is the Client will receive more than one chunk of data
        // *** with each chunk we can do the related needed actions
        // *** and finally it will be done by a complete signal.
        greetClient.greetSS(greetRequest).forEachRemaining(greetSSRes -> {
            System.out.println(greetSSRes.getResult());
        });

        System.out.println("Shutdown channel");
        channel.shutdown();
    }

    private static void doUnaryCall() {
        // Creat a channel to connect to server
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 30000)
                .usePlaintext()
                .build();

        // Creat new synchronous greet service client
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

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
        channel.shutdown();
    }
}
