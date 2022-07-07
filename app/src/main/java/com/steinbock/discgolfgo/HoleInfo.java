package com.steinbock.discgolfgo;

import com.google.android.gms.maps.model.LatLng;

public class HoleInfo {
    LatLng holeLocation;
    LatLng teeLocation;
    int number;
    int par;

    public HoleInfo(LatLng holeLocation, LatLng teeLocation, int number, int par) {
        this.holeLocation = new LatLng(holeLocation.latitude, holeLocation.longitude);
        this.teeLocation = new LatLng(teeLocation.latitude, teeLocation.longitude);
        this.number = number;
        this.par = par;
    }

    public HoleInfo(double holeLat, double holeLon, double teeLat, double teeLon, int number, int par) {
        this(new LatLng(holeLat, holeLon), new LatLng(teeLat, teeLon), number, par);
    }

    public LatLng getHoleLocation() {
        return holeLocation;
    }

    public void setHoleLocation(LatLng holeLocation) {
        this.holeLocation = holeLocation;
    }

    public LatLng getTeeLocation() {
        return teeLocation;
    }

    public void setTeeLocation(LatLng teeLocation) {
        this.teeLocation = teeLocation;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPar() {
        return par;
    }

    public void setPar(int par) {
        this.par = par;
    }

    @Override
    public String toString() {
        return "Hole{" +
                "holeLocation=" + holeLocation +
                ", teeLocation=" + teeLocation +
                ", number=" + number +
                ", par=" + par +
                '}';
    }

    public int getDistance() {
        // Get the lat/lon coordinates in radians
        double lat1 = Math.toRadians(holeLocation.latitude);
        double lon1 = Math.toRadians(holeLocation.longitude);
        double lat2 = Math.toRadians(teeLocation.latitude);
        double lon2 = Math.toRadians(teeLocation.longitude);

        // Find the difference between the two
        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        // Haversine formula using the radius of earth in ft (3956 mi * 5280ft/mi)
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double r = 3956 * 5280;
        return (int) Math.round(2 * r * Math.asin(Math.sqrt(a)));
    }

    public int getMetricDistance() {
        return (int) Math.round(getDistance() * 0.3048);
    }

    public float getBearing() {
        // Get the lat/lon coordinates in radians
        double lat1 = Math.toRadians(holeLocation.latitude);
        double lon1 = Math.toRadians(holeLocation.longitude);
        double lat2 = Math.toRadians(teeLocation.latitude);
        double lon2 = Math.toRadians(teeLocation.longitude);

        // Find the difference between the two
        double dlon = lon2 - lon1;

        double x = Math.cos(lat2) * Math.sin(dlon);
        double y = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dlon);

        double b = Math.atan2(x, y);
        return (float) Math.toDegrees(b);
    }

    public LatLng getMidPt() {
        return new LatLng((holeLocation.latitude + teeLocation.latitude) / 2f,
                (holeLocation.longitude + teeLocation.longitude) / 2f);
    }
}
