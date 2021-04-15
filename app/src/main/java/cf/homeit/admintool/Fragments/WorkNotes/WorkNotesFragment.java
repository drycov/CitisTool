package cf.homeit.admintool.Fragments.WorkNotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import cf.homeit.admintool.Adapters.TabAdapter;
import cf.homeit.admintool.Fragments.WorkNotes.Switch.SwitchFragment;
import cf.homeit.admintool.Fragments.WorkNotes.Vlan.VLANFragment;
import cf.homeit.admintool.R;

public class WorkNotesFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_switch_vlan_base, container, false);
        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.wn_viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) view.findViewById(R.id.wn_result_tabs);
        tabs.setupWithViewPager(viewPager);

        return view;

    }


    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        TabAdapter adapter = new TabAdapter(getChildFragmentManager());
        adapter.addFragment(new SwitchFragment(), getResources().getString(R.string.switch_tabTitle));
        adapter.addFragment(new VLANFragment(), getResources().getString(R.string.vlan_tabTitle));
        viewPager.setAdapter(adapter);
    }

}
