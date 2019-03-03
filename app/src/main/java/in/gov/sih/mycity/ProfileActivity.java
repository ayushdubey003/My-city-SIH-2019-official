package in.gov.sih.mycity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView nameView,karmaView,logOut;
    private TextView emailView;
    private ImageView imageView;
    private Context context;
    private LinearLayout home, profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameView = (TextView)findViewById(R.id.name);
        emailView = (TextView)findViewById(R.id.email);
        imageView = (ImageView)findViewById(R.id.image);
        nameView.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        emailView.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        context=getBaseContext();
        Glide.with(context).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(imageView);
        home = (LinearLayout) findViewById(R.id.home);
        profile = (LinearLayout) findViewById(R.id.profile);
        karmaView = (TextView) findViewById(R.id.karma);
        logOut = (TextView) findViewById(R.id.logout);
        final DatabaseReference dref=FirebaseDatabase.getInstance().getReference("karma");
        dref.getRef().child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int karma=dataSnapshot.getValue(Integer.class);
                karmaView.setText(""+karma);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        home.setAlpha(0.3f);
        profile.setAlpha(1f);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut(ProfileActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
                Toast.makeText(ProfileActivity.this, "Signed out!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
