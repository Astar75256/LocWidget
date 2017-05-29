package ru.astar.locwidget;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import java.io.IOException;
import java.util.List;

/**
 * Created by molot on 29.05.2017.
 */

public class MyLocation implements LocationListener {

    private Context context;
    private Location gpsLocation;

    public MyLocation(Context context) {
        this.context = context;
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10, this);
        gpsLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }



    public double getLatitude() {
        if (gpsLocation != null)
            return gpsLocation.getLatitude();
        return -1;
    }

    public double getLongitude() {
        if (gpsLocation != null)
            return gpsLocation.getLongitude();
        return -1;
    }


    private String getAddress(Location location) {
        String str = "";
        Geocoder geo = new Geocoder(context);

        try {
            List<Address> aList = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 5);

            if (aList.size() > 0) {
                Address a = aList.get(0);
                int maxAddrLine = a.getMaxAddressLineIndex();
                if (maxAddrLine >= 0) {
                    str = a.getAddressLine(maxAddrLine);
                    if (!str.isEmpty())
                        str += ", ";
                }
                str += a.getCountryName() + ", " + a.getAdminArea() + ", " + a.getThoroughfare() + " "
                        + a.getSubThoroughfare();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return str;
    }

    @Override
    public String toString() {
        return "Широта: " + getLatitude() + "\n"
                + "Долгота: " + getLongitude() + "\n"
                + "Адрес: " + getAddress(gpsLocation);
    }

    @Override
    public void onLocationChanged(Location location) {
        gpsLocation = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
