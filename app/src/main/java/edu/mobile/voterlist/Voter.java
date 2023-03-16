package edu.mobile.voterlist;

import edu.mobile.voterlist.model.Person;

public class Voter {
    private Person person;


    private Person personsFather;

    private String voterId;

    private Address voteBooth;



    public Address getVoteBooth() {
        return voteBooth;
    }

    public void setVoteBooth(Address voteBooth) {
        this.voteBooth = voteBooth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private String phoneNumber;

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getPersonsFather() {
        return personsFather;
    }

    public void setPersonsFather(Person personsFather) {
        this.personsFather = personsFather;
    }

}
