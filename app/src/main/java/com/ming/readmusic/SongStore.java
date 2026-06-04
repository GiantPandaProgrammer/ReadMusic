package com.ming.readmusic;

import java.util.ArrayList;

public class  SongStore {
    public ArrayList<Song> Songs = new ArrayList<>();
    public SongStore() {
        ArrayList<Song> SongList = new ArrayList<>();
        SongList.add(new Song(1, "Fur Elise", "FUR_ELISE.mid", Scale.DMajor));
        SongList.add(new Song(2, "Clair De Lune", "CLAIR_DE_LUNE.mid", Scale.GMajor));
        SongList.add(new Song(3, "Gymnopedie", "GYMNOPEDIE.mid", Scale.CMajor));
        SongList.add(new Song(3, "Minuet in G", "MINUET_IN_G.mid", Scale.GMajor));
        SongList.add(new Song(3, "Moonlight Sonata", "MOONLIGHT_SONATA.mid", Scale.CMajor));
        SongList.add(new Song(3, "Turkish March", "TURKISH_MARCH.mid", Scale.CMajor));

        Songs = SongList;
    }

    public Song GetSong(int SongId) {
        Song song = Songs.stream().filter(s -> s.Id == SongId).findFirst().get();
        return song;
    }
}
