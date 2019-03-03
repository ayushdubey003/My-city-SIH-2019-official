package in.gov.sih.mycity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class DetailsActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageView tabs[];
    private static final int number_of_tabs = 8;
    private HorizontalScrollView horizontalScrollView;
    private String city;
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().hide();
        setContentView(R.layout.activity_details);
        final LinearLayout view = (LinearLayout) findViewById(R.id.details);
        view.setVisibility(View.GONE);
        Handler handler = new Handler();
        animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        animationView.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animationView.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
            }
        }, 5000);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalscrollview);

        city = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("address", "");

        viewPager = (ViewPager) findViewById(R.id.details_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.details_tablayout);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(6);
        // tabLayout.getTabAt(0).setIcon(R.drawable.icon_places);

        tabs = new ImageView[]{((ImageView) findViewById(R.id.tab1)), ((ImageView) findViewById(R.id.tab2)),
                ((ImageView) findViewById(R.id.tab3)), ((ImageView) findViewById(R.id.tab4)),
                ((ImageView) findViewById(R.id.tab5)), ((ImageView) findViewById(R.id.tab6)),
                ((ImageView) findViewById(R.id.tab7)), ((ImageView) findViewById(R.id.tab8))};

        for (int i = 0; i < number_of_tabs; i++) {
            final int position = i;
            tabs[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectTab(position);
                }
            });
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()

        {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                selectTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        selectTab(0);
    }

    private void selectTab(int position) {
        for (int i = 0; i < number_of_tabs; i++)
            tabs[i].setAlpha(0.3f);

        if (number_of_tabs < 1)
            return;
        tabs[position].setAlpha(0.78f);
        viewPager.setCurrentItem(position, true);

    /*if (position >= number_of_tabs - 2)
        horizontalScrollView.scrollBy(horizontalScrollView.getWidth() / number_of_tabs, 0);
            if(position >= number_of_tabs  - 2)
                    horizontalScrollView.scrollBy(horizontalScrollView.getWidth() / number_of_tabs, 0);

            if(position <= 1)
                    horizontalScrollView.scrollBy(-horizontalScrollView.getWidth() / number_of_tabs, 0);
*/
        String fragmentTitle;
        switch (position)

        {
            case 0:
                fragmentTitle = "Places of " + city;
                break;
            case 1:
                fragmentTitle = "Trains From " + city;
                break;
            case 2:
                fragmentTitle = "Buses From " + city;
                break;
            case 3:
                fragmentTitle = "Hospitals in " + city;
                break;
            case 4:
                fragmentTitle = "Hotels in " + city;
                break;
            case 5:
                fragmentTitle = "Notable People in " + city;
                break;
            case 6:
                fragmentTitle = "Education in " + city;
                break;
            case 7:
                fragmentTitle = "City Details of " + city;
                break;
            default:
                fragmentTitle = "";
        }
        TextView title = (TextView) findViewById(R.id.fragment_title);
        title.setText(fragmentTitle);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new Attractions_frag();
                case 1:
                    return new TrainScheduleFragment().newInstance();
                case 2:
                    return new BusFragment().newInstance();
                case 3:
                    return new HospitalFragment();
                case 4:
                    return new HotelFragment();
                case 5:
                    return new PeopleFragment();
                case 6:
                    return new School_fragment();
                case 7:
                    return new Information().newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            //Modify the return value to include your fragment
            return number_of_tabs;
        }
    }
}

