package in.gov.sih.mycity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import in.gov.sih.mycity.R;
import in.gov.sih.mycity.TrainModel;

public class TrainAdapter extends ArrayAdapter<TrainModel> {
    public TrainAdapter(@NonNull Context context, int resource, @NonNull List<TrainModel> objects) {
        super(context, resource, objects);
    }


    //public in.gov.sih.mycity.TrainAdapter(@NonNull Context context, int resource, ) {
    //super(context, resource);
    //}

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.train, null, false);
        }
        TextView train_number=(TextView)convertView.findViewById(R.id.number);
        TextView departure_time=(TextView)convertView.findViewById(R.id.departure);
        TextView arrival_time=(TextView)convertView.findViewById(R.id.arrival);
        TextView train_name=(TextView)convertView.findViewById(R.id.train_name);
        TrainModel train=getItem(position);
        train_number.setText(train.getmTrainNumber());
        train_name.setText(train.getmTrainName());
        arrival_time.setText(train.getmArrival());
        departure_time.setText(train.getmDeparture());
        return convertView;
    }
}