# GraalVM docker image used for AoT compilation
FROM graalvm:ee-1.0.0-rc6 AS build-aot
# Add maven wrapper
ADD mvnw app/
ADD .mvn app/.mvn/
# Add settings.xml to allow snapshots
ADD settings.xml root/.m2/
# Add pom
ADD pom.xml app/
# Add sources
ADD src app/src/
# Set working dir
WORKDIR /app
# Build (java side)
RUN ./mvnw -Pnative-image
# Create new image from alpine
FROM frolvlad/alpine-glibc:alpine-3.8
RUN apk add --no-cache ca-certificates
# Copy generated native executable from build-aot
COPY --from=build-aot /app/target/bitcoin-viewer /bitcoin-viewer
# Set the entrypoint
ENTRYPOINT [ "/bitcoin-viewer" ]
