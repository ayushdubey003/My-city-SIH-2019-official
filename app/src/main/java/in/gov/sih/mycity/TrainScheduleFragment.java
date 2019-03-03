package in.gov.sih.mycity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class TrainScheduleFragment extends Fragment {
    private String mSource;
    ProgressBar mProgressBar;
    List<String> possibleDestinations;
    private ArrayAdapter arrayAdapter;
    private ArrayList trains;
    private String destination;
    ListView mList;
    TrainAdapter trainAdapter;
    TextView emptyView;
    public TrainScheduleFragment() {
        // Required empty public constructor
    }

    public static TrainScheduleFragment newInstance() {
        TrainScheduleFragment fragment = new TrainScheduleFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_train_schedule, container, false);
        emptyView=view.findViewById(android.R.id.empty);
        emptyView.setVisibility(View.GONE);
        mProgressBar = view.findViewById(R.id.progressBarTrain);
        mProgressBar.setVisibility(View.INVISIBLE);
        InputStream inputStream = getResources().openRawResource(R.raw.stations);
        ParseStation parseStation = new ParseStation(inputStream, getContext());
        List<String> stations = parseStation.getNearestStations();
        stations.sort(null);
        Spinner spinner = (Spinner) view.findViewById(R.id.source);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                stations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        mSource = spinner.getItemAtPosition(0).toString();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSource = parent.getItemAtPosition(position).toString();
                Log.e("this", mSource);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        InputStream inputStream1 = getResources().openRawResource(R.raw.stations);
        ParseStation parseStationAgain = new ParseStation(inputStream1, getContext());
        final AutoCompleteTextView destiny = (AutoCompleteTextView) view.findViewById(R.id.destination);
        possibleDestinations = parseStation.getStations();
        final ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                possibleDestinations);
        destiny.setThreshold(1);
        destiny.setAdapter(mArrayAdapter);
        TextView textView = (TextView) view.findViewById(R.id.show);
        mList = view.findViewById(R.id.train_list);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destination = destiny.getText().toString();
                emptyView.setVisibility(View.GONE);
                trainAdapter.clear();
                if (destination.length() == 0 || destination == null)
                    Toast.makeText(getContext(), "Destination cannot be empty", Toast.LENGTH_LONG).show();
                else if (mSource.equals(destination))
                    Toast.makeText(getContext(), "Source and destination cannot be same", Toast.LENGTH_LONG).show();
                else if (!possibleDestinations.contains(destination))
                    Toast.makeText(getContext(), "No such destination station", Toast.LENGTH_LONG).show();
                else {
                    ProgressBar progressBar = view.findViewById(R.id.progressBarTrain);
                    progressBar.setVisibility(View.VISIBLE);
                    TrainTask task = new TrainTask(progressBar);
                    task.execute();
                }
            }
        });
        trainAdapter = new TrainAdapter(getContext(), 0, new ArrayList<TrainModel>());
        mList.setAdapter(trainAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TrainModel curr_train=trainAdapter.getItem(i);
                String train_day=curr_train.getmArrival();
                train_day=train_day.replace("(",":");
                String[] parts=train_day.split(":");
                Log.e("VIVZ", ""+parts.length );
                Intent intent=new Intent(getActivity(),railway_web_activity.class);
                intent.putExtra("train_no",curr_train.getmTrainNumber());
                intent.putExtra("train_day",parts[1].substring(0,parts[1].length()-1));
                startActivity(intent);
            }
        });
        return view;
    }

    public class TrainTask extends AsyncTask {
        private ProgressBar mProgressBar;

        public TrainTask(ProgressBar progressBar) {
            mProgressBar = progressBar;
        }

        @Override
        protected void onPostExecute(Object o) {
            mProgressBar.setVisibility(View.GONE);

            trains = (ArrayList<TrainModel>) o;
            if(trains.size()==0){
                emptyView.setVisibility(View.VISIBLE);
            }
            trainAdapter.clear();
            trainAdapter.addAll(trains);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            ArrayList<TrainModel> trainModels = new ArrayList<>();
            InputStream inputStream = getResources().openRawResource(R.raw.schedule);
            parseTrainSchedule parseTrainSchedule = new parseTrainSchedule(inputStream, getContext(), mSource, destination);
            List<TrainModel> schedule = parseTrainSchedule.getSchedule();
            return schedule;
        }
    }
}

