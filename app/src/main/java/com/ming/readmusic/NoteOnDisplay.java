package com.ming.readmusic;

import com.leff.midi.event.NoteOn;

public class NoteOnDisplay {
    public int noteDelta;
    public boolean isSharp;
    public String letter;
    public int noteNumber;

    public int dotNum;
    private long tick;
    private int noteValue;
    private int xPos;
    private int yPos;
    private boolean isBlackKey;
    private int keyDelta;
    public NoteColor color;
    private int octaveNum;

    public int GetDelta() {
        return this.noteDelta;
    }

    public boolean IsSharp() {
        return isSharp;
    }

    public int GetOctaveNum() { return octaveNum; }

    public int GetDotNum() { return dotNum; }

    public NoteOnDisplay(int noteDelta, boolean isSharp, String letter, int octaveNum, Scale scaleKey)
    {
        this.letter = letter;
        this.noteDelta = noteDelta;
        this.isSharp = isSharp;
        this.color = NoteColor.BLACK;
        this.octaveNum = octaveNum;

        if (scaleKey == Scale.CMajor) {
            this.dotNum = this.octaveNum - 4;
            if (letter.equals("C")) {
                this.noteNumber = 1;
            } else if (letter.equals("D")) {
                this.noteNumber = 2;
            } else if (letter.equals("E")) {
                this.noteNumber = 3;
            } else if (letter.equals("F")) {
                this.noteNumber = 4;
            } else if (letter.equals("G")) {
                this.noteNumber = 5;
            } else if (letter.equals("A")) {
                this.noteNumber = 6;
            } else if (letter.equals("B")) {
                this.noteNumber = 7;
            }
        } else if (scaleKey == Scale.DMajor){
            if (letter.equals("D")) {
                this.noteNumber = 1;
                this.dotNum = this.octaveNum - 4;
            } else if (letter.equals("E")) {
                this.noteNumber = 2;
                this.dotNum = this.octaveNum - 4;
            } else if (letter.equals("F#")) {
                this.noteNumber = 3;
                this.dotNum = this.octaveNum - 4;
            } else if (letter.equals("G")) {
                this.noteNumber = 4;
                this.dotNum = this.octaveNum - 4;
            } else if (letter.equals("A")) {
                this.noteNumber = 5;
                this.dotNum = this.octaveNum - 4;
            } else if (letter.equals("B")) {
                this.noteNumber = 6;
                this.dotNum = this.octaveNum - 4;
            } else if (letter.equals("C#")) {
                this.noteNumber = 7;
                this.dotNum = this.octaveNum - 5;
            }
        } else if (scaleKey == Scale.AMajor) {
            if (letter.equals("A")) {
                this.noteNumber = 1;
                this.dotNum = this.octaveNum - 4;
            } else if (letter.equals("B")) {
                this.noteNumber = 2;
                this.dotNum = this.octaveNum - 4;
            } else if (letter.equals("C#")) {
                this.noteNumber = 3;
                this.dotNum = this.octaveNum - 5;
            } else if (letter.equals("D")) {
                this.noteNumber = 4;
                this.dotNum = this.octaveNum - 5;
            } else if (letter.equals("E")) {
                this.noteNumber = 5;
                this.dotNum = this.octaveNum - 5;
            } else if (letter.equals("F#")) {
                this.noteNumber = 6;
                this.dotNum = this.octaveNum - 5;
            } else if (letter.equals("G#")) {
                this.noteNumber = 7;
                this.dotNum = this.octaveNum - 5;
            }
        } else {
            this.dotNum = 0;
            }
        }

    public int getNoteValue() {
        return noteValue;
    }

    public long getTick() {
        return tick;
    }

    public void init(NoteOn note) {
        this.noteValue = note.getNoteValue();
        this.tick = note.getTick();
    }

    public String getNoteFileName() {
        return letter.toLowerCase() + octaveNum;
    }

    public String toString() {
        if (isSharp) {
            return noteDelta + " " + letter + " " + "isSharp";
        } else {
            return noteDelta + " " + letter + "notSharp";
        }
    }
}
