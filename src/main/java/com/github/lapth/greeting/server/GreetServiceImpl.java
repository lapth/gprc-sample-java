package com.github.lapth.greeting.server;

import com.proto.greet.*;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {

    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        // Retrieve input from request
        // Let see the proto file to know what input we have
        Greeting greeting = request.getGreeting();
        String firstName = greeting.getFirstName();
        String lastName = greeting.getLastName();

        // Build the result
        String result = "Hello " + firstName + " " + lastName;
        GreetResponse res = GreetResponse.newBuilder()
                .setResult(result)
                .build();

        // Send back the response based on gRPC
        // The result is async
        responseObserver.onNext(res);

        // Say that this stream was completed.
        // The RPC call will be ended
        responseObserver.onCompleted();
    }

    @Override
    public void greetSS(GreetSSRequest request, StreamObserver<GreetSSResponse> responseObserver) {
        // Retrieve input from request
        // Let see the proto file to know what input we have
        Greeting greeting = request.getGreeting();
        String firstName = greeting.getFirstName();
        String lastName = greeting.getLastName();

        // Will send back 5 messages to Client
        // Basically the SSRPC vs Unary are the same
        // The difference here is the SSRPC send back many message before sending the complete command.
        for (int i = 1; i < 5; i++) {
            // Build the result
            String result = "Hello " + firstName + " " + lastName + " - " + i;
            GreetSSResponse res = GreetSSResponse.newBuilder()
                    .setResult(result)
                    .build();

            // Send back the response based on gRPC
            // The result is async
            responseObserver.onNext(res);

            // Sleep for 1s
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Say that this stream was completed.
        // The RPC call will be ended
        responseObserver.onCompleted();
    }
}
