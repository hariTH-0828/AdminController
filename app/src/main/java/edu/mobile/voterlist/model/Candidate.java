package edu.mobile.voterlist.model;

import java.util.List;

public class Candidate {
    private int id;
    private String name;
    private DataFileInfo candidateImageId;
    private String partyName;
    private String qualification;
    private float averageRating;
    private int age;
    private String gender;
    private int districtId;
    private int stateId;
    private String aadhaarNumber;
    private String background;
    private Contact contact;
    private List<Promises> promises;
    private PastPerformance pastPerformance;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataFileInfo getCandidateImageId() {
        return candidateImageId;
    }

    public void setCandidateImageId(DataFileInfo candidateImageId) {
        this.candidateImageId = candidateImageId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public List<Promises> getPromises() {
        return promises;
    }

    public void setPromises(List<Promises> promises) {
        this.promises = promises;
    }

    public PastPerformance getPastPerformance() {
        return pastPerformance;
    }

    public void setPastPerformance(PastPerformance pastPerformance) {
        this.pastPerformance = pastPerformance;
    }
}
