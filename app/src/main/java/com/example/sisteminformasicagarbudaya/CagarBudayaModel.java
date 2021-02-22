package com.example.sisteminformasicagarbudaya;

import android.os.Parcel;
import android.os.Parcelable;

public class CagarBudayaModel implements Parcelable {
    private String docId, nama, deskripsi, jumlahView, thumbnailUrl, latitude, longitude;

    public CagarBudayaModel(String nama, String deskripsi, String jumlahView,
                            String thumbnailUrl, String latitude, String longitude) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.jumlahView = jumlahView;
        this.thumbnailUrl = thumbnailUrl;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected CagarBudayaModel(Parcel in) {
        docId = in.readString();
        nama = in.readString();
        deskripsi = in.readString();
        jumlahView = in.readString();
        thumbnailUrl = in.readString();
        latitude = in.readString();
        longitude = in.readString();
    }

    public static final Creator<CagarBudayaModel> CREATOR = new Creator<CagarBudayaModel>() {
        @Override
        public CagarBudayaModel createFromParcel(Parcel in) {
            return new CagarBudayaModel(in);
        }

        @Override
        public CagarBudayaModel[] newArray(int size) {
            return new CagarBudayaModel[size];
        }
    };

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getJumlahView() {
        return jumlahView;
    }

    public void setJumlahView(String jumlahView) {
        this.jumlahView = jumlahView;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(docId);
        dest.writeString(nama);
        dest.writeString(deskripsi);
        dest.writeString(jumlahView);
        dest.writeString(thumbnailUrl);
        dest.writeString(latitude);
        dest.writeString(longitude);
    }
}
