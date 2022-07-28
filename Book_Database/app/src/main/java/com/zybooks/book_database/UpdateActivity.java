package com.zybooks.book_database;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    EditText employee_name, employee_position, employee_phone;
    Button update_button;

    String id, name, position, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);



        employee_name = findViewById(R.id.name_input_update);
        employee_position = findViewById(R.id.pos_input_update);
        employee_phone = findViewById(R.id.phone_input_update);
        update_button = findViewById(R.id.add_new_update);

        getAndSetIntentData();

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                MyDatabase myDB = new MyDatabase(UpdateActivity.this);

                myDB.updateData(id, employee_name.getText().toString().trim(),
                        employee_position.getText().toString().trim(),
                        employee_phone.getText().toString().trim());

            }
        });

    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("name") &&
                getIntent().hasExtra("position") && getIntent().hasExtra("phone")){

            // getting data from intent
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            position = getIntent().getStringExtra("position");
            phone = getIntent().getStringExtra("phone");

            // setting intent data
            employee_name.setText(name);
            employee_position.setText(position);
            employee_phone.setText(phone);

        }else{
            Toast.makeText(this, "No data to update", Toast.LENGTH_SHORT).show();
        }
    }
}