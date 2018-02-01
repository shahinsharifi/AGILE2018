package com.gis.optimizer.model;

public class BasicGenome implements Comparable<BasicGenome>
{

    private String demandID;
    private String facilityID;
    private Long weight;


    public BasicGenome(String demandID, String facilityID, Long weight) {
        this.demandID = demandID;
        this.facilityID = facilityID;
        this.weight = weight;
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

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }


    @Override
    public int compareTo(BasicGenome t) {
        return this.getDemandID().compareTo(t.getDemandID());
    }

}
