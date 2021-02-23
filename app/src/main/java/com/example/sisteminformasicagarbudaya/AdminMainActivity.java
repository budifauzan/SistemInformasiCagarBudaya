package com.example.sisteminformasicagarbudaya;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminMainActivity extends AppCompatActivity {
    private EditText edtNamaCagar, edtDetail, edtLatitude, edtLongitude, edtLinkVR;
    private TextView tvNamaFile;
    private Button btnUploadThumbnail, btnTambahkan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        initiateViews();
    }

    private void initiateViews() {
        edtNamaCagar = findViewById(R.id.edt_admin_main_nama_cagar);
        edtDetail = findViewById(R.id.edt_admin_main_detail);
        edtLatitude = findViewById(R.id.edt_admin_main_latitude);
        edtLongitude = findViewById(R.id.edt_admin_main_longitude);
        edtLinkVR = findViewById(R.id.edt_admin_main_link_vr);
        tvNamaFile = findViewById(R.id.tv_admin_main_nama_file);
        btnUploadThumbnail = findViewById(R.id.btn_admin_main_upload_thumbnail);
        btnTambahkan = findViewById(R.id.btn_admin_main_tambahkan);
    }

    private void setOnClick() {
        btnUploadThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnTambahkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}