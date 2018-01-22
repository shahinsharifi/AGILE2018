package com.gis.gmap;


import com.gis.database.model.Dmatrix;
import com.gis.database.model.Municipality;
import com.gis.database.service.dmatrix.DmatrixServiceImpl;
import com.gis.database.service.municipality.MunicipalityServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.DirectionsApi;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DMatrixCrawler {


    private DateTimeFormatter parser;

    private static GeoApiContext context;


    @Autowired
    private DmatrixServiceImpl dmatrixManagerService;

    @Autowired
    private MunicipalityServiceImpl municipalityService;




    public DMatrixCrawler(@Value("${google.maps.apikey}") String googleMapKey) {

        parser = ISODateTimeFormat.dateTimeParser();
        context = new GeoApiContext.Builder().apiKey(googleMapKey).build();
    }


    public void getDistanceMatrix(LatLng origin, LatLng[] destination, String[] timeSeries) {

        DateTime date;
        DistanceMatrix distanceMatrix;
        List<DistanceMatrixRow> rows = new ArrayList<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();

        try {

          /*  List<Municipality> municipalities = municipalityService.getAll();

            LatLng[] latLngs = new LatLng[100];
            for(int i=0; i<100;i++) {
                Municipality mun = municipalities.get(i);
                LatLng latLng = new LatLng();
                latLng.lng = mun.getX();
                latLng.lat = mun.getY();
                latLngs[i] = latLng;
            }

            for(Municipality municipality:municipalities){

            }


            callRemoteMatrixCalculator(timeSeries,origin,latLngs);
*/

            List<Municipality> municipalities = municipalityService.getAll();
            Municipality municipality1 = municipalities.get(0);
            Municipality municipality2 = municipalities.get(municipalities.size()-100);

            LatLng latLng = new LatLng();
            latLng.lng = municipality1.getX();
            latLng.lat = municipality1.getY();

            LatLng[] latLngs = new LatLng[1];
            LatLng latLng2 = new LatLng();
            latLng2.lng = municipality2.getX();
            latLng2.lat = municipality2.getY();
            latLngs[0] = latLng2;

            Map<String, Long> elementData = new HashMap<>();

            int index = 1;
            for (String dateTime : timeSeries) {

                date = parser.parseDateTime(dateTime);

                distanceMatrix = DistanceMatrixApi.newRequest(context)
                        .origins(latLng)
                        .destinations(latLngs)
                        .mode(TravelMode.DRIVING)
                        .avoid(DirectionsApi.RouteRestriction.TOLLS)
                        .units(Unit.METRIC)
                        .departureTime(date)
                        .await();


                rows.addAll(Arrays.asList(distanceMatrix.rows));

                DistanceMatrixElement element = distanceMatrix.rows[0].elements[0];

                elementData.put("t" + index, element.durationInTraffic.inSeconds);

                if(!elementData.containsKey("d"))
                    elementData.put("d", element.distance.inMeters);

                index++;
            }

            Dmatrix dmatrix = new Dmatrix();

            dmatrix.setStartNodeId(municipality1.getMunId());
            dmatrix.setEndNodeId(municipality2.getMunId());

            dmatrix.setDistance(elementData.get("d"));

            dmatrix.setMon7(elementData.get("t1"));
            dmatrix.setMon12(elementData.get("t2"));
            dmatrix.setMon17(elementData.get("t3"));

            dmatrix.setWed7(elementData.get("t4"));
            dmatrix.setWed12(elementData.get("t5"));
            dmatrix.setWed17(elementData.get("t6"));

            dmatrix.setFri7(elementData.get("t7"));
            dmatrix.setFri12(elementData.get("t8"));
            dmatrix.setFri17(elementData.get("t9"));

            dmatrixManagerService.insert(dmatrix);



        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {
            System.out.println(gson.toJson(rows));
        }
    }


    private List<Map<String,Object>> callRemoteMatrixCalculator(String[] timeSeries, LatLng origin, LatLng[] destinations) throws Exception{

        DateTime date;
        DistanceMatrix distanceMatrix;
        List<Map<String,Object>> elementData = new ArrayList<>();


        int index = 1;
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
            for(DistanceMatrixElement element: distanceMatrixElements){

            }
        }

        return elementData;
    }
}
