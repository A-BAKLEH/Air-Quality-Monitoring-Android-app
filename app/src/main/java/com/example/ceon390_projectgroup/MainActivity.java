package com.example.ceon390_projectgroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(this, SettingsActivity.class);
                startActivity(intent1);
                return true;
            case R.id.action_developers:
                Toast.makeText(this, "Developers", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(this, DevelopersActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openLive(View view) {
        Toast.makeText(this, "Live", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LiveActivity.class);
        startActivity(intent);
    }

    public void openDatabase(View view) {
        Toast.makeText(this, "Database", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DatabaseActivity.class);
        startActivity(intent);
    }

}