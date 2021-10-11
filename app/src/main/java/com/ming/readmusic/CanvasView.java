package com.ming.readmusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

// cmd + [ and cmd + ]  go back and forth in code

public class CanvasView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Context context;
    private Paint mPaint;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    private ArrayList<NoteOnDisplay> notes = new ArrayList<NoteOnDisplay>();
    private int noteSpace;
    private Clef clef = Clef.Treble;
    private NoteMode noteMode = NoteMode.Note;
    private double currentTick = 480;


    private Drawer drawer;
    private boolean showHint = false;

    private int clickBoxWidth = GameConstants.spaceBetweenBeats;
    private int clickBoxHeight = 600;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
        this.setWillNotDraw(false);
        // we set a new Path
        mPath = new Path();

        // and we set a new Paint with the desired attributes
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);

/*        InputStream stream;

        try {
            stream = getContext().getAssets().open("HAPPY_BIRTHDAY.mid");
            MidiReader reader = new MidiReader(stream);
            notes = reader.GetNotes();

        } catch(final Throwable tx) {

        }*/

        clef = Clef.Treble;
        this.notes = MidiReader.GenerateRandomNoteDisplays(GameConstants.numOfNotes, clef);
        drawer = new Drawer(this.notes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum
        setMeasuredDimension(widthMeasureSpec, 3000);
    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i("size info: ", Integer.toString(w) + " "  + Integer.toString(h));
        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, 2000, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        CalNoteSpaces(mCanvas);
    }

    // Draw the keyboard and notes
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int numClef = GetNumClefs(canvas);

        for (int i = 0; i < numClef; i++) {
            drawer.DrawClefsAndLines(i, clef, canvas, getResources());
        }

        drawer.DrawVerticalLine(canvas, currentTick);

        for (int i = 0; i < notes.size(); i++) {
            drawer.DrawNote(notes.get(i), clef, canvas);
        }

        if (showHint)
        {
            drawer.DrawSelectedKeyboardNote(canvas, this.currentTick, notes);
        }

        drawer.DrawKeyboard(canvas);

        //DrawBoundingBox(canvas);
    }

    // Gets the line number?
    private int GetNumClefs(Canvas canvas) {
        if (notes.size() == 0) { return 1; }

        NoteOnDisplay lastNote = notes.get(notes.size() - 1);
        long noteTick = lastNote.getTick();
        int lineNum = ((int) (noteTick / 480)) / GameConstants.notesPerLine;

        return lineNum + 1;
    }

    private void CalNoteSpaces(Canvas canvas) {
        noteSpace = canvas.getWidth() - (GameConstants.lineSideMargins * 2) - GameConstants.clefWidth - (GameConstants.noteSideMargins * 2);
        GameConstants.notesPerLine = (int) Math.ceil((double) noteSpace / GameConstants.spaceBetweenBeats);
        drawer.SetNotesPerLine(GameConstants.notesPerLine);
    }

    public void SetTreble() {
        clef = Clef.Treble;
        this.notes = MidiReader.GenerateRandomNoteDisplays(GameConstants.numOfNotes, clef);
        this.invalidate();
    }

    public void SetBass() {
        // Can be removed?
        clef = Clef.Bass;
        this.notes = MidiReader.GenerateRandomNoteDisplays(GameConstants.numOfNotes, clef);
        this.invalidate();
    }

    public void SetBoth() {
        clef = Clef.Both;
        this.notes = MidiReader.GenerateRandomNoteDisplays(GameConstants.numOfNotes, clef);
        this.invalidate();
    }

    public void ShowHint() {
        this.showHint = true;
        this.invalidate();
    }

    public boolean isPressed = false;

    // Check if the note selected is the correct note. And move onto next note.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            return false;
        }
        
        float x = event.getX();
        float y = event.getY();

        for (int i = 0; i < this.notes.size(); i++) {
            long noteTick = this.notes.get(i).getTick();

            int lineNum = ((int) (noteTick / 480)) / GameConstants.notesPerLine;
            double beatNum = ((double) noteTick / 480) % GameConstants.notesPerLine;
            int xPos = (int) Math.ceil(GameConstants.lineSideMargins + GameConstants.noteSideMargins + GameConstants.clefWidth + beatNum * GameConstants.spaceBetweenBeats);

            int middleY = GameConstants.marginTop + (GameConstants.spaceBetweenLines * 2) + (lineNum) * (GameConstants.spaceBetweenLines *4 + 100 + GameConstants.spaceBetweenClefs);
            //Log.i("distance:", Double.toString(y - middleY));
            if (Math.abs(x - xPos) < clickBoxWidth / 2 && Math.abs(y - middleY) < clickBoxHeight / 2)
            {
                this.currentTick = noteTick;
                this.invalidate();

                int noteIndex = (int) this.currentTick / 480;
                markPriorNotesAsBlack(noteIndex);
            }
        }

        if (isClickOnKeyboard(x, y)) {

            int noteIndex = (int) this.currentTick / 480;
            NoteOnDisplay currentNote = this.notes.get(noteIndex);

            /*
            if (isClickOnSelectNote(x, y)) {
                currentNote.color = NoteColor.GREEN;
            } else {
                currentNote.color = NoteColor.RED;
            }*/
            if (!isClickOnSelectNote(x, y)) {
                return true;
            }

            this.notes.set(noteIndex, currentNote);

            this.currentTick = this.currentTick + 480;

            if (this.currentTick == GameConstants.numOfNotes * 480) {
                this.notes = MidiReader.GenerateRandomNoteDisplays(GameConstants.numOfNotes, clef);
                this.currentTick = 0;
            }

            this.showHint = false;
            this.invalidate();
        }

        return true;
    }

    private void markPriorNotesAsBlack(int index) {
        for (int i = 0; i < notes.size(); i++) {
            if (i >= index) {
                NoteOnDisplay note = notes.get(i);
                note.color = NoteColor.BLACK;
                notes.set(i, note);
            }
        }
    }

    private boolean isClickOnKeyboard(float clickX, float clickY) {
        int keyboardStartX = GameConstants.middleCstartX + - 2 * 7  * GameConstants.white_key_width;
        int keyboardEndX = GameConstants.middleCstartX + 3 * 7 * GameConstants.white_key_width;
        int keyboardStartY = GameConstants.middleCstartY;
        int keyboardEndY = GameConstants.middleCstartY + GameConstants.white_key_height;
        return keyboardStartX < clickX && clickX < keyboardEndX && keyboardStartY < clickY && clickY < keyboardEndY;
    }

    private boolean isClickOnSelectNote(float clickX, float clickY) {
        int boardStartX = GameConstants.middleCstartX;
        int boardStartY = GameConstants.middleCstartY;

        for (int i = 0; i < this.notes.size(); i++) {
            NoteOnDisplay note = notes.get(i);
            if (note.getTick() == this.currentTick) {
                if (note.isSharp) {
                    float startX = boardStartX + (50 - 12.5f) + note.noteDelta * GameConstants.white_key_width;
                    float startY = boardStartY;
                    float endX = boardStartX + (50 - 12.5f) + GameConstants.black_key_width + note.noteDelta * GameConstants.white_key_width;
                    float endY = boardStartY + GameConstants.black_key_height;

                    if (startX < clickX && clickX < endX && startY < clickY && clickY < endY) {
                        return true;
                    }
                } else {
                    float startX = boardStartX + note.noteDelta * GameConstants.white_key_width;
                    float startY = boardStartY;
                    float endX = boardStartX + (note.noteDelta + 1) * GameConstants.white_key_width;
                    float endY = boardStartY + GameConstants.white_key_height;

                    if (startX < clickX && clickX < endX && startY < clickY && clickY < endY) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

  private void DrawBoundingBox(Canvas canvas) {
        Paint boxPaint = new Paint();
        boxPaint.setColor(Color.BLACK);
        boxPaint.setStyle(Paint.Style.STROKE);

        int currentLineNum = ((int) (this.currentTick / 480)) / GameConstants.notesPerLine;
        int middleY = GameConstants.marginTop + (GameConstants.spaceBetweenLines * 2) + (currentLineNum) * (GameConstants.spaceBetweenLines *4 + 100 + GameConstants.spaceBetweenClefs);

        for (int i = 0; i < 10; i++) {
            int xPos = (int) Math.ceil(GameConstants.lineSideMargins + GameConstants.noteSideMargins + GameConstants.clefWidth + i * GameConstants.spaceBetweenBeats);

            canvas.drawRect(xPos - clickBoxWidth / 2, middleY - clickBoxHeight / 2, xPos + clickBoxWidth / 2, middleY + clickBoxHeight / 2, boxPaint);
        }

    }
}