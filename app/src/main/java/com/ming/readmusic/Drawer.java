package com.ming.readmusic;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Drawer {

    private ArrayList<NoteOnDisplay> notes;
    private int lineSideMargins = 30;
    private int clefWidth = 90;
    private int noteSideMargins = 50;
    private int spaceBetweenBeats = 100;
    private int spaceBetweenLines = 30;
    private int spaceBetweenHalfNotes = spaceBetweenLines / 2;
    private int noteSpace;
    private int notesPerLine;
    private int marginTop = 100;
    private Clef clef = Clef.Treble;
    private int numOfNotes = 10; // TODO: Enough for one line?
    private NoteMode noteMode = NoteMode.Note;
    private int spaceBetweenClefs = 60;
    private int middleCstartX = 800;
    private int middleCstartY = 400;
    private int white_key_width = 50;
    private int black_key_width = 25;
    private int white_key_height = 200;
    private int black_key_height = 130;

    public Drawer(ArrayList<NoteOnDisplay> notes)
    {
        this.notes = notes;

    }

    public void DrawSelectedKeyboardNote(Canvas canvas, double currentTick) {

        int startX = middleCstartX;
        int startY = middleCstartY;

        Paint selected = new Paint();
        selected.setColor(Color.RED);
        selected.setStyle(Paint.Style.FILL);
        selected.setStrokeWidth(5f);

        for (int i = 0; i < this.notes.size(); i++) {
            NoteOnDisplay note = this.notes.get(i);
            if (note.getTick() == currentTick) {
                if (note.isSharp) {
                    canvas.drawRect(startX + (50 - 12.5f) +  note.noteDelta * white_key_width, startY, startX + (50 - 12.5f) + black_key_width + note.noteDelta * white_key_width, startY + black_key_height, selected);
                } else {
                    canvas.drawRect(startX + note.noteDelta * white_key_width, startY, startX + (note.noteDelta + 1) * white_key_width, startY + white_key_height, selected);
                }
            }
        }
    }

    public void DrawKeyboard(Canvas canvas) {
        DrawOctave(canvas, -2);
        DrawOctave(canvas, -1);
        DrawOctave(canvas, 0);
        DrawOctave(canvas, 1);
        DrawOctave(canvas, 2);
    }

    private void DrawOctave(Canvas canvas, int octaveNum) {

        int startX = middleCstartX + octaveNum * 7 * white_key_width;
        int startY = middleCstartY;

        Paint wk = new Paint();
        wk.setColor(Color.BLACK);
        wk.setStyle(Paint.Style.STROKE);
        wk.setStrokeWidth(5f);

        for (int i = 0; i < 7; i++) {
            canvas.drawRect(startX + i * white_key_width, startY, startX + (i + 1) * white_key_width, startY + white_key_height, wk);
        }

        Paint txt = new Paint();
        txt.setColor(Color.BLACK);
        txt.setStyle(Paint.Style.STROKE);
        txt.setStrokeWidth(3f);
        txt.setTextSize(30);

        if (octaveNum == 0) {
            canvas.drawText("C4", startX + 5, startY + white_key_height, txt);
        }

        Paint bk = new Paint();
        bk.setColor(Color.BLACK);
        bk.setStyle(Paint.Style.FILL);
        bk.setStrokeWidth(5f);

        for (int i = 0; i < 7; i++) {
            if (i == 2 || i == 6) {
                continue;
            }
            canvas.drawRect(startX + (50 - 12.5f) +  i * white_key_width, startY, startX + (50 - 12.5f) + black_key_width + i * white_key_width, startY + black_key_height, bk);
        }
    }

    public void DrawVerticalLine(Canvas canvas, double currentTick) {

        int lineNum = ((int) (currentTick / 480)) / notesPerLine;
        double beatNum = ((double) currentTick / 480) % notesPerLine;

        int xPos = (int) Math.ceil(lineSideMargins + noteSideMargins + clefWidth + beatNum * spaceBetweenBeats);

        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5f);

        int middleY = marginTop + (spaceBetweenLines * 2) + (lineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);

        canvas.drawLine(xPos, middleY - spaceBetweenLines * 3.5f, xPos, middleY + spaceBetweenLines * 3.5f, p);
    }

    public void DrawNote(NoteOnDisplay note, Clef clef, Canvas canvas) {
        long noteTick = note.getTick();

        int lineNum = ((int) (noteTick / 480)) / notesPerLine;
        double beatNum = ((double) noteTick / 480) % notesPerLine;

        int middleC_Y;
        int middleLine_Y = marginTop + (spaceBetweenLines * 3) + (lineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);

        if (clef == Clef.Treble) {
            middleC_Y = marginTop + (spaceBetweenLines * 5) + (lineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);
        } else {
            middleC_Y = marginTop - spaceBetweenLines + (lineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);
        }

        int Ypos = middleC_Y + -1 * note.noteDelta * spaceBetweenHalfNotes;
        int Xpos = (int) Math.ceil(lineSideMargins + noteSideMargins + clefWidth + beatNum * spaceBetweenBeats);

        TailDirection tailDirection;
        if (Ypos > middleLine_Y) {
            tailDirection = TailDirection.Up;
        } else {
            tailDirection = TailDirection.Down;
        }

        if (noteMode == NoteMode.Note) {
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
        int lowerLine = marginTop + (spaceBetweenLines * 4) + (lineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);
        int upperLine = marginTop + (lineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);

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
                canvas.drawLine(noteX - 30, lineY + spaceBetweenLines / 2, noteX + 30, lineY + spaceBetweenLines / 2, p);
            }

            lineY = lineY + spaceBetweenLines;
        }

        while (lineY > lowerLine) {
            if (note.noteDelta % 2 == 0) {
                canvas.drawLine(noteX - 30, lineY, noteX + 30, lineY, p);
            } else {
                canvas.drawLine(noteX - 30, lineY - spaceBetweenLines / 2, noteX + 30, lineY - spaceBetweenLines / 2, p);
            }

            lineY = lineY - spaceBetweenLines;
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
        int startY = marginTop + num * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);

        for (int i = 0; i < 5; i++) {
            canvas.drawLine(lineSideMargins, startY + spaceBetweenLines * i, canvas.getWidth() - lineSideMargins, startY + spaceBetweenLines * i, p);
        }

        /*for (int i = 0; i < 5; i++) {
            canvas.drawLine(lineSideMargins, startY + spaceBetweenLines * i + spaceBetweenLines * 4 + spaceBetweenClefs, canvas.getWidth() - lineSideMargins, startY + spaceBetweenLines * i + spaceBetweenLines * 4 + spaceBetweenClefs, p);
        }*/

        if (clef == Clef.Treble) {
            Drawable t = resources.getDrawable(R.drawable.treble_clef, null);
            t.setBounds(lineSideMargins, startY, clefWidth + lineSideMargins, startY + 30 * 4);
            t.draw(canvas);
        } else {
            Drawable b = resources.getDrawable(R.drawable.bass_clef, null);
            b.setBounds(lineSideMargins, startY, clefWidth + lineSideMargins, startY + 30 * 4);
            b.draw(canvas);
        }
    }

}
