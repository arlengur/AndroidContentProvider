package ru.arlen.androidsqllite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import ru.arlen.androidsqllite.dblayer.DBManager;
import ru.arlen.androidsqllite.model.Props;

import java.util.Arrays;

public class FourthSetupViewActivity extends Activity {
    private static final String[] COLORS = {"BLACK", "BLUE", "YELLOW", "RED"};
    private DBManager dbManager;
    private Props props;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_view);

        dbManager = new DBManager(this);
        props = dbManager.getProps();
        final EditText title = findViewById(R.id.fTextSize);
        title.setText(props.getSize());

        final Spinner dropdown = findViewById(R.id.fSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, COLORS);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(Arrays.asList(COLORS).indexOf(props.getColor()));

        final EditText size = findViewById(R.id.fTextSize);
        View save = findViewById(R.id.fSaveBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!size.getText().toString().isEmpty()){
                    props.setSize(size.getText().toString());
                    props.setColor(dropdown.getSelectedItem().toString());
                    dbManager.updateProps(props);
                    startActivity(new Intent(FourthSetupViewActivity.this, FirstMainActivity.class));
                }
            }
        });

        View cancel = findViewById(R.id.fCancelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FourthSetupViewActivity.this, FirstMainActivity.class));
            }
        });
    }
}
