package ch.uzh.csg.foodchain.Adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * The type View pager adapter.
 */
//FragmentStatePagerAdapter: The fragment is recreated when tab is selected and onCreateView is called. This is best for paging across a collection of objects for which the number of pages is undetermined. It destroys fragments as the user navigates to other pages, minimizing memory usage.
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> tabTitle = new ArrayList<>();

    /**
     * Instantiates a new View pager adapter.
     *
     * @param fm the fm
     */
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    /**
     * Add fragments.
     *
     * @param fragments the fragments
     * @param titles    the titles
     */
    public void addFragments(Fragment fragments,String titles){
        this.fragments.add(fragments);
        this.tabTitle.add(titles);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle.get(position);
    }
}
