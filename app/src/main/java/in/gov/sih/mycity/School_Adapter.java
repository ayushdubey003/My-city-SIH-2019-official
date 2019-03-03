package in.gov.sih.mycity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.content.Context;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class School_Adapter extends RecyclerView.Adapter<School_Adapter.ViewHolder> {


    ArrayList<schoolmodel> schoolmodels;
    Context context;
    public School_Adapter(ArrayList<schoolmodel> schoolmodels)
    {
        this.schoolmodels=schoolmodels;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.school_item,viewGroup,false);
        ViewHolder vh=new ViewHolder(view,viewGroup.getContext(),schoolmodels);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
           viewHolder.name.setText(schoolmodels.get(i).getName());
           viewHolder.email.setText(schoolmodels.get(i).getEmail());
           viewHolder.board.setText(schoolmodels.get(i).getBoard());
           viewHolder.website.setText(schoolmodels.get(i).getWebsite());
           viewHolder.principal.setText(schoolmodels.get(i).getPrincipal());
           viewHolder.distate.setText(schoolmodels.get(i).getDistate());
           viewHolder.phone.setText(""+schoolmodels.get(i).getPhone());
           viewHolder.rtbr.setRating(schoolmodels.get(i).getAvgrat());
           int revv=(int)schoolmodels.get(i).getNum();
           viewHolder.rev.setText(String.valueOf(revv)+" reviews");
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

                           DatabaseReference dref = FirebaseDatabase.getInstance().getReference("schools");
                           SharedPreferences sharedPreferences = context.getSharedPreferences("Sc_Ratings", Context.MODE_PRIVATE);
                           SharedPreferences.Editor editor = sharedPreferences.edit();
                           float rating = sharedPreferences.getFloat("rrt", 0.0f);
                           if (rating == 0.0f) {
                               dref.child(String.valueOf(i)).child("avgrat").setValue(((schoolmodels.get(i).getAvgrat() * schoolmodels.get(i).getNum() - rating + ratingBar.getRating()) / (schoolmodels.get(i).getNum() + 1)));
                               dref.child(String.valueOf(i)).child("num").setValue(schoolmodels.get(i).getNum() + 1);
                           }

                           if (rating != 0.0f) {
                               dref.child(String.valueOf(i)).child("avgrat").setValue(((schoolmodels.get(i).getAvgrat() * schoolmodels.get(i).getNum() - rating + ratingBar.getRating()) / (schoolmodels.get(i).getNum()+1)));
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
    }

    @Override
    public int getItemCount() {
        return schoolmodels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView address,name,board,email,phone,website,principal,distate,rate,rev;
        RatingBar rtbr;


        public ViewHolder(@NonNull View itemView,Context context,ArrayList<schoolmodel> schoolmodels) {
            super(itemView);

           address=itemView.findViewById(R.id.school_address);
           name=itemView.findViewById(R.id.school_name);
           board=itemView.findViewById(R.id.school_board);
           email=itemView.findViewById(R.id.school_email);
           phone=itemView.findViewById(R.id.contact_no);
           website=itemView.findViewById(R.id.school_website);
           rate=itemView.findViewById(R.id.rate);
           rtbr=itemView.findViewById(R.id.ret);
           rtbr.setClickable(false);
           rev=itemView.findViewById(R.id.rev);
           principal=itemView.findViewById(R.id.school_principal_name);
           distate=itemView.findViewById(R.id.school_state_dist);




        }
    }
}
