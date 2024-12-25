package whitman.cs370proj.composer.Interfaces;

import javafx.scene.Node;
import whitman.cs370proj.composer.Models.InstrumentType;

public interface INote extends ISelectable, IHideable {
    void restoreTypeInfo();

    /**
     * Gets node.
     *
     * @return Returns node.
     */
    Node getNode();

    /**
     * Initializes note.
     */
    void initNoteRect();

    /**
     * Checks if note is in selection rectangle.
     *
     * @returns true if in selection rectangle, else false.
     */
    boolean isInRect(Node selectionRect);

    /**
     * Gets pitch.
     *
     * @returns the note pitch.
     */
    int getPitch();

    /**
     * Sets the note pitch.
     *
     * @param pitch the pitch to be set.
     */
    void setPitch(int pitch);

    /**
     * Gets offset.
     *
     * @returns the note offset.
     */
    int getOffset();

    /**
     * Sets the note offset.
     *
     * @param offset the offset to be set.
     */
    void setOffset(int offset);

    /**
     * Gets duration.
     *
     * @returns the note duration.
     */
    int getDuration();

    /**
     * Sets the note duration.
     *
     * @param duration the duration to be set.
     */
    void setDuration(int duration);

    /**
     * Gets MIDI channel.
     *
     * @returns the MIDI channel.
     */
    int getChannel();

    /**
     * Gets note instrument index.
     *
     * @returns the MIDI instrument index.
     */
    int getInstrument();

    /**
     * Gets raw note pitch.
     *
     * @returns the raw note pitch.
     */
    int getRawPitch();

    /**
     * Gets note instrument type.
     *
     * @returns the instrument type.
     */
    InstrumentType getInstrumentType();
}
