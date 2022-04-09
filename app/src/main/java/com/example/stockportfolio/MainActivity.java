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

import com.example.stockportfolio.fragments.IntroFragment;
import com.example.stockportfolio.fragments.HomeFragment;
import com.example.stockportfolio.receivers.DownloadBroadcastReceiver;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set up layout

        setContentView(R.layout.activity_main);

        // By default, navigation bar starts at intro page
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new IntroFragment()).commit();

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