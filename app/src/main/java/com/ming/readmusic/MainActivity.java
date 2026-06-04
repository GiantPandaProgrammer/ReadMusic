package com.ming.readmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        int value = intent.getIntExtra("SongId", 0);
        //Toast.makeText(getApplicationContext(), "Press the correct key to move to the next note.", Toast.LENGTH_LONG).show();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void setTreble(View view) {
        CanvasView canvas = (CanvasView) findViewById(R.id.signature_canvas);
        canvas.SetTreble();
    }

    public void setBass(View view) {
        CanvasView canvas = (CanvasView) findViewById(R.id.signature_canvas);
        canvas.SetBass();
    }

    public void ShowHint(View view) {
        CanvasView canvas = (CanvasView) findViewById(R.id.signature_canvas);
        canvas.ShowHint();
    }

    public void PlaySong(View view) {
        CanvasView canvas = (CanvasView) findViewById(R.id.signature_canvas);
        canvas.PlaySong();
    }
    public void Refresh(View view) {
        CanvasView canvas = (CanvasView) findViewById(R.id.signature_canvas);
        canvas.Refresh();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CanvasView canvas = (CanvasView) findViewById(R.id.signature_canvas);
        switch (item.getItemId()) {
            case R.id.treble_action:
                canvas.SetTreble();
                return true;
            case R.id.bass_action:
                canvas.SetBass();
                return true;
            case R.id.both_action:
                canvas.SetBoth();
                return true;
            case R.id.c_major_scale:
                canvas.SetCMajorScale();
                return true;
            case R.id.d_major_scale:
                canvas.SetDMajorScale();
                return true;
            case R.id.a_major_scale:
                canvas.SetAMajorScale();
                return true;
            case R.id.staff_system:
                canvas.SetStaff();
                return true;
            case R.id.number_system:
                canvas.SetNumber();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
