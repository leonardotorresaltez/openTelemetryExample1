package config;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.exporter.logging.LoggingMetricExporter;
import io.opentelemetry.exporter.logging.LoggingSpanExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.autoconfigure.OpenTelemetryResourceAutoConfiguration;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.IntervalMetricReader;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;

public final class ExampleConfiguration {

	  /** The number of milliseconds between metric exports. */
	  private static final long METRIC_EXPORT_INTERVAL_MS = 800L;

	  /**
	   * Initializes an OpenTelemetry SDK with a logging exporter and a SimpleSpanProcessor.
	   *
	   * @return A ready-to-use {@link OpenTelemetry} instance.
	   */
	  public static OpenTelemetry initOpenTelemetry___LoginExporter() {
	    // This will be used to create instruments
	    SdkMeterProvider meterProvider = SdkMeterProvider.builder().buildAndRegisterGlobal();

	    // Create an instance of IntervalMetricReader and configure it
	    // to read metrics from the meterProvider and export them to the logging exporter
	    IntervalMetricReader.builder()
	        .setMetricExporter(new LoggingMetricExporter())
	        .setMetricProducers(Collections.singleton(meterProvider))
	        .setExportIntervalMillis(METRIC_EXPORT_INTERVAL_MS)
	        .build();

	    // Tracer provider configured to export spans with SimpleSpanProcessor using
	    // the logging exporter.
	    SdkTracerProvider tracerProvider =
	        SdkTracerProvider.builder()
	            .addSpanProcessor(SimpleSpanProcessor.create(new LoggingSpanExporter()))
	            .build();
	    return OpenTelemetrySdk.builder().setTracerProvider(tracerProvider).buildAndRegisterGlobal();
	  }
	  
	  public static OpenTelemetry initOpenTelemetry() {
		  //by dafault oltp expoter try to send data to localhost:4317 ( default agent port )  
		  OtlpGrpcSpanExporter spanExporter =
		        OtlpGrpcSpanExporter.builder().setTimeout(2, TimeUnit.SECONDS).build();
		    BatchSpanProcessor spanProcessor =
		        BatchSpanProcessor.builder(spanExporter)
		            .setScheduleDelay(100, TimeUnit.MILLISECONDS)
		            .build();

		    SdkTracerProvider tracerProvider =
		        SdkTracerProvider.builder()
		            .addSpanProcessor(spanProcessor)
		            .setResource(OpenTelemetryResourceAutoConfiguration.configureResource())
		            .build();
		    OpenTelemetrySdk openTelemetrySdk =
		        OpenTelemetrySdk.builder().setTracerProvider(tracerProvider).buildAndRegisterGlobal();

		    Runtime.getRuntime().addShutdownHook(new Thread(tracerProvider::close));

		    return openTelemetrySdk;
		  }	  
	}