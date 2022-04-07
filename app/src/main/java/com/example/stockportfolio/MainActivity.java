package com.example.stockportfolio;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.stockportfolio.fragments.FavouriteFragment;
import com.example.stockportfolio.fragments.HomeFragment;
import com.example.stockportfolio.receivers.DownloadBroadcastReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

//    private Button start0, calc0, start1, calc1;
//    private TextView result0, result1;
//    private EditText ticker0, ticker1;

    private BroadcastReceiver myBroadcastReceiver;
    private BottomNavigationView bottomNavigationView;
//    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set up layout

        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Create listener for navigation buttons
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.favourite:
                        fragment = new FavouriteFragment();
                        break;
                    default:
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                return false;
            }
        });

        // By default, navigation bar starts at home page
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

        myBroadcastReceiver = new DownloadBroadcastReceiver(new Handler(Looper.getMainLooper()));

//        start0 = (Button) findViewById(R.id.start_button);
//        calc0 = (Button) findViewById(R.id.calc_button);
//        result0 = (TextView) findViewById(R.id.textview_result);
//        ticker0 = (EditText) findViewById(R.id.edit_ticker);
//
//        start1 = (Button) findViewById(R.id.start_button1);
//        calc1 = (Button) findViewById(R.id.calc_button1);
//        result1 = (TextView) findViewById(R.id.textview_result1);
//        ticker1 = (EditText) findViewById(R.id.edit_ticker1);
//
//        // start service, pass ticker info via an intent
//
//        start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), StockService.class);
//                intent.putExtra("ticker", String.valueOf(ticker.getText()));
//                intent.putExtra("index", 0);
//                startService(intent);
//            }
//        });
//
//        start1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), StockService.class);
//                intent.putExtra("ticker", String.valueOf(ticker1.getText()));
//                intent.putExtra("index", 1);
//                startService(intent);
//            }
//        });
//
//        // register broadcast receiver to get informed that data is downloaded so that we can calc
//
//        calc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                result.setText("Waiting for data.. ");
//                myBroadcastReceiver = new DownloadBroadcastReceiver(new Handler(Looper.getMainLooper()));
//                registerReceiver(myBroadcastReceiver, new IntentFilter("DOWNLOAD_COMPLETE"));
//            }
//        });
//
//        calc1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                result1.setText("Waiting for data.. ");
//                myBroadcastReceiver = new DownloadBroadcastReceiver(new Handler(Looper.getMainLooper()));
//                registerReceiver(myBroadcastReceiver, new IntentFilter("DOWNLOAD_COMPLETE"));
//            }
//        });
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