package ru.arlen.androidsqllite;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import ru.arlen.androidsqllite.dblayer.DBManager;
import ru.arlen.androidsqllite.model.Note;
import ru.arlen.androidsqllite.model.Props;

import static ru.arlen.androidsqllite.dblayer.DBManager.TITLE;

public class ThirdEditNoteActivity extends Activity {
    private DBManager dbManager;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        dbManager = new DBManager(this);
        Props props = dbManager.getProps();

        String titleParam = getIntent().getStringExtra(TITLE);
        note = dbManager.getNote(titleParam);
        final EditText title = findViewById(R.id.tTitle);
        title.setText(note.getTitle());
        final EditText content = findViewById(R.id.tContent);
        content.setText(note.getContent());

        // Text style
        content.setTextSize(Float.parseFloat(props.getSize()));
        content.setTextColor(Color.parseColor(props.getColor()));

        View update = findViewById(R.id.tUpdateBtn);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!title.getText().toString().isEmpty() && !content.getText().toString().isEmpty()){
                    note.setTitle(title.getText().toString());
                    note.setContent(content.getText().toString());
                    dbManager.updateNote(note);
                    startActivity(new Intent(ThirdEditNoteActivity.this, FirstMainActivity.class));
                }
            }
        });

        View cancel = findViewById(R.id.tCancelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThirdEditNoteActivity.this, FirstMainActivity.class));
            }
        });
    }
}
