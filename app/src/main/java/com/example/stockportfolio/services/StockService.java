package com.example.stockportfolio.services;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.example.stockportfolio.providers.HistoricalDataProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class StockService extends Service {
    private static final String REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 15000;
    private final String token = "c9577i2ad3icae7g5p6g"; // put your own token
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;
    private String ticker = "MSFT";
    private int index = -1;

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("Service", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ticker = intent.getStringExtra("ticker");
        index = intent.getIntExtra("index", -1);
        Toast.makeText(this, "download starting for " + ticker, Toast.LENGTH_SHORT).show();
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.arg2 = index;
        serviceHandler.sendMessage(msg);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "download done", Toast.LENGTH_SHORT).show();
    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // url to get historical data

            String stringUrl = "https://finnhub.io/api/v1/stock/candle?symbol=" + ticker
                    + "&resolution=1&from=1631022248&to=1631627048&token=" + token;
            String result;
            String inputLine;

            try {

                // make GET requests

                URL myUrl = new URL(stringUrl);
                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();

                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                connection.connect();

                // store json string from GET response

                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();

                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }

                reader.close();
                streamReader.close();

                result = stringBuilder.toString();

            } catch (IOException e) {
                e.printStackTrace();
                result = null;
                Thread.currentThread().interrupt();
            }

            // parse the json string into 'close' and 'volume' array

            JSONObject jsonObject = null;
            JSONArray jsonArrayClose = null;
            JSONArray jsonArrayVolume = null;

            try {
                jsonObject = new JSONObject(result);
                jsonArrayClose = jsonObject.getJSONArray("c");
                jsonArrayVolume = jsonObject.getJSONArray("v");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            Log.v("close", String.valueOf(jsonArrayClose.length()));
            Log.v("vol", String.valueOf(jsonArrayVolume.length()));

            try {
                for (int i = 0; i < jsonArrayClose.length(); i++) {
                    double close = jsonArrayClose.getDouble(i);
                    double volume = jsonArrayVolume.getDouble(i);
                    Log.v("data", i + ":, c: " + close + " v: " + volume);

                    ContentValues values = new ContentValues();
                    values.put(HistoricalDataProvider.getClose(), close);
                    values.put(HistoricalDataProvider.getVolume(), volume);
                    getContentResolver().insert(HistoricalDataProvider.getContentUri(), values);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // broadcast message that download is complete

            Intent intent = new Intent("DOWNLOAD_COMPLETE");
            intent.putExtra("index", msg.arg2);
            sendBroadcast(intent);

            stopSelf(msg.arg1);

        }
    }
}
