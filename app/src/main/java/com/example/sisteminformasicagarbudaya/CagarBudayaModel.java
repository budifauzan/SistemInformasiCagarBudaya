package com.example.sisteminformasicagarbudaya;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

public class CagarBudayaModel implements Parcelable {
    private String docId, nama, detail, thumbnailUrl, latitude, longitude, linkVR;
    private int jumlahView, jarakDariUser;

    public CagarBudayaModel() {
    }

    public CagarBudayaModel(String nama, String detail, int jumlahView,
                            String thumbnailUrl, String latitude, String longitude, String linkVR) {
        this.nama = nama;
        this.detail = detail;
        this.jumlahView = jumlahView;
        this.thumbnailUrl = thumbnailUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.linkVR = linkVR;
    }

    protected CagarBudayaModel(Parcel in) {
        docId = in.readString();
        nama = in.readString();
        detail = in.readString();
        jumlahView = in.readInt();
        thumbnailUrl = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        linkVR = in.readString();
        jarakDariUser = in.readInt();
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

    @Exclude
    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getNama() {
        return nama;
    }

    public String getDetail() {
        return detail;
    }

    public int getJumlahView() {
        return jumlahView;
    }

    public void setJumlahView(int jumlahView) {
        this.jumlahView = jumlahView;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLinkVR() {
        return linkVR;
    }

    @Exclude
    public int getJarakDariUser() {
        return jarakDariUser;
    }

    public void setJarakDariUser(int jarakDariUser) {
        this.jarakDariUser = jarakDariUser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(docId);
        dest.writeString(nama);
        dest.writeString(detail);
        dest.writeInt(jumlahView);
        dest.writeString(thumbnailUrl);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(linkVR);
        dest.writeInt(jarakDariUser);
    }
}
