package com.pmdm.examen2_albertomartinez;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapDefectoActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_defecto);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Latitud y Longitud del instituto
        //39.48175882702601, -0.42188212845378276
        LatLng instituto = new LatLng(39.48175882702601, -0.42188212845378276);
        mMap.addMarker(new MarkerOptions().position(instituto).title("CIPFP MISLATA")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        /* Se lleva el foco hasta una nueva latitud/longitud y como parametro se le pasa la LatLng
        que hemos definido y un valor para el zoom */
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(instituto, 15));

        // Para activar los controles del zoom +-
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}