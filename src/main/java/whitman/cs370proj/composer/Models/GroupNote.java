package whitman.cs370proj.composer.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import whitman.cs370proj.composer.Helpers.NoteHelper;
import whitman.cs370proj.composer.Interfaces.IComposable;
import whitman.cs370proj.composer.Interfaces.INote;

import java.util.ArrayList;
import java.util.List;

public final class GroupNote extends NoteBase {
    @Expose
    @SerializedName("child_notes")
    public List<INote> children;
    private Rectangle noteRect;
    private Group group;
    private boolean isInit;

    public GroupNote(List<INote> notes) {
        children = new ArrayList<>(notes);

        if (children.stream().anyMatch(c -> !(c instanceof IComposable)))
            throw new ClassCastException("Some of the notes in the list cannot be composed");

        for (var n : children) {
            if (n instanceof IComposable composable)
                composable.setIsInGroup(true);
        }

        var leftNote = NoteHelper.getFirstNote(children);
        var topNote = NoteHelper.getMaxPitchNote(children);
        var bottomNote = NoteHelper.getMinPitchNote(children);
        var totalDuration = NoteHelper.getTotalDuration(children);

        var topPitch = topNote.getPitch();
        if (topNote instanceof GroupNote groupNote)
            topPitch = groupNote.getTopPitch();

        var bottomPitch = bottomNote.getPitch();
        if (bottomNote instanceof GroupNote groupNote)
            bottomPitch = groupNote.getBottomPitch();

        noteRect = new Rectangle(totalDuration, (topPitch - bottomPitch) * 10 + 10);
        initNoteRect();

        this.group = new Group(noteRect);

        isInit = true;
        setOffset(leftNote.getOffset());
        setDuration(totalDuration);
        setPitch(topNote.getRawPitch());
        isInit = false;
    }

    public int getTopPitch() {
        var result = Integer.MIN_VALUE;

        for (var note : getChildren()) {
            var pitch = 0;

            if (note instanceof BasicNote basicNote)
                pitch = basicNote.getPitch();

            if (note instanceof GroupNote groupNote)
                pitch = groupNote.getTopPitch();

            if (result < pitch) {
                result = pitch;
            }
        }

        return result;
    }

    public int getBottomPitch() {
        var result = Integer.MAX_VALUE;

        for (var note : getChildren()) {
            var pitch = 0;

            if (note instanceof BasicNote basicNote)
                pitch = basicNote.getPitch();

            if (note instanceof GroupNote groupNote)
                pitch = groupNote.getBottomPitch();

            if (result > pitch) {
                result = pitch;
            }
        }

        return result;
    }

    public List<INote> getChildren() {
        return children;
    }

    @Override
    public Group getNode() {
        if (group == null) {
            for (var n : children) {
                if (n instanceof IComposable composable)
                    composable.setIsInGroup(true);
            }

            var leftNote = NoteHelper.getFirstNote(children);
            var topNote = NoteHelper.getMaxPitchNote(children);
            var bottomNote = NoteHelper.getMinPitchNote(children);
            var totalDuration = NoteHelper.getTotalDuration(children);

            var topPitch = topNote.getPitch();
            if (topNote instanceof GroupNote groupNote)
                topPitch = groupNote.getTopPitch();

            var bottomPitch = bottomNote.getPitch();
            if (bottomNote instanceof GroupNote groupNote)
                bottomPitch = groupNote.getBottomPitch();

            noteRect = new Rectangle(totalDuration, (topPitch - bottomPitch) * 10 + 10);
            initNoteRect();

            this.group = new Group(noteRect);

            isInit = true;

            offset = 0;
            duration = 0;
            pitch = 0;
            rawPitch = 0;

            setOffset(leftNote.getOffset());
            setDuration(totalDuration);
            setPitch(topNote.getRawPitch());
            isInit = false;
        }

        return group;
    }

    @Override
    public void initNoteRect() {
        noteRect.setStroke(Color.RED);
        noteRect.setStrokeWidth(2);
        noteRect.getStrokeDashArray().addAll(8d, 8d);
        noteRect.setFill(Color.TRANSPARENT);
    }

    @Override
    public void setPitch(int pitch) {
        /*
        if (getTopPitch() == 126 && pitch < 0)
            return;
        if (getBottomPitch() == 0 && pitch > 0)
            return;
         */

        for (var note : getChildren()) {
            if (isInit) break;

            if (note instanceof BasicNote basicNote)
                basicNote.setPitch(Math.min(1270, Math.max(0, note.getRawPitch() + pitch)));
            if (note instanceof GroupNote groupNote)
                groupNote.setPitch(pitch);
        }

        if (isInit) {
            var realPitch = 127 - pitch / 10;

            if (realPitch < 0 || realPitch > 127)
                throw new IllegalArgumentException("pitch");

            this.pitch = realPitch;
            this.rawPitch = pitch;
        } else {
            this.pitch = Math.min(1270, Math.max(0, getRawPitch() + pitch));
            this.rawPitch += pitch;
        }

        getNode().setTranslateY(this.rawPitch);
    }

    @Override
    public void setOffset(int offset) {
        if (this.offset + offset <= 0)
            return;
        if (this.offset + offset + getDuration() >= 2000)
            return;

        for (var note : getChildren()) {
            if (isInit) break;

            if (note instanceof BasicNote basicNote)
                basicNote.setOffset(Math.max(0, Math.min(2000, note.getOffset() + offset)));
            if (note instanceof GroupNote groupNote)
                groupNote.setOffset(offset);
        }

        this.offset += offset;
        getNode().setTranslateX(this.offset);
    }

    @Override
    public void setDuration(int duration) {
        for (var note : getChildren()) {
            if (isInit) break;

            if (note instanceof BasicNote basicNote)
                basicNote.setDuration(Math.max(10, note.getDuration() + duration));
            if (note instanceof GroupNote groupNote)
                groupNote.setDuration(duration);
        }

        this.duration += duration;
        noteRect.setWidth(this.duration);
    }

    @Override
    public int getChannel() {
        return 0;
    }

    @Override
    public int getInstrument() {
        return 0;
    }

    @Override
    public InstrumentType getInstrumentType() {
        return null;
    }

    @Override
    public void select() {
        noteRect.setStroke(Color.RED);

        for (var note : getChildren())
            note.select();

        super.select();
    }

    @Override
    public void unselect() {
        noteRect.setStroke(Color.GRAY);

        for (var note : getChildren())
            note.unselect();

        super.unselect();
    }

    @Override
    public void hide() {
        for (var note : getChildren())
            note.hide();

        noteRect.setOpacity(0.25);
    }

    @Override
    public void show() {
        for (var note : getChildren())
            note.show();

        noteRect.setOpacity(1);
    }
}
