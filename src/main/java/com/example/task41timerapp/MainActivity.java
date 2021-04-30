package com.example.task41timerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.WorkSource;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    EditText workoutType;
    TextView previousTextView;
    TextView timerText;
    ImageButton start, pause, stop;
    Boolean timerStarted = false;
    Boolean paused = false;
    Timer timer;
    TimerTask timerTask;
    double time = 0.0;
    SharedPreferences sharedPreferences;
    String WORKOUT_TYPE = "com.example.task41timerapp1";
    String TIMER_VALUE = "com.example.task41timerapp2";
    String CURRENT_TIME = "1", TIMER_STARTED = "2", PAUSED = "3", CURRENT_TIME_TEXT = "4", TEXT = "5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        workoutType = findViewById(R.id.workoutType);
        previousTextView = findViewById(R.id.previousTextView);
        timerText = (TextView) findViewById(R.id.timerTextView);
        start = (ImageButton) findViewById(R.id.startBtn);
        pause = (ImageButton) findViewById(R.id.pauseBtn);
        stop = (ImageButton) findViewById(R.id.stopBtn);
        timer = new Timer();

        sharedPreferences = getSharedPreferences("com.example.task41timerapp", MODE_PRIVATE);
        checkSharedPreferences();

        if (savedInstanceState != null){
            time = savedInstanceState.getDouble(CURRENT_TIME);
            timerStarted = savedInstanceState.getBoolean(TIMER_STARTED);
            paused = savedInstanceState.getBoolean(PAUSED);
            TEXT = savedInstanceState.getString(CURRENT_TIME_TEXT);
            if(timerStarted == true && paused == false){ StartTimer();}
            else if (timerStarted == true && paused == true) { timerText.setText(TEXT);}
            else if(timerStarted == false) { timerText.setText(TEXT);}
        }
    }
    public void checkSharedPreferences(){
        String workout_type = sharedPreferences.getString(WORKOUT_TYPE, "");
        String TimerValue = sharedPreferences.getString(TIMER_VALUE, "");
        previousTextView.setText("You spent " + TimerValue + " on " + workout_type + " last time" );
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(CURRENT_TIME, time);
        outState.putBoolean(TIMER_STARTED, timerStarted);
        outState.putBoolean(PAUSED, paused);
        outState.putString(CURRENT_TIME_TEXT, timerText.getText().toString());
    }
    public void Start(View view) {
        if (timerStarted == false || (timerStarted == true && paused == true)) {
            timerStarted = true;
            paused = false;
            StartTimer();
        }
        else {
            Toast.makeText(this, "Timer already running", Toast.LENGTH_LONG).show();
        }
    }
    public void Pause(View view) {
        if (timerStarted == true) {
            paused = true;
            timerTask.cancel();
        }
        else if (paused == true) {
            Toast.makeText(this, "Already Paused", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Please start the timer first", Toast.LENGTH_LONG).show();
        }
    }
    public void Stop(View view) {
        if(timerStarted == true) {
            timerStarted = false;
            paused = false;
            timerTask.cancel();
            time = 0.0;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(TIMER_VALUE, timerText.getText().toString());
            editor.putString(WORKOUT_TYPE, workoutType.getText().toString());
            editor.apply();
        }
        else {
            Toast.makeText(this, "Please start the timer first", Toast.LENGTH_LONG).show();
        }
    }
    public void StartTimer(){
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        timerText.setText(getTimerText());
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }
    private String getTimerText(){
        int approximate = (int) Math.round(time);
        int seconds = ((approximate % 86400) % 3600) % 60;
        int minutes = ((approximate % 86400) % 3600) / 60;
        int hours = (approximate % 86400) / 3600;
        return TimeText(seconds, minutes, hours);
    }
    private String TimeText(int seconds, int minutes, int hours){
        return String.format("%02d", hours) + ":" +  String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
    }
}