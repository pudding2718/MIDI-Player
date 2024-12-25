package whitman.cs370proj.composer.Helpers;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import whitman.cs370proj.composer.Interfaces.IComposable;
import whitman.cs370proj.composer.Interfaces.INote;
import whitman.cs370proj.composer.Models.BasicNote;
import whitman.cs370proj.composer.Models.GroupNote;
import whitman.cs370proj.composer.Models.InstrumentType;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for note objects.
 */
public final class NoteHelper {

    /**
     * Returns new note object.
     *
     * @param type     the instrument type.
     * @param pitch    the note pitch.
     * @param offset   the note offset.
     * @param duration the note duration.
     * @return the new note object.
     */
    public static BasicNote getNote(InstrumentType type, int pitch, int offset, int duration) {
        return new BasicNote(type, pitch, offset, duration);
    }

    /**
     * Gets the current note color according to instrument type.
     *
     * @param type the Instrument type.
     * @return the current note color.
     */
    public static Paint getNoteColor(InstrumentType type) {
        return switch (type) {
            case Piano -> Color.web("#a8a1a1");
            case Guitar -> Color.web("#16e4c2");
            case Violin -> Color.BLACK;
            case Marimba -> Color.web("#2424f5");
            case Accordion -> Color.web("#e82f85");
            case FrenchHorn -> Color.web("#c37d16");
            case Harpsichord -> Color.web("#26d016");
            case ChurchOrgan -> Color.web("#eed82c");
        };
    }

    /**
     * Gets the last note of screen.
     *
     * @param notes list of note objects.
     * @return the current note color.
     */
    public static INote getLastFinishNote(List<INote> notes) {
        int maxLength = Integer.MIN_VALUE;
        INote result = null;

        for (var note : notes) {
            if (maxLength < (note.getOffset() + note.getDuration())) {
                maxLength = note.getOffset() + note.getDuration();
                result = note;
            }
        }

        return result;
    }

    /**
     * Gets the total duration of a group of notes.
     *
     * @param notes list of note objects.
     * @return the total duration.
     */
    public static int getTotalDuration(List<INote> notes) {
        var firstNote = getFirstNote(notes);
        var lastNote = getLastFinishNote(notes);

        return (lastNote.getOffset() + lastNote.getDuration()) - firstNote.getOffset();
    }

    /**
     * Gets the maximum pitch of a group of notes.
     *
     * @param notes list of note objects.
     * @return the maximum pitch value.
     */
    public static INote getMaxPitchNote(List<INote> notes) {
        int maxPitch = -1;
        INote result = null;

        for (var note : notes) {
            var pitch = 0;

            if (note instanceof GroupNote groupNote)
                pitch = groupNote.getTopPitch();
            else
                pitch = note.getPitch();

            if (maxPitch < pitch) {
                maxPitch = pitch;
                result = note;
            }
        }

        return result;
    }

    /**
     * Gets the minimum pitch of a group of notes.
     *
     * @param notes list of note objects.
     * @return the minimum pitch value.
     */
    public static INote getMinPitchNote(List<INote> notes) {
        int minPitch = Integer.MAX_VALUE;
        INote result = null;

        for (var note : notes) {
            var pitch = 0;

            if (note instanceof BasicNote basicNote)
                pitch = basicNote.getPitch();

            if (note instanceof GroupNote groupNote)
                pitch = groupNote.getBottomPitch();

            if (minPitch > pitch) {
                minPitch = pitch;
                result = note;
            }
        }

        return result;
    }

    /**
     * Gets the first note in a group of notes.
     *
     * @param notes list of note objects.
     * @return the first note.
     */
    public static INote getFirstNote(List<INote> notes) {
        int minOffset = Integer.MAX_VALUE;
        INote result = null;

        for (var note : notes) {
            if (minOffset > note.getOffset()) {
                minOffset = note.getOffset();
                result = note;
            }
        }

        return result;
    }

    /**
     * Changes the instrument type of selected note(s).
     *
     * @param originalList list of selected note objects.
     * @param newType      new Instrument type.
     * @return list of note objects with instrument type newType.
     */
    public static List<INote> changeInstrument(List<INote> originalList, InstrumentType newType) {
        var result = new ArrayList<INote>();

        for (var note : originalList) {
            System.out.println(note.getPitch());
            result.add(new BasicNote(newType, 1270 - note.getPitch() * 10, note.getOffset(), note.getDuration()));
        }

        return result;
    }

    /**
     * Gets notes in selection rectangle.
     *
     * @param notes list of selected note objects.
     * @param rect  area of selection.
     * @return list of note objects in selection area.
     */
    public static List<INote> getInRectNotes(List<INote> notes, Rectangle rect) {
        var result = new ArrayList<INote>();

        for (var note : notes) {
            if (note instanceof IComposable composable) {
                if (composable.getIsInGroup()) continue;
            }
            if (!note.isInRect(rect)) continue;

            result.add(note);
        }

        return result;
    }

    /**
     * Gets currently selected notes.
     *
     * @param notes list of selected note objects.
     * @return list of currently selected notes.
     */
    public static List<INote> getAllSelectedNotes(List<INote> notes) {
        var result = new ArrayList<INote>();

        for (var note : notes) {
            if (!note.getIsSelected()) continue;
            if (note instanceof IComposable composable) {
                if (composable.getIsInGroup()) continue;
            }

            result.add(note);
        }

        return result;
    }

    /**
     * Refreshes selection rectangle.
     *
     * @param notes list of note objects.
     * @param rect  selection rectangle.
     */
    public static void refreshSelectionUseRect(List<INote> notes, Rectangle rect) {
        for (var note : notes)
            if (note.isInRect(rect))
                note.select();
            else
                note.unselect();
    }

    /**
     * Selects all notes.
     *
     * @param notes list of note objects.
     */
    public static void selectAll(List<INote> notes) {
        for (var note : notes)
            note.select();
    }

    /**
     * Unselects all notes.
     *
     * @param notes list of note objects.
     */
    public static void unselectAll(List<INote> notes) {
        for (var note : notes)
            note.unselect();
    }

    public static void hideAll(List<INote> notes) {
        for (var note : notes)
            note.hide();
    }

    public static void unhideAll(List<INote> notes) {
        for (var note : notes)
            note.show();
    }
}
