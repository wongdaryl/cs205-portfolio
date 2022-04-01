package com.example.stockportfolio;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stockportfolio.receivers.DownloadBroadcastReceiver;
import com.example.stockportfolio.services.StockService;

public class MainActivity extends AppCompatActivity {

    private Button start, calc, start1, calc1;
    private TextView result, result1;
    private EditText ticker, ticker1;

    //    Uri CONTENT_URI = Uri.parse("content://com.example.serviceexample.HistoricalDataProvider/history");
    private BroadcastReceiver myBroadcastReceiver;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set up layout

        setContentView(R.layout.activitymain);

        start = (Button) findViewById(R.id.start_button);
        calc = (Button) findViewById(R.id.calc_button);
        result = (TextView) findViewById(R.id.textview_result);
        ticker = (EditText) findViewById(R.id.edit_ticker);

        start1 = (Button) findViewById(R.id.start_button1);
        calc1 = (Button) findViewById(R.id.calc_button1);
        result1 = (TextView) findViewById(R.id.textview_result1);
        ticker1 = (EditText) findViewById(R.id.edit_ticker1);

        // start service, pass ticker info via an intent

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StockService.class);
                intent.putExtra("ticker", String.valueOf(ticker.getText()));
                intent.putExtra("index", 0);
                startService(intent);
            }
        });

        start1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StockService.class);
                intent.putExtra("ticker", String.valueOf(ticker1.getText()));
                intent.putExtra("index", 1);
                startService(intent);
            }
        });

        // register broadcast receiver to get informed that data is downloaded so that we can calc

        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText("Waiting for data.. ");
                myBroadcastReceiver = new DownloadBroadcastReceiver(new Handler(Looper.getMainLooper()));
                registerReceiver(myBroadcastReceiver, new IntentFilter("DOWNLOAD_COMPLETE"));
            }
        });

        calc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result1.setText("Waiting for data.. ");
                myBroadcastReceiver = new DownloadBroadcastReceiver(new Handler(Looper.getMainLooper()));
                registerReceiver(myBroadcastReceiver, new IntentFilter("DOWNLOAD_COMPLETE"));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(myBroadcastReceiver);
    }


}