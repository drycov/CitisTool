package cf.homeit.admintool.AbstractClases;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.lang.reflect.Field;
import java.util.Objects;

import cf.homeit.admintool.DataModels.Switch;
import cf.homeit.admintool.R;
import cf.homeit.admintool.ViewHolder.SwitchViewHolder;

import static cf.homeit.admintool.ExtendsClases.Constants.EXTRA_EDIT_KEY;
import static cf.homeit.admintool.ExtendsClases.Constants.EXTRA_SWITCH_KEY;
import static cf.homeit.admintool.ExtendsClases.Constants.EXTRA_VLAN_KEY;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.deleteFromFirebase;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.showToast;

public abstract class AbstractSwitchFragment extends Fragment{
    private static final String TAG = "AbstractSwitchFragment";
    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]
    private FirebaseRecyclerAdapter<Switch, SwitchViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
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
        return inflater.inflate(R.layout.fragment_switch,container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (mDatabase == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            mDatabase = database.getReference();
            mDatabase.keepSynced(true);
        }
        newBtn = view.findViewById(R.id.switchAdd);
        newBtn.setOnClickListener(v -> {
            navController = Navigation.findNavController(requireActivity(), R.id.first_nav_host);
            navController.navigate(R.id.newWNSwitchFragment);
        });
        mRecycler = view.findViewById(R.id.switchList);
        mRecycler.setHasFixedSize(true);
//    }
//
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(requireActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Switch>()
                .setQuery(postsQuery, Switch.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Switch, SwitchViewHolder>(options) {

            @SuppressLint({"RestrictedApi", "NonConstantResourceId"})
            @Override
            protected void onBindViewHolder(@NonNull SwitchViewHolder holder, int position, @NonNull Switch model) {
                final DatabaseReference postRef = getRef(position);
                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                holder.itemView.setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putString(EXTRA_SWITCH_KEY, postKey);
                    bundle.putBoolean(EXTRA_EDIT_KEY, false);
                    navController = Navigation.findNavController(requireActivity(), R.id.first_nav_host);
                    navController.navigate(R.id.switchDetailFragment,bundle);
                });
                holder.itemView.setOnLongClickListener(v -> {
                    PopupMenu popup = new PopupMenu(holder.itemView.getContext(), holder.itemView);

                    popup.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) item -> {
                        Bundle bundle = new Bundle();
                        bundle.putString(EXTRA_SWITCH_KEY, postKey);
                        switch (item.getItemId()) {
                            case R.id.detail:
                                showToast(requireActivity().getApplicationContext(), "Detail: "+postKey);
                                //handle menu1 click
                                bundle.putBoolean(EXTRA_EDIT_KEY, false);
                                navController = Navigation.findNavController(requireActivity(), R.id.first_nav_host);
                                navController.navigate(R.id.switchDetailFragment,bundle);
                                break;
                            case R.id.edit:
                                showToast(requireActivity().getApplicationContext(), "Edit: "+postKey);
                                bundle.putBoolean(EXTRA_EDIT_KEY, true);
                                navController = Navigation.findNavController(requireActivity(), R.id.first_nav_host);
                                navController.navigate(R.id.switchDetailFragment,bundle);
                                //handle menu2 click
                                break;
                            case R.id.delete:
                                //handle menu3 click
                                deleteFromFirebase(postKey,"switches",requireActivity().getApplicationContext());
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
                // Bind Post to ViewHolder
                holder.bindToModel(model);
            }

            @Override
            public SwitchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new SwitchViewHolder(inflater.inflate(R.layout.item_switch, viewGroup, false));
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
