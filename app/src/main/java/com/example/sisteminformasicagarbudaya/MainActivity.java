package com.example.sisteminformasicagarbudaya;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ConstraintLayout clNavbarFilter, clFilterContainer;
    private ProgressDialog progressDialog;
    private RecyclerView rvCagarBudaya;
    private Spinner spnFilter;

    private CagarBudayaAdapter cagarBudayaAdapter;

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference cagarRef = firebaseFirestore.collection("CagarBudaya");

    private final ArrayList<CagarBudayaModel> cagarBudayaModels = new ArrayList<>();

    private boolean doubleBackToExitPressedOnce = false;
    private boolean firstClick = true;
    private int selected = -1;
    private double userLat, userLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateViews();
        setOnClick();
        setRecyclerView();
        customSpinner();
        getCurrentLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Memuat ulang cagar budaya sesuai dengan filter yang dipilih sebelumnya
        if (selected == -1) {
            getDataCagar("nama");
        } else {
            spnFilter.setSelection(selected);
            switch (selected) {
                case 0:
                    getDataCagar("nama");
                    break;
                case 1:
                    getDataCagar("lokasi");
                    break;
                case 2:
                    getDataCagar("jumlahView");
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {

        // Method agar user perlu menekan tombol back dua kali untuk keluar dari aplikasi
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    private void initiateViews() {
        clNavbarFilter = findViewById(R.id.cl_navbar_filter);
        clNavbarFilter.setVisibility(View.VISIBLE);
        ImageView imgNavBarFilter = findViewById(R.id.img_navbar_filter);
        imgNavBarFilter.setImageResource(R.drawable.ic_baseline_filter_alt_24);
        clFilterContainer = findViewById(R.id.cl_main_filter_container);
        TextView tvNavTitle = findViewById(R.id.tv_navbar_title);
        tvNavTitle.setText("Daftar Cagar Budaya");
        rvCagarBudaya = findViewById(R.id.rv_main_cagar_budaya);
        spnFilter = findViewById(R.id.spn_main_filter);
        progressDialog = new ProgressDialog(this);
    }

    private void setOnClick() {

        // Menampilkan atau menyembunyikan menu filter
        clNavbarFilter.setOnClickListener(v -> {
            if (clFilterContainer.getVisibility() == View.GONE) {
                clFilterContainer.setVisibility(View.VISIBLE);
            } else {
                clFilterContainer.setVisibility(View.GONE);
            }
        });
    }

    private void setRecyclerView() {
        cagarBudayaAdapter = new CagarBudayaAdapter(this, cagarBudayaModels);
        rvCagarBudaya.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvCagarBudaya.setAdapter(cagarBudayaAdapter);
    }

    private void customSpinner() {
        String[] parameterSort = {"Nama", "Lokasi terdekat", "Dilihat paling banyak"};
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, parameterSort) {
            @Override
            public boolean isEnabled(int position) {
                return position != -1;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnFilter.setAdapter(spinnerArrayAdapter);
        spnFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Agar aplikasi tidak memuat data cagar budaya pada saat pertama kali spinner disentuh
                if (firstClick) {
                    firstClick = false;
                } else if (position == 0) {
                    getDataCagar("nama");
                    selected = 0;
                } else if (position == 1) {
                    getDataCagar("lokasi");
                    selected = 1;
                } else {
                    getDataCagar("jumlahView");
                    selected = 2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getCurrentLocation() {

        // Mengecek apakah izin untuk akses GPS sudah diberikan atau belum
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Meminta izin untuk akses GPS
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {

            // Mengambil lokasi dari user
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient
                    .getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            userLat = location.getLatitude();
                            userLong = location.getLongitude();
                        }
                    });
        }
    }

    private void getDataCagar(String metodeSort) {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (metodeSort.equals("nama")) {

            // Mengambil data dari Firestore urut berdasarkan nama cagar
            cagarBudayaModels.clear();
            cagarRef
                    .orderBy("nama", Query.Direction.ASCENDING)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                            CagarBudayaModel cagarBudayaModel = queryDocumentSnapshot.toObject(CagarBudayaModel.class);
                            cagarBudayaModel.setDocId(queryDocumentSnapshot.getId());
                            cagarBudayaModels.add(cagarBudayaModel);
                            calculateDistance(cagarBudayaModel);
                        }

                        progressDialog.dismiss();
                        cagarBudayaAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Log.d(TAG, e.toString());
                    });
        } else if (metodeSort.equals("lokasi")) {
            compareEveryCagarDistance();
                        
            progressDialog.dismiss();
            cagarBudayaAdapter.notifyDataSetChanged();
        } else {

            // Mengambil data dari Firestore urut berdasarkan jumlahView dari cagar
            cagarBudayaModels.clear();
            cagarRef
                    .orderBy("jumlahView", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                            CagarBudayaModel cagarBudayaModel = queryDocumentSnapshot.toObject(CagarBudayaModel.class);
                            cagarBudayaModel.setDocId(queryDocumentSnapshot.getId());
                            cagarBudayaModels.add(cagarBudayaModel);
                            calculateDistance(cagarBudayaModel);
                        }

                        progressDialog.dismiss();
                        cagarBudayaAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Log.d(TAG, e.toString());
                    });
        }

    }

    private void calculateDistance(CagarBudayaModel cagarBudayaModel) {

        // Menghitung jarak dari user ke cagar
        float[] result = new float[1];
        Location.distanceBetween(userLat, userLong,
                Double.parseDouble(cagarBudayaModel.getLatitude()),
                Double.parseDouble(cagarBudayaModel.getLongitude()), result);

        cagarBudayaModel.setJarakDariUser((int) result[0]);
    }

    private void compareEveryCagarDistance() {

        // Urutkan jarak cagar menggunakan Bubble Sort
        CagarBudayaModel cagarModelTemp;
        for (int i = 0; i < cagarBudayaModels.size(); i++) {
            for (int j = 1; j < cagarBudayaModels.size() - i; j++) {
                if (cagarBudayaModels.get(j - 1).getJarakDariUser() > cagarBudayaModels.get(j).getJarakDariUser()) {
                    cagarModelTemp = cagarBudayaModels.get(j - 1);
                    cagarBudayaModels.set(j - 1, cagarBudayaModels.get(j));
                    cagarBudayaModels.set(j, cagarModelTemp);
                }
            }
        }
    }

}

