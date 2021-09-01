package com.ming.readmusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
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
    private double currentTick = 480;

    private Drawer drawer;
    //private double currentBeatNum;
    //private int currentLineNum;
    private boolean showHint = false;

    private int clickBoxWidth = spaceBetweenBeats;
    private int clickBoxHeight = 260;

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
        this.notes = MidiReader.GenerateRandomNoteDisplays(numOfNotes, clef);
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

    // override onDraw
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
            drawer.DrawSelectedKeyboardNote(canvas, this.currentTick);
        }

        drawer.DrawKeyboard(canvas);
    }

    private int GetNumClefs(Canvas canvas) {
        if (notes.size() == 0) { return 1; }

        NoteOnDisplay lastNote = notes.get(notes.size() - 1);
        long noteTick = lastNote.getTick();
        int lineNum = ((int) (noteTick / 480)) / notesPerLine;

        return lineNum + 1;
    }

    private void CalNoteSpaces(Canvas canvas) {
        noteSpace = canvas.getWidth() - (lineSideMargins * 2) - clefWidth - (noteSideMargins * 2);
        notesPerLine = (int) Math.ceil((double) noteSpace / spaceBetweenBeats);
    }

    public void SetTreble() {
        clef = Clef.Treble;
        this.notes = MidiReader.GenerateRandomNoteDisplays(numOfNotes, clef);
        this.invalidate();
    }

    public void SetBass() {
        clef = Clef.Bass;
        this.notes = MidiReader.GenerateRandomNoteDisplays(numOfNotes, clef);
        this.invalidate();
    }

    public void ShowHint() {
        this.showHint = true;
        this.invalidate();
    }

    public boolean isPressed = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            return false;
        }
        
        float x = event.getX();
        float y = event.getY();

        for (int i = 0; i < this.notes.size(); i++) {
            long noteTick = this.notes.get(i).getTick();

            int lineNum = ((int) (noteTick / 480)) / notesPerLine;
            double beatNum = ((double) noteTick / 480) % notesPerLine;
            int xPos = (int) Math.ceil(lineSideMargins + noteSideMargins + clefWidth + beatNum * spaceBetweenBeats);

            int middleY = marginTop + (spaceBetweenLines * 2) + (lineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);
            //Log.i("distance:", Double.toString(y - middleY));
            if (Math.abs(x - xPos) < clickBoxWidth / 2 && Math.abs(y - middleY) < clickBoxHeight / 2)
            {
                this.currentTick = noteTick;
                this.invalidate();
            }

            if (isClickOnSelectNote(x, y))
            {
                int noteIndex = (int) this.currentTick / 480;
                NoteOnDisplay currentNote = this.notes.get(noteIndex);
                currentNote.color = NoteColor.GREEN;
                this.notes.set(noteIndex, currentNote);

                this.currentTick = this.currentTick + 480;

                if (this.currentTick == numOfNotes * 480) {
                    this.notes = MidiReader.GenerateRandomNoteDisplays(numOfNotes, clef);
                    this.currentTick = 0;
                }

                this.showHint = false;
                this.invalidate();
                break;
            }
        }

        return true;
    }

    private boolean isClickOnSelectNote(float clickX, float clickY) {
        int boardStartX = middleCstartX;
        int boardStartY = middleCstartY;

        for (int i = 0; i < this.notes.size(); i++) {
            NoteOnDisplay note = notes.get(i);
            if (note.getTick() == this.currentTick) {
                if (note.isSharp) {
                    float startX = boardStartX + (50 - 12.5f) + note.noteDelta * white_key_width;
                    float startY = boardStartY;
                    float endX = boardStartX + (50 - 12.5f) + black_key_width + note.noteDelta * white_key_width;
                    float endY = boardStartY + black_key_height;

                    if (startX < clickX && clickX < endX && startY < clickY && clickY < endY) {
                        return true;
                    }
                } else {
                    float startX = boardStartX + note.noteDelta * white_key_width;
                    float startY = boardStartY;
                    float endX = boardStartX + (note.noteDelta + 1) * white_key_width;
                    float endY = boardStartY + white_key_height;

                    if (startX < clickX && clickX < endX && startY < clickY && clickY < endY) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // Unused Code
  /*  private void DrawBoundingBox(Canvas canvas) {
        Paint boxPaint = new Paint();
        boxPaint.setColor(Color.BLACK);
        boxPaint.setStyle(Paint.Style.STROKE);

        int currentLineNum = ((int) (this.currentTick / 480)) / notesPerLine;
        int middleY = marginTop + (spaceBetweenLines * 2) + (currentLineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);

        for (int i = 0; i < 10; i++) {
            int xPos = (int) Math.ceil(lineSideMargins + noteSideMargins + clefWidth + i * spaceBetweenBeats);

            canvas.drawRect(xPos - clickBoxWidth / 2, middleY - clickBoxHeight / 2, xPos + clickBoxWidth / 2, middleY + clickBoxHeight / 2, boxPaint);
        }

    }*/
}