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
                    calc.setBackgroundColor(((Activity)context).getResources().getColor(R.color.light_purple));
                    start.setClickable(true);
                    start.setBackgroundColor(((Activity)context).getResources().getColor(R.color.light_purple));
                    start.setText(((Activity)context).getResources().getString(R.string.download));
                }
            });
        }
        else if (intent.getAction().equals("CALCULATE")) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    int index = intent.getIntExtra("index", -1);
                    TextView annualRet, volatility;
                    Button calc;
                    if (index == 0) {
                        annualRet = (TextView) ((Activity)context).findViewById(R.id.ar0);
                        volatility = (TextView) ((Activity)context).findViewById(R.id.v0);
                        calc = (Button) ((Activity)context).findViewById(R.id.calc0);
                    } else if (index == 1) {
                        annualRet = (TextView) ((Activity)context).findViewById(R.id.ar1);
                        volatility = (TextView) ((Activity)context).findViewById(R.id.v1);
                        calc = (Button) ((Activity)context).findViewById(R.id.calc1);
                    } else if (index == 2) {
                        annualRet = (TextView) ((Activity)context).findViewById(R.id.ar2);
                        volatility = (TextView) ((Activity)context).findViewById(R.id.v2);
                        calc = (Button) ((Activity)context).findViewById(R.id.calc2);
                    } else if (index == 3) {
                        annualRet = (TextView) ((Activity)context).findViewById(R.id.ar3);
                        volatility = (TextView) ((Activity)context).findViewById(R.id.v3);
                        calc = (Button) ((Activity)context).findViewById(R.id.calc3);
                    } else if (index == 4) {
                        annualRet = (TextView) ((Activity)context).findViewById(R.id.ar4);
                        volatility = (TextView) ((Activity)context).findViewById(R.id.v4);
                        calc = (Button) ((Activity)context).findViewById(R.id.calc4);
                    }
                    else {
                        return;
                    }

                    Uri CONTENT_URI = Uri.parse("content://com.example.stockportfolio.providers.HistoricalDataProvider/history");
                    String ticker = intent.getStringExtra("ticker");
                    String selection = "ticker = '" + ticker + "'";
                    int count = 0;
                    double sum_ret = 0.0;
                    double sum_price = 0.0;
                    double sum_volume = 0.0;
                    Cursor cursor = context.getContentResolver().query(CONTENT_URI, null, selection, null, null);
                    if (cursor.moveToFirst()) {

                        double close = cursor.getDouble(cursor.getColumnIndexOrThrow("close"));
                        double open = cursor.getDouble(cursor.getColumnIndexOrThrow("open"));

                        double ret = (close - open) / open;
                        sum_ret += ret;

                        double volume = cursor.getDouble(cursor.getColumnIndexOrThrow("volume"));
                        sum_price += close * volume;
                        sum_volume += volume;
                        while (!cursor.isAfterLast()) {
                            int id = cursor.getColumnIndex("id");
                            close = cursor.getDouble(cursor.getColumnIndexOrThrow("close"));
                            open = cursor.getDouble(cursor.getColumnIndexOrThrow("open"));
                            ret = (close - open) / open;
                            sum_ret += ret;
                            volume = cursor.getDouble(cursor.getColumnIndexOrThrow("volume"));
                            sum_price += close * volume;
                            sum_volume += volume;
                            cursor.moveToNext();
                            Log.v("row", count + ": ret = " + ret);
                            count++;
                        }
                    }
                    else {
                        annualRet.setText(R.string.no_records_found);
                    }

                    double meanRet = sum_ret / count;
                    double annualizedReturn = 250 * meanRet * 100;

                    double vwap = sum_price / sum_volume;
                    annualRet.setText(String.format("%.2f", annualizedReturn) + "%");
                    annualRet.setTextColor(((Activity)context).getResources().getColor(R.color.light_gray));

                    double sum_diff = 0.0;
                    count = 0;
                    cursor = context.getContentResolver().query(CONTENT_URI, null, selection, null, null);
                    if (cursor.moveToFirst()) {

                        double close = cursor.getDouble(cursor.getColumnIndexOrThrow("close"));
                        double open = cursor.getDouble(cursor.getColumnIndexOrThrow("open"));

                        double ret = (close - open) / open;
                        sum_diff += Math.pow((ret - meanRet) , 2);
//                        sum_ret += ret;

                        double volume = cursor.getDouble(cursor.getColumnIndexOrThrow("volume"));
                        sum_price += close * volume;
                        sum_volume += volume;
                        while (!cursor.isAfterLast()) {
                            int id = cursor.getColumnIndex("id");
                            close = cursor.getDouble(cursor.getColumnIndexOrThrow("close"));
                            open = cursor.getDouble(cursor.getColumnIndexOrThrow("open"));
                            ret = (close - open) / open;
                            sum_diff += Math.pow((ret - meanRet) , 2);
//                            sum_ret += ret;
//                            volume = cursor.getDouble(cursor.getColumnIndexOrThrow("volume"));
//                            sum_price += close * volume;
//                            sum_volume += volume;
                            cursor.moveToNext();
                            Log.v("stddev", count + ": ret = " + ret);
                            count++;
                        }
                    }
                    else {
                        annualRet.setText(R.string.no_records_found);
                    }

                    double std_dev = Math.sqrt( sum_diff / count);
                    Log.v("std_dev", ""+std_dev);
                    double volatilityValue = Math.sqrt(250) * std_dev * 100;
                    volatility.setText(String.format("%.2f", volatilityValue) + "%");
                    volatility.setTextColor(((Activity)context).getResources().getColor(R.color.light_gray));

                    calc.setClickable(true);
                    calc.setText(R.string.calculate);
                    calc.setBackgroundColor(((Activity)context).getResources().getColor(R.color.light_purple));

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
                    start.setBackgroundColor(((Activity)context).getResources().getColor(R.color.light_purple));
                    start.setText(((Activity)context).getResources().getString(R.string.download));
                }
            });
        }
    }
}
