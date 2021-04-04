package cf.homeit.admintool.AbstractClases;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.lang.reflect.Field;

import cf.homeit.admintool.DataModels.Vlan;
import cf.homeit.admintool.R;
import cf.homeit.admintool.ViewHolder.VlanViewHolder;

import static cf.homeit.admintool.ExtendsClases.Constants.EXTRA_EDIT_KEY;
import static cf.homeit.admintool.ExtendsClases.Constants.EXTRA_VLAN_KEY;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.showToast;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.deleteFromFirebase;

public abstract class AbstractVLANFragment extends Fragment {
    private static final String TAG = "AbstractSwitchFragment";
    // [START define_database_reference]
    private DatabaseReference mDatabase,mDatabaseReference;
    // [END define_database_reference]
    private FirebaseRecyclerAdapter<Vlan, VlanViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private GridLayoutManager mManager;
    NavController navController;
    private FloatingActionButton newBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_vlan,container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (mDatabase == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            mDatabase = database.getReference();
            mDatabase.keepSynced(true);
        }
        newBtn = view.findViewById(R.id.vlanAdd);
        newBtn.setOnClickListener(v -> {
            navController = Navigation.findNavController(requireActivity(), R.id.first_nav_host);
            navController.navigate(R.id.newWNVlanFragment);
        });
        mRecycler = view.findViewById(R.id.vlanList);
        mRecycler.setHasFixedSize(true);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new GridLayoutManager(requireActivity(),2);
//        mManager.setReverseLayout(true);
//        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Vlan>()
                .setQuery(postsQuery, Vlan.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Vlan, VlanViewHolder>(options) {

            @SuppressLint({"RestrictedApi", "NonConstantResourceId"})
            @Override
            protected void onBindViewHolder(@NonNull VlanViewHolder holder, int position, @NonNull Vlan model) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                holder.itemView.setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putString(EXTRA_VLAN_KEY, postKey);
                    bundle.putString(EXTRA_EDIT_KEY, "read");
                    navController = Navigation.findNavController(requireActivity(), R.id.first_nav_host);
                    navController.navigate(R.id.vlanDetailFragment,bundle);
                });
                holder.itemView.setOnLongClickListener(v -> {
                    PopupMenu popup = new PopupMenu(holder.itemView.getContext(), holder.itemView);
                    popup.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) item -> {
                        Bundle bundle = new Bundle();
                        switch (item.getItemId()) {
                            case R.id.detail:
                                showToast(requireActivity().getApplicationContext(), "Detail: "+postKey);
                                bundle.putString(EXTRA_VLAN_KEY, postKey);
                                bundle.putString(EXTRA_EDIT_KEY, "read");
                                navController = Navigation.findNavController(requireActivity(), R.id.first_nav_host);
                                navController.navigate(R.id.vlanDetailFragment,bundle);
                                break;
                            case R.id.edit:
                                showToast(requireActivity().getApplicationContext(), "Edit: "+postKey);
                                bundle.putString(EXTRA_VLAN_KEY, postKey);
                                bundle.putString(EXTRA_EDIT_KEY, "edit");
                                navController = Navigation.findNavController(requireActivity(), R.id.first_nav_host);
                                navController.navigate(R.id.vlanDetailFragment,bundle);
                                break;
                            case R.id.delete:
                                deleteFromFirebase(postKey,"vlans",requireActivity().getApplicationContext());
                                break;
                        }
                        return false;
                    });
                    // here you can inflate your menu
                    popup.inflate(R.menu.options_menu);
                    popup.setGravity(Gravity.TOP);

                    // if you want icon with menu items then write this try-catch block.
                    try {
                        Field mFieldPopup=popup.getClass().getDeclaredField("mPopup");
                        mFieldPopup.setAccessible(true);
                        MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popup);
                        mPopup.setForceShowIcon(true);
                    } catch (Exception e) {

                    }
                    popup.show();
                    return false;
                });
                holder.bindToModel(model);
            }

            @Override
            public VlanViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new VlanViewHolder(inflater.inflate(R.layout.item_vlan, viewGroup, false));
            }


        };
        mRecycler.setAdapter(mAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

}