package jterm.katydek.handlerdemo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Random mRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This is "final" because we need to access it from within Runnable.run() below.
        final TextView textView = (TextView) findViewById(R.id.random_text);

        // Initialize a new Random variable.
        mRandom = new Random();

        // Create a new Handler. This schedules runnables.
        final Handler mHandler = new Handler();

        // Create a new Runnable. This executes some code when run by the handler.
        final Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                textView.setText("Next number: " + mRandom.nextInt(100));
                mHandler.postDelayed(this, 1000);
            }
        };
        mHandler.postDelayed(mRunnable, 1000);
    }
}
