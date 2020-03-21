package mobileos.usna.edu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Author: Midn Hitoshi Oue
 * Date: April 25, 2019
 * Description: This class will be responsible for getting a QuizInfo object array after
 *              it has been loaded from the database and load it onto the new Quiz
 *              layout and perform procedures when the user picks an answer, gets awarded points,
 *              and let the user know if the answer is wrong or right.
 */
public class QuizLoader extends AppCompatActivity implements View.OnClickListener{

    TextView points;
    TextView countQuestion;
    TextView question;
    Button choice1;
    Button choice2;
    Button choice3;
    Button next;
    TextView correct;
    int num;
    int pointCounter;
    String answer;

    //Quiz storing questions n Answering variable
    ArrayList<QuizInfo> questions;

    //temporary var
    QuizInfo test;

    /**
     * this method initializes the quiz layout set up for display
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_layout);

        //initialize all views in quiz layout
        points = findViewById(R.id.points);
        countQuestion = findViewById(R.id.count);
        question = findViewById(R.id.question);
        choice1 = findViewById(R.id.choice1);
        choice2 = findViewById(R.id.choice2);
        choice3 = findViewById(R.id.choice3);
        correct = findViewById(R.id.messageAnswer);
        next = findViewById(R.id.next);

        //initiliaze needed variables to set points or question number
        num = 0;
        pointCounter = 0;


        //load question and answers from quizinfo

        //set buttons as a click listener
        choice1.setOnClickListener(this);
        choice2.setOnClickListener(this);
        choice3.setOnClickListener(this);
        next.setOnClickListener(this);

        //get intent from IslandLayout
        Intent intent = getIntent();
        questions = (ArrayList<QuizInfo>) intent.getSerializableExtra("quiz");

        //setViews();

        //randomize questions order
         Collections.shuffle(questions);


        //sets the random question to layout
        setViews(questions.get(num));



    }

    /**
     * a helper method that sets the views in quiz layout with quiz information.
     * order of button are set randomly
     * @param info
     */
    private void setViews(QuizInfo info){
        Log.i("Oue_Project", "setview happened");
        //choice1.setBackgroundColor(Color.BLUE);
        //set question to view
        question.setText(info.getQuestion());

        //set the next button to invisible so user can see it only when answer is revealed
        next.setVisibility(View.GONE);

        //randomizes choices
        Random rand = new Random();
        int n = rand.nextInt(3) + 1;
        if ( n == 1){
          //  Toast.makeText(getBaseContext(),"random number is 1", Toast.LENGTH_SHORT).show();

            choice1.setText(info.getAnswer());
            choice2.setText(info.getFalseOne());
            choice3.setText(info.getFalseTwo());
        }else if (n == 2){
            //Toast.makeText(getBaseContext(),"random number is 2", Toast.LENGTH_SHORT).show();

            choice2.setText(info.getAnswer());
            choice1.setText(info.getFalseOne());
            choice3.setText(info.getFalseTwo());
        }else if(n == 3){
           // Toast.makeText(getBaseContext(),"random number is 3", Toast.LENGTH_SHORT).show();

            choice3.setText(info.getAnswer());
            choice1.setText(info.getFalseTwo());
            choice2.setText(info.getFalseOne());
        }else{
            //testing purposes to see if randomizer worked.
            Toast.makeText(getBaseContext(),"random number is out of range", Toast.LENGTH_SHORT).show();
        }

        //get answer that will be used for comparison later
        answer = questions.get(num).getAnswer();

        //reset all views that were set previously during the onClick Event
        choice1.setBackgroundColor(Color.LTGRAY);
        choice2.setBackgroundColor(Color.LTGRAY);
        choice3.setBackgroundColor(Color.LTGRAY);
        correct.setText("");

        //set what number question the user is on in the quiz layout
        countQuestion.setText(Integer.toString(num+1));
        num++;

        //set all button to be clickable again
        if(!choice1.isClickable()){
            choice1.setClickable(true);
            choice2.setClickable(true);
            choice3.setClickable(true);
        }




    }

    /**
     * this method deals with if the user clicks on one of the three buttons and check whether
     * the answer is correct or wrong and lets the user know
     * @param v
     */
    @Override
    public void onClick(View v) {
        Log.i("Oue_Project", "onClick happened");
        //grab the view selected and cast to a Button to check answer or go to next one
        Button check = (Button) v;
        if(check == choice1 || check == choice2 || check == choice3) {
            if (check.getText().toString().equals(answer)) {
                //Toast.makeText(getBaseContext(), "answer selected is correct!!", Toast.LENGTH_SHORT).show();
                check.setBackgroundColor(Color.GREEN);
                correct.setText("CORRECT!");
                correct.setTextColor(Color.GREEN);
                pointCounter++;
                points.setText(Integer.toString(pointCounter));
            } else {
                //if answer is wrong!
                //Toast.makeText(getBaseContext(), "answer selected is Wrong!!", Toast.LENGTH_SHORT).show();
                check.setBackgroundColor(Color.RED);
                correct.setText("WRONG!");
                correct.setTextColor(Color.RED);
            }
            //set the next quesiton button to visible so user can see it
            next.setVisibility(View.VISIBLE);

            //set all buttons on layout to unclickable so user wont be able to select another answer
            choice1.setClickable(false);
            choice2.setClickable(false);
            choice3.setClickable(false);

        }else if (check == next){
            if(num+1 > questions.size()){
                Toast.makeText(getBaseContext(),"no more questions left", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("Points", pointCounter);
                setResult(RESULT_OK, intent);
                finish();

            }else {
                setViews(questions.get(num));
            }

        }



    }
}
