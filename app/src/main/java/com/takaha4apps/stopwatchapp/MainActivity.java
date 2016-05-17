package com.takaha4apps.stopwatchapp;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private long _startTime;
    private long _elapsedTime = 0l;

    private Handler _handler = new Handler();
    private Runnable _updateTimer;

    private Button _startButton;
    private Button _stopButton;
    private Button _resetButton;

    private TextView _timerLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _startButton = (Button) findViewById(R.id.startButton);
        _stopButton = (Button) findViewById(R.id.stopButton);
        _resetButton = (Button) findViewById(R.id.resetButton);

        _timerLabel = (TextView) findViewById(R.id.timerLabel);

        setButtonState(true, false, false);
    }

    private void setButtonState(boolean start , boolean stop , boolean reset){
        _startButton.setEnabled(start);
        _stopButton.setEnabled(stop);
        _resetButton.setEnabled(reset);
    }

    public void startTimer(View view){
        //startTimeの取得
        _startTime = SystemClock.elapsedRealtime();//起動してからの経過時間（ミリ秒）
        //一定時間ごとに現在の経過時間を表示
        // Handler -> Runable(処理) -> UI
        _updateTimer = new Runnable() {
            @Override
            public void run() {
                long t = SystemClock.elapsedRealtime() - _startTime + _elapsedTime; //ミリ秒
                SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.SSS" , Locale.US);
                _timerLabel.setText(sdf.format(t));
                _handler.removeCallbacks(_updateTimer);
                _handler.postDelayed(_updateTimer,10);
            }
        };
        _handler.postDelayed(_updateTimer, 10);

        //ボタンの操作
        setButtonState(false, true, false);
    }

    public void stopTimer(View view){
        _elapsedTime += SystemClock.elapsedRealtime() - _startTime;
        _handler.removeCallbacks(_updateTimer);
        setButtonState(true,false,true);
    }

    public void resetTimer(View view){
        _timerLabel.setText("00:00.000");
        _elapsedTime = 0l;
        setButtonState(true,false,false);
    }
}
