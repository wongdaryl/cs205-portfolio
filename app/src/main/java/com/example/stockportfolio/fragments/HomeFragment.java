package com.example.stockportfolio.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stockportfolio.MainActivity;
import com.example.stockportfolio.R;
import com.example.stockportfolio.receivers.DownloadBroadcastReceiver;
import com.example.stockportfolio.services.StockService;

public class HomeFragment extends Fragment {

    private Button start0, calc0, start1, calc1;
    private TextView result0, result1;
    private EditText ticker0, ticker1;
    private BroadcastReceiver myBroadcastReceiver;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myBroadcastReceiver = new DownloadBroadcastReceiver(new Handler(Looper.getMainLooper()));
        getActivity().registerReceiver(myBroadcastReceiver, new IntentFilter("DOWNLOAD_COMPLETE"));
        getActivity().registerReceiver(myBroadcastReceiver, new IntentFilter("DOWNLOAD_FAILED"));
        getActivity().registerReceiver(myBroadcastReceiver, new IntentFilter("CALCULATE"));


        start0 = (Button)getActivity().findViewById(R.id.start0);
        start0.setText(R.string.download);
        calc0 = (Button)getActivity().findViewById(R.id.calc0);
        calc0.setText(R.string.calculate);
        calc0.setBackgroundColor(getResources().getColor(R.color.neutral_500));
        result0 = (TextView)getActivity().findViewById(R.id.result0);
        result0.setText(R.string.vwap);
        ticker0 = (EditText)getActivity().findViewById(R.id.ticker0);
        ticker0.setHint(R.string.ticker);

        start0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), StockService.class);

                // Error handling: handle case if no input
                if(TextUtils.isEmpty(ticker0.getText())) {
                    Toast.makeText(getActivity().getApplicationContext(), "No input provided", Toast.LENGTH_SHORT).show();
                    return;
                }

                intent.putExtra("ticker", String.valueOf(ticker0.getText()));
                intent.putExtra("index", 0);
                start0.setClickable(false);
                start0.setBackgroundColor(getResources().getColor(R.color.neutral_500));
                start0.setText(R.string.downloading);
                result0.setText(R.string.vwap);
                getActivity().startService(intent);

            }
        });

        calc0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("CALCULATE");
                intent.putExtra("ticker", String.valueOf(ticker0.getText()));
                intent.putExtra("index", 0);
//                calc0.setBackgroundColor(R.color.neutral_500);
                calc0.setClickable(false);
                calc0.setText(R.string.calculating);
                calc0.setBackgroundColor(getActivity().getResources().getColor(R.color.neutral_500));
                result0.setTextColor(getActivity().getResources().getColor(R.color.neutral_500));
                getActivity().sendBroadcast(intent);
            }
        });
        calc0.setClickable(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(myBroadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}