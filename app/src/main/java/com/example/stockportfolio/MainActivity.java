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