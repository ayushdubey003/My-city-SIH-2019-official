package in.gov.sih.mycity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class School_fragment extends Fragment {

    RecyclerView recyclerView;
    School_Adapter school_adapter;
    ArrayList<schoolmodel> schoolmodels;
    DatabaseReference dref;

    public School_fragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_school, container,false);
        recyclerView=view.findViewById(R.id.recycler);

        schoolmodels=new ArrayList<>();

        dref= FirebaseDatabase.getInstance().getReference("schools");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                schoolmodels.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    schoolmodel att=ds.getValue(schoolmodel.class);
                    schoolmodels.add(att);
                }
                Log.i("onDataChange: ",""+schoolmodels.size());
                school_adapter=new School_Adapter(schoolmodels);
                recyclerView.setAdapter(school_adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }


}
