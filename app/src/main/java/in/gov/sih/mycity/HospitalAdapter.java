package in.gov.sih.mycity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {
    ArrayList<HospitalModel> hospitalModels;
    Context context;


    public HospitalAdapter(ArrayList<HospitalModel> hospitalModels)
    {
        this.hospitalModels=hospitalModels;
    }



    @Override
    public HospitalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_item,parent,false);
        HospitalAdapter.ViewHolder viewHolder=new HospitalAdapter.ViewHolder(view,parent.getContext(),hospitalModels);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalAdapter.ViewHolder holder, final int position) {
        holder.name.setText(hospitalModels.get(position).getName());
        final String hotelName = hospitalModels.get(position).getName();
        final String uid=FirebaseAuth.getInstance().getUid().substring(0,10);
        holder.address.setText(hospitalModels.get(position).getAddress());
        int revv=(int)hospitalModels.get(position).getNum();
        holder.rev.setText(revv+" reviews");
        holder.rat.setRating(hospitalModels.get(position).getAvgrat());
        holder.ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HospitalFragment.scrollPos = position;
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                final RatingBar ratingBar=new RatingBar(context);
                ratingBar.setPadding(100, 100, 100, 0);
                final DatabaseReference dref=FirebaseDatabase.getInstance().getReference("hospitals");
                final SharedPreferences sharedPreferences=context.getSharedPreferences("Ratings",Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor=sharedPreferences.edit();
                final float rating = sharedPreferences.getFloat(uid+hotelName,0.0f);
                builder.setView(ratingBar);
                if(rating!=0.0f)
                    ratingBar.setRating(rating);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        if(rating==0.0f) {
                            dref.child(String.valueOf(position)).child("avgrat").setValue(((hospitalModels.get(position).getAvgrat() * hospitalModels.get(position).getNum() - rating + ratingBar.getRating()) / (hospitalModels.get(position).getNum()+1)));
                            dref.child(String.valueOf(position)).child("num").setValue(hospitalModels.get(position).getNum()+1);
                        }

                        if(rating!=0.0f) {
                            dref.child(String.valueOf(position)).child("avgrat").setValue(((hospitalModels.get(position).getAvgrat() * hospitalModels.get(position).getNum() - rating + ratingBar.getRating()) / (hospitalModels.get(position).getNum())));
                        }
                        editor.putFloat(uid+hotelName,ratingBar.getRating());
                        editor.commit();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                Dialog dialog=builder.create();
                dialog.show();
            }
        });


    }
    @Override
    public int getItemCount() {
        return hospitalModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name,address,rev;
        Button ret;
        RatingBar rat;
        ArrayList<HospitalModel> hospitalModels;

        public ViewHolder(@NonNull View itemView, Context context,ArrayList<HospitalModel> hospitalModels) {
            super(itemView);
            this.hospitalModels=hospitalModels;
            name=itemView.findViewById(R.id.name);
            address=itemView.findViewById(R.id.address);
            rev=itemView.findViewById(R.id.number_of_reviews);
            rat=itemView.findViewById(R.id.rating_bar);
            ret=itemView.findViewById(R.id.rate_us);
            name.setOnClickListener(this);
            address.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://maps.google.com/maps?q="+hospitalModels.get(getAdapterPosition()).getName()+hospitalModels.get(getAdapterPosition()).getAddress()));
            context.startActivity(intent);
        }
    }
}
