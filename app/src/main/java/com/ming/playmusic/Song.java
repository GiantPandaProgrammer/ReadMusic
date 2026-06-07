package com.ming.playmusic;

public class Song {
    public int Id;
    public String Name;
    public String FileName;
    public Scale Scale;
    public Song(int Id, String Name, String FileName, Scale Scale) {
        this.Id = Id;
        this.Name = Name;
        this.FileName = FileName;
        this.Scale = Scale;
    }
}
