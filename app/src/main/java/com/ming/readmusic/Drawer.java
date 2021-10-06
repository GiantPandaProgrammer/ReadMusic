package com.ming.readmusic;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Drawer {

    private ArrayList<NoteOnDisplay> notes;
    public GameConstants noteSpace;
    public int notesPerLine;

    public Drawer(ArrayList<NoteOnDisplay> notes)
    {
        this.notes = notes;
    }

    public void SetNotesPerLine(int notesPerLine) {
        this.notesPerLine = notesPerLine;
    }

    public void DrawSelectedKeyboardNote(Canvas canvas, double currentTick, ArrayList<NoteOnDisplay> notes) {

        int startX = GameConstants.middleCstartX;
        int startY = GameConstants.middleCstartY;

        Paint selected = new Paint();
        selected.setColor(Color.RED);
        selected.setStyle(Paint.Style.FILL);
        selected.setStrokeWidth(5f);

        for (int i = 0; i < notes.size(); i++) {
            NoteOnDisplay note = notes.get(i);
            if (note.getTick() == currentTick) {
                if (note.isSharp) {
                    canvas.drawRect(startX + (50 - 12.5f) +  note.noteDelta * GameConstants.white_key_width, startY, startX + (50 - 12.5f) + GameConstants.black_key_width + note.noteDelta * GameConstants.white_key_width, startY + GameConstants.black_key_height, selected);
                } else {
                    Log.d("hint", Integer.toString(note.noteDelta));
                    canvas.drawRect(startX + note.noteDelta * GameConstants.white_key_width, startY, startX + (note.noteDelta + 1) * GameConstants.white_key_width, startY + GameConstants.white_key_height, selected);
                }
            }
        }
    }

    public void DrawKeyboard(Canvas canvas) {
        Paint wk = new Paint();
        wk.setColor(Color.BLACK);
        wk.setStyle(Paint.Style.STROKE);
        wk.setStrokeWidth(5f);

        DrawOctave(canvas, -2);
        DrawOctave(canvas, -1);
        DrawOctave(canvas, 0);
        DrawOctave(canvas, 1);
        DrawOctave(canvas, 2);
    }

    private void DrawOctave(Canvas canvas, int octaveNum) {

        int startX = GameConstants.middleCstartX + octaveNum * 7 * GameConstants.white_key_width;
        int startY = GameConstants.middleCstartY;

        Paint wk = new Paint();
        wk.setColor(Color.BLACK);
        wk.setStyle(Paint.Style.STROKE);
        wk.setStrokeWidth(5f);

        for (int i = 0; i < 7; i++) {
            canvas.drawRect(startX + i * GameConstants.white_key_width, startY, startX + (i + 1) * GameConstants.white_key_width, startY + GameConstants.white_key_height, wk);
        }

        Paint txt = new Paint();
        txt.setColor(Color.BLACK);
        txt.setStyle(Paint.Style.STROKE);
        txt.setStrokeWidth(3f);
        txt.setTextSize(30);

        if (octaveNum == 0) {
            canvas.drawText("C4", startX + 5, startY + GameConstants.white_key_height, txt);
        }

        Paint bk = new Paint();
        bk.setColor(Color.BLACK);
        bk.setStyle(Paint.Style.FILL);
        bk.setStrokeWidth(5f);

        for (int i = 0; i < 7; i++) {
            if (i == 2 || i == 6) {
                continue;
            }
            canvas.drawRect(startX + (50 - 12.5f) +  i * GameConstants.white_key_width, startY, startX + (50 - 12.5f) + GameConstants.black_key_width + i * GameConstants.white_key_width, startY + GameConstants.black_key_height, bk);
        }
    }

    public void DrawVerticalLine(Canvas canvas, double currentTick) {

        int lineNum = ((int) (currentTick / 480)) / notesPerLine;
        double beatNum = ((double) currentTick / 480) % notesPerLine;

        int xPos = (int) Math.ceil(GameConstants.lineSideMargins + GameConstants.noteSideMargins + GameConstants.clefWidth + beatNum * GameConstants.spaceBetweenBeats);

        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5f);

        int middleC_Y = GameConstants.marginTop + (GameConstants.spaceBetweenLines * 5) + (lineNum) * (GameConstants.spaceBetweenLines *4 + 100 + GameConstants.spaceBetweenClefs);
        canvas.drawLine(xPos, middleC_Y - GameConstants.spaceBetweenLines * 8f, xPos, middleC_Y + GameConstants.spaceBetweenLines * 8f, p);
    }

    public void DrawNote(NoteOnDisplay note, Clef clef, Canvas canvas) {
        long noteTick = note.getTick();

        int lineNum = ((int) (noteTick / 480)) / notesPerLine;
        double beatNum = ((double) noteTick / 480) % notesPerLine;

        int middleC_Y;
        int middleLine_Y;

        if (clef == Clef.Treble) {
            middleLine_Y = GameConstants.marginTop + (GameConstants.spaceBetweenLines * 3) + (lineNum) * (GameConstants.spaceBetweenLines *4 + 100 + GameConstants.spaceBetweenClefs);
        } else {
            middleLine_Y = GameConstants.marginTop + (GameConstants.spaceBetweenLines * 8) + (lineNum) * (GameConstants.spaceBetweenLines *4 + 100 + GameConstants.spaceBetweenClefs);
        }

        //if (clef == Clef.Treble) {
            middleC_Y = GameConstants.marginTop + (GameConstants.spaceBetweenLines * 5) + (lineNum) * (GameConstants.spaceBetweenLines *4 + 100 + GameConstants.spaceBetweenClefs);
        //} else {
            // This should be (GameConstants.spaceBetweenLines * 5) ? Same?
        //    middleC_Y = GameConstants.marginTop - GameConstants.spaceBetweenLines + (lineNum) * (GameConstants.spaceBetweenLines *4 + 100 + GameConstants.spaceBetweenClefs);
        //}

        int Ypos = middleC_Y + -1 * note.noteDelta * GameConstants.spaceBetweenHalfNotes;
        int Xpos = (int) Math.ceil(GameConstants.lineSideMargins + GameConstants.noteSideMargins + GameConstants.clefWidth + beatNum * GameConstants.spaceBetweenBeats);

        TailDirection tailDirection;
        if (Ypos > middleLine_Y) {
            tailDirection = TailDirection.Up;
        } else {
            tailDirection = TailDirection.Down;
        }

        if (GameConstants.noteMode == NoteMode.Note) {
            DrawNoteShape(canvas, Xpos, Ypos, 30, 40, tailDirection, note.color);
        } else {
            DrawLetter(canvas, note.letter, Xpos, Ypos);
        }

        DrawShortLine(canvas, note, Xpos, Ypos);
        if (note.isSharp) {
            DrawSharp(canvas, Xpos, Ypos);
        }
    }

    private void DrawNoteShape(Canvas canvas, int x, int y, int height, int width, TailDirection tailDirection, NoteColor color) {
        Paint p = new Paint();
        p.setColor(Color.BLACK);

        if (color == NoteColor.BLACK) {
            p.setColor(Color.BLACK);
        } else if (color == NoteColor.RED) {
            p.setColor(Color.RED);
        } else if (color == NoteColor.GREEN) {
            p.setColor(Color.GREEN);
        }

        p.setStyle(Paint.Style.FILL);

        int startX = x - width / 2;
        int stopX = x + width / 2;
        int startY = y - height / 2;
        int stopY = y + height / 2;
        canvas.drawOval(startX, startY, stopX, stopY, p);

        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5f);

        int tailLength = 80;

        if (tailDirection == TailDirection.Up)
            canvas.drawLine(stopX -2, y, stopX - 2, y - tailLength, p);
        else
            canvas.drawLine(stopX -2, y, stopX - 2, y + tailLength, p);
    }

    private void DrawLetter(Canvas canvas, String letter, int x, int y) {
        Paint textPaint = new Paint();
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);

        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(14f);
        textPaint.setTextSize(60);

        Rect bounds = new Rect();
        textPaint.getTextBounds(letter, 0, letter.length(), bounds);
        int startX = x - (bounds.width() / 2);
        int startY = y + (bounds.height() / 2);
        canvas.drawText(letter, startX, startY, textPaint);
    }

    private void DrawShortLine(Canvas canvas, NoteOnDisplay note, int noteX, int noteY) {
        int lineNum = 0;
        int lowerLine = GameConstants.marginTop + (GameConstants.spaceBetweenLines * 4) + (lineNum) * (GameConstants.spaceBetweenLines *4 + 100 + GameConstants.spaceBetweenClefs);
        int upperLine = GameConstants.marginTop + (lineNum) * (GameConstants.spaceBetweenLines *4 + 100 + GameConstants.spaceBetweenClefs);

        Paint p = new Paint();
        p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3f);

        int lineY = noteY;

        while (lineY < upperLine) {
            if (note.noteDelta % 2 == 0) {
                canvas.drawLine(noteX - 30, lineY, noteX + 30, lineY, p);
            } else {
                canvas.drawLine(noteX - 30, lineY + GameConstants.spaceBetweenLines / 2, noteX + 30, lineY + GameConstants.spaceBetweenLines / 2, p);
            }

            lineY = lineY + GameConstants.spaceBetweenLines;
        }

        while (lineY > lowerLine) {
            if (note.noteDelta % 2 == 0) {
                canvas.drawLine(noteX - 30, lineY, noteX + 30, lineY, p);
            } else {
                canvas.drawLine(noteX - 30, lineY - GameConstants.spaceBetweenLines / 2, noteX + 30, lineY - GameConstants.spaceBetweenLines / 2, p);
            }

            lineY = lineY - GameConstants.spaceBetweenLines;
        }
    }

    private void DrawSharp(Canvas canvas, int x, int y) {
        Paint textPaint = new Paint();
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(14f);
        textPaint.setTextSize(60);

        Rect bounds = new Rect();
        textPaint.getTextBounds("#", 0, "#".length(), bounds);
        int startX = x - (bounds.width() / 2) - 40;
        int startY = y + (bounds.height() / 2);
        canvas.drawText("#", startX, startY, textPaint);
    }

    public void DrawClefsAndLines(int num, Clef clef, Canvas canvas, Resources resources) {
        Paint p = new Paint();
        p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3f);
        int spaceBetweenClefs = 60;
        int spaceBetweenLines = 30;
        int startY = GameConstants.marginTop + num * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);

        for (int i = 0; i < 5; i++) {
            canvas.drawLine(GameConstants.lineSideMargins, startY + spaceBetweenLines * i, canvas.getWidth() - GameConstants.lineSideMargins, startY + spaceBetweenLines * i, p);
        }

        for (int i = 0; i < 5; i++) {
            canvas.drawLine(GameConstants.lineSideMargins, startY + spaceBetweenLines * i + spaceBetweenLines * 4 + spaceBetweenClefs, canvas.getWidth() - GameConstants.lineSideMargins, startY + spaceBetweenLines * i + spaceBetweenLines * 4 + spaceBetweenClefs, p);
        }

        Drawable t = resources.getDrawable(R.drawable.treble_clef, null);
        t.setBounds(GameConstants.lineSideMargins, startY, GameConstants.clefWidth + GameConstants.lineSideMargins, startY + 30 * 4);
        t.draw(canvas);

        Drawable b = resources.getDrawable(R.drawable.bass_clef, null);
        b.setBounds(GameConstants.lineSideMargins, startY + spaceBetweenLines * 4 + spaceBetweenClefs, GameConstants.clefWidth + GameConstants.lineSideMargins, startY + spaceBetweenLines * 4 + spaceBetweenClefs + 30 * 4);
        b.draw(canvas);

        /*
        if (clef == Clef.Treble) {
            Drawable t = resources.getDrawable(R.drawable.treble_clef, null);
            t.setBounds(lineSideMargins, startY, clefWidth + lineSideMargins, startY + 30 * 4);
            t.draw(canvas);
        } else {
            Drawable b = resources.getDrawable(R.drawable.bass_clef, null);
            b.setBounds(lineSideMargins, startY, clefWidth + lineSideMargins, startY + 30 * 4);
            b.draw(canvas);
        }*/
    }

}
