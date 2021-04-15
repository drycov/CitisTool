package cf.homeit.admintool.ViewHolder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import cf.homeit.admintool.DataModels.Note;
import cf.homeit.admintool.R;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    public final MaterialTextView noteTitle, noteContent, noteDate, noteId;

    public NoteViewHolder(View itemView) {
        super(itemView);
        noteTitle = itemView.findViewById(R.id.noteTitle);
        noteContent = itemView.findViewById(R.id.noteContent);
        noteDate = itemView.findViewById(R.id.noteTime);
        noteId = itemView.findViewById(R.id.noteId);
    }

    public void bindToModel(Note model) {
        noteTitle.setText(model.noteTitle);
        noteContent.setText(model.noteContent);
        noteDate.setText(model.noteTime);
        noteId.setText(model.noteId);
    }
}
