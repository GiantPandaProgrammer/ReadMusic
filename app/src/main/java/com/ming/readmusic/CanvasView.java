package com.ming.readmusic;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;

// cmd + [ and cmd + ]  go back and forth in code

public class CanvasView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Context context;
    private Paint mPaint;
    private float mX, mY;
    private Scale scale = Scale.CMajor;
    private NoteDisplaySystem system = NoteDisplaySystem.Staff;
    private static final float TOLERANCE = 5;
    private ArrayList<NoteOnDisplay> currentNotes = new ArrayList<NoteOnDisplay>();
    private ArrayList<NoteOnDisplay> allNotes = new ArrayList<NoteOnDisplay>();
    private int noteSpace;
    private Clef clef = Clef.Treble;
    private NoteMode noteMode = NoteMode.Note;
    private double currentTick = 0;
    private NoteBundle numNotes = NoteBundle.Single;
    private Drawer drawer;
    private boolean showHint = true;
    private boolean playSound = true;
    private int clickBoxWidth = GameConstants.spaceBetweenBeats;
    private int clickBoxHeight = 600;
    private Handler handler = new Handler();
    private NoteOnDisplay currentSelected = null;

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
        this.currentNotes = MidiReader.GenerateRandomNoteDisplays(GameConstants.numOfNotes, clef, numNotes, scale);
        this.allNotes = MidiReader.GetAllNotes(scale);
        drawer = new Drawer(this.currentNotes);
        system = NoteDisplaySystem.Staff;
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

        if (system == NoteDisplaySystem.Staff) {
            for (int i = 0; i < numClef; i++) {
                drawer.DrawClefsAndLines(i, clef, canvas, getResources(), this.scale);
            }
        }
        if (system == NoteDisplaySystem.Staff) {
            drawer.DrawVerticalLine(canvas, currentTick);
        } else
        {
            drawer.DrawHorizontalLine(canvas, currentTick);
        }

        for (int i = 0; i < currentNotes.size(); i++) {
            drawer.DrawNote(currentNotes.get(i), clef, canvas, this.system);
        }
        NoteOnDisplay newSelected = null;

        for (int i = 0; i < currentNotes.size(); i++) {
            NoteOnDisplay note = currentNotes.get(i);
            if (note.getTick() < currentTick + 100 && note.getTick() > currentTick - 100) {
                newSelected = note;
            }
        }

        if ((currentSelected != null && newSelected != null && newSelected.getTick() != currentSelected.getTick())
        || (newSelected == null && currentSelected != null)) {
            if (currentSelected.color == NoteColor.BLACK)
                currentSelected.color = NoteColor.RED;


        }

        //if (newSelected != null)
            currentSelected = newSelected;

        //Log.d("test", currentSelected.toString());
        if (showHint && currentSelected != null && (!drawer.isBlackNote(currentSelected)))
        {
            drawer.DrawSelectedKeyboardNote(canvas, currentSelected);
        }

        drawer.DrawKeyboard(canvas);

        if (showHint && currentSelected != null && drawer.isBlackNote(currentSelected))
        {
            drawer.DrawSelectedKeyboardNote(canvas, currentSelected);
        }

        //DrawBoundingBox(canvas);
    }

    // Gets the line number?
    private int GetNumClefs(Canvas canvas) {
        if (currentNotes.size() == 0) { return 1; }

        NoteOnDisplay lastNote = currentNotes.get(currentNotes.size() - 1);
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
        this.Refresh();
    }

    public void SetBass() {
        clef = Clef.Bass;
        this.Refresh();
    }

    public void SetBoth() {
        clef = Clef.Both;
        this.Refresh();
    }

    public void SetSingle() {
        numNotes = NoteBundle.Single;
        this.Refresh();
    }

    public void SetCMajorScale() {
        this.scale = Scale.CMajor;
        this.Refresh();
    }

    public void SetDMajorScale() {
        this.scale = Scale.DMajor;
        this.Refresh();
    }

    public void SetAMajorScale() {
        this.scale = Scale.AMajor;
        this.Refresh();
    }

    public void SetGMajorScale() {
        this.scale = Scale.GMajor;
        this.Refresh();
    }
    public void SetStaff() {
        this.system = NoteDisplaySystem.Staff;
        //this.Refresh();
        this.invalidate();
    }

    public void SetNumber() {
        this.system = NoteDisplaySystem.Number;
        //this.Refresh();
        this.invalidate();
    }

    public void Refresh() {
        this.currentNotes = MidiReader.GenerateRandomNoteDisplays(GameConstants.numOfNotes, clef, numNotes, scale);
        this.currentTick = 0;
        this.invalidate();
    }
    // Add single, double, triple options

    public void ShowHint() {
        this.showHint = !this.showHint;
        this.invalidate();
    }

    public void PlaySong() {
        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                Log.d("Hello", Double.toString(currentTick));
                currentTick = currentTick + 48;

                if (currentTick == GameConstants.numOfNotes * 480) {
                    currentTick = 0;
                } else {
                    handler.postDelayed(this, 100);
                }

                invalidate();
            }
        };

        handler.postDelayed(r, 100);

        /*
        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                Log.d("Hello", Double.toString(currentTick));
                currentTick = currentTick + 480;

                if (currentTick == GameConstants.numOfNotes * 480) {
                    currentTick = 0;
                } else {
                    handler.postDelayed(this, 1000);
                }

                invalidate();
            }
        };

        handler.postDelayed(r, 1000);
        */
    }

    public boolean isPressed = false;

    public void PlayNote(NoteOnDisplay note) {
        if (note.isSharp)
        {
            Log.d("sharp", "sharp");
        }
        String fileName = "piano_sounds/" + note.getNoteFileName() + ".ogg";
        Log.d("file name", fileName);
        AssetFileDescriptor afd = null;
        try {
            afd = context.getAssets().openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MediaPlayer player = new MediaPlayer();

        try {
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            player.prepare();
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Check if the note selected is the correct note. And move onto next note.
    @Override
    public boolean onTouchEvent(MotionEvent event) {


        int action = event.getAction();
        //Log.i("extra pointer", Integer.toString(action));

        if (event.getAction() == MotionEvent.ACTION_UP) {
            return false;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            for (int i = 0; i < this.currentNotes.size(); i++) {
                long noteTick = this.currentNotes.get(i).getTick();

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
                }
            }

            if (isClickOnKeyboard(x, y)) {

                NoteOnDisplay clickedNote = getNoteClickedOn(x, y);
                if (clickedNote != null)
                {
                    Log.d("clicked on ", clickedNote.letter);
                    PlayNote(clickedNote);
                } else {
                    Log.d("note not found","note not found");
                }


                int noteIndex = (int) this.currentTick / 480;
                NoteOnDisplay currentNote = this.currentNotes.get(noteIndex);

                // for each note in current tick
                // if list of x, y is not in note return true
                if (!isClickOnSelectNote(x, y)) {
                    return true;
                }

                this.currentNotes.set(noteIndex, currentNote);

                this.currentTick = this.currentTick + 480;

                if (this.currentTick == GameConstants.numOfNotes * 480) {
                    this.Refresh();
                    this.currentTick = 0;
                }

                //this.showHint = false;
                this.invalidate();
            }
        }

        if (event.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
            //Log.i("extra pointer", "extra pointer");
        }

        // Shift + Ctrl then drag
        // What is event.getAction() ?
        // Phone not being detected
        // Action_pointer_down(1) not being hit
        return true;
    }

    private boolean isClickOnKeyboard(float clickX, float clickY) {
        int keyboardStartX = GameConstants.middleCstartX + - 2 * 7  * GameConstants.white_key_width;
        int keyboardEndX = GameConstants.middleCstartX + 3 * 7 * GameConstants.white_key_width;
        int keyboardStartY = GameConstants.middleCstartY;
        int keyboardEndY = GameConstants.middleCstartY + GameConstants.white_key_height;
        return keyboardStartX < clickX && clickX < keyboardEndX && keyboardStartY < clickY && clickY < keyboardEndY;
    }

    private NoteOnDisplay getNoteClickedOn(float clickX, float clickY) {
        int boardStartX = GameConstants.middleCstartX;
        int boardStartY = GameConstants.middleCstartY;

        NoteOnDisplay selectedNote = null;
        // Check both notes here
        for (int i = 0; i < this.allNotes.size(); i++) {
            NoteOnDisplay note = allNotes.get(i);
                if (note.isSharp) {
                    float startX = boardStartX + (50 - 12.5f) + note.noteDelta * GameConstants.white_key_width;
                    float startY = boardStartY;
                    float endX = boardStartX + (50 - 12.5f) + GameConstants.black_key_width + note.noteDelta * GameConstants.white_key_width;
                    float endY = boardStartY + GameConstants.black_key_height;

                    if (startX < clickX && clickX < endX && startY < clickY && clickY < endY) {
                        selectedNote = note;
                        break;
                    }
                } else {
                    float startX = boardStartX + note.noteDelta * GameConstants.white_key_width;
                    float startY = boardStartY;
                    float endX = boardStartX + (note.noteDelta + 1) * GameConstants.white_key_width;
                    float endY = boardStartY + GameConstants.white_key_height;

                    if (startX < clickX && clickX < endX && startY < clickY && clickY < endY) {
                        if (selectedNote == null) {
                            selectedNote = note;
                        }
                    }
                }
            }

        return selectedNote;
    }

    private boolean isClickOnSelectNote(float clickX, float clickY) {
        int boardStartX = GameConstants.middleCstartX;
        int boardStartY = GameConstants.middleCstartY;

        // Check both notes here
        for (int i = 0; i < this.currentNotes.size(); i++) {
            NoteOnDisplay note = currentNotes.get(i);
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