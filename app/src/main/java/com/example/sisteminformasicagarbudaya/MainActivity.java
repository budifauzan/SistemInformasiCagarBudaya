package com.example.sisteminformasicagarbudaya;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    private CagarBudayaAdapter cagarBudayaAdapter;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference cagarRef = firebaseFirestore.collection("CagarBudaya");

    private ArrayList<CagarBudayaModel> cagarBudayaModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateViews();
        setOnClick();
        setRecyclerView();
        getDataCagar();
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

    private void getDataCagar() {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

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