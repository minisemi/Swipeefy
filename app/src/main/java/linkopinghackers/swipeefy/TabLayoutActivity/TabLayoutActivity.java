package linkopinghackers.swipeefy.TabLayoutActivity;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.LinearLayout;
import android.support.design.widget.TabLayout;


import java.util.ArrayList;
import java.util.HashMap;

import linkopinghackers.swipeefy.R;
import linkopinghackers.swipeefy.SessionManager;

public class TabLayoutActivity extends AppCompatActivity implements FragmentCommunicator {

    private ScrollableViewPager viewPager;
    private TabLayout tabLayout;
    private static String PREF_NAME;
    public static String POSITION = "POSITION";
    final int PAGE_COUNT = 2;
    private int[] tabIcons = {
            R.drawable.ic_swipe,
            R.drawable.ic_heart
    };
    private SampleFragmentPagerAdapter sampleFragmentPagerAdapter;
    private final int
            SWIPE_POSITION = 0,
            FAVOURITE_POSITION = 1;
    private int eventID = 0;
    private SessionManager sessionManager;
    private Boolean scrollable;
    private SwipeFragment swipeFragment;
    private FavouritesFragment favouritesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);
        sessionManager = new SessionManager();
        scrollable = true;

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ScrollableViewPager) findViewById(R.id.viewpager);
        sampleFragmentPagerAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sampleFragmentPagerAdapter);
        //android.support.v4.view.ViewPager

        viewPager.setOffscreenPageLimit(1);

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


        for (int i = 0; i < PAGE_COUNT; i++){ tabLayout.getTabAt(i).setIcon(tabIcons[i]);}
        viewPager.setCurrentItem(0);
        viewPager.setScrollable(false);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition(), true);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {            }
        });

        swipeFragment = (SwipeFragment) sampleFragmentPagerAdapter.getItem(SWIPE_POSITION);
        favouritesFragment = (FavouritesFragment) sampleFragmentPagerAdapter.getItem(FAVOURITE_POSITION);


    }

    @Override
    public void addPlaylistToFavourites(Playlist playlist) {

        favouritesFragment.addFavourite(playlist);

    }

    public SessionManager getSessionManager (){
        return sessionManager;
    }

    @Override
    public void onBackPressed() {this.moveTaskToBack(true);}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        viewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
    }
}
