package whitman.cs370proj.composer.Models;

import com.google.gson.annotations.Expose;
import javafx.scene.Node;
import whitman.cs370proj.composer.Helpers.JsonHelper;
import whitman.cs370proj.composer.Interfaces.IComposable;
import whitman.cs370proj.composer.Interfaces.INote;


public abstract class NoteBase implements INote, IComposable {
    @Expose
    protected String type = getClass().getName();
    @Expose
    protected boolean isSelected;
    @Expose
    protected int duration;
    @Expose
    protected int rawPitch;
    @Expose
    protected int pitch;
    @Expose
    protected int offset;
    @Expose
    private boolean isInGroup;

    @Override
    public final void restoreTypeInfo() {
        type = getClass().getName();
    }

    public boolean getIsInGroup() {
        return isInGroup;
    }

    public void setIsInGroup(boolean isInGroup) {
        this.isInGroup = isInGroup;
    }

    @Override
    public boolean isInRect(Node selectionRect) {
        return selectionRect.getBoundsInParent().intersects(getNode().getBoundsInParent());
    }

    @Override
    public int getPitch() {
        return pitch;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public int getRawPitch() {
        return rawPitch;
    }

    @Override
    public void select() {
        isSelected = true;
    }

    @Override
    public void unselect() {
        isSelected = false;
    }

    @Override
    public boolean getIsSelected() {
        return isSelected;
    }

    @Override
    public String toString() {
        return JsonHelper.ToJson(this);
    }
}
