# gRPC Server Sample docker file
FROM gcr.io/google_appengine/openjdk

RUN echo 'deb http://httpredir.debian.org/debian buster-backports main' > /etc/apt/sources.list.d/buster-backports.list \
    && apt-get update \
    && apt-get install --no-install-recommends -y -q ca-certificates \
    && apt-get -y -q upgrade \
    && apt-get install --no-install-recommends -y openjdk-8-jre-headless \
    && rm -rf /var/lib/apt/lists/*

ADD ./build/libs/grpc-server.jar /grpc-sample/server.jar

EXPOSE 30000

ENTRYPOINT ["java", "-jar", "/grpc-sample/server.jar"]
