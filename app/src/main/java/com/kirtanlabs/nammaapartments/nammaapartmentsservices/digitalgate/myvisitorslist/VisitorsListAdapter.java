package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.myvisitorslist;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

import java.text.DateFormatSymbols;
import java.util.Locale;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/5/2018
 */
public class VisitorsListAdapter extends RecyclerView.Adapter<VisitorsListAdapter.VisitorViewHolder> implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    /* ------------------------------------------------------------- *
     * Public Members
     * ------------------------------------------------------------- */
    public static int count = 5;
    private final Context mCtx;
    private final BaseActivity baseActivity;
    private View rescheduleDialog;
    private AlertDialog dialog;

    private EditText editPickDate;
    private EditText editPickTime;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public VisitorsListAdapter(Context mCtx) {
        this.mCtx = mCtx;
        baseActivity = (BaseActivity) mCtx;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public VisitorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_visitors_and_my_daily_services_list, parent, false);
        return new VisitorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitorViewHolder holder, int position) {
        holder.textVisitorName.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textVisitorType.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textInvitationDateOrServiceRating.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textInvitationTime.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textInvitedByOrNumberOfFlats.setTypeface(Constants.setLatoRegularFont(mCtx));

        holder.textVisitorNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textVisitorTypeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitationDateOrServiceRatingValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitationTimeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitedByOrNumberOfFlatsValue.setTypeface(Constants.setLatoBoldFont(mCtx));

        holder.textCall.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textMessage.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textReschedule.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textCancel.setTypeface(Constants.setLatoRegularFont(mCtx));

        holder.textCall.setOnClickListener(this);
        holder.textMessage.setOnClickListener(this);
        holder.textReschedule.setOnClickListener(this);
        holder.textCancel.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        //TODO: To change the get item count here
        return count;
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textCall:
                //TODO: Change Mobile Number here
                baseActivity.makePhoneCall("9885665744");
                break;
            case R.id.textMessage:
                //TODO: Change Mobile Number here
                baseActivity.sendTextMessage("9885665744");
                break;
            case R.id.textRescheduleOrEdit:
                openRescheduleDialog();
                break;
            case R.id.textCancel:
                baseActivity.openCancelDialog(R.string.my_visitors_list);
                break;
            case R.id.editPickDate:
                baseActivity.pickDate(mCtx, this);
                break;
            case R.id.editPickTime:
                baseActivity.pickTime(mCtx, this);
                break;
            case R.id.buttonReschedule:
                //TODO: On click of Reschedule button the rescheduled date and time should go to firebase.
                dialog.cancel();
                break;
            case R.id.buttonCancel:
                dialog.cancel();
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Overriding OnDateSet & OnTimeSet Listener
     * ------------------------------------------------------------- */

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (view.isShown()) {
            String selectedDate = new DateFormatSymbols().getMonths()[month].substring(0, 3) + " " + dayOfMonth + ", " + year;
            editPickDate.setText(selectedDate);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (view.isShown()) {
            String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            editPickTime.setText(selectedTime);
        }
    }

    /*-------------------------------------------------------------------------------
     *Private Methods
     *-----------------------------------------------------------------------------*/

    /**
     * This method is invoked when user clicks on reschedule icon.
     */
    private void openRescheduleDialog() {
        rescheduleDialog = View.inflate(mCtx, R.layout.layout_dialog_reschedule, null);

        /*Getting Id's for all the views*/
        editPickDate = rescheduleDialog.findViewById(R.id.editPickDate);
        editPickTime = rescheduleDialog.findViewById(R.id.editPickTime);
        TextView textPickDate = rescheduleDialog.findViewById(R.id.textPickDate);
        TextView textPickTime = rescheduleDialog.findViewById(R.id.textPickTime);
        TextView buttonReschedule = rescheduleDialog.findViewById(R.id.buttonReschedule);
        TextView buttonCancel = rescheduleDialog.findViewById(R.id.buttonCancel);

        /*Setting Fonts for all the views*/
        textPickDate.setTypeface(Constants.setLatoRegularFont(mCtx));
        textPickTime.setTypeface(Constants.setLatoRegularFont(mCtx));
        buttonReschedule.setTypeface(Constants.setLatoRegularFont(mCtx));
        buttonCancel.setTypeface(Constants.setLatoRegularFont(mCtx));

        /*We don't want the keyboard to be displayed when user clicks on the pick date and time edit fields*/
        editPickDate.setInputType(InputType.TYPE_NULL);
        editPickTime.setInputType(InputType.TYPE_NULL);

        /*This method is used to create reschedule dialog */
        createRescheduleDialog();

        /*Setting OnClick Listeners to the views*/
        editPickDate.setOnClickListener(this);
        editPickTime.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        buttonReschedule.setOnClickListener(this);
    }

    /**
     * This method is invoked to create a reschedule dialog.
     */
    private void createRescheduleDialog() {
        AlertDialog.Builder alertRescheduleDialog = new AlertDialog.Builder(mCtx);
        alertRescheduleDialog.setView(rescheduleDialog);
        dialog = alertRescheduleDialog.create();

        new Dialog(mCtx);
        dialog.show();
    }

    /* ------------------------------------------------------------- *
     * Visitor View Holder class
     * ------------------------------------------------------------- */
    class VisitorViewHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textVisitorName;
        private final TextView textVisitorNameValue;
        private final TextView textVisitorType;
        private final TextView textVisitorTypeValue;
        private final TextView textInvitationDateOrServiceRating;
        private final TextView textInvitationDateOrServiceRatingValue;
        private final TextView textInvitationTime;
        private final TextView textInvitationTimeValue;
        private final TextView textInvitedByOrNumberOfFlats;
        private final TextView textInvitedByOrNumberOfFlatsValue;

        private final TextView textCall;
        private final TextView textMessage;
        private final TextView textReschedule;
        private final TextView textCancel;

        public final LinearLayout itemList;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        VisitorViewHolder(View itemView) {
            super(itemView);
            textVisitorName = itemView.findViewById(R.id.textVisitorOrServiceName);
            textVisitorType = itemView.findViewById(R.id.textVisitorOrServiceType);
            textInvitationDateOrServiceRating = itemView.findViewById(R.id.textInvitationDateOrServiceRating);
            textInvitationTime = itemView.findViewById(R.id.textInvitationTime);
            textInvitedByOrNumberOfFlats = itemView.findViewById(R.id.textInvitedByOrNumberOfFlats);

            textVisitorNameValue = itemView.findViewById(R.id.textVisitorOrServiceNameValue);
            textVisitorTypeValue = itemView.findViewById(R.id.textVisitorOrServiceTypeValue);
            textInvitationDateOrServiceRatingValue = itemView.findViewById(R.id.textInvitationDateOrServiceRatingValue);
            textInvitationTimeValue = itemView.findViewById(R.id.textInvitationTimeValue);
            textInvitedByOrNumberOfFlatsValue = itemView.findViewById(R.id.textInvitedByOrNumberOfFlatsValue);

            textCall = itemView.findViewById(R.id.textCall);
            textMessage = itemView.findViewById(R.id.textMessage);
            textReschedule = itemView.findViewById(R.id.textRescheduleOrEdit);
            textCancel = itemView.findViewById(R.id.textCancel);

            itemList = itemView.findViewById(R.id.itemList);
        }
    }

}
