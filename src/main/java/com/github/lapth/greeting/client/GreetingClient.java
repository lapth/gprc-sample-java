package com.github.lapth.greeting.client;

import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import javax.sound.midi.SysexMessage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClient {

    public static void main(String[] args) {
        System.out.println("Hello gRPC Client");

        System.out.println("Creating channel");

        // doUnaryCall();
        // doServerStreamingCall();
        // doClientStreamingCall();
        doBDStreamingCall();
    }

    private static void doBDStreamingCall() {
        // Creat a channel to connect to server
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 30000)
                .usePlaintext()
                .build();

        // Create a asynchronous greet client
        // Note: it is different with Unary and SSRPC, here it is asynchronous
        GreetServiceGrpc.GreetServiceStub greetClient = GreetServiceGrpc.newStub(channel);

        // We need a way to wait for server complete its job and send back the complete signal
        // Here we use CountDownLatch, depended on you
        CountDownLatch latch = new CountDownLatch(1);

        // Call the service with a StreamObserver response
        // The StreamObserver response is delegated to server and controlled by server
        // Client needs to listen the signal from server to do needed actions
        StreamObserver<GreetBDRequest> greetBDRequestStreamObserver = greetClient.greetBD(new StreamObserver<GreetBDResponse>() {

            // Server just sent a message
            @Override
            public void onNext(GreetBDResponse value) {
                System.out.println(value);
            }

            // Server just sent an error
            @Override
            public void onError(Throwable t) {}

            // Server just sent a complete signal
            @Override
            public void onCompleted() {
                System.out.println("Server just sent complete signal.");

                // Countdown to shutdown client once received completed signal from server
                latch.countDown();
            }
        });

        // OK, now Client will send the messages to server
        System.out.println("Sending Lap Tran");
        greetBDRequestStreamObserver.onNext(
                GreetBDRequest.newBuilder().setGreeting(
                        Greeting.newBuilder()
                                .setFirstName("Lap")
                                .setLastName("Tran")
                                .build()
                ).build());
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Another message
        System.out.println("Sending Bill Gate");
        greetBDRequestStreamObserver.onNext(
                GreetBDRequest.newBuilder().setGreeting(
                        Greeting.newBuilder()
                                .setFirstName("Bill")
                                .setLastName("Gate")
                                .build()
                ).build());
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Another message
        System.out.println("Sending Binh Truong");
        greetBDRequestStreamObserver.onNext(
                GreetBDRequest.newBuilder().setGreeting(
                        Greeting.newBuilder()
                                .setFirstName("Binh")
                                .setLastName("Truong")
                                .build()
                ).build());
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Send complete message and wait for response from server
        greetBDRequestStreamObserver.onCompleted();

        // Block thread and wait until server finished or kill thread after 5m
        try {
            latch.await(5L, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void doClientStreamingCall() {
        // Creat a channel to connect to server
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 30000)
                .usePlaintext()
                .build();

        // Create a asynchronous greet client
        // Note: it is different with Unary and SSRPC, here it is asynchronous
        GreetServiceGrpc.GreetServiceStub greetClient = GreetServiceGrpc.newStub(channel);

        // We need a way to wait for server complete its job and send back the complete signal
        // Here we use CountDownLatch, depended on you
        CountDownLatch latch = new CountDownLatch(1);

        // Call the service with a StreamObserver response
        // The StreamObserver response is delegated to server and controlled by server
        // Client needs to listen the signal from server to do needed actions
        StreamObserver<GreetCSRequest> greetCSRequestStreamObserver = greetClient.greetCS(new StreamObserver<GreetCSResponse>() {

            // Server just sent a message
            @Override
            public void onNext(GreetCSResponse value) {
                System.out.println(value);
            }

            // Server just sent an error
            @Override
            public void onError(Throwable t) {}

            // Server just sent a complete signal
            @Override
            public void onCompleted() {
                System.out.println("Server just sent complete signal.");

                // Countdown to shutdown client once received completed signal from server
                latch.countDown();
            }
        });

        // OK, now Client will send the messages to server
        System.out.println("Sending Lap Tran");
        greetCSRequestStreamObserver.onNext(
                GreetCSRequest.newBuilder().setGreeting(
                    Greeting.newBuilder()
                        .setFirstName("Lap")
                        .setLastName("Tran")
                        .build()
                ).build());
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Another message
        System.out.println("Sending Bill Gate");
        greetCSRequestStreamObserver.onNext(
                GreetCSRequest.newBuilder().setGreeting(
                        Greeting.newBuilder()
                                .setFirstName("Bill")
                                .setLastName("Gate")
                                .build()
                ).build());
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Another message
        System.out.println("Sending Binh Truong");
        greetCSRequestStreamObserver.onNext(
                GreetCSRequest.newBuilder().setGreeting(
                        Greeting.newBuilder()
                                .setFirstName("Binh")
                                .setLastName("Truong")
                                .build()
                ).build());
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Send complete message and wait for response from server
        greetCSRequestStreamObserver.onCompleted();

        // Block thread and wait until server finished or kill thread after 5m
        try {
            latch.await(5L, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
