package com.example.sisteminformasicagarbudaya;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ConstraintLayout clNavbarFilter, clFilterContainer;
    private TextView tvNavTitle;
    private ImageView imgNavBarFilter;
    private ProgressDialog progressDialog;
    private RecyclerView rvCagarBudaya;
    private Spinner spnFilter;

    private CagarBudayaAdapter cagarBudayaAdapter;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference cagarRef = firebaseFirestore.collection("CagarBudaya");

    private ArrayList<CagarBudayaModel> cagarBudayaModels;
    private boolean doubleBackToExitPressedOnce = false;
    private boolean firstClick = true;
    private int selection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateViews();
        setOnClick();
        setRecyclerView();
        customSpinner();
        getDataCagar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        customSpinner();
        spnFilter.setSelection(selection);
    }

    @Override
    public void onBackPressed() {
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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void initiateViews() {
        clNavbarFilter = findViewById(R.id.cl_navbar_filter);
        clNavbarFilter.setVisibility(View.VISIBLE);
        imgNavBarFilter = findViewById(R.id.img_navbar_filter);
        imgNavBarFilter.setImageResource(R.drawable.ic_baseline_filter_alt_24);
        clFilterContainer = findViewById(R.id.cl_main_filter_container);
        tvNavTitle = findViewById(R.id.tv_navbar_title);
        tvNavTitle.setText("Daftar Cagar Budaya");
        rvCagarBudaya = findViewById(R.id.rv_main_cagar_budaya);
        spnFilter = findViewById(R.id.spn_main_filter);
        progressDialog = new ProgressDialog(this);
    }

    private void setOnClick() {
        clNavbarFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clFilterContainer.getVisibility() == View.GONE) {
                    clFilterContainer.setVisibility(View.VISIBLE);
                } else {
                    clFilterContainer.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setRecyclerView() {
        cagarBudayaModels = new ArrayList<>();
        cagarBudayaAdapter = new CagarBudayaAdapter(this, cagarBudayaModels);
        rvCagarBudaya.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvCagarBudaya.setAdapter(cagarBudayaAdapter);
    }

    private void getDataCagar() {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        cagarBudayaModels.clear();
        cagarRef
                .orderBy("nama", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                            CagarBudayaModel cagarBudayaModel = queryDocumentSnapshot.toObject(CagarBudayaModel.class);
                            cagarBudayaModel.setDocId(queryDocumentSnapshot.getId());
                            cagarBudayaModels.add(cagarBudayaModel);
                        }
                        progressDialog.dismiss();
                        cagarBudayaAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    private void customSpinner() {
        String parameterSort[] = {"Nama", "Lokasi terdekat", "Dilihat paling banyak"};
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, parameterSort) {
            @Override
            public boolean isEnabled(int position) {
                return position != -1;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
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
                if (firstClick) {
                    firstClick = false;
                } else if (position == 0) {
                    getDataCagar();
                    selection = 0;
                } else if (position == 1) {
                    Toast.makeText(MainActivity.this, "Ini nanti urutkan berdasarkan lokasi terdekat", Toast.LENGTH_SHORT).show();
                    selection = 1;
                } else {
                    getDataCagarBerdasarkanJumlahView();
                    selection = 2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getDataCagarBerdasarkanJumlahView() {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        cagarBudayaModels.clear();
        cagarRef
                .orderBy("jumlahView", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                            CagarBudayaModel cagarBudayaModel = queryDocumentSnapshot.toObject(CagarBudayaModel.class);
                            cagarBudayaModel.setDocId(queryDocumentSnapshot.getId());
                            cagarBudayaModels.add(cagarBudayaModel);
                        }
                        progressDialog.dismiss();
                        cagarBudayaAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG, e.toString());
                    }
                });
    }
}