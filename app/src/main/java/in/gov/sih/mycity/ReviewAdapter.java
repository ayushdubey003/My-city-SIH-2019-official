package in.gov.sih.mycity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    ArrayList<ReviewModel> reviews;
    String child;

    public ReviewAdapter(ArrayList<ReviewModel> reviews,String child)
    {
        this.reviews=reviews;
        this.child=child;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_review,viewGroup,false);
        ViewHolder vh=new ViewHolder(view);
        return  vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,final int i) {

        viewHolder.rating.setRating(reviews.get(i).getRating());
        viewHolder.review.setText(reviews.get(i).getReview());
        viewHolder.name.setText(reviews.get(i).getUsername());
        final HashMap<String , Object> vote=new HashMap<>();
        viewHolder.votes.setText(""+reviews.get(i).getVotes());
        viewHolder.up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference href= FirebaseDatabase.getInstance().getReference("reviews");

                vote.put("votes",reviews.get(i).getVotes()+1);
                href.child(child).child(reviews.get(i).getUserid()).updateChildren(vote);
                DatabaseReference kref=FirebaseDatabase.getInstance().getReference("karma");

                kref.child(reviews.get(i).getUserid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int v=dataSnapshot.getValue(Integer.class);
                        dataSnapshot.getRef().setValue(v+1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
        viewHolder.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference href=FirebaseDatabase.getInstance().getReference("reviews");

                vote.put("votes",reviews.get(i).getVotes()-1);
                href.child(child).child(reviews.get(i).getUserid()).updateChildren(vote);
                DatabaseReference kref=FirebaseDatabase.getInstance().getReference("karma");

                kref.child(reviews.get(i).getUserid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int v=dataSnapshot.getValue(Integer.class);
                        dataSnapshot.getRef().setValue(v-1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RatingBar rating;
        TextView name,review,votes;
        ImageView up,down;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.username);
            review=itemView.findViewById(R.id.review);
            votes=itemView.findViewById(R.id.votes);
            rating=itemView.findViewById(R.id.ratingBar2);
            up=itemView.findViewById(R.id.up);
            down=itemView.findViewById(R.id.down);
        }
    }


}
