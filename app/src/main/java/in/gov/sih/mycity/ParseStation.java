package in.gov.sih.mycity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ParseStation {
    static InputStream inputStream;
    private static Context mContext;
    public ParseStation(InputStream inputStream, Context context) {
        this.inputStream = inputStream;
        mContext=context;
    }

    public List<String> getNearestStations() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<String> stations=new ArrayList<>();
        int u=0;
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                u++;
                if(u==1)
                    continue;
                String[] row = csvLine.split(",");
                if(row[0]==null||row[0].length()==0)
                    continue;
                SharedPreferences sharedPreferences=mContext.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
                float latitude=sharedPreferences.getFloat("latitude",0.0f);
                float longitude=sharedPreferences.getFloat("longitude",0.0f);
                float longitude1=Float.parseFloat(row[1]);
                float latitude1=Float.parseFloat(row[2]);
                float dist=distance(latitude,longitude,latitude1,longitude1);
                if(dist<5.0f)
                    stations.add(row[6].toUpperCase());
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
        return stations;
    }
    private float distance(float lat1, float lon1, float lat2, float lon2) {
        float theta = lon1 - lon2;
        float dist = (float)(Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta)));
        dist = (float) Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515f;
        return (dist);
    }

    private float deg2rad(float deg) {
        return (deg * (float)(Math.PI / 180.0));
    }

    private float rad2deg(float rad) {
        return (float)(rad * ((float)180.0) / ((float) Math.PI));
    }

    public List<String> getStations() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<String> stations=new ArrayList<>();
        int u=0;
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                u++;
                if(u==1)
                    continue;
                String[] row = csvLine.split(",");
                if(row[0]==null||row[0].length()==0)
                    continue;
                stations.add(row[6].toUpperCase());
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
        return stations;
    }
}