package com.example.studytimer;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {
    TextView txtTimeSpent;
    Chronometer chrTime;
    ImageButton btnStart,btnPause,btnStop;
    EditText txtTaskName;
    SharedPreferences sharedPreferences;
    Boolean clockState;
    String lastRecordedTime,taskType;
    long CurrentTime,ExtraTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clockState = false;
        ExtraTime =0L;
        txtTimeSpent = findViewById(R.id.txtTimespent);
        chrTime = findViewById(R.id.chrTime);
        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clockState){
                    chrTime.setBase(SystemClock.elapsedRealtime() - ExtraTime);
                    chrTime.start();
                    clockState = true;
                }
            }
        });
        btnPause = findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clockState){
                    chrTime.stop();
                    ExtraTime = SystemClock.elapsedRealtime() - chrTime.getBase();
                    clockState = false;
                }
            }
        });
        btnStop = findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskType = txtTaskName.getText().toString();
                lastRecordedTime = chrTime.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("taskType",taskType);
                editor.putString("lastRecordedTime",lastRecordedTime);
                editor.apply();
                txtTimeSpent.setText(String.format("You spent %s on %s last time!", lastRecordedTime,taskType));
                chrTime.stop();
                chrTime.setBase(SystemClock.elapsedRealtime());
                ExtraTime =0L;
                clockState = false;
            }
        });

        txtTaskName = findViewById(R.id.txtTaskName);
        sharedPreferences = getSharedPreferences("Info",MODE_PRIVATE);
        txtTimeSpent.setText(String.format("You spent %s on %s last time!", sharedPreferences.getString("lastRecordedTime", ""),
                sharedPreferences.getString("taskType", "")));
        if(savedInstanceState!=null){
            taskType = savedInstanceState.getString("taskType");
            clockState = savedInstanceState.getBoolean("clockState");
            lastRecordedTime = savedInstanceState.getString("lastRecordedTime");
            CurrentTime = savedInstanceState.getLong("currentTime");
            ExtraTime = savedInstanceState.getLong("extraTime");
            if(!clockState){
                chrTime.setBase(SystemClock.elapsedRealtime() - ExtraTime);
            }
            else {
                chrTime.setBase(CurrentTime);
                chrTime.start();
            }
        }
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("taskType",taskType);
        outState.putLong("extraTime",ExtraTime);
        outState.putBoolean("clockState",clockState);
        CurrentTime = chrTime.getBase();
        outState.putLong("currentTime",CurrentTime);
        outState.putString("lastRecordedTime",lastRecordedTime);
    }
}