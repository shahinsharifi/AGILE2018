package com.gis.database.model;

import javax.persistence.*;

@Entity
//@Table(name = "dmatrix3", schema = "public", catalog = "poi")
public class Dmatrix {

    private int id;
    private String startNodeId;
    private String endNodeId;
    private Long distance;
    private Long duration;
    private Long mon7;
    private Long mon12;
    private Long mon17;
    private Long wed7;
    private Long wed12;
    private Long wed17;
    private Long fri7;
    private Long fri12;
    private Long fri17;
    private Double euclidean;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "start_node_id")
    public String getStartNodeId() {
        return startNodeId;
    }

    public void setStartNodeId(String startNodeId) {
        this.startNodeId = startNodeId;
    }

    @Basic
    @Column(name = "end_node_id")
    public String getEndNodeId() {
        return endNodeId;
    }

    public void setEndNodeId(String endNodeId) {
        this.endNodeId = endNodeId;
    }

    @Basic
    @Column(name = "duration")
    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @Basic
    @Column(name = "distance")
    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    @Basic
    @Column(name = "mon_7")
    public Long getMon7() {
        return mon7;
    }

    public void setMon7(Long mon7) {
        this.mon7 = mon7;
    }

    @Basic
    @Column(name = "mon_12")
    public Long getMon12() {
        return mon12;
    }

    public void setMon12(Long mon12) {
        this.mon12 = mon12;
    }

    @Basic
    @Column(name = "mon_17")
    public Long getMon17() {
        return mon17;
    }

    public void setMon17(Long mon17) {
        this.mon17 = mon17;
    }

    @Basic
    @Column(name = "wed_7")
    public Long getWed7() {
        return wed7;
    }

    public void setWed7(Long wed7) {
        this.wed7 = wed7;
    }

    @Basic
    @Column(name = "wed_12")
    public Long getWed12() {
        return wed12;
    }

    public void setWed12(Long wed12) {
        this.wed12 = wed12;
    }

    @Basic
    @Column(name = "wed_17")
    public Long getWed17() {
        return wed17;
    }

    public void setWed17(Long wed17) {
        this.wed17 = wed17;
    }

    @Basic
    @Column(name = "fri_7")
    public Long getFri7() {
        return fri7;
    }

    public void setFri7(Long fri7) {
        this.fri7 = fri7;
    }

    @Basic
    @Column(name = "fri_12")
    public Long getFri12() {
        return fri12;
    }

    public void setFri12(Long fri12) {
        this.fri12 = fri12;
    }

    @Basic
    @Column(name = "fri_17")
    public Long getFri17() {
        return fri17;
    }

    public void setFri17(Long fri17) {
        this.fri17 = fri17;
    }


    @Basic
    @Column(name = "ecludian")
    public Double getEuclidean() {
        return euclidean;
    }

    public void setEuclidean(Double euclidean) {
        this.euclidean = euclidean;
    }
}
