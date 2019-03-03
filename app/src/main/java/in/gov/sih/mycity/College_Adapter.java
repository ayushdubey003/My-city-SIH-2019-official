package in.gov.sih.mycity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.content.Context;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class College_Adapter extends RecyclerView.Adapter<College_Adapter.ViewHolder> {


    ArrayList<Collegemodel> collegemodels;
    Context context;
    public College_Adapter(ArrayList<Collegemodel> collegemodels)
    {
        this.collegemodels=collegemodels;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.college_item,viewGroup,false);
        ViewHolder vh=new ViewHolder(view,viewGroup.getContext(),collegemodels);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(collegemodels.get(i).getName());
        viewHolder.state_dist.setText(collegemodels.get(i).getState()+", "+collegemodels.get(i).getDistrict());
        viewHolder.uni_name.setText(collegemodels.get(i).getUni_name());
        viewHolder.uni_type.setText(collegemodels.get(i).getUni_type());
        viewHolder.rtbr.setRating(collegemodels.get(i).getAvgrat());
        int revv=(int)collegemodels.get(i).getNum();
        viewHolder.rev.setText(String.valueOf(revv));
        viewHolder.rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflator=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout=inflator.inflate(R.layout.dialog_item,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final RatingBar ratingBar=layout.findViewById(R.id.ratingBar);
                final EditText editText=layout.findViewById(R.id.editText2);
                builder.setView(layout);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("colleges");
                        SharedPreferences sharedPreferences = context.getSharedPreferences("Sc_Ratings", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        float rating = sharedPreferences.getFloat("rrt", 0.0f);
                        if (rating == 0.0f) {
                            dref.child(String.valueOf(i)).child("avgrat").setValue(((collegemodels.get(i).getAvgrat() * collegemodels.get(i).getNum() - rating + ratingBar.getRating()) / (collegemodels.get(i).getNum() + 1)));
                            dref.child(String.valueOf(i)).child("num").setValue(collegemodels.get(i).getNum() + 1);
                        }

                        if (rating != 0.0f) {
                            dref.child(String.valueOf(i)).child("avgrat").setValue(((collegemodels.get(i).getAvgrat() * collegemodels.get(i).getNum() - rating + ratingBar.getRating()) / (collegemodels.get(i).getNum()+1)));
                        }
                        editor.putFloat("rrt", ratingBar.getRating());
                        editor.commit();


                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                Dialog dialog = builder.create();
                dialog.show();

            }
        });

        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://maps.google.com/maps?q="+collegemodels.get(i).getName()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return collegemodels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView state_dist,name,uni_name,uni_type,rate,rev;
        RatingBar rtbr;
        ImageView img;


        public ViewHolder(@NonNull View itemView,Context context,ArrayList<Collegemodel> collegemodels) {
            super(itemView);

            state_dist=itemView.findViewById(R.id.state_dist);
            name=itemView.findViewById(R.id.college_name);
            uni_name=itemView.findViewById(R.id.university_name);
            uni_type=itemView.findViewById(R.id.university_type);
            rate=itemView.findViewById(R.id.rate_cl);
            rtbr=itemView.findViewById(R.id.ret_cl);
            rtbr.setClickable(false);
            rev=itemView.findViewById(R.id.rev_cl);
            img=itemView.findViewById(R.id.college_map_nav);

        }
    }
}
