package mobileos.usna.edu;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

/**
 * Author: MIDN Hitoshi Oue
 * Date: April 27, 2019
 * Description: this class is a dialog pop up that asks the user if they want to
 *              unlock an island. the dialog will pass info back through a dialoglistener if
 *              user decides to do so. signalling that the total points should be subtracted in the
 *              main activity and store to file.
 */
public class UnlockDialog extends DialogFragment implements DialogInterface.OnClickListener {
    int total;
    String tag;
    DialogListener myListener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        total = getArguments().getInt("minusPoints");
        tag = getArguments().getString("ViewTag");
        //Toast.makeText(getContext(), total , Toast.LENGTH_SHORT).show();

        //initialize myListener with the parent activity
        Activity activity = getActivity();
        try {
            myListener = (DialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MyDialogListener");
        }


        builder.setTitle("UNLOCK THE ISLAND!");
        //builder.setIcon(R.drawable.cs_logo);
        builder.setMessage("You have enough points to unlock this island. will you spend 5 points to unlock this island");
        builder.setPositiveButton("BUY", this);
        builder.setNegativeButton("EXIT", this);


        return builder.create();

    }



    @Override
    public void onClick(DialogInterface dialog, int which) {

        String buttonName = "";

        switch (which) {
            case Dialog.BUTTON_NEGATIVE: buttonName = "Negative";

            break;
            case Dialog.BUTTON_NEUTRAL:  buttonName = "Neutral";

            break;
            case Dialog.BUTTON_POSITIVE: buttonName = "Positive";
            int remainPoints = total - 5;
            myListener.onUnlockDialog(remainPoints, tag );
            break;
        }

    }
}
