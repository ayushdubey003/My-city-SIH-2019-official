package in.gov.sih.mycity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.content.Context;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

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
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
           viewHolder.name.setText(schoolmodels.get(i).getName());
           viewHolder.email.setText(schoolmodels.get(i).getEmail());
           viewHolder.email.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                           "mailto",schoolmodels.get(i).getEmail(), null));
                   context.startActivity(intent);
               }
           });
           viewHolder.board.setText(schoolmodels.get(i).getBoard());
           viewHolder.website.setText(schoolmodels.get(i).getWebsite());
           if(schoolmodels.get(i).getWebsite().equals("-NA-") == false){
               viewHolder.website.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent intent =new Intent(Intent.ACTION_VIEW);
                       intent.setData(Uri.parse(schoolmodels.get(i).getWebsite()));
                       context.startActivity(intent);
                   }
               });
           }
           viewHolder.principal.setText(schoolmodels.get(i).getPrincipal());
           viewHolder.distate.setText(schoolmodels.get(i).getDistate());
           viewHolder.phone.setText(""+schoolmodels.get(i).getPhone());
           viewHolder.phone.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent =new Intent(Intent.ACTION_DIAL);
                   intent.setData(Uri.parse("tel:"+schoolmodels.get(i).getPhone()));
                   context.startActivity(intent);
               }
           });
           viewHolder.rtbr.setRating(schoolmodels.get(i).getAvgrat());
           int revv=(int)schoolmodels.get(i).getNum();
           viewHolder.rev.setText(String.valueOf(revv)+" reviews");
           viewHolder.rev.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   School_fragment.scrollPos = i;
                   AlertDialog.Builder builder=new AlertDialog.Builder(context);
                   LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                   View layout=inflater.inflate(R.layout.dialog_recycler,null);
                   builder.setView(layout);
                   final RecyclerView recyclerView=layout.findViewById(R.id.recycler);
                   DatabaseReference fref=FirebaseDatabase.getInstance().getReference("reviews");
                   final ArrayList<ReviewModel> reviews=new ArrayList<>();
                   fref.child("schools"+i).addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                             reviews.clear();
                             for(DataSnapshot ds: dataSnapshot.getChildren())
                             {
                                 ReviewModel rre=ds.getValue(ReviewModel.class);
                                 reviews.add(rre);
                             }
                             ReviewAdapter reviewAdapter=new ReviewAdapter(reviews,"schools"+i);
                             recyclerView.setAdapter(reviewAdapter);
                             recyclerView.setLayoutManager(new LinearLayoutManager(context));

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });
                   Dialog dialog=builder.create();
                   dialog.show();
               }
           });
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
                           if (rating == 0.0f || schoolmodels.get(i).getNum()==0) {
                               dref.child(String.valueOf(i)).child("avgrat").setValue(((schoolmodels.get(i).getAvgrat() * schoolmodels.get(i).getNum() - rating + ratingBar.getRating()) / (schoolmodels.get(i).getNum() + 1)));
                               dref.child(String.valueOf(i)).child("num").setValue(schoolmodels.get(i).getNum() + 1);
                           }

                           if (rating != 0.0f) {
                               dref.child(String.valueOf(i)).child("avgrat").setValue(((schoolmodels.get(i).getAvgrat() * schoolmodels.get(i).getNum() - rating + ratingBar.getRating()) / (schoolmodels.get(i).getNum())+1));
                           }
                           editor.putFloat("rrt", ratingBar.getRating());
                           editor.commit();

                           String review=editText.getText().toString();
                           if(!review.equals("")) {
                               DatabaseReference sref = FirebaseDatabase.getInstance().getReference("reviews");
                               HashMap<String, Object> data = new HashMap<>();
                               data.put("review", review);
                               data.put("rating", ratingBar.getRating());
                               data.put("username", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                               data.put("userid", FirebaseAuth.getInstance().getUid());
                               data.put("votes", 0);
                               sref.child("schools" + i).child(FirebaseAuth.getInstance().getUid()).updateChildren(data);
                           }

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

           viewHolder.loc.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent(Intent.ACTION_VIEW);
                   intent.setData(Uri.parse("http://maps.google.com/maps?q="+schoolmodels.get(i).getName()+schoolmodels.get(i).getAddress()));
                   context.startActivity(intent);
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
        ImageView loc;



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
           loc=itemView.findViewById(R.id.school_map_nav);




        }
    }
}
