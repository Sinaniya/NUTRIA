package ch.uzh.csg.foodchain.Fragments.ProducerFragments;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ch.uzh.csg.foodchain.Fragments.ConsumerFragments.QRScaningFragment;
import ch.uzh.csg.foodchain.Fragments.SettingFragments.SettingFragment;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.GeneralUtils;


/**
 * The type Wallet fragment.
 */
public class WalletFragment extends android.app.Fragment {

    /**
     * The url
     */
    private String url = "http://192.168.43.4:9090/";


    private BottomNavigationView bottomNavigationView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_wallet, container, false);

        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottomBarWallet);
        bottomNavigationView.getMenu().getItem(3).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        openChromeCustomTab(url);

        return view;
    }

    /**
     * The M on navigation item selected listener.
     */
    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selected_fragment = null;
            switch (item.getItemId()) {

                case R.id.navigation_product:
                    selected_fragment = new ProducerFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).addToBackStack("abc").commit();
                    return true;

                case R.id.navigation_scanner:
                    selected_fragment = new QRScaningFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).addToBackStack("abc").commit();
                    return true;

                case R.id.navigation_home:
                    selected_fragment = new GenerateQRCodeFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).addToBackStack("abc").commit();
                    return true;

                case R.id.navigation_setting:
                    selected_fragment = new SettingFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).addToBackStack("abc").commit();
                    return true;

                case R.id.navigation_wallet:
                    selected_fragment = new WalletFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).addToBackStack("abc").commit();
                    return true;
            }

            return false;

        }

    };

    private void openChromeCustomTab(String url) {

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent intent = builder.build();
        intent.launchUrl(getActivity(), Uri.parse(url));
    }

}
