package whitman.cs370proj.composer.Interfaces;

public interface ISelectable {

    /**
     * Selects note.
     */
    void select();

    /**
     * Unselects note.
     */
    void unselect();

    /**
     * Check is note is selected.
     *
     * @return true if selected, else false.
     */
    boolean getIsSelected();
}
