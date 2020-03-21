package mobileos.usna.edu;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: MIDN Hitoshi Oue
 * Description: This is the main Activity of the whole project which has a map layout
 *              with flags that act like buttons. this main activity branches out towards
 *              other classes and layouts which include DatabasesHelper, IslandLayout, and QuizLayout
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, DialogListener {
    //Views in layout that will be used to interact from user
    ImageView fsmFlag;
    ImageView marshallFlag;
    ImageView solomonFlag;
    ImageView frenchFlag;
    ImageView tongaFlag;
    TextView showTotalPoints;
    DatabaseHelper mDatabase;
    final int ISLAND_REQUEST_CODE = 123;
    PointsGainedDialog pointsDialog;
    //variables that will be used to read and write to file
    Map<String, Boolean> islandLocker;
    int totalPoints;
    int id;

    /**
     * this function initializez all views and sets up the database
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, WelcomeActivity.class));
        setContentView(R.layout.activity_main);


        //initiliaze all views, buttons, etc.
        fsmFlag = findViewById(R.id.fsm);
        marshallFlag = findViewById(R.id.marshalls);
        solomonFlag = findViewById(R.id.solomon);
        frenchFlag = findViewById(R.id.FrenchPoly);
        tongaFlag = findViewById(R.id.tonga);
        showTotalPoints = findViewById(R.id.totalPoints);

        //set onClicklistener for buttons
        fsmFlag.setOnClickListener(this);
        marshallFlag.setOnClickListener(this);
        solomonFlag.setOnClickListener(this);
        frenchFlag.setOnClickListener(this);
        tongaFlag.setOnClickListener(this);

        //initalize open DB
        mDatabase = new DatabaseHelper(this);

        //check if database exists
        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
        if(database.exists() == false){
            mDatabase.getReadableDatabase();
            if(copyDatabase(this)){
                Toast.makeText(getBaseContext(),  "DB Copied", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(getBaseContext(),  "DB Error", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //get stored Points and Unlocked
        totalPoints = (int) readObjectFromFile("points.txt");
        showTotalPoints.setText(Integer.toString(totalPoints));

        //get stored Locked/Unlocked Islands
        islandLocker = (Map<String, Boolean>) readObjectFromFile("islandLockers.txt");
        lockIslands();

    }

    /**
     * this is a helper function that basically copies the database file stored into assets into the
     * phone's internal memory/file device explorer so that it can be read from in the right file location.
     * @param context
     * @return
     */
    private boolean copyDatabase(Context context){
        try{
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while((length = inputStream.read(buff)) > 0){
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.i("Oue_Project", "DB Copied");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * this functions deals with what the main Activity will do when certain views and buttons
     * get clicked
     * @param v
     */
    @Override
    public void onClick(View v) {

        Toast.makeText(getBaseContext(),  v.getTag() + " is the flag clicked", Toast.LENGTH_SHORT).show();
        //get island info from database and store into IslandInfo object
        IslandInfo island = mDatabase.getIslandInfo((String)v.getTag());

        //if island is unlocked, start the island activity
        if(islandLocker.get(v.getTag()).equals(true)){
            StartIslandLayout(island);
        }else{ //if unlocked send alert to user, not enough points!
            if(totalPoints < 5){
                //make AlertDialog pop up!
                AlertDialog alerDialog = new AlertDialog();
                alerDialog.show(getSupportFragmentManager(), "alertDialog");
            }else if (totalPoints >= 5){  // if user has enough points, ask use rif they want to unlock the island
                //make buyDialog pop up!
                UnlockDialog unlockDialog = new UnlockDialog();
                Bundle args = new Bundle();
                args.putInt("minusPoints", totalPoints);
                args.putString("ViewTag", v.getTag().toString());
                unlockDialog.setArguments(args);
                unlockDialog.show(getSupportFragmentManager(), "unlockDialog");

            }


        }



    }

    /**
     * this function is a helper function that will launch the islandlayout class and pass the
     * island information loaded from the database within an intent.
     * @param targetIsland
     */
    private void StartIslandLayout(IslandInfo targetIsland){

        // create intent
        Intent intent = new Intent(getBaseContext(), IslandLayout.class);
        intent.putExtra("island", targetIsland);
        // start activity for result
        startActivityForResult(intent, ISLAND_REQUEST_CODE);


    }

    /**
     * this is a method that is called when the island layout passes the numbe rof points
     * gained from the quiz and adds it to the total points and persists it to the file
     * @param requestCode
     * @param returnCode
     * @param receivedIntent
     */
    @Override
    public void onActivityResult(int requestCode, int returnCode, Intent receivedIntent){
        super.onActivityResult(requestCode, returnCode, receivedIntent);
        if (requestCode == ISLAND_REQUEST_CODE){
            if(returnCode == RESULT_OK){

                int gainedPoints = receivedIntent.getIntExtra("Points", 0);
                //add the points to the main Map screen
                totalPoints += gainedPoints;
                showTotalPoints.setText(Integer.toString(totalPoints));
                //store points to file
                writeObjectToFile("points.txt", totalPoints);

                //Toast.makeText(getBaseContext(),  gainedPoints +" gained from Quiz!", Toast.LENGTH_SHORT).show();
                //create a dialog to display how many points gained
                 pointsDialog = new PointsGainedDialog();
                Bundle args = new Bundle();
                args.putInt("gainedPoints", gainedPoints);
                pointsDialog.setArguments(args);
                pointsDialog.show(getSupportFragmentManager(), "PointsGainedDialog");

            }
        }




    }

    /**
     * this method is used to persists island unlocked and points to file"
     * @param filename the name of file to write to
     * @param object the object or list of tasks that will get stored
     */
    //save object to file
    public void writeObjectToFile(String filename, Object object){

        //create file out stream
        try {

            FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);

            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(object);
            Log.i("Oue_Project", "Task written to file");

            oos.close();

            fos.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }


    /**
     * this method reads fom the files both points.txt or islandlockers.txt to get info
     * @param filename "points.txt or islandLocker.txt
     * @return returns a persisted data
     */
    public Object readObjectFromFile(String filename){

        Object returnObject = null;

        try {
            FileInputStream fis = openFileInput(filename);

            ObjectInputStream ois = new ObjectInputStream(fis);

            returnObject = ois.readObject();

            ois.close();
            fis.close();


        } catch (FileNotFoundException e){
            //if a file is not found initialize it here!!!
            if(filename.equals("points.txt")){
                int tempPoints = 0;
                writeObjectToFile("points.txt", tempPoints);
                return tempPoints;
            }else if(filename.equals("islandLockers.txt")){
               /* Map<String, Boolean> tempLock = new HashMap<>();
                tempLock.put(getString(R.string.FSM), true);
                tempLock.put(getString(R.string.Marshalls), false);
                tempLock.put(getString(R.string.Solomon), false);
                tempLock.put(getString(R.string.French), false);
                tempLock.put(getString(R.string.Tonga), false);
                writeObjectToFile("islandLockers.txt", tempLock);*/
               //if no file exists make one set it all to false and return
                return initializeLock();
            }


        } catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        return returnObject;

    }

    /**
     * this is a helper function that returns a map that stores all the island names
     * and sets them to false which means they are all unlocked except the FSM island.
     * <Island Name, boolean value if locked?>
     * @return
     */
    private Map<String, Boolean> initializeLock(){
        Map<String, Boolean> tempLock = new HashMap<>();
        tempLock.put(getString(R.string.FSM), true);
        tempLock.put(getString(R.string.Marshalls), false);
        tempLock.put(getString(R.string.Solomon), false);
        tempLock.put(getString(R.string.French), false);
        tempLock.put(getString(R.string.Tonga), false);
        writeObjectToFile("islandLockers.txt", tempLock);
        return tempLock;
    }

    /**
     * this is another helper functions that set all the flags that have a lock status to a
     * transparent green color signaling that it is locked and cannot be clicked
     */
    private void lockIslands(){
        if(islandLocker.get(marshallFlag.getTag()) == false){
            marshallFlag.setColorFilter( 0x80808000, PorterDuff.Mode.MULTIPLY );
        }else{
            marshallFlag.setColorFilter(null);
        }
        if(islandLocker.get(solomonFlag.getTag()) == false){
            solomonFlag.setColorFilter( 0x80808000, PorterDuff.Mode.MULTIPLY );
        }else{
            solomonFlag.setColorFilter(null);
        }

        if(islandLocker.get(frenchFlag.getTag()) == false){
            frenchFlag.setColorFilter( 0x80808000, PorterDuff.Mode.MULTIPLY );
        }else{
            frenchFlag.setColorFilter(null);
        }
        if(islandLocker.get(tongaFlag.getTag()) == false){
            tongaFlag.setColorFilter( 0x80808000, PorterDuff.Mode.MULTIPLY );
        }else{
            tongaFlag.setColorFilter(null);
        }





    }

    /**
     * this method launches the unlock Dialog if the user has enough points to unlock an island.
     * all islands are worth 5 points to unlock
     * @param remainPoints
     * @param tag
     */
    @Override
    public void onUnlockDialog(int remainPoints, String tag) {
        totalPoints = remainPoints;
        writeObjectToFile("points.txt", totalPoints);
        showTotalPoints.setText(Integer.toString(totalPoints));
        islandLocker.remove(tag);
        islandLocker.put(tag, true);
        writeObjectToFile("islandLockers.txt", islandLocker);
        lockIslands();


    }

    /**
     * this function is used to bind the options menu to this layout
     * to display the "ERASE" option
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //get inflater
        MenuInflater inflater = getMenuInflater();
        //transform XML to Java - set menu object
        inflater.inflate(R.menu.options_erase, menu);
        return true;
    }

    /**
     * launches the erase dialog if user clicks on the option ERASE
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String optionSelected = "Nothing";
        switch (item.getItemId()) {
            case R.id.erase:
                optionSelected = "Erase";
                //do activity here
                //startQuizActivity();
                startEraseDialog();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        //display snackbar
        Toast.makeText(getBaseContext(),optionSelected + " option selected", Toast.LENGTH_SHORT).show();

        return true;

    }

    /**
     * this is a helper function that launches the Erase Dialog
     */
    private void startEraseDialog(){
        EraseDialog eraseDialog = new EraseDialog();
        eraseDialog.show(getSupportFragmentManager(), "EraseDialog");

    }

    /**
     * this is a method that erases all persisted data like points and island unlocked
     * basically overwriting the files to a new one
     */
    @Override
    public void onEraseDialog(){
        totalPoints = 0;
        writeObjectToFile("points.txt", totalPoints);
        showTotalPoints.setText(Integer.toString(totalPoints));
        islandLocker = initializeLock();
        writeObjectToFile("islandLockers.txt", islandLocker);
        lockIslands();

    }

}
