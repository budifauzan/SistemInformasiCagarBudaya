package com.example.sisteminformasicagarbudaya;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ConstraintLayout clNavbarFilter, clFilterContainer;
    private TextView tvNavTitle;
    private ImageView imgNavBarFilter;
    private ProgressDialog progressDialog;
    private RecyclerView rvCagarBudaya;
    private CagarBudayaAdapter cagarBudayaAdapter;

    private ArrayList<CagarBudayaModel> cagarBudayaModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateViews();
        setOnClick();
        setRecyclerView();
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
}