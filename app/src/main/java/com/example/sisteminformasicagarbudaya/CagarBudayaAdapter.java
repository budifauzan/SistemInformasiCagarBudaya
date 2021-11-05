package com.example.sisteminformasicagarbudaya;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CagarBudayaAdapter extends RecyclerView.Adapter<CagarBudayaAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<CagarBudayaModel> models;

    public CagarBudayaAdapter(Context context, ArrayList<CagarBudayaModel> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public CagarBudayaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_cagar_budaya, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CagarBudayaAdapter.ViewHolder holder, int position) {
        CagarBudayaModel cagarBudayaModel = models.get(position);
        holder.tvNama.setText(cagarBudayaModel.getNama());
        holder.tvJarak.setText(String.format("%,d", cagarBudayaModel.getJarakDariUser()) + "m");
        Glide.with(context).load(cagarBudayaModel.getThumbnailUrl()).into(holder.imgThumbnail);
        holder.tvJumlahView.setText(String.format("%,d", cagarBudayaModel.getJumlahView()) + " x dilihat");
        holder.clContainer.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailCagarActivity.class);
            intent.putExtra("cagarBudayaModel", cagarBudayaModel);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNama;
        private final TextView tvJarak;
        private final TextView tvJumlahView;
        private final ImageView imgThumbnail;
        private final ConstraintLayout clContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_card_cagar_budaya_nama);
            tvJarak = itemView.findViewById(R.id.tv_card_cagar_budaya_jarak);
            tvJumlahView = itemView.findViewById(R.id.tv_card_cagar_budaya_jumlah_view);
            imgThumbnail = itemView.findViewById(R.id.img_card_cagar_budaya_thumb);
            clContainer = itemView.findViewById(R.id.cl_card_cagar_budaya_container);
        }
    }
}
