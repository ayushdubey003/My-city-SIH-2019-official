package in.gov.sih.mycity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParseCity {
    InputStream inputStream;
    SharedPreferences locShared;
    SharedPreferences cityState;

    public ParseCity(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public List<String> getCity(Context context) {
        List<String> resultList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        locShared = context.getSharedPreferences("locPrefs", Context.MODE_PRIVATE);
        cityState = context.getSharedPreferences("statePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = locShared.edit();
        SharedPreferences.Editor edit1 = cityState.edit();
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");

                if (row[2].equals(":") || row[3].equals(":")) {
                    continue;
                }
                row[2] = row[2].substring(0, row[2].length() - 2);
                row[3] = row[3].substring(0, row[3].length() - 2);
                resultList.add(row[1]);
                String s = row[3].trim() + ":" + row[2].trim();
                edit.putString(row[1], s);
                edit1.putString(row[1], row[4]);
                edit.apply();
                edit1.apply();
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
        return resultList;
    }
}
