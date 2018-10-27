package com.kirtanlabs.nammaapartments.services.societyservices.digigate.mysweethome;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.util.List;

import static android.view.View.GONE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_GRANTEDACCESS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_PRIVILEGES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class MySweetHomeAdapter extends RecyclerView.Adapter<MySweetHomeAdapter.MySweetHomeHolder> implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final BaseActivity baseActivity;
    private final List<NammaApartmentUser> nammaApartmentUserList;
    private boolean grantedAccess;
    private Button buttonYes, buttonNo;
    private View accessDialog;
    private Dialog dialog;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    MySweetHomeAdapter(List<NammaApartmentUser> nammaApartmentUserList, Context mCtx) {
        this.mCtx = mCtx;
        baseActivity = (BaseActivity) mCtx;
        this.nammaApartmentUserList = nammaApartmentUserList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView Adapter object
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public MySweetHomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*inflating and returning our view holder*/
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_visitors_and_my_daily_services_list, parent, false);
        return new MySweetHomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MySweetHomeHolder holder, int position) {
        String stringMemberName = mCtx.getResources().getString(R.string.history_name);

        holder.textMemberName.setText(stringMemberName);
        holder.textGrantedAccess.setText(R.string.access);

        /*Creating an instance of NammaApartmentUser class and retrieving the values from Firebase.*/
        NammaApartmentUser nammaApartmentUser = nammaApartmentUserList.get(position);
        holder.textMemberNameValue.setText(nammaApartmentUser.getPersonalDetails().getFullName());

        /*Setting the value of Relation to display in the My Sweet Home screen*/
        grantedAccess = nammaApartmentUser.getPrivileges().isGrantedAccess();
        String accessValue;
        if (grantedAccess) {
            accessValue = mCtx.getString(R.string.yes);
            holder.textGrantedAccessValue.setText(accessValue);
        } else {
            accessValue = mCtx.getString(R.string.no);
            holder.textGrantedAccessValue.setText(accessValue);
        }

        Glide.with(mCtx.getApplicationContext()).load(nammaApartmentUser.getPersonalDetails().getProfilePhoto())
                .into(holder.visitorOrDailyServiceProfilePic);

        holder.textEdit.setText(R.string.edit);
        holder.textCancel.setText(R.string.remove);

        /* Making these views Visibility GONE , as they are not required here*/
        holder.textInvitationDateOrServiceRating.setVisibility(GONE);
        holder.textInvitedByOrNumberOfFlats.setVisibility(GONE);
        holder.textInvitationDateOrServiceRatingValue.setVisibility(GONE);
        holder.textInvitedByOrNumberOfFlatsValue.setVisibility(GONE);

        /*Here we are changing edit icon*/
        holder.textEdit.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.edit_new, 0, 0);

    }

    @Override
    public int getItemCount() {
        return nammaApartmentUserList.size();
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonYes:
                grantedAccess = true;
                buttonYes.setBackgroundResource(R.drawable.button_guest_selected);
                buttonNo.setBackgroundResource(R.drawable.button_guest_not_selected);
                buttonYes.setTextColor(Color.WHITE);
                buttonNo.setTextColor(Color.BLACK);
                break;
            case R.id.buttonNo:
                grantedAccess = false;
                buttonYes.setBackgroundResource(R.drawable.button_guest_not_selected);
                buttonNo.setBackgroundResource(R.drawable.button_guest_selected);
                buttonYes.setTextColor(Color.BLACK);
                buttonNo.setTextColor(Color.WHITE);
                break;
            case R.id.buttonCancel:
                dialog.cancel();
                break;
        }
    }

    /*-------------------------------------------------------------------------------
     *Private Methods
     *-----------------------------------------------------------------------------*/

    /**
     * This method creates Access Dialog in which user can change access of other family members/friends.
     *
     * @param nammaApartmentUser instance of NammaApartment User class in which it contains values in cardview.
     * @param position           of cardview for which granted access to be manipulated.
     */
    private void openAccessDialog(NammaApartmentUser nammaApartmentUser, int position) {
        accessDialog = View.inflate(mCtx, R.layout.layout_granted_access, null);

        /*Getting Id's for all the views*/
        TextView textGrantAccess = accessDialog.findViewById(R.id.textGrantAccess);
        buttonYes = accessDialog.findViewById(R.id.buttonYes);
        buttonNo = accessDialog.findViewById(R.id.buttonNo);
        TextView buttonChangeAccess = accessDialog.findViewById(R.id.buttonChangeAccess);
        TextView buttonCancel = accessDialog.findViewById(R.id.buttonCancel);

        /*Setting Fonts for all the views*/
        textGrantAccess.setTypeface(setLatoBoldFont(mCtx));
        buttonYes.setTypeface(setLatoRegularFont(mCtx));
        buttonNo.setTypeface(setLatoRegularFont(mCtx));
        buttonChangeAccess.setTypeface(setLatoRegularFont(mCtx));
        buttonCancel.setTypeface(setLatoRegularFont(mCtx));

        grantedAccess = nammaApartmentUser.getPrivileges().isGrantedAccess();
        /*Based on the Access Value From Card View we are displaying proper Granted Access buttons.*/
        if (grantedAccess) {
            buttonYes.setBackgroundResource(R.drawable.button_guest_selected);
            buttonNo.setBackgroundResource(R.drawable.button_guest_not_selected);
            buttonYes.setTextColor(Color.WHITE);
            buttonNo.setTextColor(Color.BLACK);
        } else {
            buttonYes.setBackgroundResource(R.drawable.button_guest_not_selected);
            buttonNo.setBackgroundResource(R.drawable.button_guest_selected);
            buttonYes.setTextColor(Color.BLACK);
            buttonNo.setTextColor(Color.WHITE);
        }

        /*Setting OnClick Listeners to the views*/
        buttonYes.setOnClickListener(this);
        buttonNo.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        buttonChangeAccess.setOnClickListener(v -> {
            updateFamilyMemberDetails(position);
            dialog.cancel();
        });

        /*This method is used to create access dialog */
        createAccessDialog();

    }

    /**
     * This method is invoked to create a access dialog.
     */
    private void createAccessDialog() {
        AlertDialog.Builder alertAccessDialog = new AlertDialog.Builder(mCtx);
        alertAccessDialog.setView(accessDialog);
        dialog = alertAccessDialog.create();
        new Dialog(mCtx);
        dialog.show();
    }

    /**
     * Based on the position access will be updated in both UI and Firebase
     *
     * @param position of card view for which granted access has been manipulated
     */
    private void updateFamilyMemberDetails(int position) {
        NammaApartmentUser nammaApartmentUser = nammaApartmentUserList.get(position);
        boolean updatedAccessValue = grantedAccess;
        nammaApartmentUser.getPrivileges().setGrantedAccess(updatedAccessValue);
        notifyItemChanged(position);
        DatabaseReference updatedGrantedAccessReference = PRIVATE_USERS_REFERENCE.child(nammaApartmentUser.getUID())
                .child(FIREBASE_CHILD_PRIVILEGES)
                .child(FIREBASE_CHILD_GRANTEDACCESS);
        updatedGrantedAccessReference.setValue(updatedAccessValue);
    }

    /* ------------------------------------------------------------- *
     * My Sweet Home Holder class
     * ------------------------------------------------------------- */

    class MySweetHomeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        final LinearLayout layoutTitle;
        final LinearLayout layoutTitleValues;
        final TextView textMemberName;
        final TextView textMemberNameValue;
        final TextView textMemberRelation;
        final TextView textMemberRelationValue;
        final TextView textInvitationDateOrServiceRating;
        final TextView textInvitationDateOrServiceRatingValue;
        final TextView textGrantedAccess;
        final TextView textGrantedAccessValue;
        final TextView textInvitedByOrNumberOfFlats;
        final TextView textInvitedByOrNumberOfFlatsValue;
        final TextView textCall;
        final TextView textMessage;
        final TextView textEdit;
        final TextView textCancel;
        private final de.hdodenhof.circleimageview.CircleImageView visitorOrDailyServiceProfilePic;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        MySweetHomeHolder(View itemView) {
            super(itemView);

            /*Getting Id's for all the views*/
            layoutTitle = itemView.findViewById(R.id.layoutTitle);
            layoutTitleValues = itemView.findViewById(R.id.layoutTitleValues);
            textMemberName = itemView.findViewById(R.id.textVisitorOrServiceName);
            textMemberRelation = itemView.findViewById(R.id.textVisitorOrServiceType);
            textInvitationDateOrServiceRating = itemView.findViewById(R.id.textInvitationDateOrServiceRating);
            textGrantedAccess = itemView.findViewById(R.id.textInvitationTimeOrStatus);
            textInvitedByOrNumberOfFlats = itemView.findViewById(R.id.textInvitedByOrNumberOfFlats);

            textMemberNameValue = itemView.findViewById(R.id.textVisitorOrServiceNameValue);
            textMemberRelationValue = itemView.findViewById(R.id.textVisitorOrServiceTypeValue);
            textInvitationDateOrServiceRatingValue = itemView.findViewById(R.id.textInvitationDateOrServiceRatingValue);
            textGrantedAccessValue = itemView.findViewById(R.id.textInvitationTimeOrStatusValue);
            textInvitedByOrNumberOfFlatsValue = itemView.findViewById(R.id.textInvitedByOrNumberOfFlatsValue);
            visitorOrDailyServiceProfilePic = itemView.findViewById(R.id.visitorOrDailyServiceProfilePic);

            textCall = itemView.findViewById(R.id.textCall);
            textMessage = itemView.findViewById(R.id.textMessage);
            textEdit = itemView.findViewById(R.id.textRescheduleOrEdit);
            textCancel = itemView.findViewById(R.id.textCancel);

            /*TODO: Add a fix in the Next Version*/
            textMemberRelation.setVisibility(GONE);
            textMemberRelationValue.setVisibility(GONE);

            /*Setting font for all the views*/
            textMemberName.setTypeface(Constants.setLatoRegularFont(mCtx));
            textInvitationDateOrServiceRating.setTypeface(Constants.setLatoRegularFont(mCtx));
            textGrantedAccess.setTypeface(Constants.setLatoRegularFont(mCtx));
            textInvitedByOrNumberOfFlats.setTypeface(Constants.setLatoRegularFont(mCtx));

            textMemberNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textInvitationDateOrServiceRatingValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textGrantedAccessValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textInvitedByOrNumberOfFlatsValue.setTypeface(Constants.setLatoBoldFont(mCtx));

            textCall.setTypeface(Constants.setLatoBoldItalicFont(mCtx));
            textMessage.setTypeface(Constants.setLatoBoldItalicFont(mCtx));
            textEdit.setTypeface(Constants.setLatoBoldItalicFont(mCtx));
            textCancel.setTypeface(Constants.setLatoBoldItalicFont(mCtx));

            /*Programmatically setting the layoutTitle as this layout contains only three titles */
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 1.13f;
            params.gravity = Gravity.CENTER;
            layoutTitle.setLayoutParams(params);

            /*Programmatically setting the layoutTitleValues as this layout contains only three values */
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 0.87f;
            layoutParams.gravity = Gravity.CENTER;
            layoutTitleValues.setLayoutParams(layoutParams);

            /*Setting events for items in card view*/
            textCall.setOnClickListener(this);
            textMessage.setOnClickListener(this);
            textEdit.setOnClickListener(this);
            textCancel.setOnClickListener(this);
        }

        /* ------------------------------------------------------------- *
         * Overriding OnClick Listeners
         * ------------------------------------------------------------- */
        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            NammaApartmentUser nammaApartmentUser = nammaApartmentUserList.get(position);
            /*Here first we are getting current user admin value based on the NammaApartment User class.*/
            NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) mCtx.getApplicationContext()).getNammaApartmentUser();
            boolean isAdmin = currentNammaApartmentUser.getPrivileges().isAdmin();
            switch (v.getId()) {
                case R.id.textCall:
                    baseActivity.makePhoneCall(nammaApartmentUser.getPersonalDetails().getPhoneNumber());
                    break;
                case R.id.textMessage:
                    baseActivity.sendTextMessage(nammaApartmentUser.getPersonalDetails().getPhoneNumber());
                    break;
                case R.id.textRescheduleOrEdit:
                    /*Here we are checking if the value is true i.e if the user is admin and can edit other
                     *non admin family members.*/
                    if (isAdmin) {
                        /*Create an Access Dialog in which user can change access of other family members.*/
                        openAccessDialog(nammaApartmentUser, position);
                    } else {
                        /*Here we are showing users a dialog box since they are not admin of that particular flat.*/
                        baseActivity.showNotificationDialog(mCtx.getResources().getString(R.string.non_admin_edit_title_message),
                                mCtx.getResources().getString(R.string.non_admin_edit_message), null);
                    }
                    break;
                case R.id.textCancel:
                    /*Here we are showing a notification dialog to users that remove feature is not yet implemented*/
                    baseActivity.showNotificationDialog(mCtx.getString(R.string.remove_dialog_title),
                            mCtx.getString(R.string.remove_dialog_message), null);
                    break;
            }
        }
    }

}
