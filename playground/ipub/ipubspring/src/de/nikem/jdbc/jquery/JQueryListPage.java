package de.nikem.jdbc.jquery;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class JQueryListPage implements Serializable {
	private static final long serialVersionUID = -7889719245186844185L;
	
	private String echo;
	private int totalRecords;
	private int totalDisplayRecords;
	private List<Map<String, ?>> aaData;
	public String getEcho() {
		return echo;
	}
	public void setEcho(String echo) {
		this.echo = echo;
	}
	public int getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
	public int getTotalDisplayRecords() {
		return totalDisplayRecords;
	}
	public void setTotalDisplayRecords(int totalDisplayRecords) {
		this.totalDisplayRecords = totalDisplayRecords;
	}
	public List<Map<String, ?>> getAaData() {
		return aaData;
	}
	public void setAaData(List<Map<String, ?>> aaData) {
		this.aaData = aaData;
	}
}
