package com.example.sisteminformasicagarbudaya;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailCagarActivity extends AppCompatActivity {
    private ImageView imgThumbnail;
    private TextView tvNamaCagar;
    private TextView tvDetail;
    private Button btnMasukModeVR, btnLokasiCagar;
    private ConstraintLayout clNavBarBack;

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private int jumlahView;
    private String docId;
    private CagarBudayaModel cagarBudayaModel;

    private static final String TAG = "DetailCagarActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_cagar);
        initiateViews();
        setViewFromIntent();
        setOnClick();
        increaseJumlahView();
    }

    private void initiateViews() {
        imgThumbnail = findViewById(R.id.img_detail_cagar_thumbnail);
        TextView tvNavTitle = findViewById(R.id.tv_navbar_title);
        tvNavTitle.setText("Detail Cagar Budaya");
        tvNamaCagar = findViewById(R.id.tv_detail_cagar_nama_cagar);
        tvDetail = findViewById(R.id.tv_detail_cagar_detail);
        btnMasukModeVR = findViewById(R.id.btn_detail_cagar_masuk_mode_vr);
        btnLokasiCagar = findViewById(R.id.btn_detail_cagar_lokasi);
        clNavBarBack = findViewById(R.id.cl_navbar_back);
        clNavBarBack.setVisibility(View.VISIBLE);
        ImageView imgNavBarBack = findViewById(R.id.img_navbar_back);
        imgNavBarBack.setImageResource(R.drawable.ic_baseline_arrow_back_24);
    }

    private void setViewFromIntent() {
        Intent intent = getIntent();
        cagarBudayaModel = intent.getParcelableExtra("cagarBudayaModel");
        if (cagarBudayaModel != null) {
            Glide.with(this).load(cagarBudayaModel.getThumbnailUrl()).into(imgThumbnail);
            tvNamaCagar.setText(cagarBudayaModel.getNama());
            tvDetail.setText(cagarBudayaModel.getDetail());
            jumlahView = cagarBudayaModel.getJumlahView();
            docId = cagarBudayaModel.getDocId();
        }
    }

    private void setOnClick() {
        clNavBarBack.setOnClickListener(view -> finish());

        btnMasukModeVR.setOnClickListener(v -> {
            Intent intent = new Intent(DetailCagarActivity.this, VRActivity.class);
            intent.putExtra("linkVR", cagarBudayaModel.getLinkVR());
            startActivity(intent);
        });

        btnLokasiCagar.setOnClickListener(v -> {
            Intent intent = new Intent(DetailCagarActivity.this, MapsActivity.class);
            intent.putExtra("cagarBudayaModel", cagarBudayaModel);
            startActivity(intent);
        });
    }

    private void increaseJumlahView() {
        jumlahView++;
        DocumentReference cagarRef = firebaseFirestore.collection("CagarBudaya").document(docId);
        cagarRef.update("jumlahView", jumlahView)
                .addOnFailureListener(e -> Log.d(TAG, e.toString()));
    }
}