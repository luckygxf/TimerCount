package com.gaahead.gxf.counttime;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    //init view
    private Button btn_start;
    private Button btn_stop;
    private Button btn_reset;
    private TextView tv_hour;
    private TextView tv_min;
    private TextView tv_sec;


    //handle for update UI
    private Handler myHandler;
    private TimerTask timerTask;
    private Timer timer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init view control
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //init
    public void init(){
        //get button from xml
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_reset = (Button) findViewById(R.id.btn_reset);

        //init textview
        tv_hour = (TextView) findViewById(R.id.tv_hour);
        tv_min = (TextView) findViewById(R.id.tv_min);
        tv_sec = (TextView) findViewById(R.id.tv_sec);

        //set onclick listener
        View.OnClickListener btnListener = new ButtonListenerImpl();
        btn_start.setOnClickListener(btnListener);
        btn_stop.setOnClickListener(btnListener);
        btn_reset.setOnClickListener(btnListener);

        //init handle
        initMyHandle();

        //init timer and timerTask
        initTimerAndTimerTask();

    }

    //onclick listener
    class ButtonListenerImpl implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_start:
                    start();
                    break;
                case R.id.btn_stop:
                    stop();
                    break;
                case R.id.btn_reset:
                    reset();
                    break;
            }
        }

        //start count
        private void start(){
            initTimerAndTimerTask();
            timer.schedule(timerTask, 1000, 1000);
            Toast.makeText(MainActivity.this, "Start Count", Toast.LENGTH_LONG).show();
        }

        //stop count
        private void stop(){
            timer.cancel();
            Toast.makeText(MainActivity.this, "Stop Count", Toast.LENGTH_LONG).show();
        }

        //reset count
        private void reset(){
            timer.cancel();
            tv_hour.setText(String.format("%02d", 0));
            tv_min.setText(String.format("%02d", 0));
            tv_sec.setText(String.format("%02d", 0));
            Toast.makeText(MainActivity.this, "Reset Count", Toast.LENGTH_LONG).show();
        }
    }

    //init handle
    private void initMyHandle(){
        myHandler = new Handler(){
            public void handleMessage(Message message){
                switch(message.what){
                    case 1:
                        addCount();
                        break;
                }//switch
            }//handleMessage

        };
    }

    //init timer and timertask
    private void initTimerAndTimerTask(){
        timerTask = new TimerTask() {
            @Override
            public void run() {
                //new a Message
                Message message = new Message();
                message.what = 1;
                //send message to handle
                myHandler.sendMessage(message);
            }
        };//timerTask

        //init timer
        timer = new Timer(true);

    }

    //start count and update UI
    public void addCount(){
        int carry_min = 0;
        int carry_hour = 0;
        //get second has passed
        String seconds_string = tv_sec.getText().toString();
        int seconds_int = Integer.valueOf(seconds_string);

        String minutes_string = tv_min.getText().toString();
        int minutes_int = Integer.valueOf(minutes_string);

        String hour_string = tv_hour.getText().toString();
        int hours_int = Integer.valueOf(hour_string);

        //update seconds
        if(seconds_int == 59){
            seconds_int = 0;
            carry_min = 1;
        }
        else
            seconds_int++;
        //update minutes
        if(carry_min == 1){
            if(minutes_int == 59){
                carry_hour = 1;
                minutes_int = 0;
            }
            else
                minutes_int ++;
        }
        //update hours
        if(carry_hour == 1){
            if(hours_int == 23){
                hours_int = 0;
            }
            else
                hours_int++;
        }

        //update textView
        tv_hour.setText(String.format("%02d", hours_int));
        tv_min.setText(String.format("%02d", minutes_int));
        tv_sec.setText(String.format("%02d", seconds_int));
    }

}
