package com.echarts;

public class AddressInfo {  
    private String address;  
    private String lat;//经度  
    private String lng;//纬度  
    private int value;
    public AddressInfo() {  
      
    }
    public AddressInfo(String address){
    	this.address=address;
    }
    public int getValue(){
    	return this.value;
    }
    public void setValue(int value){
    	this.value=value;
    }
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}
    
}