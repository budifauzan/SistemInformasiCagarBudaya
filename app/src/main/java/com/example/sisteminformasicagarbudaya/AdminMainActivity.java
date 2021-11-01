package com.example.sisteminformasicagarbudaya;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminMainActivity extends AppCompatActivity {

    private static final String TAG = "AdminMainActivity";

    private EditText edtNamaCagar, edtDetail, edtLatitude, edtLongitude, edtLinkVR;
    private TextView tvNamaFile;
    private ImageView imgThumbnail;
    private Button btnUploadThumbnail, btnTambahkan;
    private ProgressDialog progressDialog;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference cagarRef = firebaseFirestore.collection("CagarBudaya");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        initiateViews();
        setOnClick();
    }

    private void initiateViews() {
        edtNamaCagar = findViewById(R.id.edt_admin_main_nama_cagar);
        edtDetail = findViewById(R.id.edt_admin_main_detail);
        edtLatitude = findViewById(R.id.edt_admin_main_latitude);
        edtLongitude = findViewById(R.id.edt_admin_main_longitude);
        edtLinkVR = findViewById(R.id.edt_admin_main_link_vr);
        tvNamaFile = findViewById(R.id.tv_admin_main_nama_file);
        btnUploadThumbnail = findViewById(R.id.btn_admin_main_upload_thumbnail);
        imgThumbnail = findViewById(R.id.img_admin_main_thumbnail);
        btnTambahkan = findViewById(R.id.btn_admin_main_tambahkan);
        progressDialog = new ProgressDialog(this);
    }

    private void setOnClick() {
        btnUploadThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminMainActivity.this, "Upload thumbnail", Toast.LENGTH_SHORT).show();
            }
        });
        btnTambahkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahCagarBudaya();
            }
        });
    }

    private void tambahCagarBudaya() {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        CagarBudayaModel cagarBudayaModel = new CagarBudayaModel(
                edtNamaCagar.getText().toString(),
                edtDetail.getText().toString(),
                0,
                "Ini thumbnail URL",
                edtLatitude.getText().toString(),
                edtLongitude.getText().toString(),
                edtLinkVR.getText().toString()
        );
        cagarRef.add(cagarBudayaModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressDialog.dismiss();
                        Toast.makeText(AdminMainActivity.this, "Data berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
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