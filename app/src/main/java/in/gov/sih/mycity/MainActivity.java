package in.gov.sih.mycity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.akhgupta.easylocation.EasyLocationAppCompatActivity;
import com.akhgupta.easylocation.EasyLocationRequest;
import com.akhgupta.easylocation.EasyLocationRequestBuilder;
import com.google.android.gms.location.LocationRequest;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends EasyLocationAppCompatActivity {
    public static final int RC_SIGN_IN = 1;
    public static final String ANONYMOUS = "ANONYMOUS";
    private final int REQ_CODE_SPEECH_INPUT = 2;

    private LinearLayout popupButton;
    private LinearLayout root;
    private LinearLayout popup;
    private TextView logout;

    private LinearLayout home, profile;


    private double lati, longi;
    private TextView user;
    private String address, district = "";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    static AutoCompleteTextView mAutoCompleteTextView;
    SharedPreferences mSharedPreferences;
    ArrayAdapter<String> mArrayAdapter;
    RecyclerView common_cities;
    Button go;
    GridLayoutManager gridLayoutManager;

    private FirebaseAuth mAuth;
    private String mUsername;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ImageView mSpeechButton;
    private LinearLayout layer;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startActivity(new Intent(this, DetailsActivity.class));finish();
        user = findViewById(R.id.user);
        user.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        final DatabaseReference dref=FirebaseDatabase.getInstance().getReference("karma");
         dref.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if(!dataSnapshot.hasChild(FirebaseAuth.getInstance().getUid()))
                 {
                     dref.getRef().child(FirebaseAuth.getInstance().getUid()).setValue(0);
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });

        InputStream inputStream = getResources().openRawResource(R.raw.cities);
        ParseCity city = new ParseCity(inputStream);
        List<String> cities = city.getCity(MainActivity.this);
        mAutoCompleteTextView = findViewById(R.id.auto);
        Log.e("check", Integer.toString(cities.size()));
        mArrayAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1,
                cities);
        mAutoCompleteTextView.setThreshold(1);
        mAutoCompleteTextView.setAdapter(mArrayAdapter);

        final ImageView loc = (ImageView) findViewById(R.id.location);
        LocationRequest locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(5000);
        final EasyLocationRequest easyLocationRequest = new EasyLocationRequestBuilder()
                .setLocationRequest(locationRequest)
                .setFallBackToLastLocationTime(3000)
                .build();
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSingleLocationFix(easyLocationRequest);
               //  mAutoCompleteTextView.setText(getString(R.string.getLocationFromGeoCoder));

            }
        });

        mSpeechButton = (ImageView) findViewById(R.id.voice_search);
        mSpeechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });

        go = (Button) findViewById(R.id.button);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCity = mAutoCompleteTextView.getText().toString();
                selectedCity = selectedCity.trim();
                if (selectedCity.length() == 0 || selectedCity == null)
                    Toast.makeText(MainActivity.this, "Entered Location cannot be empty", Toast.LENGTH_LONG).show();
                else {
                    SharedPreferences locShared = getSharedPreferences("locPrefs", MODE_PRIVATE);
                    String coordinates = locShared.getString(selectedCity, "0:0");
                    if (coordinates.equals("0:0"))
                        Toast.makeText(MainActivity.this, "Entered Location does not exist in database", Toast.LENGTH_LONG).show();
                    else {
                        longi = Double.parseDouble(coordinates.substring(0, 5));
                        lati = Double.parseDouble(coordinates.substring(6));
                        List<Address> addresss = null;
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        try {
                            addresss = geocoder.getFromLocation(lati, longi, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.e("this", coordinates);
                        mSharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putFloat("latitude", (float) lati);
                        editor.putFloat("longitude", (float) longi);
                        editor.putString("address", selectedCity);
                        if (addresss.get(0).getSubAdminArea() != null)
                            editor.putString("district", addresss.get(0).getSubAdminArea());
                        else
                            editor.putString("district", "Port Blair");
                        editor.apply();
                        Intent intent = new Intent(MainActivity.this, IntroductionActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

        mAutoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    check();
                } else
                    go.setVisibility(View.GONE);
            }
        });
        int[] imgs = {R.drawable.bangalore, R.drawable.chennai, R.drawable.delhi, R.drawable.hyderabad, R.drawable.kolkata, R.drawable.mumbai2, R.drawable.pune2};

        String[] data = getResources().getStringArray(R.array.common_cities_name);

        common_cities = findViewById(R.id.common_cities);
        common_cities.setLayoutManager(new GridLayoutManager(this, 2));
        common_cities_adapter my_common_citiesadap = new common_cities_adapter(this, data, imgs);

        common_cities.setAdapter(my_common_citiesadap);

        layer = (LinearLayout) findViewById(R.id.layer);
        layer.setVisibility(View.GONE);

        logout = (TextView) findViewById(R.id.logout);

        popupButton = (LinearLayout) findViewById(R.id.popup_button);
        popup = (LinearLayout) findViewById(R.id.popup);
        root = (LinearLayout) findViewById(R.id.root);

        popup.setVisibility(View.GONE);

        popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popup.getVisibility() == View.GONE) {
                    layer.setVisibility(View.VISIBLE);
                    popup.setVisibility(View.VISIBLE);
                    popup.bringToFront();
                    root.animate().alpha(0.3f);
                }
            }
        });

        layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.animate().setDuration(1000).alpha(1f);
                popup.setVisibility(View.GONE);
                root.animate().setDuration(1000).alpha(1f);
                layer.setVisibility(View.GONE);
            }
        });

        popup.setOnClickListener(null);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startActivity(new Intent(MainActivity.this,LoginActivity.class));
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
                Toast.makeText(MainActivity.this, "Signed out!", Toast.LENGTH_SHORT).show();
            }
        });

        home = (LinearLayout) findViewById(R.id.home);
        profile = (LinearLayout) findViewById(R.id.profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
            }
        });

    }

    public void check() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!mAutoCompleteTextView.getText().toString().trim().equals("")) {
                    go.setVisibility(View.VISIBLE);

                } else {
                    go.setVisibility(View.GONE);
                }
                check();
            }
        };

        handler.postDelayed(runnable, 1000);


    }

    @Override
    public void onLocationPermissionGranted() {
        Toast.makeText(this, "Please wait while current location is fetched....", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationPermissionDenied() {
        Toast.makeText(this, "Enable Location for app to function properly", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationReceived(Location location) {
        lati = location.getLatitude();
        longi = location.getLongitude();
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lati, longi, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null) {
            address = addresses.get(0).getLocality();
            district = addresses.get(0).getSubAdminArea();
            mSharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putFloat("latitude", (float) lati);
            editor.putFloat("longitude", (float) longi);
            editor.putString("address", address);
            editor.putString("district", district);
            //Toast.makeText(this, district, Toast.LENGTH_SHORT).show();
            editor.commit();
            mAutoCompleteTextView.setText(address);

        } else
            Toast.makeText(this, "Location cannot be updated! Please enter location manually", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationProviderEnabled() {
        Toast.makeText(this, "Location On", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationProviderDisabled() {
        Toast.makeText(this, "Enable Location for app to function properly", Toast.LENGTH_LONG).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mAutoCompleteTextView.setText(result.get(0));
                }
                break;
        }
    }


}

class common_cities_adapter extends RecyclerView.Adapter<common_cities_adapter.ViewHolder> {

    private String[] mData;
    private int[] imgs;
    private LayoutInflater mInflater;


    // data is passed into the constructor
    common_cities_adapter(Context context, String[] data, int[] imgs) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.imgs = imgs;

    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.common_cities_item, parent, false);
        return new ViewHolder(view, parent.getContext());
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.myTextView.setText(mData[position]);
        holder.myImage.setImageResource(imgs[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mAutoCompleteTextView.setText(mData[position]);
            }
        });
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;
        ImageView myImage;

        ViewHolder(View itemView, Context context) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.city_name);
            myImage = itemView.findViewById(R.id.common_cities_image);

        }


    }


}