package com.ming.readmusic;

import java.util.ArrayList;

public class  SongStore {
    public SongStore() {
        ArrayList<Song> SongList = new ArrayList<>();
        SongList.add(new Song(1, "Fur Elise", "FUR_ELISE.MIDI", Scale.DMajor));

    }
}
