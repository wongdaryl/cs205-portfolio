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
                        Button calc;
                        Button start;
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

                        Uri CONTENT_URI = Uri.parse("content://com.example.stockportfolio.providers.HistoricalDataProvider/history");
                        String ticker = intent.getStringExtra("ticker");
                        String selection = "ticker = '" + ticker + "'";
                        int count = 0;
                        double sum_ret = 0.0;
                        Cursor cursor = context.getContentResolver().query(CONTENT_URI, null, selection, null, null);
                        if (cursor.moveToFirst()) {

                            double prevClose = cursor.getDouble(cursor.getColumnIndexOrThrow("close"));
                            cursor.moveToNext();
                            count++;

                            while (!cursor.isAfterLast()) {
                                double close = cursor.getDouble(cursor.getColumnIndexOrThrow("close"));
                                double ret = (close - prevClose) / prevClose;

                                sum_ret += ret;
                                cursor.moveToNext();
                                prevClose = close;
                                count++;
                            }
                        } else {
                            annualRet.setText(R.string.no_records_found);
                        }

                        double meanRet = sum_ret / count;
                        double annualizedReturn = 250 * meanRet * 100;
                        Log.v("meanRet", ""+meanRet);

                        annualRet.setText(String.format("%.2f", annualizedReturn) + "%");
                        annualRet.setTextColor(((Activity) context).getResources().getColor(R.color.light_gray));

                        double sum_diff = 0.0;
                        count = 0;
                        cursor = context.getContentResolver().query(CONTENT_URI, null, selection, null, null);
                        if (cursor.moveToFirst()) {

                            double prevClose = cursor.getDouble(cursor.getColumnIndexOrThrow("close"));
                            count++;
                            cursor.moveToNext();

                            while (!cursor.isAfterLast()) {
                                double close = cursor.getDouble(cursor.getColumnIndexOrThrow("close"));
                                double ret = (close - prevClose) / prevClose;
                                sum_diff += Math.pow((ret - meanRet), 2);
                                cursor.moveToNext();
                                prevClose = close;
                                count++;
                            }
                        } else {
                            annualRet.setText(R.string.no_records_found);
                        }

                        double std_dev = Math.sqrt(sum_diff / count);
                        Log.v("std_dev", "" + std_dev);
                        double volatilityValue = Math.sqrt(250) * std_dev * 100;
                        volatility.setText(String.format("%.2f", volatilityValue) + "%");
                        volatility.setTextColor(((Activity) context).getResources().getColor(R.color.light_gray));

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
