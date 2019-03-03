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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class School_fragment extends Fragment {

    RecyclerView recyclerView;
    School_Adapter school_adapter;
    College_Adapter college_adapter;
    ArrayList<schoolmodel> schoolmodels;
    ArrayList<Collegemodel> collegemodels;
    DatabaseReference dref,dref2;
    Spinner spinner;

    public static int scrollPos = 0;

    public School_fragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_school, container,false);
        recyclerView=view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        spinner=view.findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    recyclerView.setAdapter(school_adapter);
                }
                else{
                    recyclerView.setAdapter(college_adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                recyclerView.scrollToPosition(scrollPos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        collegemodels=new ArrayList<>();

        dref2= FirebaseDatabase.getInstance().getReference("colleges");
        dref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                collegemodels.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Collegemodel att=ds.getValue(Collegemodel.class);
                    collegemodels.add(att);
                }
                Log.i("onDataChange: ",""+collegemodels.size());
                college_adapter=new College_Adapter(collegemodels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return view;
    }


}
