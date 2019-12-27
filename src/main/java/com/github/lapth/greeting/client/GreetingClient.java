package com.github.lapth.greeting.client;

import com.proto.dummy.DummyServiceGrpc;
import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class GreetingClient {

    public static void main(String[] args) {
        System.out.println("Hello gRPC Client");

        System.out.println("Creating channel");
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

        // Call and catch the error
        try {
            GreetResponse greetResponse = greetClient.unAuthGreet(greetRequest);
        } catch (StatusRuntimeException e) {
            e.printStackTrace();
        }

        System.out.println("Shutdown channel");
        channel.shutdown();
    }

}
