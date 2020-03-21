package mobileos.usna.edu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

/**
 * Author: MIDN Hitoshi Oue
 * Date: April 27, 2019
 * Description: this dialog class that is responsible for displaying to the user how many
 *              points they have gained from taking a quiz. this is displayed in the main activity
 *
 */
public class PointsGainedDialog extends DialogFragment implements DialogInterface.OnClickListener  {

    int points;

    /**
     * this method initializes the dialog and gets the intent of how many points gained
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        points = getArguments().getInt("gainedPoints");

        builder.setTitle("Congratulations!");
        //builder.setIcon(R.drawable.cs_logo);
        builder.setMessage("You gained " + points + " points from the Quiz");
        builder.setPositiveButton("EXIT", this);


        return builder.create();

    }

    /**
     * this method does not get used since the only button on this dialog is EXIT
     * @param dialog
     * @param which
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
