package mobileos.usna.edu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Author: MIDN Hitoshi Oue
 * Date: April 24, 2019
 * Description: this class is tasked for processing information displayed to screen
 *              when user clicks on a certain flag on the map and binds its information
 *                from the Database to views in the layout. In addition, this class is
 *                responsible for launching the quiz for the specific island country via
 *                options menu.
 */
public class IslandLayout extends AppCompatActivity {

    //views needed to set
    TextView history;
    TextView countryName;
    TextView  language;
    TextView culture;
    TextView population;
    TextView greetings;
    TextView facts;
    ImageView flag;
    ImageView pic1;
    ImageView pic2;
    ImageView pic3;
    ImageView map;


    //island variable
    IslandInfo island;

    //requestcode for quiz
    int QUIZ_REQUEST_CODE = 12;

    /**
     * this function is used to intitiaze all views with pertinent island info that
     * was selected on the map using a certain layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the layout to island generic layout
        setContentView(R.layout.islandinfo_main);

        //get intent from main_activity and store into island variable
        Intent intent = getIntent();
        island = (IslandInfo) intent.getSerializableExtra("island");

        //set all views to layout
        countryName = findViewById(R.id.countryName);
        history = findViewById(R.id.history);
        language = findViewById(R.id.languages);
        facts = findViewById(R.id.facts);
        culture = findViewById(R.id.culture);
        flag = findViewById(R.id.flag);
        map = findViewById(R.id.islandMap);
        population = findViewById(R.id.population);
        greetings = findViewById(R.id.greetings);


        //temporary change these soon
        pic1 = findViewById(R.id.temp2);
        pic2 = findViewById(R.id.temp3);
        pic3 = findViewById(R.id.temp4);
        //run the function to set Views with island content
        convertToImageAndSet();
        StringandIntSet();



    }

    /**
     * this function is a helper function that initializes the island information
     * in queried in the database to views in the layout specifically pictures
     */
    private void convertToImageAndSet(){
        //flag image convert
        //Bitmap bitmap = BitmapFactory.decodeByteArray(island.getFlag(), 0, island.getFlag().length);
        //flag.setImageBitmap(bitmap);
        //pic1 image convert
        //bitmap = BitmapFactory.decodeByteArray(island.getPic1(), 0, island.getPic1().length);
        //pic1.setImageBitmap(bitmap);
        //bitmap = BitmapFactory.decodeByteArray(island.getPic2(), 0, island.getPic2().length);
        //pic2.setImageBitmap(bitmap);
        String path = "mobileos.usna.edu:drawable/";
        int image = this.getResources().getIdentifier(path + island.getFlag(), null, null);
        flag.setImageResource(image);
        image = this.getResources().getIdentifier(path + island.getPic1(), null, null);
        pic1.setImageResource(image);
        image = this.getResources().getIdentifier(path + island.getPic2(), null, null);
        pic2.setImageResource(image);
        image = this.getResources().getIdentifier(path + island.getPic3(), null, null);
        pic3.setImageResource(image);
        image = this.getResources().getIdentifier(path + island.getMap(), null, null);
        map.setImageResource(image);
        //map.set

        //int imageR = this.getResources().getIdentifier("mobile")

    }
    /**
     * this is another helper function that initializes island information specifically
     * Strings from the Datatabase to the views in the layout
     */
    private void StringandIntSet(){
        countryName.setText(island.getCountry());
        history.setText(island.getHistory());
        population.setText(Integer.toString(island.getPopulation()));
        facts.setText(island.getFacts());
        culture.setText(island.getCulture());
        language.setText(island.getLanguages());
        greetings.setText(island.getGreeting());



    }

    /**
     * this function is used to bind the options menu to this layout
     * to display the "Take Quiz" option
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //get inflater
        MenuInflater inflater = getMenuInflater();
        //transform XML to Java - set menu object
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    /**
     * this method launches the quiz activity only if the user proceeds to click the
     * TAKE QUIZ option
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String optionSelected = "Nothing";
        switch (item.getItemId()) {
            case R.id.quiz:
                optionSelected = "Quiz";
                //do quiz activity here
                startQuizActivity();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        //display snackbar
        Toast.makeText(getBaseContext(),optionSelected + " option selected", Toast.LENGTH_SHORT).show();

        return true;

    }

    /**
     * this is a helper method that launches the quiz activity and passes the neccessary
     * quizInfo object queried from the database to the layout
     */
    private void startQuizActivity(){
        DatabaseHelper mDatabase = new DatabaseHelper(this);
        ArrayList<QuizInfo> quiz = mDatabase.getQuizInfo(island.getCountry());
        // create intent
        Intent intent = new Intent(getBaseContext(), QuizLoader.class);
        intent.putExtra("quiz", quiz);
        // start activity for result
        startActivityForResult(intent, QUIZ_REQUEST_CODE);


    }

    /**
     * this method is runned when the quiz activity is finished and the user has
     * finish taking the quiz is deals with tallying up all points gained from the quiz and passing
     * intent from the quiz activity to the main activity
     * @param requestCode
     * @param returnCode
     * @param receivedIntent
     */
    @Override
    public void onActivityResult(int requestCode, int returnCode, Intent receivedIntent){
        if (requestCode == QUIZ_REQUEST_CODE){
            if(returnCode == RESULT_OK){
                //when quiz activity ends, pass the intent to main activity
                setResult(RESULT_OK, receivedIntent);

                //terminate
                finish();
            }
        }




    }





}
