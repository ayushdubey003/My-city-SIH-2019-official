package in.gov.sih.mycity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
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
                startActivity(new Intent(IntroductionActivity.this,DetailsActivity.class));
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

        ArrayAdapter<String> uuttii=new ArrayAdapter<>(IntroductionActivity.this,android.R.layout.simple_list_item_1,utilities);
        builder.setAdapter(uuttii, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which)
                {
                    case 0:  intent.setData(Uri.parse(mUrl+"Post+Office+in "+address));
                        break;
                    case 1:  intent.setData(Uri.parse(mUrl+"Police+Stations+in "+address));
                        break;
                    case 2:  intent.setData(Uri.parse(mUrl+"Petrol+Pumps+in "+address));
                        break;
                    case 3:  intent.setData(Uri.parse(mUrl+"ATMs+in "+address));
                        break;
                    case 4:  intent.setData(Uri.parse(mUrl+"General+Stores+in "+address));
                        break;
                    case 5:  intent.setData(Uri.parse(mUrl+"Bus+Stands+in "+address));
                        break;
                    case 6:  intent.setData(Uri.parse(mUrl+"Airports+in "+address));
                        break;
                    case 7:  intent.setData(Uri.parse(mUrl+"Ports+in "+address));
                        break;
                    case 8:  intent.setData(Uri.parse(mUrl+"Restaurants+in "+address));
                        break;



                }

                startActivity(intent);
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
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
        emer.add("Ambulance");
        emer.add("Gas Leakage");
        emer.add("Tourist-Helpline");
        emer.add("Child-Helpline");
        emer.add("Blood-Requirement");
        emer.add("Women-Helpline");
        emer.add("Ambulance Network (Emergency or Non-Emergency)");


        final ArrayList<String> num=new ArrayList<>();
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

        builder.setTitle("Emergency Numbers");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final Intent intent =new Intent(Intent.ACTION_DIAL);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(IntroductionActivity.this,android.R.layout.simple_list_item_1,emer);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which)
                {

                    case 0: intent.setData(Uri.parse("tel:"+num.get(0)));
                        break;
                    case 1: intent.setData(Uri.parse("tel:"+num.get(1)));
                        break;
                    case 2: intent.setData(Uri.parse("tel:"+num.get(2)));
                        break;
                    case 3: intent.setData(Uri.parse("tel:"+num.get(3)));
                        break;
                    case 4: intent.setData(Uri.parse("tel:"+num.get(4)));
                        break;
                    case 5: intent.setData(Uri.parse("tel:"+num.get(5)));
                        break;
                    case 6: intent.setData(Uri.parse("tel:"+num.get(6)));
                        break;
                    case 7: intent.setData(Uri.parse("tel:"+num.get(7)));
                        break;
                    case 8: intent.setData(Uri.parse("tel:"+num.get(8)));
                        break;
                    case 9: intent.setData(Uri.parse("tel:"+num.get(9)));
                        break;
                }

                startActivity(intent);

            }
        });

        AlertDialog dialog=builder.create();
        dialog.show();
    }
}