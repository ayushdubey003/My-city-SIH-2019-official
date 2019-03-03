package in.gov.sih.mycity;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class HotelFragment extends Fragment {

    DatabaseReference dref;
    ArrayList<HotelModel> hotelModels;
    RecyclerView recyclerView;
    HotelAdapter hotelAdapter;

    public static int scrollPos = 0;

    public HotelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_hotel, container, false);
        recyclerView=view.findViewById(R.id.recycler);

        hotelModels=new ArrayList<>();
        dref= FirebaseDatabase.getInstance().getReference("hotels");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hotelModels.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    HotelModel att=ds.getValue(HotelModel.class);
                    hotelModels.add(att);

                }
                Log.e("tag",""+hotelModels.size());
                hotelAdapter=new HotelAdapter(hotelModels);
                recyclerView.setAdapter(hotelAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                recyclerView.scrollToPosition(scrollPos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}
