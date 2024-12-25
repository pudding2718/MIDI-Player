package whitman.cs370proj.composer.Interfaces;

public interface IComposable {
    /**
     * Determines if object is part of group.
     *
     * @return returns true if in group, else false.
     */
    boolean getIsInGroup();

    /**
     * Adds or removes object from group.
     *
     * @param isInGroup true or false, add or remove object to group.
     */
    void setIsInGroup(boolean isInGroup);
}
