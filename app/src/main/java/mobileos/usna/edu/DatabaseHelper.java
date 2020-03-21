package mobileos.usna.edu;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


/**
 * Author: MIDN Hitoshi Oue
 * Date: April 27, 2019
 * Description: this class is responsible for connecting to the sqlite database file that stores all the island
 *              information when a flag gets clicked on as well as the quiz questions
 */
public class DatabaseHelper extends SQLiteOpenHelper {

   public static final String DBNAME = "sample.sqlite";
   public static final String DBLOCATION = "/data/data/mobileos.usna.edu/databases/";
    private Context mContent;
    private SQLiteDatabase mDatabase;

    /**
     * constructor to set up database and get context from main activity
     */
    public DatabaseHelper(Context context){
        super(context, DBNAME, null, 1);
        this.mContent = context;
    }

    /**
     * this function does not get used or actually override
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    /**
     * this function also does not get override or used
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * this function is used to open up the database file and store it in a variable for use
     */
    public void openDatabase() {
        String dPath = mContent.getDatabasePath(DBNAME).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * this functions closes the connection with the database variable
     */
    public void closeDatabase(){
        if(mDatabase != null){
            mDatabase.close();
        }
    }

    /**
     * this is a function that queries a select statement from the database to get the island information of
     * the flag tha tis was clicked on
     * @param countryName the name of the requested island/flag that was clicked on
     * @return
     */
    public IslandInfo getIslandInfo(String countryName){
        IslandInfo island = null;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Countries WHERE Country = ?", new String[] {countryName});
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
           // island = new IslandInfo(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getBlob(3), cursor.getBlob(4), cursor.getInt(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getBlob(9), cursor.getBlob(10));
            island = new IslandInfo(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11));
            Log.i("Oue_Project", "reading from database and storing in island object");
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return island;



    }

    /**
     * this is a function that queries the database for all quiz questions that are relevant to the island that they
     * are visiting and stores it into a Quizinfo array object for load
     * @param countryName the name of the island they are visiting
     * @return
     */
    public ArrayList<QuizInfo> getQuizInfo(String countryName){
       ArrayList<QuizInfo> questions = new ArrayList<>();
       QuizInfo quest = null;
       openDatabase();
       Cursor cursor = mDatabase.rawQuery("SELECT Question, CorrectAnswer, FalseOne, FalseTwo FROM Quiz WHERE Country = ?", new String[]{countryName});
       cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            quest = new QuizInfo(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            Log.i("Oue_Project", "reading from database and storing in QuizInfo object");
            questions.add(quest);
            cursor.moveToNext();
        }

        return questions;

    }
}
