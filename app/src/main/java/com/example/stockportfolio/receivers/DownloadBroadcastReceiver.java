package com.example.stockportfolio.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.stockportfolio.R;

public class DownloadBroadcastReceiver extends BroadcastReceiver {

    private final Handler handler;

    public DownloadBroadcastReceiver(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("DOWNLOAD_COMPLETE")) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    int index = intent.getIntExtra("index", -1);
                    Button calc;
                    Button start;
                    if (index == 0) {
                        calc = (Button) ((Activity)context).findViewById(R.id.calc0);
                        start = (Button) ((Activity)context).findViewById(R.id.start0);
                    } else if (index == 1) {
                        calc = (Button) ((Activity)context).findViewById(R.id.calc1);
                        start = (Button) ((Activity)context).findViewById(R.id.start1);
                    } else if (index == 2) {
                        calc = (Button) ((Activity)context).findViewById(R.id.calc2);
                        start = (Button) ((Activity)context).findViewById(R.id.start2);
                    } else if (index == 3) {
                        calc = (Button) ((Activity)context).findViewById(R.id.calc3);
                        start = (Button) ((Activity)context).findViewById(R.id.start3);
                    } else if (index == 4) {
                        calc = (Button) ((Activity)context).findViewById(R.id.calc4);
                        start = (Button) ((Activity)context).findViewById(R.id.start4);
                    } else {
                        return;
                    }
                    calc.setClickable(true);
                    calc.setBackgroundColor(((Activity)context).getResources().getColor(R.color.purple_500));
                    start.setClickable(true);
                    start.setBackgroundColor(((Activity)context).getResources().getColor(R.color.purple_500));
                    start.setText(((Activity)context).getResources().getString(R.string.download));
                }
            });
        }
        else if (intent.getAction().equals("CALCULATE")) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    int index = intent.getIntExtra("index", -1);
                    TextView result, volatility;
                    Button calc;
                    if (index == 0) {
                        result = (TextView) ((Activity)context).findViewById(R.id.ar0);
                        volatility = (TextView) ((Activity)context).findViewById(R.id.v0);
                        calc = (Button) ((Activity)context).findViewById(R.id.calc0);
                    } else if (index == 1) {
                        result = (TextView) ((Activity)context).findViewById(R.id.result1);
                        calc = (Button) ((Activity)context).findViewById(R.id.calc1);
                    } else if (index == 2) {
                        result = (TextView) ((Activity)context).findViewById(R.id.result2);
                        calc = (Button) ((Activity)context).findViewById(R.id.calc2);
                    } else if (index == 3) {
                        result = (TextView) ((Activity)context).findViewById(R.id.result3);
                        calc = (Button) ((Activity)context).findViewById(R.id.calc3);
                    } else if (index == 4) {
                        result = (TextView) ((Activity)context).findViewById(R.id.result4);
                        calc = (Button) ((Activity)context).findViewById(R.id.calc4);
                    }
                    else {
                        return;
                    }

                    Uri CONTENT_URI = Uri.parse("content://com.example.stockportfolio.providers.HistoricalDataProvider/history");
                    String ticker = intent.getStringExtra("ticker");
                    String selection = "ticker = '" + ticker + "'";
                    double sum_price = 0.0;
                    double sum_volume = 0.0;
                    Cursor cursor = context.getContentResolver().query(CONTENT_URI, null, selection, null, null);
                    if (cursor.moveToFirst()) {
                        double close = cursor.getDouble(cursor.getColumnIndexOrThrow("close"));
                        double volume = cursor.getDouble(cursor.getColumnIndexOrThrow("volume"));
                        sum_price += close * volume;
                        sum_volume += volume;
                        while (!cursor.isAfterLast()) {
                            int id = cursor.getColumnIndex("id");
                            close = cursor.getDouble(cursor.getColumnIndexOrThrow("close"));
                            volume = cursor.getDouble(cursor.getColumnIndexOrThrow("volume"));
                            sum_price += close * volume;
                            sum_volume += volume;
                            cursor.moveToNext();
                            Log.v("data", close + "");
                        }
                    }
                    else {
                        result.setText(R.string.no_records_found);
                    }

                    double vwap = sum_price / sum_volume;
                    result.setText(String.format("%.2f", vwap));
                    result.setTextColor(((Activity)context).getResources().getColor(R.color.white));
                    calc.setClickable(true);
                    calc.setText(R.string.calculate);
                    calc.setBackgroundColor(((Activity)context).getResources().getColor(R.color.purple_500));

                }
            });
        }
        else if (intent.getAction().equals("DOWNLOAD_FAILED")) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    int index = intent.getIntExtra("index", -1);
                    Button start;
                    if (index == 0) {
                        start = (Button) ((Activity)context).findViewById(R.id.start0);
                    } else if (index == 1) {
                        start = (Button) ((Activity)context).findViewById(R.id.start1);
                    } else if (index == 2) {
                        start = (Button) ((Activity)context).findViewById(R.id.start2);
                    } else if (index == 3) {
                        start = (Button) ((Activity)context).findViewById(R.id.start3);
                    } else if (index == 4) {
                        start = (Button) ((Activity)context).findViewById(R.id.start4);
                    }
                    else {
                        return;
                    }
                    start.setClickable(true);
                    start.setBackgroundColor(((Activity)context).getResources().getColor(R.color.purple_500));
                    start.setText(((Activity)context).getResources().getString(R.string.download));
                }
            });
        }
    }
}
