package in.gov.sih.mycity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class weather_activity extends AppCompatActivity {


    WebView mwebview;
    ProgressBar progressBar;
    ImageView back_button;
    TextView temp_min,temp_max,desc,temp,press,humidity,wind_speed,wind_dir;
    String url="http://api.openweathermap.org/data/2.5/weather?q=noida&units=metric&appid=40099ca65b8dbe7c1d8936ce418b4f50";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_activity);

        temp_min=findViewById(R.id.temp_min);
        temp_max=findViewById(R.id.temp_max);
        desc=findViewById(R.id.desc);
        temp=findViewById(R.id.temp);
        press=findViewById(R.id.pressure);
        humidity=findViewById(R.id.humidity);
        wind_speed=findViewById(R.id.windspeed);
        wind_dir=findViewById(R.id.wind_dir);
        progressBar=findViewById(R.id.progressbar);
        back_button=findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

// ...

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {
                            JSONObject root=new JSONObject(response);
                            JSONArray weather_obj=root.getJSONArray("weather");

                            String main_obj="";
                            String desc_obj="";
                            for(int i=0;i<weather_obj.length();i++)
                            {
                                JSONObject currentarticle=weather_obj.getJSONObject(i);
                                desc_obj=currentarticle.getString("description");
                            }


                            JSONObject main2=root.getJSONObject("main");

                            Double temp_obj=main2.getDouble("temp");
                            Double pres_obj=main2.getDouble("pressure");
                            Double humid_obj=main2.getDouble("humidity");
                            Double tempmin_obj=main2.getDouble("temp_min");
                            Double tempmax_obj=main2.getDouble("temp_max");

                            JSONObject wind_obj=root.getJSONObject("wind");

                            Double speed_obj=wind_obj.getDouble("speed");
                            Double dir_obj=wind_obj.getDouble("deg");


                            temp_min.setText(tempmin_obj.toString());
                            temp_max.setText(tempmax_obj.toString());
                            desc.setText(desc_obj);
                            temp.setText(temp_obj.toString());
                            press.setText(pres_obj.toString());
                            humidity.setText(humid_obj.toString());
                            wind_speed.setText(speed_obj.toString());
                            wind_dir.setText(dir_obj.toString());


                        } catch (JSONException e) {
                            Log.e("VIVZ", "Problem parsing the JSON results", e);

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("VIVZ",""+error.getMessage());
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

        mwebview = findViewById(R.id.webview);
        WebSettings webSettings = mwebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mwebview.loadUrl("https://openweathermap.org/weathermap?basemap=map&cities=false&layer=precipitation&lat=28.58&lon=77.33&zoom=4");

        mwebview.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                progressBar.setVisibility(View.GONE);
            }
        });
    }



}
