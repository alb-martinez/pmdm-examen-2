package com.pmdm.examen2_albertomartinez;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapRealActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_real);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // el motodo rastreoGPS devuelve un Location de donde se extraerán latitud y longitud
        Location location = rastreoGPS();
        double dLongitud = location.getLongitude();
        double dLatitud = location.getLatitude();

        // Se utilizan para crear una nueva LatLng
        LatLng nuevaPosicion = new LatLng(dLatitud, dLongitud);
        mMap.setMapType(googleMap.MAP_TYPE_NORMAL);

        // Permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Se activa el icono "diana" que hace que el mapa navegue hasta la ubicación actual
        mMap.setMyLocationEnabled(true);
        // Se activan los controles de zoom para el mapa
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Se asigna la nueva posición al movimiento de la camara, para que el mapa navegue hasta el punto
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nuevaPosicion, 15));
    }

    // Metodo que crea y devuelve un objeto Location a partir de la ultima posición conocida del GPS
    private Location rastreoGPS() {
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }

        Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        return location;
    }

}