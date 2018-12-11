package ru.arlen.androidsqllite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ru.arlen.androidsqllite.dblayer.DBManager;

import static ru.arlen.androidsqllite.dblayer.DBManager.TITLE;

public class FirstMainActivity extends Activity {
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DBManager(this);

        final ListView titlesList = findViewById(R.id.fTitles);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, dbManager.getNotes());
        titlesList.setAdapter(adapter);
        titlesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FirstMainActivity.this, ThirdEditNoteActivity.class);
                intent.putExtra(TITLE, (String) titlesList.getItemAtPosition(position));
                startActivity(intent);
            }
        });

        View create = findViewById(R.id.fCreateBtn);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstMainActivity.this, SecondCreateNoteActivity.class));
            }
        });
        View setup = findViewById(R.id.fSetupBtn);
        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstMainActivity.this, FourthSetupViewActivity.class));
            }
        });
    }
}
