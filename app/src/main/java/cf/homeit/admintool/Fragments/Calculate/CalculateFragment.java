package cf.homeit.admintool.Fragments.Calculate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import cf.homeit.admintool.Adapters.TabAdapter;
import cf.homeit.admintool.R;

public class CalculateFragment  extends Fragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_calc,container, false);
            // Setting ViewPager for each Tabs
            ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
            setupViewPager(viewPager);
            // Set Tabs inside Toolbar
            TabLayout tabs = (TabLayout) view.findViewById(R.id.result_tabs);
            tabs.setupWithViewPager(viewPager);
            return view;
        }


        // Add Fragments to Tabs
        private void setupViewPager(ViewPager viewPager) {
            TabAdapter adapter = new TabAdapter(getChildFragmentManager());
            adapter.addFragment(new CIDRCalculateFragment(), getResources().getString(R.string.cidr_tabTitle));
            adapter.addFragment(new VLSMCalculateFragment(), getResources().getString(R.string.vlsm_tabTitle));
            viewPager.setAdapter(adapter);
        }

    }
