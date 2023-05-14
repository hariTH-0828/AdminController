package edu.mobile.voterlist.model;

import androidx.annotation.NonNull;

public class Parties {

    private int id;
    private String partyName;

    public String getPartyName() {
        return partyName;
    }

    public int getId() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return partyName;
    }
}
