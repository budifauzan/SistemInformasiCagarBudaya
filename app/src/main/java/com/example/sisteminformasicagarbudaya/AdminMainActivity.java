package com.example.sisteminformasicagarbudaya;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AdminMainActivity extends AppCompatActivity {

    private static final String TAG = "AdminMainActivity";
    private final int PICK_IMAGE_REQUEST = 1;

    private EditText edtNamaCagar, edtDetail, edtLatitude, edtLongitude, edtLinkVR;
    private TextView tvNamaFile;
    private ImageView imgThumbnail;
    private Button btnUploadThumbnail, btnTambahkan;
    private ProgressDialog progressDialog;

    private Uri uri;
    private String downloadURL;

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference cagarRef = firebaseFirestore.collection("CagarBudaya");

    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = firebaseStorage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        initiateViews();
        setOnClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Mengambil filePath dari file yang dipilih
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uri = data.getData();

            // Mengambil nama file
            File file = new File(uri.toString());
            tvNamaFile.setText(file.getName());
            try {

                // Mengambil file foto dan menampilkan ke thumbnail
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imgThumbnail.setImageBitmap(bitmap);
            } catch (IOException e) {
                Log.d(TAG, e.toString());
            }
        }
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
        btnUploadThumbnail.setOnClickListener(v -> chooseThumbnail());
        btnTambahkan.setOnClickListener(v -> uploadThumbnail());
    }

    private void chooseThumbnail() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), PICK_IMAGE_REQUEST);
    }

    private void uploadThumbnail() {
        if (uri != null) {
            progressDialog.setTitle("Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            // Upload file foto ke Firebase Storage
            StorageReference reference = storageReference.child("Thumbnails/" + UUID.randomUUID());
            reference.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> getDownloadURL(reference))
                    .addOnFailureListener(e -> {
                        Log.d(TAG, e.toString());
                        progressDialog.dismiss();
                    })
                    .addOnProgressListener(snapshot -> {
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    });
        }
    }

    private void getDownloadURL(StorageReference storageReference) {

        // Mengambil download URL dari file yang telah di upload
        storageReference.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    downloadURL = uri.toString();
                    addCagarBudaya(downloadURL);
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, e.toString());
                    progressDialog.dismiss();
                });
    }

    private void addCagarBudaya(String downloadURL) {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Membuat objek cagarBudayaModel berisikan data-data yang sudah diinput
        CagarBudayaModel cagarBudayaModel = new CagarBudayaModel(
                edtNamaCagar.getText().toString(),
                edtDetail.getText().toString(),
                0,
                downloadURL,
                edtLatitude.getText().toString(),
                edtLongitude.getText().toString(),
                edtLinkVR.getText().toString()
        );

        // Menambahkan cagarBudayaModel ke Firestore
        cagarRef.add(cagarBudayaModel)
                .addOnSuccessListener(documentReference -> {
                    progressDialog.dismiss();
                    Toast.makeText(AdminMainActivity.this, "Data berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, e.toString());
                    progressDialog.dismiss();
                });
    }
}