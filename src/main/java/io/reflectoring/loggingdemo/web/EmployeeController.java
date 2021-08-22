package io.reflectoring.loggingdemo.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import config.ExampleConfiguration;
import domain.Employee;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.api.trace.Span;

@RestController
public class EmployeeController {
	
	private final Tracer tracer;
	
		public EmployeeController() {
			//microservice name ?
			System.setProperty("otel.resource.attributes", "service.name=OtlpExporterExample");
			OpenTelemetry oTel = ExampleConfiguration.initOpenTelemetry();
			//instrumentation library, only for reference, it could be any string 
			tracer = oTel.getTracer("io.opentelemetry.contrib.brave");	
		}
		
		  public void myWonderfulUseCase() {
			    Span parentSpan = this.tracer.spanBuilder("start myWonderfulUseCase").startSpan();
			    parentSpan.addEvent("Event 0");
			    doWork(parentSpan);
			    
			    parentSpan.addEvent("Event 1");
			    parentSpan.end();
			  }	
		  
		  private void doWork(Span parentSpan) {
			  Span childSpan = tracer.spanBuilder("doWork")
				        .setParent(Context.current().with(parentSpan))
				        .startSpan();

			    try {
			    System.out.println("inside the span");
			      Thread.sleep(1000);
			    } catch (InterruptedException e) {
			    } finally {
			    	childSpan.end();
			    }
			  }		  
	
	  @GetMapping("/employees")
	  public List<Employee> all() {
		  Employee emp1 = new Employee();
		  emp1.setId("1");
		  emp1.setName("Leo");
		  Employee emp2 = new Employee();
		  emp2.setId("2");
		  emp2.setName("Jaime");
		  
		  List listEmployee = new ArrayList();
		  listEmployee.add(emp1);
		  listEmployee.add(emp2);
		  
//		  for (int i = 0; i < 2; i++) {
//		      myWonderfulUseCase();
//		    }		  
		  
		  
		  System.out.println("return from /employees");
	    return listEmployee;
	  }	
	
}
