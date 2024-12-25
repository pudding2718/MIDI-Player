package whitman.cs370proj.composer.Models;

import com.google.gson.annotations.Expose;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import whitman.cs370proj.composer.Helpers.NoteHelper;

public final class BasicNote extends NoteBase {
    private Rectangle noteRect;

    @Expose
    private final InstrumentType instrumentType;

    public BasicNote(InstrumentType instrumentType, int pitch, int offset, int duration) {
        this.noteRect = new Rectangle(100, 10);
        this.instrumentType = instrumentType;

        setDuration(duration);
        setPitch(pitch);
        setOffset(offset);

        initNoteRect();
    }

    @Override
    public int getInstrument() {
        return getInstrumentType().getValue().getValue() - 1;
    }

    @Override
    public int getChannel() {
        return getInstrumentType().getValue().getKey();
    }

    @Override
    public void setDuration(int duration) {
        this.duration = duration;

        getNode().setWidth(duration);
    }

    @Override
    public void select() {
        getNode().setStroke(Color.RED);
        getNode().setStrokeWidth(3);

        super.select();
    }

    @Override
    public void unselect() {
        getNode().setStrokeWidth(0);

        super.unselect();
    }

    @Override
    public void initNoteRect() {
        getNode().setFill(NoteHelper.getNoteColor(getInstrumentType()));
    }

    @Override
    public Rectangle getNode() {
        if (noteRect == null) {
            this.noteRect = new Rectangle(100, 10);

            setDuration(duration);
            setPitch(rawPitch);
            setOffset(offset);

            initNoteRect();
        }

        return noteRect;
    }

    @Override
    public boolean isInRect(Node selectionRect) {
        return selectionRect.getBoundsInParent().intersects(getNode().getBoundsInParent());
    }

    @Override
    public void setPitch(int pitch) {
        var realPitch = 127 - pitch / 10;

        if (realPitch < 0 || realPitch > 127)
            throw new IllegalArgumentException("pitch");

        this.pitch = realPitch;
        this.rawPitch = pitch;

        getNode().setTranslateY(pitch);
    }

    @Override
    public void setOffset(int offset) {
        if (offset < 0 || offset > 2000)
            throw new IllegalArgumentException("offset");

        this.offset = offset;
        getNode().setTranslateX(offset);
    }

    @Override
    public InstrumentType getInstrumentType() {
        return instrumentType;
    }

    @Override
    public void hide() {
        noteRect.setOpacity(0.25);
    }

    @Override
    public void show() {
        noteRect.setOpacity(1);
    }
}
