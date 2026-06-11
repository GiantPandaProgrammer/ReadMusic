package com.ming.playmusic;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SongListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);


        CustomAdapter customAdapter = new CustomAdapter(new SongStore().Songs, this);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        // Set LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(customAdapter);






    }
}