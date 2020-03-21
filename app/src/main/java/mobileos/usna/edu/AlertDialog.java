package mobileos.usna.edu;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Author: MIDN Hitoshi Oue
 * Date: April 27, 2019
 * Description: this class is a dialog pop up that is used to alert the user
 *              that they do not have enough points to unlock an islands when a
 *              flag gets clicked on the map
 */
public class AlertDialog extends DialogFragment implements DialogInterface.OnClickListener {

    /**
     * this function initializes the dialog with texts and buttons
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());

        builder.setTitle("HOLD UP!");
        //builder.setIcon(R.drawable.cs_logo);
        builder.setMessage("You do NOT have enough Points!. This island is worth 5 points to unlock!");
        builder.setPositiveButton("EXIT", this);


        return builder.create();

    }
    /**
     * this method does not get used at all
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
