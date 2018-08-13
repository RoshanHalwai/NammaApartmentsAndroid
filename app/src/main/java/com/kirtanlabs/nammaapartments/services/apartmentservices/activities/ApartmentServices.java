package com.kirtanlabs.nammaapartments.services.apartmentservices.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.home.adapters.ApartmentServiceAdapter;

import static com.kirtanlabs.nammaapartments.utilities.Constants.SCREEN_TITLE;

public class ApartmentServices extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.layout_apartment_services;
    }

    @Override
    protected int getActivityTitle() {
        return getIntent().getIntExtra(SCREEN_TITLE, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id of recycler view*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApartmentServiceAdapter adapter = new ApartmentServiceAdapter(this);

        recyclerView.setAdapter(adapter);

    }
}