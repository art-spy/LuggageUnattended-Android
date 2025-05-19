package com.example.leluun;

public class Orthodrome {
	
	
	 public double entfernung(double Xloc, double Yloc, double Xlug, double Ylug){
	    
		 double radius = 6371;
		 double dLat = Math.toRadians(Xlug-Xloc);
	     double dLon = Math.toRadians(Ylug-Yloc);
	      double a =  Math.pow(Math.sin(dLat/2),2) + 
	         Math.cos(Math.toRadians(Xloc)) * Math.cos(Math.toRadians(Xlug)) *
	         Math.pow(Math.sin(dLon/2),2);
	      double c = 2 * Math.asin(Math.sqrt(a));
	      return radius * c;
		 
	    }
	
}
