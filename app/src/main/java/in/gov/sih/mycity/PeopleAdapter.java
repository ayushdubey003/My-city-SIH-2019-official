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

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {
    ArrayList<PeopleModel> peopleModels;
    Context context;


    public PeopleAdapter(ArrayList<PeopleModel> peopleModels)
    {
        this.peopleModels=peopleModels;
    }



    @Override
    public PeopleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_item,parent,false);
        PeopleAdapter.ViewHolder viewHolder=new PeopleAdapter.ViewHolder(view,parent.getContext(),peopleModels);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleAdapter.ViewHolder holder, final int position) {
        Glide.with(context).load(peopleModels.get(position).getUrl()).into(holder.img);
        holder.name.setText(peopleModels.get(position).getName());
        final String peopleName = peopleModels.get(position).getName();
        final String uid=FirebaseAuth.getInstance().getUid().substring(0,10);
        holder.desc.setText(peopleModels.get(position).getDesc());



    }
    @Override
    public int getItemCount() {
        return peopleModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,rev,desc,price,ret;
        RatingBar rat;
        ImageView img;
        ArrayList<PeopleModel> peopleModels;

        public ViewHolder(@NonNull View itemView, Context context,ArrayList<PeopleModel> peopleModels) {
            super(itemView);
            this.peopleModels=peopleModels;
            name=itemView.findViewById(R.id.name);
            desc=itemView.findViewById(R.id.description);
            img=itemView.findViewById(R.id.image);

        }
    }
}
