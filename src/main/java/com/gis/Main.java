package com.gis;

import com.gis.gmap.DMatrixCrawler;
import com.gis.optimizer.OptimizationEngine;
import com.google.maps.model.LatLng;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {

		ApplicationContext ctx = SpringApplication.run(Main.class, args);
		executeOptimization(ctx);
	}


	private static void calculateDistanceMatrix(ApplicationContext ctx){

		DMatrixCrawler matrixCrawler = ctx.getBean(DMatrixCrawler.class);


		String[] timeSeries = new String[]{ //Planning period is one week.
				"2018-02-12T07:00:00.045Z", "2018-02-12T12:00:00.045Z", "2018-02-12T17:00:00.045Z" //Monday (7am , 12am , 5pm)
				,"2018-02-14T07:00:00.045Z", "2018-02-14T12:00:00.045Z", "2018-02-14T17:00:00.045Z" //Wednesday (7am , 12am , 5pm)
				,"2018-02-16T07:00:00.045Z", "2018-02-16T12:00:00.045Z", "2018-02-16T17:00:00.045Z" //Friday (7am , 12am , 5pm)
		};


		LatLng origin = new LatLng(52.156274, 4.492423);
		LatLng[] destinations = new LatLng[]{
				new LatLng(52.033926, 4.259178)
				//TODO: to be added. Max = 25 points
		};

		matrixCrawler.getDistanceMatrix(origin, destinations, timeSeries);
	}


	private static void executeOptimization(ApplicationContext ctx) {

		try {

			OptimizationEngine optimizationEngine = ctx.getBean(OptimizationEngine.class);
			optimizationEngine.evolve();

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
}
