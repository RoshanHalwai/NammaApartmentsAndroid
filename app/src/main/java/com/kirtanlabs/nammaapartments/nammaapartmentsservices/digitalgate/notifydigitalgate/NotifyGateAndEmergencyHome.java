package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.notifydigitalgate;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentshome.Service;

import java.util.ArrayList;
import java.util.List;

public class NotifyGateAndEmergencyHome extends BaseActivity {

    int serviceType;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.layout_notify_gate_and_emergency;
    }

    @Override
    protected int getActivityTitle() {
        /*We use a common class for Notify Digital Gate and Emergency, we set the title
         * based on the user click on NotifyGateAndEmergencyHome Home screen*/
        if (getIntent().getIntExtra(Constants.SERVICE_TYPE, 0) == R.string.notify_digital_gate) {
            serviceType = R.string.notify_digital_gate;
        } else {
            serviceType = R.string.emergency;
        }
        return serviceType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        RecyclerView recyclerView = findViewById(R.id.listViewNotifyServices);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the notificationServicesList
        List<Service> notificationServicesList = new ArrayList<>();

        /*Since we are using same layout for Notify Digital Cab and emergency we need to
         * change the images for emergency and text*/
        if (getIntent().getIntExtra(Constants.SERVICE_TYPE, 0) == R.string.notify_digital_gate) {
            /*Adding some values to our list*/
            notificationServicesList.add(new Service(R.drawable.taxi, getString(R.string.expecting_cab_arrival)));
            notificationServicesList.add(new Service(R.drawable.delivery_man, getString(R.string.expecting_package_arrival)));
            notificationServicesList.add(new Service(R.drawable.team, getString(R.string.expecting_visitor)));
            notificationServicesList.add(new Service(R.drawable.gift, getString(R.string.handed_things_to_my_guest)));
            notificationServicesList.add(new Service(R.drawable.delivery, getString(R.string.handed_things_to_my_daily_services)));

        } else {
            notificationServicesList.add(new Service(R.drawable.medical_emergency_heart, getString(R.string.medical_emergency)));
            notificationServicesList.add(new Service(R.drawable.fire_alarm, getString(R.string.raise_fire_alarm)));
            notificationServicesList.add(new Service(R.drawable.theft_alarm, getString(R.string.raise_theft_alarm)));
        }
        /*Creating the Adapter*/
        NotifyGateAndEmergencyAdapter adapter = new NotifyGateAndEmergencyAdapter(this, notificationServicesList);

        /*Attaching adapter to the listview */
        recyclerView.setAdapter(adapter);
    }
}