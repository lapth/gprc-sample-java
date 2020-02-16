# UnaryRPC code sample

In this branch you should see the sample code for UnaryRPC

The server is ran at port: **30000**

**Configure your environment**
```
cd ~
mkdir grpc-sample
cd grpc-sample
git clone https://github.com/lapth/gprc-sample-java.git
cd gprc-sample-java
git checkout BiDiStreamingRPC
export PROJECT_HOME=~/grpc-sample/gprc-sample-java
cd $PROJECT_HOME
chmod +x gradlew
```

**Build jar file**
```
cd $PROJECT_HOME
gradlew clean jarClient jarServer
```
You should see the jar files in /build/libs/ folder

**Run server**
```
cd $PROJECT_HOME/build/libs/
java -jar grpc-server.jar
```

**Run client**
```
cd $PROJECT_HOME/build/libs/
java -jar grpc-client.jar
```

# Docker for GKE, Optional for local test
**Build docker**
```
cd $PROJECT_HOME
export PROJECT_ID=<PROJECT_ID>
gradlew clean jarClient jarServer
docker build -t gcr.io/${PROJECT_ID}/grpc-server:v4.0 .
```
**Verify the images**
```
docker images
```
**Upload to GCR (charged repository)**
```
docker push gcr.io/${PROJECT_ID}/grpc-server:v4.0
```
**Run server docker locally**
```
docker run --rm -p 30000:30000 gcr.io/${PROJECT_ID}/grpc-server:v4.0
```
