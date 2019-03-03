package in.gov.sih.mycity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Locale;


public class IntroductionActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AlertDialog.Builder builder;
    private EditText nameEdit;
    private EditText contactEdit;
    private TextView addButton;
    private TextView cancelButton;
    String address="";
    SharedPreferences emerPrefs;
    SharedPreferences utilPrefs;
    private String cityName;
    private ImageView mSpeechButton;
    private final int REQ_CODE_SPEECH_INPUT = 2;

    private static String mUrl="http://maps.google.com/maps?q=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        builder=new AlertDialog.Builder(this);
        emerPrefs=getSharedPreferences("EmerPrefs",MODE_PRIVATE);
        utilPrefs=getSharedPreferences("UtilPrefs",MODE_PRIVATE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
        SharedPreferences stateCityprefs=getSharedPreferences("statePrefs",MODE_PRIVATE);

        //Initialization of variables
        TextView city_location=(TextView)findViewById(R.id.city_location);
        TextView utilities=(TextView)findViewById(R.id.utilities);
        TextView emergency=(TextView)findViewById(R.id.emergency_contact);
        TextView info=(TextView)findViewById(R.id.information);
        TextView redirectMap=(TextView)findViewById(R.id.location);
        TextView weather_act=(TextView) findViewById(R.id.weather);

        address=sharedPreferences.getString("address","No such Location")+" , "
                +stateCityprefs.getString(sharedPreferences.getString("address","No such Location"),
                "No Such Location");
        city_location.setText(address);
        cityName = sharedPreferences.getString("address","No such Location");

        weather_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroductionActivity.this,weather_activity.class));
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroductionActivity.this,DetailsActivity.class));
                //startActivity(new Intent(IntroductionActivity.this,SplashScreen.class));
            }
        });

        utilities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUtility();
            }
        });

        redirectMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=mUrl+address;
                Uri uri=Uri.parse(url);
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showList();
            }
        });

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
        LatLng location = new LatLng(sharedPreferences.getFloat("latitude",(float)0.0), sharedPreferences.getFloat("longitude",(float)0.0));
        String city=sharedPreferences.getString("address","Default");
        mMap.addMarker(new MarkerOptions().position(location).title(city));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(location, 8);
        mMap.animateCamera(yourLocation);
    }

    public void showUtility()
    {
        final String url ;
        final Intent intent=new Intent(Intent.ACTION_VIEW);
        //startActivity(intent);
        AlertDialog.Builder builder=new AlertDialog.Builder(IntroductionActivity.this);
        builder.setTitle("Select utility");
        final ArrayList<String> utilities=new ArrayList<>();
        utilities.add("Post Office");
        utilities.add("Police Stations");
        utilities.add("Petrol Pumps");
        utilities.add("ATMs");
        utilities.add("General Stores");
        utilities.add("Bus Stand");
        utilities.add("Airport");
        utilities.add("Port");
        utilities.add("Restaurants");
        utilities.add("CourtHouse");
        utilities.add("Yamaha Showroom");

        final int intCount = utilPrefs.getInt(address+"count",0);

        for (int i=0;i<intCount;i++){
            String utility = utilPrefs.getString(address+Integer.toString(i),"");
            utilities.add(utility);
        }

        final ArrayAdapter<String> uuttii=new ArrayAdapter<>(IntroductionActivity.this,android.R.layout.simple_list_item_1,utilities);
        builder.setAdapter(uuttii, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String utility = uuttii.getItem(which);
                utility = utility.replaceAll(" ","+");
                intent.setData(Uri.parse(mUrl+utility+"+in+"+address));
                startActivity(intent);
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Add Utilities", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addUtilities();
            }
        });


        Dialog dialog=builder.create();
        dialog.show();


    }

    public void showList()
    {

        final ArrayList<String> emer=new ArrayList<>();
        emer.add("Emergency");
        emer.add("Police");
        emer.add("Fire");
        emer.add("Ambulance");
        emer.add("Gas Leakage");
        emer.add("Tourist-Helpline");
        emer.add("Child-Helpline");
        emer.add("Blood-Requirement");
        emer.add("Women-Helpline");
        emer.add("Ambulance Network (Emergency or Non-Emergency)");


        final ArrayList<String> num=new ArrayList<>();
        if(cityName.equals("Noida")){
            num.add("112");
            num.add("01202473395");
            num.add("01204356491");
            num.add("09540929495");
            num.add("01206440555");
            num.add("1363");
            num.add("09140857492");
            num.add("07042840641");
            num.add("01126944805");
            num.add("09343180000");
        }
        else{
            num.add("112");
            num.add("100");
            num.add("102");
            num.add("108");
            num.add("1906");
            num.add("1363");
            num.add("1098");
            num.add("104");
            num.add("181");
            num.add("09343180000");
        }


        final int intCount = emerPrefs.getInt(address+"count",0);

        for (int i=0;i<intCount;i++){
            String temp = emerPrefs.getString(address+Integer.toString(i),"s:v");
            String[] tempArray = temp.split(":");
            String name = tempArray[0];
            String contact = tempArray[1];
            Log.e("error",contact);
            emer.add(name);
            num.add(contact);
        }

        builder.setTitle("Emergency Numbers");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Add Emergency Number", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addEmergency();
            }
        });
        final Intent intent =new Intent(Intent.ACTION_DIAL);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(IntroductionActivity.this,android.R.layout.simple_list_item_1,emer);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent.setData(Uri.parse("tel:"+num.get(which)));
                startActivity(intent);

            }
        });

        AlertDialog dialog=builder.create();
        dialog.show();
    }

    public void addEmergency()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(IntroductionActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View v = inflater.inflate(R.layout.emergency_dialog, null);
        builder.setView(v);
        final Dialog dialog=builder.create();
        dialog.setContentView(R.layout.emergency_dialog);
        dialog.show();

        nameEdit = (EditText) dialog.findViewById(R.id.name);
        contactEdit = (EditText) dialog.findViewById(R.id.contact);
        addButton = (TextView) dialog.findViewById(R.id.add);
        cancelButton = (TextView) dialog.findViewById(R.id.cancel);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEdit.getText().toString();
                String contact =contactEdit.getText().toString();
                SharedPreferences.Editor edit=emerPrefs.edit();
                edit.putInt(address+"count",emerPrefs.getInt(address+"count",0)+1);
                edit.putString(address+Integer.toString(emerPrefs.getInt(address+"count",0)),name+":"+contact);
                edit.apply();
                dialog.cancel();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


    }

    private void addUtilities() {
        AlertDialog.Builder builder=new AlertDialog.Builder(IntroductionActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View v = inflater.inflate(R.layout.utility_dialog, null);
        builder.setView(v);
        final Dialog dialog=builder.create();
        dialog.setContentView(R.layout.utility_dialog);
        dialog.show();

        nameEdit = (EditText) dialog.findViewById(R.id.utility_name);
        addButton = (TextView) dialog.findViewById(R.id.add);
        cancelButton = (TextView) dialog.findViewById(R.id.cancel);
        mSpeechButton = (ImageView) dialog.findViewById(R.id.voice_search);
        mSpeechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEdit.getText().toString();
                SharedPreferences.Editor edit=utilPrefs.edit();
                edit.putInt(address+"count",utilPrefs.getInt(address+"count",0)+1);
                edit.putString(address+Integer.toString(utilPrefs.getInt(address+"count",0)),name);
                edit.apply();
                dialog.cancel();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.enter_journal_message));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (Exception e) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    nameEdit.setText(result.get(0));
                }
                break;
        }
    }
}