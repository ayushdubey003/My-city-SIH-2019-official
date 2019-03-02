package in.gov.sih.mycity;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class IntroductionActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AlertDialog.Builder builder;
    String address="";

    private static String mUrl="http://maps.google.com/maps?q=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        builder=new AlertDialog.Builder(this);

        //TODO: Correct Map Activity
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

        address=sharedPreferences.getString("address","No such Location")+" , "
                +stateCityprefs.getString(sharedPreferences.getString("address","No such Location"),
                "No Such Location");
        city_location.setText(address);

        //TODO: Add Intents on clicks
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(IntroductionActivity.this,"Info Clicked",Toast.LENGTH_LONG).show();
            }
        });

        utilities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(IntroductionActivity.this,"Utilities Clicked",Toast.LENGTH_LONG).show();
            }
        });

        redirectMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(IntroductionActivity.this,"Show on Map Clicked",Toast.LENGTH_LONG).show();
            }
        });

        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(IntroductionActivity.this,"Emergency Numbers Clicked",Toast.LENGTH_LONG).show();
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
}