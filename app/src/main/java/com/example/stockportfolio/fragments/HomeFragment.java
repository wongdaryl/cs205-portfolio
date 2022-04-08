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
import com.example.stockportfolio.providers.HistoricalDataProvider;

public class HomeFragment extends Fragment {

    private Button start0, calc0, start1, calc1, start2, calc2, start3, calc3, start4, calc4;
    private TextView ar0, v0, result1, result2, result3, result4;
    private EditText ticker0, ticker1, ticker2, ticker3, ticker4;
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
        ar0 = (TextView)getActivity().findViewById(R.id.ar0);
        ar0.setText(R.string.AR);
        v0 = (TextView)getActivity().findViewById(R.id.v0);
        v0.setText(R.string.vol);
        ticker0 = (EditText)getActivity().findViewById(R.id.ticker0);
        ticker0.setHint(R.string.ticker);

        start1 = (Button)getActivity().findViewById(R.id.start1);
        start1.setText(R.string.download);
        calc1 = (Button)getActivity().findViewById(R.id.calc1);
        calc1.setText(R.string.calculate);
        calc1.setBackgroundColor(getResources().getColor(R.color.neutral_500));
        result1 = (TextView)getActivity().findViewById(R.id.result1);
        result1.setText(R.string.vwap);
        ticker1 = (EditText)getActivity().findViewById(R.id.ticker1);
        ticker1.setHint(R.string.ticker);

        start2 = (Button)getActivity().findViewById(R.id.start2);
        start2.setText(R.string.download);
        calc2 = (Button)getActivity().findViewById(R.id.calc2);
        calc2.setText(R.string.calculate);
        calc2.setBackgroundColor(getResources().getColor(R.color.neutral_500));
        result2 = (TextView)getActivity().findViewById(R.id.result2);
        result2.setText(R.string.vwap);
        ticker2 = (EditText)getActivity().findViewById(R.id.ticker2);
        ticker2.setHint(R.string.ticker);

        start3 = (Button)getActivity().findViewById(R.id.start3);
        start3.setText(R.string.download);
        calc3 = (Button)getActivity().findViewById(R.id.calc3);
        calc3.setText(R.string.calculate);
        calc3.setBackgroundColor(getResources().getColor(R.color.neutral_500));
        result3 = (TextView)getActivity().findViewById(R.id.result3);
        result3.setText(R.string.vwap);
        ticker3 = (EditText)getActivity().findViewById(R.id.ticker3);
        ticker3.setHint(R.string.ticker);

        start4 = (Button)getActivity().findViewById(R.id.start4);
        start4.setText(R.string.download);
        calc4 = (Button)getActivity().findViewById(R.id.calc4);
        calc4.setText(R.string.calculate);
        calc4.setBackgroundColor(getResources().getColor(R.color.neutral_500));
        result4 = (TextView)getActivity().findViewById(R.id.result4);
        result4.setText(R.string.vwap);
        ticker4 = (EditText)getActivity().findViewById(R.id.ticker4);
        ticker4.setHint(R.string.ticker);

        start0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), StockService.class);

                // Error handling: handle case if no input
                if(TextUtils.isEmpty(ticker0.getText())) {
                    Toast.makeText(getActivity().getApplicationContext(), "No input provided", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Handle case if ticker already downloaded
                if (HistoricalDataProvider.getRecords().containsKey(ticker0.getText().toString())) {
                    if (HistoricalDataProvider.getRecords().get(ticker0.getText().toString()) == 1) {
                        Toast.makeText(getActivity().getApplicationContext(), "Already downloaded data on " + ticker0.getText(), Toast.LENGTH_SHORT).show();
                        calc0.setClickable(true);
                        calc0.setBackgroundColor(getResources().getColor(R.color.purple_500));
                    } else if (HistoricalDataProvider.getRecords().get(ticker0.getText().toString()) == 0) {
                        Toast.makeText(getActivity().getApplicationContext(), "No data on " + ticker0.getText(), Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

                intent.putExtra("ticker", String.valueOf(ticker0.getText()));
                intent.putExtra("index", 0);
                start0.setClickable(false);
                start0.setBackgroundColor(getResources().getColor(R.color.neutral_500));
                start0.setText(R.string.downloading);
                ar0.setText(R.string.AR);
                v0.setText(R.string.vol);
                getActivity().startService(intent);
            }
        });

        start1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), StockService.class);

                // Error handling: handle case if no input
                if(TextUtils.isEmpty(ticker1.getText())) {
                    Toast.makeText(getActivity().getApplicationContext(), "No input provided", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Handle case if ticker already downloaded
                if (HistoricalDataProvider.getRecords().containsKey(ticker1.getText().toString())) {
                    if (HistoricalDataProvider.getRecords().get(ticker1.getText().toString()) == 1) {
                        Toast.makeText(getActivity().getApplicationContext(), "Already downloaded data on " + ticker1.getText(), Toast.LENGTH_SHORT).show();
                        calc1.setClickable(true);
                        calc1.setBackgroundColor(getResources().getColor(R.color.purple_500));
                    } else if (HistoricalDataProvider.getRecords().get(ticker1.getText().toString()) == 0) {
                        Toast.makeText(getActivity().getApplicationContext(), "No data on " + ticker1.getText(), Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

                intent.putExtra("ticker", String.valueOf(ticker1.getText()));
                intent.putExtra("index", 1);
                start1.setClickable(false);
                start1.setBackgroundColor(getResources().getColor(R.color.neutral_500));
                start1.setText(R.string.downloading);
                result1.setText(R.string.vwap);
                getActivity().startService(intent);
            }
        });

        start2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), StockService.class);

                // Error handling: handle case if no input
                if(TextUtils.isEmpty(ticker2.getText())) {
                    Toast.makeText(getActivity().getApplicationContext(), "No input provided", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Handle case if ticker already downloaded
                if (HistoricalDataProvider.getRecords().containsKey(ticker2.getText().toString())) {
                    if (HistoricalDataProvider.getRecords().get(ticker2.getText().toString()) == 1) {
                        Toast.makeText(getActivity().getApplicationContext(), "Already downloaded data on " + ticker2.getText(), Toast.LENGTH_SHORT).show();
                        calc2.setClickable(true);
                        calc2.setBackgroundColor(getResources().getColor(R.color.purple_500));
                    } else if (HistoricalDataProvider.getRecords().get(ticker2.getText().toString()) == 0) {
                        Toast.makeText(getActivity().getApplicationContext(), "No data on " + ticker2.getText(), Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

                intent.putExtra("ticker", String.valueOf(ticker2.getText()));
                intent.putExtra("index", 2);
                start2.setClickable(false);
                start2.setBackgroundColor(getResources().getColor(R.color.neutral_500));
                start2.setText(R.string.downloading);
                result2.setText(R.string.vwap);
                getActivity().startService(intent);
            }
        });

        start3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), StockService.class);

                // Error handling: handle case if no input
                if(TextUtils.isEmpty(ticker3.getText())) {
                    Toast.makeText(getActivity().getApplicationContext(), "No input provided", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Handle case if ticker already downloaded
                if (HistoricalDataProvider.getRecords().containsKey(ticker3.getText().toString())) {
                    if (HistoricalDataProvider.getRecords().get(ticker3.getText().toString()) == 1) {
                        Toast.makeText(getActivity().getApplicationContext(), "Already downloaded data on " + ticker3.getText(), Toast.LENGTH_SHORT).show();
                        calc3.setClickable(true);
                        calc3.setBackgroundColor(getResources().getColor(R.color.purple_500));
                    } else if (HistoricalDataProvider.getRecords().get(ticker3.getText().toString()) == 0) {
                        Toast.makeText(getActivity().getApplicationContext(), "No data on " + ticker3.getText(), Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

                intent.putExtra("ticker", String.valueOf(ticker3.getText()));
                intent.putExtra("index", 3);
                start3.setClickable(false);
                start3.setBackgroundColor(getResources().getColor(R.color.neutral_500));
                start3.setText(R.string.downloading);
                result3.setText(R.string.vwap);
                getActivity().startService(intent);
            }
        });

        start4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), StockService.class);

                // Error handling: handle case if no input
                if(TextUtils.isEmpty(ticker4.getText())) {
                    Toast.makeText(getActivity().getApplicationContext(), "No input provided", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Handle case if ticker already downloaded
                if (HistoricalDataProvider.getRecords().containsKey(ticker4.getText().toString())) {
                    if (HistoricalDataProvider.getRecords().get(ticker4.getText().toString()) == 1) {
                        Toast.makeText(getActivity().getApplicationContext(), "Already downloaded data on " + ticker4.getText(), Toast.LENGTH_SHORT).show();
                        calc4.setClickable(true);
                        calc4.setBackgroundColor(getResources().getColor(R.color.purple_500));
                    } else if (HistoricalDataProvider.getRecords().get(ticker4.getText().toString()) == 0) {
                        Toast.makeText(getActivity().getApplicationContext(), "No data on " + ticker4.getText(), Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

                intent.putExtra("ticker", String.valueOf(ticker4.getText()));
                intent.putExtra("index", 4);
                start4.setClickable(false);
                start4.setBackgroundColor(getResources().getColor(R.color.neutral_500));
                start4.setText(R.string.downloading);
                result4.setText(R.string.vwap);
                getActivity().startService(intent);
            }
        });

        calc0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("CALCULATE");
                intent.putExtra("ticker", String.valueOf(ticker0.getText()));
                intent.putExtra("index", 0);
                calc0.setClickable(false);
                calc0.setText(R.string.calculating);
                calc0.setBackgroundColor(getActivity().getResources().getColor(R.color.neutral_500));
                ar0.setTextColor(getActivity().getResources().getColor(R.color.neutral_500));
                v0.setTextColor(getActivity().getResources().getColor(R.color.neutral_500));
                getActivity().sendBroadcast(intent);
            }
        });
        calc0.setClickable(false);

        calc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("CALCULATE");
                intent.putExtra("ticker", String.valueOf(ticker1.getText()));
                intent.putExtra("index", 1);
                calc1.setClickable(false);
                calc1.setText(R.string.calculating);
                calc1.setBackgroundColor(getActivity().getResources().getColor(R.color.neutral_500));
                result1.setTextColor(getActivity().getResources().getColor(R.color.neutral_500));
                getActivity().sendBroadcast(intent);
            }
        });
        calc1.setClickable(false);

        calc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("CALCULATE");
                intent.putExtra("ticker", String.valueOf(ticker2.getText()));
                intent.putExtra("index", 2);
                calc2.setClickable(false);
                calc2.setText(R.string.calculating);
                calc2.setBackgroundColor(getActivity().getResources().getColor(R.color.neutral_500));
                result2.setTextColor(getActivity().getResources().getColor(R.color.neutral_500));
                getActivity().sendBroadcast(intent);
            }
        });
        calc2.setClickable(false);

        calc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("CALCULATE");
                intent.putExtra("ticker", String.valueOf(ticker3.getText()));
                intent.putExtra("index", 3);
                calc3.setClickable(false);
                calc3.setText(R.string.calculating);
                calc3.setBackgroundColor(getActivity().getResources().getColor(R.color.neutral_500));
                result3.setTextColor(getActivity().getResources().getColor(R.color.neutral_500));
                getActivity().sendBroadcast(intent);
            }
        });
        calc3.setClickable(false);

        calc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("CALCULATE");
                intent.putExtra("ticker", String.valueOf(ticker4.getText()));
                intent.putExtra("index", 4);
                calc4.setClickable(false);
                calc4.setText(R.string.calculating);
                calc4.setBackgroundColor(getActivity().getResources().getColor(R.color.neutral_500));
                result4.setTextColor(getActivity().getResources().getColor(R.color.neutral_500));
                getActivity().sendBroadcast(intent);
            }
        });
        calc4.setClickable(false);
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