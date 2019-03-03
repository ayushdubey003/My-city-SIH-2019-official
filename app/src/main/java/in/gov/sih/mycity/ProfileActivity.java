package in.gov.sih.mycity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private TextView nameView;
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
        home.setAlpha(0.3f);
        profile.setAlpha(1f);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
            }
        });


    }
}
