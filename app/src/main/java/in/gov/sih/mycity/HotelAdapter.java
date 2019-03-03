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

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {
    ArrayList<HotelModel> hotelModels;
    Context context;


    public HotelAdapter(ArrayList<HotelModel> hotelModels)
    {
        this.hotelModels=hotelModels;
    }



    @Override
    public HotelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_item,parent,false);
        HotelAdapter.ViewHolder viewHolder=new HotelAdapter.ViewHolder(view,parent.getContext(),hotelModels);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HotelAdapter.ViewHolder holder, final int position) {
        Glide.with(context).load(hotelModels.get(position).getImgurl()).into(holder.img);
        holder.name.setText(hotelModels.get(position).getName());
        final String hotelName = hotelModels.get(position).getName();
        final String uid=FirebaseAuth.getInstance().getUid().substring(0,10);
        holder.desc.setText(hotelModels.get(position).getDesc());
        holder.price.setText(Integer.toString(hotelModels.get(position).getAmnt()));
        int revv=(int)hotelModels.get(position).getNum();
        holder.rev.setText(revv+" reviews");
        holder.rat.setRating(hotelModels.get(position).getAvgrat());
        holder.ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HotelFragment.scrollPos = position;
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                final RatingBar ratingBar=new RatingBar(context);
                ratingBar.setPadding(100, 100, 100, 0);
                final DatabaseReference dref=FirebaseDatabase.getInstance().getReference("hotels");
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
                            dref.child(String.valueOf(position)).child("avgrat").setValue(((hotelModels.get(position).getAvgrat() * hotelModels.get(position).getNum() - rating + ratingBar.getRating()) / (hotelModels.get(position).getNum()+1)));
                            dref.child(String.valueOf(position)).child("num").setValue(hotelModels.get(position).getNum()+1);
                        }

                        if(rating!=0.0f) {
                            dref.child(String.valueOf(position)).child("avgrat").setValue(((hotelModels.get(position).getAvgrat() * hotelModels.get(position).getNum() - rating + ratingBar.getRating()) / (hotelModels.get(position).getNum())));
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
        return hotelModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name,rev,desc,price,ret;
        RatingBar rat;
        ImageView img;
        ArrayList<HotelModel> hotelModels;

        public ViewHolder(@NonNull View itemView, Context context,ArrayList<HotelModel> hotelModels) {
            super(itemView);
            this.hotelModels=hotelModels;
            name=itemView.findViewById(R.id.hotel_title);
            desc=itemView.findViewById(R.id.desc);
            rev=itemView.findViewById(R.id.count_rating);
            rat=itemView.findViewById(R.id.rating_bar);
            img=itemView.findViewById(R.id.image);
            price=itemView.findViewById(R.id.hotel_price);
            ret=itemView.findViewById(R.id.rate_us);
            name.setOnClickListener(this);
            desc.setOnClickListener(this);
            price.setOnClickListener(this);
            img.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://maps.google.com/maps?q="+hotelModels.get(getAdapterPosition()).getName()));
            context.startActivity(intent);
        }
    }
}
