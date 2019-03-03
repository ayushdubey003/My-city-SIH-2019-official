package in.gov.sih.mycity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BusFragment extends Fragment {

    private TextView sourceView;
    private Context context;
    private String city;
    private EditText edittext;
    private Calendar myCalendar;
    private Spinner spinner;
    private TextView show;

    public BusFragment() {
        // Required empty public constructor
    }

    public static BusFragment newInstance() {
        BusFragment fragment = new BusFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View returnView = inflater.inflate(R.layout.fragment_bus, container,
                false);
        sourceView = (TextView) returnView.findViewById(R.id.source);
        context = getContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        city = sharedPreferences.getString("address", " ");
        sourceView.setText(city);
        show = (TextView) returnView.findViewById(R.id.show);
        myCalendar = Calendar.getInstance();

        edittext = (EditText) returnView.findViewById(R.id.date);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        String[] cities = new String[]{"Aligarh", "Agra", "Jaipur", "Kanpur", "Lucknow"};

        spinner = (Spinner) returnView.findViewById(R.id.destination);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = edittext.getEditableText().toString();
                if (s.length() == 0 || s == null) {
                    Toast.makeText(getContext(),"Date cannot be empty",Toast.LENGTH_LONG).show();
                } else {
                    String url = getUrl();
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            }
        });

        return returnView;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        long time1 = myCalendar.getTime().getTime();
        Date date = new Date();
        long time2 = date.getTime();
        if (time1 < time2) {
            Toast.makeText(getContext(), "Enter Valid Date", Toast.LENGTH_LONG).show();
            return;
        }
        edittext.setText(sdf.format(myCalendar.getTime()));

    }

    private String getMonth(int i) {
        switch (i) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            default:
                return "Dec";

        }
    }

    private String getUrl() {
        String url = "";
        int choice = spinner.getSelectedItemPosition();
        int targetCityId = 0;
        switch (choice) {
            case 0:
                targetCityId = 79613;
                break;
            case 1:
                targetCityId = 1290;
                break;
            case 2:
                targetCityId = 807;
                break;
            case 3:
                targetCityId = 1377;
                break;
            default:
                targetCityId = 1439;
        }

        String date = edittext.getEditableText().toString();
        date = date.substring(0, 2) + "-" + getMonth((Integer.parseInt(date.substring(3, 5))))
                + "-" + "20" + date.substring(6);

        url = "https://www.redbus.in/search?fromCityName=Noida&fromCityId=1429&toCityName=" +
                spinner.getSelectedItem() + "&toCityId=" + targetCityId + "&onward=" + date +
                "&opId=0&busType=Any";

        return url;
    }
}
