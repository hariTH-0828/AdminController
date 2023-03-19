package edu.mobile.voterlist.model;

public class States {
	private int id;
	private String state;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return state;
	}
}
