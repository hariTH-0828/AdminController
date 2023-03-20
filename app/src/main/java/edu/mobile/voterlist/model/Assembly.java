package edu.mobile.voterlist.model;

public class Assembly {

    private int id;

    private String assembly;

    private int districtId;


    public String getAssembly() {
        return assembly;
    }
    public void setAssembly(String assembly) {
        this.assembly = assembly;
    }
    public int getDistrictId() {
        return districtId;
    }
    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return assembly;
    }
}
