package com.gis.optimizer.model;

public class BasicGenome implements Comparable<BasicGenome>
{

    private String facilityID;
    private String demandID;


    public BasicGenome(String facilityID, String demandID) {
        this.facilityID = facilityID;
        this.demandID = demandID;
    }

    public String getFacilityID() {
        return facilityID;
    }

    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

    public String getDemandID() {
        return demandID;
    }

    public void setDemandID(String demandID) {
        this.demandID = demandID;
    }

    @Override
    public int compareTo(BasicGenome t) {
        return this.getDemandID().compareTo(t.getDemandID());
    }

}
