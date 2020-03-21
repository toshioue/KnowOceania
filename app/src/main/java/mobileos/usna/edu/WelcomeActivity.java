package mobileos.usna.edu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Author: MIDN Hitoshi Oue
 * Date: April 300, 2019
 * Description: this is just an activity that launches the welcome page displaying the logo of the app
 *              it gets launched from the main
 */
public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);

        start = findViewById(R.id.startButton);
        start.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == start){
            finish();
        }
    }
}


