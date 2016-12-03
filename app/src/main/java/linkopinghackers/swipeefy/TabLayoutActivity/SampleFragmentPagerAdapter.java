package linkopinghackers.swipeefy.TabLayoutActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Alexander on 2016-08-10.
 */

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

        final int PAGE_COUNT = 2;
        private Fragment[] tabFragments = new Fragment [PAGE_COUNT];

        public SampleFragmentPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
            tabFragments[0] = new SwipeFragment();
            tabFragments[1] = new FavouritesFragment();
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }


        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            //behöver förmodligen inte skicka med position till fragments
            // kan behöva sätta en lästa med fragments och skapa dem i pageradapter om communicator interface inte fungerar

            return tabFragments[position];
        }



}
