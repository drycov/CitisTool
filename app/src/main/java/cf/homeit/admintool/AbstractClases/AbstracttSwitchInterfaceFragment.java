package cf.homeit.admintool.AbstractClases;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import cf.homeit.admintool.DataModels.Port;
import cf.homeit.admintool.R;
import cf.homeit.admintool.ViewHolder.SwitchInterfaceViewHolder;

import static cf.homeit.admintool.ExtendsClases.Constants.EXTRA_SWITCH_KEY;
import static cf.homeit.admintool.ExtendsClases.SupportVoids.showToast;

public abstract class AbstracttSwitchInterfaceFragment extends Fragment{
    private static final String TAG = "AbstracttSwitchInterfaceFragment";
    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]
    private FirebaseRecyclerAdapter<Port, SwitchInterfaceViewHolder> mAdapter;
    private RecyclerView mRecycler;
    NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_port,container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (mDatabase == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            mDatabase = database.getReference();
            mDatabase.keepSynced(true);
        }
        FloatingActionButton newBtn = view.findViewById(R.id.portAdd);
        newBtn.setOnClickListener(v -> {
//            navController = Navigation.findNavController(requireActivity(), R.id.first_nav_host);
//            navController.navigate(R.id.newWNSwitchFragment);
        });
        mRecycler = view.findViewById(R.id.portList);
        mRecycler.setHasFixedSize(true);
    }
//
//
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        GridLayoutManager  mManager = new GridLayoutManager(requireActivity(),2);
//        mManager.setReverseLayout(true);
//        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Port>()
                .setQuery(postsQuery, Port.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Port, SwitchInterfaceViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull SwitchInterfaceViewHolder holder, int position, @NonNull Port model) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                holder.itemView.setOnLongClickListener((View.OnLongClickListener) view -> {
                    PopupMenu popup = new PopupMenu(requireContext().getApplicationContext(), holder.itemView);
                    popup.inflate(R.menu.options_menu);
                    popup.setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()) {
                            case R.id.detail:
                                showToast(requireActivity().getApplicationContext(), "Detail");
                                //handle menu1 click
                                break;
                            case R.id.edit:
                                showToast(requireActivity().getApplicationContext(), "Edit");

                                //handle menu2 click
                                break;
                            case R.id.delete:
                                showToast(requireActivity().getApplicationContext(), "Delete");

                                //handle menu3 click
                                break;
                        }
                        return false;
                    });
                    popup.show();
                    return true;
                });
                holder.itemView.setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putString(EXTRA_SWITCH_KEY, postKey);
//                    navController = Navigation.findNavController(requireActivity(), R.id.first_nav_host);
//                    navController.navigate(R.id.switchDetailFragment,bundle);
                });
                // Bind Post to ViewHolder
                holder.bindToModel(model);
            }

            @NonNull
            @Override
            public SwitchInterfaceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new SwitchInterfaceViewHolder(inflater.inflate(R.layout.item_switch, viewGroup, false));
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