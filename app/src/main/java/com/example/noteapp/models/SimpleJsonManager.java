package com.example.noteapp.models;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SimpleJsonManager {

    private static final String FILE_NAME = "notes.json";
    private Context context;
    private Gson gson;

    public SimpleJsonManager(Context context) {
        this.context = context;
        this.gson = new Gson();
    }

    public boolean saveNote(List<Note> notes) {
        try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                OutputStreamWriter osw = new OutputStreamWriter(fos)) {
            String json = gson.toJson(notes);
            osw.write(json);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("SimpleJsonManger", "Error saving notes", e);
            return false;
        }
    }

    public List<Note> loadNotes() {
        List<Note> notes = new ArrayList<>();
        try (FileInputStream fis = context.openFileInput(FILE_NAME);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader reader = new BufferedReader(isr)) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line);
            Type listType = new TypeToken<List<Note>>() {
            }.getType();
            notes = gson.fromJson(sb.toString(), listType);

        } catch (FileNotFoundException e) {
            // TODO: handle exception
            Log.d("SimpleJsonManger", "No existing notes file");
        } catch (IOException e) {
            Log.e("SimpleJsonManger", "Error loading notes", e);
        }
        return notes != null ? notes : new ArrayList<>();
    }

    public boolean addNote(Note note) {
        List<Note> notes = loadNotes();
        notes.add(note);
        return saveNote(notes);
    }

    public boolean deleteNote(String noteId) {
        List<Note> notes = loadNotes();
        boolean removed = notes.removeIf(n -> n.getId().equals(noteId));
        return removed && saveNote(notes);
    }

    public List<Note> createSampleNotes() {
        List<Note> sample = new ArrayList<>();
        sample.add(new Note("test1", "test1test1test1"));
        Note meeting = new Note("test2", "test2test2test2");
        meeting.toggleImportant();
        sample.add(meeting);
        saveNote(sample);
        return sample;
    }
    public boolean toggleImportant(String noteId) {
        List<Note> notes = loadNotes();
        for (Note n : notes) {
            if (n.getId().equals(noteId)) {
                n.toggleImportant();
                return saveNote(notes);
            }
        }
        return false;
    }

}
