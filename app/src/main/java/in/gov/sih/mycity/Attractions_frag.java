package in.gov.sih.mycity;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Attractions_frag extends Fragment {

        public static int scrollpos = 0;

     DatabaseReference dref;
     ArrayList<AttractionModel> attractionModels;
     RecyclerView recyclerView;
     Attraction_Adapter attraction_adapter;
     RelativeLayout progressBar;

    public Attractions_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_attractions, container, false);
        recyclerView=view.findViewById(R.id.recycler);

        progressBar = (RelativeLayout) view.findViewById(R.id.progress_bar);

        attractionModels=new ArrayList<>();
        dref= FirebaseDatabase.getInstance().getReference("mainattraction");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                attractionModels.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    AttractionModel att=ds.getValue(AttractionModel.class);
                    attractionModels.add(att);
                }

                 attraction_adapter=new Attraction_Adapter(attractionModels);
                 recyclerView.setAdapter(attraction_adapter);
                  recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                  recyclerView.smoothScrollToPosition(scrollpos);

                  progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

}
