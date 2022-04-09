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
        switch (intent.getAction()) {
            case "DOWNLOAD_COMPLETE":
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = intent.getIntExtra("index", -1);
                        Button calc, start;
                        // get the relevant buttons using the index value
                        switch (index) {
                            case 0:
                                calc = (Button) ((Activity) context).findViewById(R.id.calc0);
                                start = (Button) ((Activity) context).findViewById(R.id.start0);
                                break;
                            case 1:
                                calc = (Button) ((Activity) context).findViewById(R.id.calc1);
                                start = (Button) ((Activity) context).findViewById(R.id.start1);
                                break;
                            case 2:
                                calc = (Button) ((Activity) context).findViewById(R.id.calc2);
                                start = (Button) ((Activity) context).findViewById(R.id.start2);
                                break;
                            case 3:
                                calc = (Button) ((Activity) context).findViewById(R.id.calc3);
                                start = (Button) ((Activity) context).findViewById(R.id.start3);
                                break;
                            case 4:
                                calc = (Button) ((Activity) context).findViewById(R.id.calc4);
                                start = (Button) ((Activity) context).findViewById(R.id.start4);
                                break;
                            default:
                                return;
                        }
                        // enable the start and calc buttons after data is downloaded
                        calc.setClickable(true);
                        calc.setBackgroundColor(((Activity) context).getResources().getColor(R.color.light_purple));
                        start.setClickable(true);
                        start.setBackgroundColor(((Activity) context).getResources().getColor(R.color.light_purple));
                        start.setText(((Activity) context).getResources().getString(R.string.download));
                    }
                });
                break;
            case "CALCULATE":
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = intent.getIntExtra("index", -1);
                        TextView annualRet, volatility;
                        Button calc;
                        // get the relevant TextViews and Buttons to be updated in calculation
                        switch (index) {
                            case 0:
                                annualRet = (TextView) ((Activity) context).findViewById(R.id.ar0);
                                volatility = (TextView) ((Activity) context).findViewById(R.id.v0);
                                calc = (Button) ((Activity) context).findViewById(R.id.calc0);
                                break;
                            case 1:
                                annualRet = (TextView) ((Activity) context).findViewById(R.id.ar1);
                                volatility = (TextView) ((Activity) context).findViewById(R.id.v1);
                                calc = (Button) ((Activity) context).findViewById(R.id.calc1);
                                break;
                            case 2:
                                annualRet = (TextView) ((Activity) context).findViewById(R.id.ar2);
                                volatility = (TextView) ((Activity) context).findViewById(R.id.v2);
                                calc = (Button) ((Activity) context).findViewById(R.id.calc2);
                                break;
                            case 3:
                                annualRet = (TextView) ((Activity) context).findViewById(R.id.ar3);
                                volatility = (TextView) ((Activity) context).findViewById(R.id.v3);
                                calc = (Button) ((Activity) context).findViewById(R.id.calc3);
                                break;
                            case 4:
                                annualRet = (TextView) ((Activity) context).findViewById(R.id.ar4);
                                volatility = (TextView) ((Activity) context).findViewById(R.id.v4);
                                calc = (Button) ((Activity) context).findViewById(R.id.calc4);
                                break;
                            default:
                                return;
                        }

                        // retrieve data of specified ticker value using HistoricalDataProvider
                        Uri CONTENT_URI = Uri.parse("content://com.example.stockportfolio.providers.HistoricalDataProvider/history");
                        String ticker = intent.getStringExtra("ticker");
                        String selection = "ticker = '" + ticker + "'";
                        int count = 0;
                        double sum_ret = 0.0;
                        Cursor cursor = context.getContentResolver().query(CONTENT_URI, null, selection, null, null);
                        if (cursor.moveToFirst()) {

                            // get the first previous day close price
                            double prevClose = cursor.getDouble(cursor.getColumnIndexOrThrow("close"));
                            cursor.moveToNext();

                            while (!cursor.isAfterLast()) {
                                // calculate return value by taking current day close - prev day close
                                double close = cursor.getDouble(cursor.getColumnIndexOrThrow("close"));
                                double ret = (close - prevClose) / prevClose;

                                sum_ret += ret;
                                cursor.moveToNext();
                                prevClose = close;
                                count++;
                            }
                        } else {
                            // if no values are found in db, update relevant TextViews
                            annualRet.setText(R.string.no_data);
                            annualRet.setTextColor(((Activity) context).getResources().getColor(R.color.light_gray));
                            volatility.setText(R.string.no_data);
                            volatility.setTextColor(((Activity) context).getResources().getColor(R.color.light_gray));
                            calc.setText(R.string.calculate);
                            return;
                        }

                        // calculate mean return for std dev calculation later on
                        double meanRet = sum_ret / count;

                        // calculate annualized return using formulae
                        double annualizedReturn = 250 * meanRet * 100;
                        Log.v("meanRet", ""+meanRet);

                        annualRet.setText(String.format("%.2f", annualizedReturn) + "%");
                        annualRet.setTextColor(((Activity) context).getResources().getColor(R.color.light_gray));

                        // calculate sum of squared difference between ret and mean return to be used in std dev calculation
                        double sum_squared_diff = 0.0;
                        count = 0;
                        cursor = context.getContentResolver().query(CONTENT_URI, null, selection, null, null);
                        if (cursor.moveToFirst()) {

                            // get the first previous day close price
                            double prevClose = cursor.getDouble(cursor.getColumnIndexOrThrow("close"));
                            cursor.moveToNext();

                            while (!cursor.isAfterLast()) {
                                // calculate return value by taking current day close - prev day close
                                double close = cursor.getDouble(cursor.getColumnIndexOrThrow("close"));
                                double ret = (close - prevClose) / prevClose;

                                // sum the squared difference between return and mean return
                                sum_squared_diff += Math.pow((ret - meanRet), 2);
                                cursor.moveToNext();
                                prevClose = close;
                                count++;
                            }
                        } else {
                            // if no values are found in db, update relevant TextViews
                            annualRet.setText(R.string.no_data);
                            annualRet.setTextColor(((Activity) context).getResources().getColor(R.color.light_gray));
                            volatility.setText(R.string.no_data);
                            volatility.setTextColor(((Activity) context).getResources().getColor(R.color.light_gray));
                            calc.setText(R.string.calculate);
                            return;
                        }

                        // calculate std dev using formula
                        double std_dev = Math.sqrt(sum_squared_diff / count);
                        Log.v("std_dev", "" + std_dev);

                        // calculate volatility using formula
                        double volatilityValue = Math.sqrt(250) * std_dev * 100;
                        volatility.setText(String.format("%.2f", volatilityValue) + "%");
                        volatility.setTextColor(((Activity) context).getResources().getColor(R.color.light_gray));

                        // update buttons
                        calc.setClickable(true);
                        calc.setText(R.string.calculate);
                        calc.setBackgroundColor(((Activity) context).getResources().getColor(R.color.light_purple));

                    }
                });
                break;
            case "DOWNLOAD_FAILED":
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = intent.getIntExtra("index", -1);
                        Button start, calc;

                        // get the relevant Buttons to be updated
                        switch (index) {
                            case 0:
                                start = (Button) ((Activity) context).findViewById(R.id.start0);
                                calc = (Button) ((Activity) context).findViewById(R.id.calc0);
                                break;
                            case 1:
                                start = (Button) ((Activity) context).findViewById(R.id.start1);
                                calc = (Button) ((Activity) context).findViewById(R.id.calc1);
                                break;
                            case 2:
                                start = (Button) ((Activity) context).findViewById(R.id.start2);
                                calc = (Button) ((Activity) context).findViewById(R.id.calc2);
                                break;
                            case 3:
                                start = (Button) ((Activity) context).findViewById(R.id.start3);
                                calc = (Button) ((Activity) context).findViewById(R.id.calc3);
                                break;
                            case 4:
                                start = (Button) ((Activity) context).findViewById(R.id.start4);
                                calc = (Button) ((Activity) context).findViewById(R.id.calc4);
                                break;
                            default:
                                return;
                        }

                        // disable start and calculate button
                        start.setClickable(true);
                        start.setBackgroundColor(((Activity) context).getResources().getColor(R.color.light_purple));
                        start.setText(((Activity) context).getResources().getString(R.string.download));
                        calc.setClickable(false);
                        calc.setText(R.string.calculate);
                        calc.setBackgroundColor(((Activity) context).getResources().getColor(R.color.neutral_500));
                    }
                });
                break;
        }
    }
}
