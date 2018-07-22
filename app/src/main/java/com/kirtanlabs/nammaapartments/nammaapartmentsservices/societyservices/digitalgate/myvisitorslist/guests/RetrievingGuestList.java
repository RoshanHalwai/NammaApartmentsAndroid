package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.myvisitorslist.guests;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.invitevisitors.NammaApartmentGuest;

import java.util.ArrayList;
import java.util.List;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 7/20/2018
 */
public class RetrievingGuestList {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private DatabaseReference userDataReference;
    private int count = 0;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public RetrievingGuestList(Context context) {
        NammaApartmentsGlobal nammaApartmentsGlobal = ((NammaApartmentsGlobal) context.getApplicationContext());
        userDataReference = nammaApartmentsGlobal.getUserDataReference();
    }

    /* ------------------------------------------------------------- *
     * Public Methods
     * ------------------------------------------------------------- */

    /**
     * @param guestListCallback receiving result with list of all guest data of userUID present in
     *                          userUIDList
     * @param userUIDList       which contains a list of userUID whose guest list needs to be retrieved
     */
    public void getGuests(GuestListCallback guestListCallback, List<String> userUIDList) {
        List<NammaApartmentGuest> nammaApartmentAllGuestList = new ArrayList<>();
        isGuestReferenceExists(guestReferenceExits -> {
            if (guestReferenceExits) {
                for (String userUID : userUIDList) {
                    getGuests(nammaApartmentGuestList -> {
                        nammaApartmentAllGuestList.addAll(nammaApartmentGuestList);
                        count++;
                        if (count == userUIDList.size()) {
                            guestListCallback.onCallBack(nammaApartmentAllGuestList);
                        }
                    }, userUID);
                }
            } else {
                guestListCallback.onCallBack(nammaApartmentAllGuestList);
            }
        });
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * @param guestListCallback receiving result with list of all guest data of userUID
     * @param userUID           whose guests needs to be retrieved from firebase
     */
    private void getGuests(GuestListCallback guestListCallback, String userUID) {
        getGuestUIDList(guestUIDList -> getGuestsList(guestListCallback, guestUIDList), userUID);
    }

    /**
     * @param guestListCallback receiving result with list of all guest data whose UID is present in
     *                          guestUIDList
     * @param guestUIDList      contains the list of all guests UID whose data needs to be retrieved from firebase
     */
    private void getGuestsList(GuestListCallback guestListCallback, List<String> guestUIDList) {
        List<NammaApartmentGuest> nammaApartmentGuestList = new ArrayList<>();
        if (guestUIDList.isEmpty()) {
            guestListCallback.onCallBack(nammaApartmentGuestList);
        } else {
            for (String guestUID : guestUIDList) {
                getGuestDataByUID(nammaApartmentGuest -> {
                    nammaApartmentGuestList.add(nammaApartmentGuest);
                    if (nammaApartmentGuestList.size() == guestUIDList.size()) {
                        guestListCallback.onCallBack(nammaApartmentGuestList);
                    }
                }, guestUID);
            }
        }
    }

    /**
     * @param guestUIDListCallback receiving result of Guest UID List
     * @param userUID              of the particular user whose guest UID List needs to be retrieved
     *                             from firebase
     */
    private void getGuestUIDList(GuestUIDListCallback guestUIDListCallback, String userUID) {
        DatabaseReference guestListReference = userDataReference.child(Constants.FIREBASE_CHILD_VISITORS).child(userUID);
        guestListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> guestUIDList = new ArrayList<>();
                for (DataSnapshot visitorUIDSnapshot : dataSnapshot.getChildren()) {
                    guestUIDList.add(visitorUIDSnapshot.getKey());
                }
                guestUIDListCallback.onCallBack(guestUIDList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * @param guestDataCallback receiving result of the Guest Data
     * @param guestUID          UID of the Guest whose data is to be retrieved from firebase
     */
    private void getGuestDataByUID(GuestDataCallback guestDataCallback, String guestUID) {
        DatabaseReference guestDataReference = Constants.PREAPPROVED_VISITORS_REFERENCE.child(guestUID);
        guestDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                guestDataCallback.onCallBack(dataSnapshot.getValue(NammaApartmentGuest.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * @param guestsReferenceCallback receives boolean value true if there is at least one visitor for the flat
     *                                else returns false
     */
    private void isGuestReferenceExists(GuestsReferenceCallback guestsReferenceCallback) {
        DatabaseReference guestDataReference = userDataReference.child(Constants.FIREBASE_CHILD_VISITORS);
        guestDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                guestsReferenceCallback.onCallback(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /* ------------------------------------------------------------- *
     * Interfaces
     * ------------------------------------------------------------- */

    public interface GuestListCallback {
        void onCallBack(List<NammaApartmentGuest> nammaApartmentGuestList);
    }

    private interface GuestUIDListCallback {
        void onCallBack(List<String> guestUIDList);
    }

    private interface GuestsReferenceCallback {
        void onCallback(boolean guestReferenceExits);
    }

    private interface GuestDataCallback {
        void onCallBack(NammaApartmentGuest nammaApartmentGuest);
    }

}
