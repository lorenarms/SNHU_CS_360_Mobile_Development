package com.zybooks.db_with_login;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateActivity extends AppCompatActivity {

    EditText event_title, event_description;
    Button update_button, delete_button, time_button, date_button;
    String alarm_generator;

    String id, title, description, date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        event_title = findViewById(R.id.title_input_update);
        event_description = findViewById(R.id.description_input_update);
        time_button = findViewById(R.id.btn_Date_Update);
        date_button = findViewById(R.id.btn_Time_Update);
        update_button = findViewById(R.id.add_new_update);
        delete_button = findViewById(R.id.edit_button);

        getAndSetIntentData();

        time_button.setOnClickListener(view -> {
            selectTime();
        });

        date_button.setOnClickListener(view -> selectDate());

        update_button.setOnClickListener(view -> {

            Main_DBHelper myDB = new Main_DBHelper(UpdateActivity.this);
            myDB.updateData(id, event_title.getText().toString().trim(),
                    event_description.getText().toString().trim(),
                    date_button.getText().toString().trim(),
                    time_button.getText().toString().trim());
            finish();

        });
        delete_button.setOnClickListener(view -> confirmDialog());

    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("title") &&
                getIntent().hasExtra("description") && getIntent().hasExtra("date") &&
                getIntent().hasExtra("time")){

            // getting data from intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            description = getIntent().getStringExtra("description");
            date = getIntent().getStringExtra("date");
            time = getIntent().getStringExtra("time");


            // setting intent data
            event_title.setText(title);
            event_description.setText(description);
            date_button.setText(date);
            time_button.setText(time);

        }else{
            Toast.makeText(this, "No data to update", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + title + "?");
        builder.setMessage("Are you sure you want to delete " + title + "?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            Main_DBHelper myDB = new Main_DBHelper(UpdateActivity.this);
            myDB.deleteOneRow(id);
            finish();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {

        });
        builder.create().show();

    }

    private void selectTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, i, i1) -> {
            alarm_generator = i + ":" + i1;
            time_button.setText(formatTime(i, i1));
        }, hour, minute, false);
        timePickerDialog.show();
    }

    private void selectDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this::onDateSet, year, month, day);
        datePickerDialog.show();
    }

    public String formatTime(int hour, int minute) {

        String time;
        String minutes_reformatted;

        if (minute / 10 == 0) {
            minutes_reformatted = "0" + minute;
        } else {
            minutes_reformatted = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + minutes_reformatted + " AM";
        } else if (hour < 12) {
            time = hour + ":" + minutes_reformatted + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + minutes_reformatted + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + minutes_reformatted + " PM";
        }


        return time;
    }

    @SuppressLint({"UnspecifiedImmutableFlag", "SimpleDateFormat"})
    private void setAlarm(String text, String date, String time) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmBrodcast.class);
        intent.putExtra("event", text);
        intent.putExtra("time", date);
        intent.putExtra("date", time);

        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateAndTime = date + " " + alarm_generator;
        DateFormat formatter;
        formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateAndTime);
            assert date1 != null;
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
            Toast.makeText(getApplicationContext(), "Alarm", Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Intent intentBack = new Intent(getApplicationContext(), MainActivity.class);
        intentBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentBack);

    }

    @SuppressLint("SetTextI18n")
    private void onDateSet(DatePicker datePicker, int year1, int month1, int day1) {
        date_button.setText(String.format("%d-%d-%d", day1, month1 + 1, year1));
    }
}