download the last version of the agent in :

https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent-all.jar


start the app in the following way:

java -javaagent:path/to/opentelemetry-javaagent-all.jar \
     -Dotel.resource.attributes=service.name=microservice-service-name \
     -jar openTelemetryExample1-3.1.jar