# Build
FROM ghcr.io/graalvm/native-image-community:22 AS builder
WORKDIR /app
COPY . .
RUN ./mvnw -Pnative -DskipTests native:compile

# Run
FROM alpine:3.18
RUN apk add --no-cache ca-certificates gcompat
WORKDIR /app
COPY --from=builder /app/target/rinhabackend3 /app/rinhabackend3
EXPOSE 8080
ENTRYPOINT ["/app/rinhabackend3"]
