package com.gis.optimizer.model;

public class BasicGenome implements Comparable<BasicGenome>
{

    private String demandID;
    private String facilityID;


    public BasicGenome(String demandID, String facilityID) {
        this.demandID = demandID;
        this.facilityID = facilityID;
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
