package com.example.sisteminformasicagarbudaya;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {
    private ConstraintLayout clNavbarMenu, clNavbarFilter, clFilterContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateViews();
        setOnClick();
    }

    private void initiateViews() {
        clNavbarMenu = findViewById(R.id.cl_navbar_menu);
        clNavbarFilter = findViewById(R.id.cl_navbar_filter);
        clFilterContainer = findViewById(R.id.cl_main_filter_container);
    }

    private void setOnClick() {
        clNavbarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
}