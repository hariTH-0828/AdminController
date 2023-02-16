package edu.mobile.voterlist;

public class Users {

    String name, fatherName, gender, phoneNo, state, assembly, wardNo, epicNo;

    public Users() {

    }

    public Users(String name, String fatherName, String gender, String phoneNo, String state, String assembly, String wardNo, String epicNo){
        this.name = name;
        this.fatherName = fatherName;
        this.gender = gender;
        this.phoneNo = phoneNo;
        this.state = state;
        this.assembly = assembly;
        this.wardNo = wardNo;
        this.epicNo = epicNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAssembly() {
        return assembly;
    }

    public void setAssembly(String assembly) {
        this.assembly = assembly;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public String getEpicNo() {
        return epicNo;
    }

    public void setEpicNo(String epicNo) {
        this.epicNo = epicNo;
    }

}
