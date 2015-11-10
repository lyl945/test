package com.surong.leadloan.utils;

import java.util.Comparator;
import java.util.List;

import com.surong.leadload.api.data.Member;

public class DistanceComparator implements Comparator< Member> {
	private List<Member> member;
	private double lat;
	private double long1;
	public DistanceComparator(List<Member> member, double lat, double long1) {
		super();
		this.member = member;
		this.lat = lat;
		this.long1 = long1;
	}

	@Override
	public int compare(Member arg0, Member arg1) {
		int distance =distanceBetween(lat,long1,Double.valueOf(arg0.getLatitude()),Double.valueOf(arg0.getLongitude()));
		int distances =distanceBetween(lat,long1,Double.valueOf(arg1.getLatitude()),Double.valueOf(arg1.getLongitude()));
		int dif = distance - distances ;
		if(dif>0) return 1;
		else if(dif<0) return -1;
		else return 0;
		// TODO Auto-generated method stub
	}
	public static int distanceBetween(double lat1, double lon1, double lat2,
			double lon2) {
		double R = 6370996.81;
		double PI = Math.PI;
		return (int) (R * Math.acos(Math.cos(lat1 * PI / 180)
				* Math.cos(lat2 * PI / 180)
				* Math.cos(lon1 * PI / 180 - lon2 * PI / 180)
				+ Math.sin(lat1 * PI / 180) * Math.sin(lat2 * PI / 180)));
	}

}
