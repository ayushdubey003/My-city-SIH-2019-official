package in.gov.sih.mycity;

import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

        private ViewPager viewPager;
        private TabLayout tabLayout;
        private ImageView tabs[];
        private static final int number_of_tabs = 4;
        private HorizontalScrollView horizontalScrollView;
        private String city;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                //getActionBar().hide();
                setContentView(R.layout.activity_details);

                horizontalScrollView = (HorizontalScrollView)findViewById(R.id.horizontalscrollview);

                city = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("address", "");

                viewPager = (ViewPager) findViewById(R.id.details_viewpager);
                tabLayout = (TabLayout) findViewById(R.id.details_tablayout);
                MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

                viewPager.setAdapter(myPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
                viewPager.setOffscreenPageLimit(6);
               // tabLayout.getTabAt(0).setIcon(R.drawable.icon_places);

                tabs = new ImageView[]{((ImageView)findViewById(R.id.tab1)), ((ImageView)findViewById(R.id.tab2)),
                        ((ImageView)findViewById(R.id.tab3)), ((ImageView)findViewById(R.id.tab4)),
                        ((ImageView)findViewById(R.id.tab5)), ((ImageView)findViewById(R.id.tab6))};

                for(int i = 0;i < number_of_tabs;i ++){
                        final int position = i;
                        tabs[i].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                        selectTab(position);
                                }
                        });
                }

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        private void selectTab(int position){
                for(int i = 0;i < number_of_tabs;i ++)
                        tabs[i].setAlpha(0.3f);

                if(number_of_tabs < 1)
                        return;
                tabs[position].setAlpha(0.78f);
                viewPager.setCurrentItem(position, true);

                if(position >= number_of_tabs  - 2)
                        horizontalScrollView.scrollBy(horizontalScrollView.getWidth() / number_of_tabs, 0);

                if(position <= 1)
                        horizontalScrollView.scrollBy(-horizontalScrollView.getWidth() / number_of_tabs, 0);

                String fragmentTitle;
                switch (position){
                        case 0:
                                fragmentTitle = "City Details of " + city;
                                break;
                        case 1:
                                fragmentTitle = "Places of " + city;
                                break;
                        case 2:
                                fragmentTitle = "Trains From " + city;
                                break;
                        case 3:
                                fragmentTitle = "Hospitals in " + city;
                                break;
                        case 4:
                                fragmentTitle = "Hotels in " + city;
                                break;
                        case 5:
                                fragmentTitle = "Trains from " + city;
                                break;
                        default:
                                fragmentTitle= "";
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
                                        return new Information().newInstance();
                                case 1:
                                        return new Attractions_frag();
                                case 2:
                                        return new TrainScheduleFragment().newInstance();
                                case 3:
                                        return new School_fragment();
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

