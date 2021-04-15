package cf.homeit.admintool.DataModels;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Note {
    public String noteId, noteTitle, noteContent, noteTime, noteAuthor;

    public Note() {
    }

    public Note(String noteId, String noteTitle, String noteContent, String noteTime, String noteAuthor) {
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteTime = noteTime;
        this.noteAuthor = noteAuthor;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("noteId", noteId);
        result.put("noteTitle", noteTitle);
        result.put("noteContent", noteContent);
        result.put("noteTime", noteTime);
        result.put("noteAuthor", noteAuthor);
        return result;
    }

}