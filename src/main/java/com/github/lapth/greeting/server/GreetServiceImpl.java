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

    @Override
    public StreamObserver<GreetCSRequest> greetCS(StreamObserver<GreetCSResponse> responseObserver) {

        // With client stream model, we have to build a stream observer and return it back to client
        StreamObserver<GreetCSRequest> greetCSRequestStreamObserver = new StreamObserver<GreetCSRequest>() {
            // Global result
            private String result = "";

            // Client just sent a message
            @Override
            public void onNext(GreetCSRequest value) {
                // Retrieve input from request
                // Let see the proto file to know what input we have
                Greeting greeting = value.getGreeting();
                String firstName = greeting.getFirstName();
                String lastName = greeting.getLastName();

                result += "Hello " + firstName + " " + lastName + ". ";
            }

            // Client just sent an error
            @Override
            public void onError(Throwable t) {
                // Ignore for now
            }

            // Client just sent complete signal
            // Client sends this signal once it want to receive the response from server and finish the request
            @Override
            public void onCompleted() {
                GreetCSResponse greetRes = GreetCSResponse.newBuilder()
                        .setResult(result)
                        .build();
                // Send back the response
                responseObserver.onNext(greetRes);
                // Send complete signal
                responseObserver.onCompleted();
            }
        };

        // Return the stream observer and delegate the control back to Client
        return greetCSRequestStreamObserver;
    }

    @Override
    public StreamObserver<GreetBDRequest> greetBD(StreamObserver<GreetBDResponse> responseObserver) {

        // The Bidirectional streaming RPC should be similar with Client Streaming RPC
        // The difference is the response will be send back many times

        // With client stream model, we have to build a stream observer and return it back to client
        StreamObserver<GreetBDRequest> greetBDRequestStreamObserver = new StreamObserver<GreetBDRequest>() {
            // Client just sent a message
            @Override
            public void onNext(GreetBDRequest value) {
                // Retrieve input from request
                // Let see the proto file to know what input we have
                Greeting greeting = value.getGreeting();
                String firstName = greeting.getFirstName();
                String lastName = greeting.getLastName();

                // Build the result
                String result = "Hello " + firstName + " " + lastName + ".";
                GreetBDResponse greetRes = GreetBDResponse.newBuilder()
                        .setResult(result)
                        .build();

                // Send back the response
                responseObserver.onNext(greetRes);
            }

            // Client just sent an error
            @Override
            public void onError(Throwable t) {
                // Ignore for now
            }

            // Client just sent complete signal
            @Override
            public void onCompleted() {
                // OK I will close the connection too
                responseObserver.onCompleted();
            }
        };

        // Return the stream observer and delegate the control back to Client
        return greetBDRequestStreamObserver;
    }
}
