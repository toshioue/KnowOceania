package mobileos.usna.edu;

/**
 * Author: MIDN Hitoshi Oue
 * Date: April 27, 2019
 * Description: this interface is used to get information from the unlock and erase dialog
 *
 */
public interface DialogListener {

    public void onUnlockDialog(int remainPoints, String tag);
    public void onEraseDialog();
}
