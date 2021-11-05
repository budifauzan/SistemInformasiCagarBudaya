package com.example.sisteminformasicagarbudaya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity {

    private ImageView imgNavBarBack;
    private TextView tvNavTitle;
    private ConstraintLayout clNavBarBack;

    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initiateViews();
        setOnClick();
        setMapsFragment();
    }

    private void initiateViews() {
        tvNavTitle = findViewById(R.id.tv_navbar_title);
        tvNavTitle.setText("Lokasi Cagar Budaya");
        clNavBarBack = findViewById(R.id.cl_navbar_back);
        clNavBarBack.setVisibility(View.VISIBLE);
        imgNavBarBack = findViewById(R.id.img_navbar_back);
        imgNavBarBack.setImageResource(R.drawable.ic_baseline_arrow_back_24);
    }

    private void setOnClick() {
        clNavBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setMapsFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this::onMapReady);
        }
    }


    public void onMapReady(GoogleMap googleMap) {
        Intent intent = getIntent();
        CagarBudayaModel cagarBudayaModel = intent.getParcelableExtra("cagarBudayaModel");
        mGoogleMap = googleMap;
        // Tambahkan marker dan pindahkan kamera ke marker
        LatLng lokasiCagar = new LatLng(Double.parseDouble(cagarBudayaModel.getLatitude()),
                Double.parseDouble(cagarBudayaModel.getLongitude()));
        mGoogleMap.addMarker(new MarkerOptions()
                .position(lokasiCagar)
                .title(cagarBudayaModel.getNama()));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasiCagar, 12));
    }
}