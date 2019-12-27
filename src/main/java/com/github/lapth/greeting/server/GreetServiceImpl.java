package com.github.lapth.greeting.server;

import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import io.grpc.Status;
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
    public void unAuthGreet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        Greeting greeting = request.getGreeting();
        String firstName = greeting.getFirstName();
        String lastName = greeting.getLastName();

        // For basic understanding, read here https://grpc.io/docs/guides/error/
        // For more detail, read here https://github.com/grpc/grpc-java/tree/master/examples/src/main/java/io/grpc/examples/errorhandling

        // Return a build-in gRPC error
        responseObserver.onError(
                Status.UNAUTHENTICATED
                        .withDescription("I do know this guy: " + firstName + " " + lastName)
                        .asRuntimeException()
        );
    }
}
