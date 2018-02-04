package com.gis.gmap;


import com.gis.database.model.Dmatrix;
import com.gis.database.model.Municipality;
import com.gis.database.service.dmatrix.DmatrixServiceImpl;
import com.gis.database.service.municipality.MunicipalityServiceImpl;
import com.gis.optimizer.OptimizationEngine;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.DirectionsApi;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.*;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DMatrixCrawler {


    private DateTimeFormatter parser;

    private static GeoApiContext context;

    private static final Logger LOGGER = LoggerFactory.getLogger(DMatrixCrawler.class);



    @Autowired
    private DmatrixServiceImpl dmatrixManagerService;

    @Autowired
    private MunicipalityServiceImpl municipalityService;




    public DMatrixCrawler(@Value("${google.maps.apikey}") String googleMapKey) {

        parser = ISODateTimeFormat.dateTimeParser();
        context = new GeoApiContext.Builder().apiKey(googleMapKey).build();
    }


    public void getDistanceMatrix(LatLng origin, LatLng[] destination, String[] timeSeries) {

        List<String> lookupList = new ArrayList<>();

        try {

            List<Municipality> municipalities = municipalityService.getAll();

            List<Dmatrix> distanceMatrixList = dmatrixManagerService.getAll();
            for (Dmatrix dmatrix : distanceMatrixList) {
                lookupList.add(dmatrix.getStartNodeId() + "|" + dmatrix.getEndNodeId());
            }

            for (Municipality municipality : municipalities) {

                for (Municipality municipality2 : municipalities) {

                    if (!municipality.getMunId().equals(municipality2.getMunId()) &&
                            !lookupList.contains(municipality.getMunId() + "|" + municipality2.getMunId())) {

                        //Origin
                        LatLng origin1 = new LatLng();
                        origin1.lng = municipality.getX();
                        origin1.lat = municipality.getY();

                        //Destination
                        LatLng[] destinations = new LatLng[1];
                        LatLng destination1 = new LatLng();
                        destination1.lng = municipality2.getX();
                        destination1.lat = municipality2.getY();
                        destinations[0] = destination1;

                        Map<String, Object> distanceData = callRemoteMatrixCalculator(
                                timeSeries,
                                origin1,
                                destinations
                        );

                        if (distanceData.size() > 0) {

                            Dmatrix dmatrix = new Dmatrix();
                            Dmatrix dmatrix2 = new Dmatrix();

                            dmatrix.setDistance((Long) distanceData.get("distance"));
                            dmatrix.setDuration((Long) distanceData.get("duration"));

                            dmatrix.setMon7((Long) distanceData.get("t1"));
                            dmatrix.setMon12((Long) distanceData.get("t2"));
                            dmatrix.setMon17((Long) distanceData.get("t3"));

                            dmatrix.setStartNodeId(municipality.getMunId());
                            dmatrix.setEndNodeId(municipality2.getMunId());

                            dmatrixManagerService.insert(dmatrix);

                            dmatrix2.setStartNodeId(municipality2.getMunId());
                            dmatrix2.setEndNodeId(municipality.getMunId());
                            dmatrix2.setDistance((Long) distanceData.get("distance"));
                            dmatrix2.setDuration((Long) distanceData.get("duration"));

                            dmatrix2.setMon7((Long) distanceData.get("t1"));
                            dmatrix2.setMon12((Long) distanceData.get("t2"));
                            dmatrix2.setMon17((Long) distanceData.get("t3"));

                            dmatrixManagerService.insert(dmatrix2);


                            lookupList.add(municipality.getMunId() + "|" + municipality2.getMunId());
                            lookupList.add(municipality2.getMunId() + "|" + municipality.getMunId());
                        }
                    }
                }

                LOGGER.info(municipality.getMunId() + "->" + lookupList.size());
            }

        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            LOGGER.info(lookupList.size() + " travel times have been calculated ...");
        }
    }


    private Map<String, Object> callRemoteMatrixCalculator(String[] timeSeries, LatLng origin, LatLng[] destinations) throws Exception {

        ReadableInstant date;
        DistanceMatrix distanceMatrix;
        Map<String, Object> data = new HashMap<>();


        int index = 1;
        long distance = 0l;
        long duration = 0l;
        for (String dateTime : timeSeries) {

            date = parser.parseDateTime(dateTime);

            distanceMatrix = DistanceMatrixApi.newRequest(context)
                    .origins(origin)
                    .destinations(destinations)
                    .mode(TravelMode.DRIVING)
                    .avoid(DirectionsApi.RouteRestriction.TOLLS)
                    .units(Unit.METRIC)
                    .departureTime(date)
                    .await();

            DistanceMatrixElement[] distanceMatrixElements = distanceMatrix.rows[0].elements;
            for (DistanceMatrixElement element : distanceMatrixElements) {
                if (element.status == DistanceMatrixElementStatus.OK) {
                    data.put("t" + index, element.durationInTraffic.inSeconds);

                    distance = element.distance.inMeters;
                    duration = element.duration.inSeconds;
                }
            }
            index++;
        }

        if(distance != 0 && duration != 0) {
            data.put("duration", duration);
            data.put("distance", distance);
        }

        return data;
    }
}
