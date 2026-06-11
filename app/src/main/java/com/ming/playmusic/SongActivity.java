package com.ming.playmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class SongActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        int songId = intent.getIntExtra("SongId", 0);
        //Toast.makeText(getApplicationContext(), "Press the correct key to move to the next note.", Toast.LENGTH_LONG).show();

        if (songId != 0) {
            CanvasView canvas = (CanvasView) findViewById(R.id.signature_canvas);
            canvas.SetSong(songId);
        } else {
            Intent myIntent = new Intent(this, SongListActivity.class);
            this.startActivity(myIntent);
        }
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
        canvas.PlaySong(false);
    }
    public void Listen(View view) {
        CanvasView canvas = (CanvasView) findViewById(R.id.signature_canvas);
        canvas.PlaySong(true);
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
