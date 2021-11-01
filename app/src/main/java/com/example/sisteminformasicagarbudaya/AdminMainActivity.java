package com.example.sisteminformasicagarbudaya;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference cagarRef = firebaseFirestore.collection("CagarBudaya");

    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();

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
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uri = data.getData();
            File file = new File(uri.toString());
            tvNamaFile.setText(file.getName());
            try {
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
        btnUploadThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilihFoto();
            }
        });
        btnTambahkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFoto();
            }
        });
    }

    private void pilihFoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), PICK_IMAGE_REQUEST);
    }

    private void uploadFoto() {
        if (uri != null) {
            progressDialog.setTitle("Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            StorageReference reference = storageReference.child("Thumbnails/" + UUID.randomUUID());
            reference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            getDownloadURL(reference);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, e.toString());
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    private void getDownloadURL(StorageReference storageReference) {
        storageReference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadURL = uri.toString();
                        tambahCagarBudaya(downloadURL);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.toString());
                        progressDialog.dismiss();
                    }
                });
    }

    private void tambahCagarBudaya(String downloadURL) {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        CagarBudayaModel cagarBudayaModel = new CagarBudayaModel(
                edtNamaCagar.getText().toString(),
                edtDetail.getText().toString(),
                0,
                downloadURL,
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
                        Log.d(TAG, e.toString());
                        progressDialog.dismiss();
                    }
                });
    }
}