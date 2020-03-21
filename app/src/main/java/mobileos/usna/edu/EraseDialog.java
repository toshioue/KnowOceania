package mobileos.usna.edu;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Author: MIDN Hitoshi Oue
 * Date: April 27, 2019
 * Description: this class is a dialog that prompts the user whether they want to erase all their
 *              points and unlocked items so the user can restart the whole game. it was also used for testing purposes
 *              on whether pints and islands unlocked are actually persisted to the file
 *
 *
 */
public class EraseDialog extends DialogFragment implements DialogInterface.OnClickListener {
    DialogListener myListener;

    /**
     * this function initiliazes the dialog and sets up the Dialoglistener to send info back to user
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());

        //Toast.makeText(getContext(), total , Toast.LENGTH_SHORT).show();

        //initialize myListener with the parent activity
        Activity activity = getActivity();
        try {
            myListener = (DialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MyDialogListener");
        }


        builder.setTitle("Warning!");
        //builder.setIcon(R.drawable.cs_logo);
        builder.setMessage("Do you want to ERASE your saved data and play again?");
        builder.setPositiveButton("YES", this);
        builder.setNegativeButton("EXIT", this);


        return builder.create();

    }

    /**
     * this is a method used to determine the next course of action if the user wants to exit out of the
     * erase dialog or actually want to erase their saved data.
     * @param dialog
     * @param which
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {

        String buttonName = "";

        switch (which) {
            case Dialog.BUTTON_NEGATIVE: buttonName = "Negative";

                break;
            case Dialog.BUTTON_NEUTRAL:  buttonName = "Neutral";

                break;
            case Dialog.BUTTON_POSITIVE: buttonName = "Positive";
                myListener.onEraseDialog(); // this method call signals the main activity to delete info
                break;
        }

    }
}
