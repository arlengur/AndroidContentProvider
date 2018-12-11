package ru.arlen.androidsqllite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import ru.arlen.androidsqllite.dblayer.DBManager;

public class SecondCreateNoteActivity extends Activity {
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        dbManager = new DBManager(this);

        final EditText title = findViewById(R.id.sTitle);
        final EditText content = findViewById(R.id.sContent);
        View create = findViewById(R.id.sCreateBtn);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!title.getText().toString().isEmpty() && !content.getText().toString().isEmpty()){
                    dbManager.addNote(title.getText().toString(), content.getText().toString());
                    startActivity(new Intent(SecondCreateNoteActivity.this, FirstMainActivity.class));
                }
            }
        });

        View cancel = findViewById(R.id.sCancelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondCreateNoteActivity.this, FirstMainActivity.class));
            }
        });
    }
}
