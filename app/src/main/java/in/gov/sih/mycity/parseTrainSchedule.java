package in.gov.sih.mycity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static in.gov.sih.mycity.ParseStation.inputStream;

public class parseTrainSchedule {
    InputStream inputStream;
    Context mContext;
    private String mSource;
    private String mDestination;

    public parseTrainSchedule(InputStream inputStream, Context context, String source, String destination) {
        this.inputStream = inputStream;
        mContext = context;
        mSource = source;
        mDestination = destination;
    }

    public List<TrainModel> getSchedule() {
        int u = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<TrainModel> schedule = new ArrayList<>();
        HashMap<String, Integer> map = new HashMap<>();
        HashMap<String, String> map1 = new HashMap<>();
        int c = 0;
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                if (row[5].equalsIgnoreCase(mSource) || row[5].equalsIgnoreCase(mDestination)) {
                    if (row[1].equals(row[3]))
                        continue;
                    String train_number = row[7];
                    if (map.containsKey(train_number) == false) {
                        if (row[5].equalsIgnoreCase(mSource)) {
                            map.put(train_number, 1);
                            if(!row[3].equalsIgnoreCase("None"))
                                map1.put(train_number, row[3].substring(0,5)+" (Day "+row[2].substring(0,row[2].indexOf('.'))+")");
                            else
                                map1.put(train_number, row[1].substring(0,5)+" (Day "+row[2].substring(0,row[2].indexOf('.'))+")");
                        } else {
                            map.put(train_number, 2);
                        }
                    } else if (map.get(train_number) == 1) {
                        if (row[5].equalsIgnoreCase(mDestination)) {
                            map.put(train_number, 2);
                            row[7] = row[7].trim();
                            if (row[7].length() < 5) {
                                if (row[7].length() == 4)
                                    row[7] = "0" + row[7];
                                else
                                    row[7] = "00" + row[7];
                            }
                            TrainModel ob = new TrainModel(row[7],
                                    map1.get(train_number),
                                    row[1].substring(0,5)+" (Day "+row[2].substring(0,row[2].indexOf('.'))+")",
                                    row[6]);
                            schedule.add(ob);
                        }
                    }
                }
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
        return schedule;
    }
}
