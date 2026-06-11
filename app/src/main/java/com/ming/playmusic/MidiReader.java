package com.ming.playmusic;

import android.util.Log;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOn;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class MidiReader {

    private ArrayList<NoteOn> notes;

    public MidiReader(InputStream input) {

        // 1. Open up a MIDI file
        MidiFile mf = null;
        //File input = new File("src/main/assets/HAPPY_BIRTHDAY.mid");

        try {
            mf = new MidiFile(input);
        } catch (IOException e) {
            System.err.println("Error parsing MIDI file:");
            e.printStackTrace();
            return;
        }

        // 2. Do some editing to the file
        // 2a. Strip out anything but notes from track 1
        notes = new ArrayList<NoteOn>();

        for (int i = 0; i < mf.getTrackCount(); i++) {

            MidiTrack T = mf.getTracks().get(i);

            // It's a bad idea to modify a set while iterating, so we'll collect
            // the events first, then remove them afterwards
            Iterator<MidiEvent> it = T.getEvents().iterator();
            //ArrayList<Long> ticks = new ArrayList<Long>();
            //ArrayList<Double> beats = new ArrayList<Double>();

            while (it.hasNext()) {
                MidiEvent E = it.next();

                if (E.getClass().equals(NoteOn.class)) {
                    NoteOn note = (NoteOn) E;
                    if (note.getVelocity() != 0) {
                        notes.add(note);
                    }
                    //beats.add((double) (note.getTick() / 480));
                    //notes.add(note.getNoteValue());
                }
            }
        }
    }

    public ArrayList<NoteOn> GetNotes() {
        return this.notes;
    }

    public static NoteOnDisplay GetNoteDisplay(NoteOn note, Scale scaleKey) {
        //https://www.inspiredacoustics.com/en/MIDI_note_numbers_and_center_frequencies
        // C# is norminal in D major and not a sharp
        switch (note.getNoteValue()) {
            case 36: //C2
                return new NoteOnDisplay(-14, false, "C", 2, scaleKey);
            case 37: //C#2
                return new NoteOnDisplay(-14, true, "C#", 2, scaleKey);
            case 38: //D2
                return new NoteOnDisplay(-13, false, "D", 2, scaleKey);
            case 39: //D#2
                return new NoteOnDisplay(-13, true, "D#", 2, scaleKey);
            case 40: //E2
                return new NoteOnDisplay(-12, false, "E", 2, scaleKey);
            case 41: //F2
                return new NoteOnDisplay(-11, false, "F", 2, scaleKey);
            case 42: //F#2
                return new NoteOnDisplay(-11, true, "F#", 2, scaleKey);
            case 43: //G2
                return new NoteOnDisplay(-10, false, "G", 2, scaleKey);
            case 44: //G#2
                return new NoteOnDisplay(-10, true, "G#", 2, scaleKey);
            case 45: //A2
                return new NoteOnDisplay(-9, false, "A", 2, scaleKey);
            case 46: //A#2
                return new NoteOnDisplay(-9, true, "A#", 2, scaleKey);
            case 47: //B2
                return new NoteOnDisplay(-8, false, "B", 2, scaleKey);
            case 48: //C3
                return new NoteOnDisplay(-7, false, "C", 3, scaleKey);
            case 49: //C#3
                return new NoteOnDisplay(-7, true, "C#", 3, scaleKey);
            case 50: //D3
                return new NoteOnDisplay(-6, false, "D", 3, scaleKey);
            case 51: //D#3
                return new NoteOnDisplay(-6, true, "D#", 3, scaleKey);
            case 52: //E3
                return new NoteOnDisplay(-5, false, "E", 3, scaleKey);
            case 53: //F3
                return new NoteOnDisplay(-4, false, "F", 3, scaleKey);
            case 54: //F#3
                return new NoteOnDisplay(-4, true, "F#", 3, scaleKey);
            case 55: //G3
                return new NoteOnDisplay(-3, false, "G", 3, scaleKey);
            case 56: //G#3
                return new NoteOnDisplay(-3, true, "G#", 3, scaleKey);
            case 57: //A3
                return new NoteOnDisplay(-2, false, "A", 3, scaleKey);
            case 58: //A#3
                return new NoteOnDisplay(-2, true, "A#", 3, scaleKey);
            case 59: //B3
                return new NoteOnDisplay(-1, false, "B", 3, scaleKey);
            case 60: //C4 Middle C
                return new NoteOnDisplay(0, false, "C", 4, scaleKey);
            case 61: //C#4
                return new NoteOnDisplay(0, true, "C#", 4, scaleKey);
            case 62: //D4
                return new NoteOnDisplay(1, false, "D", 4, scaleKey);
            case 63: //D#4
                return new NoteOnDisplay(1, true, "D#", 4, scaleKey);
            case 64: //E4
                return new NoteOnDisplay(2, false, "E", 4, scaleKey);
            case 65: //F4
                return new NoteOnDisplay(3, false, "F", 4, scaleKey);
            case 66: //F#4
                return new NoteOnDisplay(3, true, "F#", 4, scaleKey);
            case 67: //G4
                return new NoteOnDisplay(4, false, "G", 4, scaleKey);
            case 68: //G#4
                return new NoteOnDisplay(4, true, "G#", 4, scaleKey);
            case 69: //A4
                return new NoteOnDisplay(5, false, "A", 4, scaleKey);
            case 70: //A#4
                return new NoteOnDisplay(5, true, "A#", 4, scaleKey);
            case 71: //B4
                return new NoteOnDisplay(6, false, "B", 4, scaleKey);
            case 72: //C5
                return new NoteOnDisplay(7, false, "C", 5, scaleKey);
            case 73: //C#5
                return new NoteOnDisplay(7, true, "C#", 5, scaleKey);
            case 74: //D5
                return new NoteOnDisplay(8, false, "D", 5, scaleKey);
            case 75: //D#5
                return new NoteOnDisplay(8, true, "D#", 5, scaleKey);
            case 76: //E5
                return new NoteOnDisplay(9, false, "E", 5, scaleKey);
            case 77: //F5
                return new NoteOnDisplay(10, false, "F", 5, scaleKey);
            case 78: //F#5
                return new NoteOnDisplay(10, true, "F#", 5, scaleKey);
            case 79: //G5
                return new NoteOnDisplay(11, false, "G", 5, scaleKey);
            case 80: //G#5
                return new NoteOnDisplay(11, true, "G#", 5, scaleKey);
            case 81: //A5
                return new NoteOnDisplay(12, false, "A", 5, scaleKey);
            case 82: //A#5
                return new NoteOnDisplay(12, true, "A#", 5, scaleKey);
            case 83: //B5
                return new NoteOnDisplay(13, false, "B", 5, scaleKey);
            case 84: //C6
                return new NoteOnDisplay(14, false, "C", 6, scaleKey);
            case 85: //C6#
                return new NoteOnDisplay(14, true, "C#", 6, scaleKey);
            case 86: //D6
                return new NoteOnDisplay(15, false, "D", 6, scaleKey);
            case 87: //D6#
                return new NoteOnDisplay(15, true, "D#", 6, scaleKey);
            case 88: //E6
                return new NoteOnDisplay(16, false, "E", 6, scaleKey);
            case 89: //F6
                return new NoteOnDisplay(17, false, "F", 6, scaleKey);
            case 90: //F6#
                return new NoteOnDisplay(17, true, "F#", 6, scaleKey);
            case 91: //G6
                return new NoteOnDisplay(18, false, "G", 6, scaleKey);
            case 92: //G6#
                return new NoteOnDisplay(18, true, "G#", 6, scaleKey);
            case 93: //A6
                return new NoteOnDisplay(19, false, "A", 6, scaleKey);
            case 94: //A6#
                return new NoteOnDisplay(19, true, "A#", 6, scaleKey);
            case 95: //B6
                return new NoteOnDisplay(20, false, "B", 6, scaleKey);
            default:
                Log.d("Missing Note", Integer.toString(note.getNoteValue()));
                return new NoteOnDisplay(0, false, "", 0, scaleKey);
        }
    }

    public static ArrayList<NoteOnDisplay> GetAllNotes(Scale scaleKey) {
        ArrayList<Integer> notes = new ArrayList<>();
        notes.add(36);
        notes.add(37);
        notes.add(38);
        notes.add(39);
        notes.add(40);
        notes.add(41);
        notes.add(42);
        notes.add(43);
        notes.add(44);
        notes.add(45);
        notes.add(46);
        notes.add(47);
        notes.add(48);
        notes.add(49);
        notes.add(50);
        notes.add(51);
        notes.add(52);
        notes.add(53);
        notes.add(54);
        notes.add(55);
        notes.add(56);
        notes.add(57);
        notes.add(58);
        notes.add(59);
        notes.add(60);
        notes.add(61);
        notes.add(62);
        notes.add(63);
        notes.add(64);
        notes.add(65);
        notes.add(66);
        notes.add(67);
        notes.add(68);
        notes.add(69);
        notes.add(70);
        notes.add(71);
        notes.add(72);
        notes.add(73);
        notes.add(74);
        notes.add(75);
        notes.add(76);
        notes.add(77);
        notes.add(78);
        notes.add(79);
        notes.add(80);
        notes.add(81);
        notes.add(82);
        notes.add(83);
        notes.add(84);
        notes.add(85);
        notes.add(86);
        notes.add(87);
        notes.add(88);
        notes.add(89);
        notes.add(90);
        notes.add(91);
        notes.add(92);
        notes.add(93);
        notes.add(94);
        notes.add(95);

        ArrayList<NoteOnDisplay> allNotesDisplay = new ArrayList<>();
        for (int i = 0; i < notes.size(); i++)
        {
            allNotesDisplay.add(GetNoteDisplay(new NoteOn(0, 1, notes.get(i), 100), scaleKey));
        }

        return allNotesDisplay;
    }

    public static ArrayList<Integer> GetCTrebleNotes() {
        ArrayList<Integer> notes = new ArrayList<Integer>();

        notes.add(60); //C4 Middle C
        notes.add(62); //D4
        notes.add(64); //E4
        notes.add(65); //F4
        notes.add(67); //G4
        notes.add(69); //A4
        notes.add(71); //B4
        notes.add(72); //C5
        notes.add(74); //D5
        notes.add(76); //E5
        notes.add(77); //F5
        notes.add(79); //G5
        notes.add(81); //A5
        notes.add(83); //B5
        notes.add(84); //C5
        return notes;
    }

    public static ArrayList<Integer> GeCBassNotes() {
        ArrayList<Integer> notes = new ArrayList<Integer>();
        notes.add(41); //F2
        notes.add(43); //G2
        notes.add(45); //A2
        notes.add(47); //B2
        notes.add(48); //C3
        notes.add(50); //D3
        notes.add(52); //E3
        notes.add(53); //F3
        notes.add(55); //G3
        notes.add(57); //A3
        notes.add(59); //B3
        notes.add(60); //C4 Middle C
        return notes;
    }

    public static ArrayList<Integer> GetBothCNotes() {
        ArrayList<Integer> notes = new ArrayList<Integer>();
        notes.addAll(GetCTrebleNotes());
        notes.addAll(GeCBassNotes());
        /*notes.add(41); //F2
        notes.add(43); //G2
        notes.add(45); //A2
        notes.add(47); //B2
        notes.add(48); //C3
        notes.add(50); //D3
        notes.add(52); //E3
        notes.add(53); //F3
        notes.add(55); //G3
        notes.add(57); //A3
        notes.add(59); //B3
        notes.add(60); //C4 Middle C
        notes.add(62); //D4
        notes.add(64); //E4
        notes.add(65); //F4
        notes.add(67); //G4
        notes.add(69); //A4
        notes.add(71); //B4
        notes.add(72); //C5
        notes.add(74); //D5
        notes.add(76); //E5
        notes.add(77); //F5
        notes.add(79); //G5
        notes.add(81); //A5
        notes.add(83); //B5
        notes.add(84); //C5*/

        return notes;
    }

    public static ArrayList<Integer> GetCMajorNotes(Clef clef) {
        ArrayList<Integer> notes = new ArrayList<Integer>();
        if (clef == Clef.Treble) {
            notes = GetCTrebleNotes();
        } else if (clef == Clef.Bass) {
            notes = GeCBassNotes();
        } else {
            notes = GetBothCNotes();
        }

        return notes;
    }

    public static ArrayList<Integer> GetDTrebleNotes() {
        ArrayList<Integer> notes = new ArrayList<Integer>();

        notes.add(61); //C4S Middle C
        notes.add(62); //D4
        notes.add(64); //E4
        notes.add(66); //F4S
        notes.add(67); //G4
        notes.add(69); //A4
        notes.add(71); //B4
        notes.add(73); //C5S
        notes.add(74); //D5
        notes.add(76); //E5
        notes.add(78); //F5S
        notes.add(79); //G5
        notes.add(81); //A5
        notes.add(83); //B5
        notes.add(85); //C5S
        return notes;
    }

    public static ArrayList<Integer> GeDBassNotes() {
        ArrayList<Integer> notes = new ArrayList<Integer>();
        notes.add(42); //F2S
        notes.add(43); //G2
        notes.add(45); //A2
        notes.add(47); //B2
        notes.add(49); //C3S
        notes.add(50); //D3
        notes.add(52); //E3
        notes.add(54); //F3S
        notes.add(55); //G3
        notes.add(57); //A3
        notes.add(59); //B3
        notes.add(61); //C4S Middle C
        return notes;
    }

    public static ArrayList<Integer> GetDMajorBothNotes() {
        ArrayList<Integer> notes = new ArrayList<Integer>();
        notes.addAll(GetDTrebleNotes());
        notes.addAll(GeDBassNotes());
        return notes;
    }

    public static ArrayList<Integer> GetDMajorNotes(Clef clef) {
        ArrayList<Integer> notes = new ArrayList<Integer>();
        if (clef == Clef.Treble) {
            notes = GetDTrebleNotes();
        } else if (clef == Clef.Bass) {
            notes = GeDBassNotes();
        } else {
            notes = GetDMajorBothNotes();
        }

        return notes;
    }

    public static ArrayList<Integer> GetAMajorTrebleNotes() {
        ArrayList<Integer> notes = new ArrayList<Integer>();

        notes.add(61); //C4S Middle C
        notes.add(62); //D4
        notes.add(64); //E4
        notes.add(66); //F4S
        notes.add(68); //G4S
        notes.add(69); //A4
        notes.add(71); //B4
        notes.add(73); //C5S
        notes.add(74); //D5
        notes.add(76); //E5
        notes.add(78); //F5S
        notes.add(80); //G5S
        notes.add(81); //A5
        notes.add(83); //B5
        notes.add(85); //C5S
        return notes;
    }

    public static ArrayList<Integer> GeAMajorBassNotes() {
        ArrayList<Integer> notes = new ArrayList<Integer>();
        notes.add(42); //F2S
        notes.add(44); //G2S
        notes.add(45); //A2
        notes.add(47); //B2
        notes.add(49); //C3S
        notes.add(50); //D3
        notes.add(52); //E3
        notes.add(54); //F3S
        notes.add(56); //G3S
        notes.add(57); //A3
        notes.add(59); //B3
        notes.add(61); //C4S Middle C
        return notes;
    }

    public static ArrayList<Integer> GetAMajorBothNotes() {
        ArrayList<Integer> notes = new ArrayList<Integer>();
        notes.addAll(GetAMajorTrebleNotes());
        notes.addAll(GetAMajorTrebleNotes());
        return notes;
    }

    public static ArrayList<Integer> GetGMajorTrebleNotes() {
        ArrayList<Integer> notes = new ArrayList<Integer>();

        notes.add(60); //C4 Middle C
        notes.add(62); //D4
        notes.add(64); //E4
        notes.add(66); //F4S
        notes.add(67); //G4
        notes.add(69); //A4
        notes.add(71); //B4
        notes.add(72); //C5
        notes.add(74); //D5
        notes.add(76); //E5
        notes.add(78); //F5S
        notes.add(79); //G5
        notes.add(81); //A5
        notes.add(83); //B5
        notes.add(84); //C5
        return notes;
    }

    public static ArrayList<Integer> GetGMajorBassNotes() {
        ArrayList<Integer> notes = new ArrayList<Integer>();
        notes.add(42); //F2S
        notes.add(43); //G2
        notes.add(45); //A2
        notes.add(47); //B2
        notes.add(48); //C3
        notes.add(50); //D3
        notes.add(52); //E3
        notes.add(54); //F3S
        notes.add(55); //G3
        notes.add(57); //A3
        notes.add(59); //B3
        notes.add(60); //C4 Middle C
        return notes;
    }

    public static ArrayList<Integer> GetGMajorBothNotes() {
        ArrayList<Integer> notes = new ArrayList<Integer>();
        notes.addAll(GetGMajorTrebleNotes());
        notes.addAll(GetGMajorTrebleNotes());
        return notes;
    }

    public static ArrayList<Integer> GetAMajorNotes(Clef clef) {
        ArrayList<Integer> notes = new ArrayList<Integer>();
        if (clef == Clef.Treble) {
            notes = GetAMajorTrebleNotes();
        } else if (clef == Clef.Bass) {
            notes = GeAMajorBassNotes();
        } else {
            notes = GetAMajorBothNotes();
        }

        return notes;
    }

    public static ArrayList<Integer> GetGMajorNotes(Clef clef) {
        ArrayList<Integer> notes = new ArrayList<Integer>();
        if (clef == Clef.Treble) {
            notes = GetAMajorTrebleNotes();
        } else if (clef == Clef.Bass) {
            notes = GeAMajorBassNotes();
        } else {
            notes = GetAMajorBothNotes();
        }

        return notes;
    }

    public static ArrayList<NoteOnDisplay> GenerateRandomNoteDisplays(int numNotes, Clef clef, NoteBundle bundle, Scale key) {
        return GetNoteDisplays(GenerateRandomNotes(numNotes, clef, bundle, key), key);
    }

    public static ArrayList<NoteOn> GenerateRandomNotes(int numNotes, Clef clef, NoteBundle bundle, Scale key) {
        ArrayList<NoteOn> randomNotes = new ArrayList<NoteOn>();
        ArrayList<Integer> bagOfNotes = new ArrayList<>();

        if (key == Scale.CMajor) {
            bagOfNotes = GetCMajorNotes(clef);
        } else if (key == Scale.DMajor)
        {
            bagOfNotes = GetDMajorNotes(clef);
        } else if (key == Scale.AMajor)
        {
            bagOfNotes = GetAMajorNotes(clef);
        } else if (key == Scale.GMajor) {
            bagOfNotes = GetGMajorNotes(clef);
        }

        for (int i = 0; i < numNotes; i++) {
            long tick = 480 * i;
            Random r = new Random();
            if (bundle == NoteBundle.Single) {
                Integer randomIndex = r.nextInt(bagOfNotes.size());
                NoteOn note = new NoteOn(tick, 1, bagOfNotes.get(randomIndex), 100);
                randomNotes.add(note);
            } else if (bundle == NoteBundle.Double) {
                Integer randomIndex = r.nextInt(bagOfNotes.size() - 2);
                NoteOn note = new NoteOn(tick, 1, bagOfNotes.get(randomIndex), 100);
                randomNotes.add(note);
                NoteOn note2 = new NoteOn(tick, 1, bagOfNotes.get(randomIndex + 2), 100);
                randomNotes.add(note2);
            } else if (bundle == NoteBundle.Triple) {
                Integer randomIndex = r.nextInt(bagOfNotes.size() - 4);
                NoteOn note = new NoteOn(tick, 1, bagOfNotes.get(randomIndex), 100);
                randomNotes.add(note);
                NoteOn note2 = new NoteOn(tick, 1, bagOfNotes.get(randomIndex + 2), 100);
                randomNotes.add(note2);
                NoteOn note3 = new NoteOn(tick, 1, bagOfNotes.get(randomIndex + 4), 100);
                randomNotes.add(note3);
        }


        }
        return randomNotes;
    }

    public static ArrayList<NoteOnDisplay> GetNoteDisplays(ArrayList<NoteOn> notes, Scale scaleKey) {
        ArrayList<NoteOnDisplay> noteDisplayList = new ArrayList<NoteOnDisplay>();
        for (int i = 0; i < notes.size(); i++) {
            NoteOnDisplay noteDisplay = GetNoteDisplay(notes.get(i), scaleKey);
            noteDisplay.init(notes.get(i));
            noteDisplayList.add(noteDisplay);
        }

        return noteDisplayList;
    }
}
