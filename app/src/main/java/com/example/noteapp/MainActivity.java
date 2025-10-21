package com.example.noteapp;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.noteapp.models.Note;
import com.example.noteapp.models.SimpleJsonManger;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SimpleJsonManager jsonManger;
    private List<Note> notes;
    private ArrayAdapter<Note> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        jsonManger = new SimpleJsonManager(this);
        notes = jsonManger.loadNotes();

        ListView listView = findViewById(R.id.listViewNotes);
        Button btnNewNote = findViewById(R.id.buttonNewNote);
        Button btnAddSample = findViewById(R.id.buttonAddSample);
        Button btnClearAll = findViewById(R.id.buttonClearAll);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(adapter);

        btnNewNote.setOnClickListener(v -> showNewNoteDialog());

        btnAddSample.setOnClickListener(v -> {
            notes = jsonManger.createSampleNotes();
            refreshList();
        });

        btnClearAll.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmer")
                    .setMessage("Voulew-vous vraiment tout effacer ?")
                    .setPositiveButton("oui", (dialog, which) -> {
                        notes.clear();
                        jsonManger.saveNotes(notes);
                        refreshList();
                    })
                    .setNegativeButton("Non", null)
                    .show();
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Note note = notes.get(position);
            new AlertDialog.Builder(this)
                    .setTitle(note.getTitle())
                    .setMessage(note.getTitle())
                    .setPositiveButton("OK", null)
                    .show();
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Note note = notes.get(position);
            new AlertDialog.Builder(this)
                    .setTitle(note.getTitle())
                    .setItems(new CharSequence[] { "Toggle Important", "Supprimer" }, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                jsonManger.toggleImportant(note.getId());
                                notes = jsonManger.loadNotes();
                                refreshList();
                                break;
                            case 1:
                                jsonManger.deleteNote(note.getId());
                                notes = jsonManger.loadNotes();
                                refreshList();
                                break;
                        }
                    }).show();
            return true;
        });
    }

    private void showNewNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nounvelle Note");

        View view = getLayoutInflater().inflate(R.layout.dialog_new_note, null);
        EditText etTitle = view.findViewById(R.id.etTitle);
        EditText etContent = view.findViewById(R.id.etContent);

        builder.setView(view);

        builder.setPositiveButton("Ajouter", (dialog, which) -> {
            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();

            if (!title.isEmpty() && !content.isEmpty()) {
                Note note = new Note(title, content);
                jsonManger.addNote(note);
                notes = jsonManger.loadNotes();
                refreshList();
            } else {
                Toast.makeText(this, "Veuiller remplir le titre et le contenu", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Anuuler", null);
        builder.show();
    }

    private void refreshList() {
        adapter.clear();
        adapter.addAll(notes);
        adapter.notifyDataSetChanged();
    }

}
