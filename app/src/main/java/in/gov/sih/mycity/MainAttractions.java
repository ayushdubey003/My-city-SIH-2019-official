package in.gov.sih.mycity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainAttractions extends Fragment {

    private ArrayAdapter arrayAdapter;
    private ListView list;
    private ProgressBar progressBar;
    private ArrayList<Attraction> arrayList;

    private static String latitude, longitude , address;
    private static String LOG_TAG = "MAIN_ATTRACTIONS";

    public MainAttractions() {
        // Required empty public constructor
    }

    public static MainAttractions newInstance() {
        MainAttractions fragment = new MainAttractions();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View returnView = inflater.inflate(R.layout.fragment_main_attractions, container,
                false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs",
                Context.MODE_PRIVATE);
        latitude = (sharedPreferences.getFloat("latitude", 0.0f)) + "";
        longitude = (sharedPreferences.getFloat("longitude", 0.0f)) + "";
        address = (sharedPreferences.getString("address", "")) + "";


        list = (ListView)returnView.findViewById(R.id.list);
        arrayList = new ArrayList<>();
        arrayAdapter = new AttractionAdapter(getActivity(), 0, arrayList);
        list.setAdapter(arrayAdapter);

        progressBar = (ProgressBar)returnView.findViewById(R.id.progress_bar);

        getAttractions();

        return returnView;
    }

    private void getAttractionsByFallback(){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=coordinates" +
                "|pageimages|pageterms&colimit=50&piprop=thumbnail&pithumbsize=360&pilimit=50&" +
                "wbptterms=description&generator=geosearch&ggscoord=" +
                latitude + "|" + longitude +
                "&ggsradius=10000&ggslimit=50";

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(LOG_TAG, "Fallback Response Obtained");
                updateUI(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG, error.getMessage());
            }
        });

        queue.add(stringRequest);
    }

    private void updateUI(String response){
        try{
            JSONObject rootJsonObject = new JSONObject(response);
            ArrayList<Attraction> attractions = new ArrayList<>();

            JSONObject query = rootJsonObject.getJSONObject("query");
            JSONObject pages = query.getJSONObject("pages");

            Iterator<String> keys = pages.keys();

            int numberOfAttractions = 0;
            while (keys.hasNext() && numberOfAttractions < 15){
                String key = keys.next();
                JSONObject attraction = pages.getJSONObject(key);

                String title = null, imageURL = null, description = null;

                title = attraction.getString("title");

                if(attraction.has("thumbnail")) {
                    JSONObject JSONthumbnail = attraction.getJSONObject("thumbnail");
                    imageURL = JSONthumbnail.getString("source");
                }

                if(attraction.has("terms")) {
                    JSONObject JSONdescription = attraction.getJSONObject("terms");
                    description = JSONdescription.getJSONArray("description").getString(0);
                }

                Attraction att = new Attraction(title, imageURL, description);
                if(filter(att)){
                    attractions.add(att);
                    numberOfAttractions ++;
                }
            }

            addAttractions(attractions);

        }catch (JSONException e){
            Log.e("MainAttractions", e.getMessage());
        }
        finally {
            progressBar.setVisibility(View.GONE);
        }
    }

    private class AttractionAdapter extends ArrayAdapter<Attraction>{

        public AttractionAdapter(@NonNull Context context, int resource,
                                 @NonNull List<Attraction> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(getActivity()).
                        inflate(R.layout.attraction_item, null);
            }

            ImageView attractionImage = (ImageView)convertView.findViewById(R.id.attraction_image);
            TextView attractionTitle = (TextView)convertView.findViewById(R.id.attraction_title);
            TextView attractionDescription = (TextView)convertView.findViewById(R.id.attraction_description);

            final Attraction attraction = getItem(position);
            if(true) {
                Glide.with(getActivity()).load(attraction.getImageURL()).into(attractionImage);
                System.out.print(attraction.getImageURL());
            }
            else
                attractionImage.setImageResource(R.drawable.generic_city_vector);

            attractionTitle.setText(attraction.getTitle());
            attractionDescription.setText(attraction.getDescription());

            attractionImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String map = "https://maps.google.co.in/maps?q=" +
                            attraction.getTitle() + ", " + address;
                    Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                    getActivity().startActivity(mapsIntent);
                }
            });

            return convertView;
        }
    }

    private boolean filter(Attraction attraction){
        if(attraction.getDescription() == null)
            return false;

        String description = attraction.getDescription();
        String title = attraction.getTitle();

        if(description.contains("district") || description.contains("District"))
            return false;

        if(description.contains("human settlement"))
            description = "A settlement of the city";

        if(title.contains("district") || title.contains("District"))
            return false;

        if(title.contains("constituency") || title.contains("Constituency"))
            return false;

        if(title.contains("division") || title.contains("Division"))
            return false;

        if(title.equals(address))
            return false;

        description = description.substring(0, 1).toUpperCase() + description.substring(1);
        attraction.setDescription(description);

        title = title.substring(0, 1).toUpperCase() + title.substring(1);
        attraction.setTitle(title);

        return true;
    }

    private void getAttractions(){
        ScrapingTask task = new ScrapingTask();
        task.execute();
    }

    private class ScrapingTask extends AsyncTask{

        @Override
        protected void onPostExecute(Object o) {
            ArrayList<Attraction> attractions = (ArrayList<Attraction>)o;
            if(attractions.size() < 1)
                getAttractionsByFallback();
            else
                addAttractions(attractions);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            ArrayList<Attraction> attractions = new ArrayList<>();
            String url = "https://www.google.com/search?q=places+to+visit+in+" + address;
            try{
                Document html = Jsoup.connect(url).get();
                Elements places = html.select("table[class=jktSrf]").select("td");

                Elements place = places.select("a[class=mCovnb]").eq(0);
                url = place.attr("href");

                url = "https://www.google.com" + url;
                html = Jsoup.connect(url).get();

                places = html.select("ol").eq(0).select("li");
                for(int i = 0;i < places.size() && i < 15;i ++){
                    place = places.select("li").eq(i);
                    String title = place.select("h2").eq(0).text();
                    String imageURL = place.select("img").eq(0).attr("src");
                    String description = place.select("p").eq(0).text(); //+ "\n" + place.select("p").eq(1).text();

                    if(description == null || description.trim().equals(""))
                        continue;

                    attractions.add(new Attraction(title, imageURL, description));
                }

            }catch (IOException e){
                Log.e(LOG_TAG, e.getMessage());
            }

            return attractions;

        };
    }

    private void addAttractions(ArrayList<Attraction> attractions){
        arrayAdapter.clear();
        arrayAdapter.addAll(attractions);
        progressBar.setVisibility(View.GONE);
    }

}