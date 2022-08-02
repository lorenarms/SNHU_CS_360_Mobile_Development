package com.zybooks.db_with_login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    EditText title, description_input, date_input;
    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title = findViewById(R.id.title_input);
        description_input = findViewById(R.id.description_input);
        date_input = findViewById(R.id.date_input);
        add_button = findViewById(R.id.add_new);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main_DBHelper myDB = new Main_DBHelper(AddActivity.this);
                myDB.addEmployee(title.getText().toString().trim(),
                        description_input.getText().toString().trim(),
                        date_input.getText().toString().trim());

                finish();

            }
        });





    }
}