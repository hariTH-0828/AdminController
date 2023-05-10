package edu.mobile.voterlist.model;

import java.util.List;

public class Candidate {
    private int id;
    private String name;
    private DataFileInfo candidateImageId;
    private String partyName;
    private String qualification;
    private String area;
    private float averageRating;
    private int age;
    private String gender;
    private String nativeDistrict;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public String getNativeDistrict() {
        return nativeDistrict;
    }

    public void setNativeDistrict(String nativeDistrict) {
        this.nativeDistrict = nativeDistrict;
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
