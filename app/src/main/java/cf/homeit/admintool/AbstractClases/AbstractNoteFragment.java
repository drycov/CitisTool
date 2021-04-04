package cf.homeit.admintool.AbstractClases;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import cf.homeit.admintool.DataModels.Note;
import cf.homeit.admintool.R;
import cf.homeit.admintool.ViewHolder.NoteViewHolder;

import static cf.homeit.admintool.ExtendsClases.Constants.EXTRA_NOTE_KEY;

public abstract class AbstractNoteFragment extends Fragment{
    private static final String TAG = "NoteFragment";
    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]
    private FirebaseRecyclerAdapter<Note, NoteViewHolder> mAdapter;
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
        return inflater.inflate(R.layout.fragment_note,container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (mDatabase == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            mDatabase = database.getReference();
            mDatabase.keepSynced(true);
        }
        newBtn = view.findViewById(R.id.noteAdd);
        newBtn.setOnClickListener(v -> {
            navController = Navigation.findNavController(requireActivity(), R.id.first_nav_host);
            navController.navigate(R.id.newNoteFragment);
        });
        mRecycler = view.findViewById(R.id.noteList);
        mRecycler.setHasFixedSize(true);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//         Set up Layout Manager, r/everse layout
        mManager = new LinearLayoutManager(requireActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Note>()
                .setQuery(postsQuery, Note.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Note, NoteViewHolder>(options) {

            @Override
            public NoteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new NoteViewHolder(inflater.inflate(R.layout.item_note, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(NoteViewHolder viewHolder, int position, final Note model) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                viewHolder.itemView.setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putString(EXTRA_NOTE_KEY, postKey);
                    navController = Navigation.findNavController(requireActivity(), R.id.first_nav_host);
                    navController.navigate(R.id.noteDetailFragment,bundle);
                });
                // Bind Post to ViewHolder
                viewHolder.bindToModel(model);
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
