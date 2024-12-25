package whitman.cs370proj.composer.Handlers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import whitman.cs370proj.composer.Controllers.MainController;
import whitman.cs370proj.composer.Helpers.NoteHelper;
import whitman.cs370proj.composer.Models.BasicNote;
import whitman.cs370proj.composer.Models.GroupNote;

import java.awt.*;

/**
 * Class to handle mouse drag event.
 */
public class OnMouseDraggedEventHandler implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent mouseEvent) {
        var xDiff = mouseEvent.getX() - MainController.Instance.lastDragPos.x;
        var yDiff = mouseEvent.getY() - MainController.Instance.lastDragPos.y;

        MainController.Instance.lastDragPos.setLocation(mouseEvent.getX(), mouseEvent.getY());

        var rectPoint = (Point) MainController.Instance.selectionRectangle.getUserData();
        var x = Math.min(rectPoint.x, mouseEvent.getX());
        var y = Math.min(rectPoint.y, mouseEvent.getY());
        var width = mouseEvent.getX() - rectPoint.x;
        var height = mouseEvent.getY() - rectPoint.y;
        var widthAbs = Math.abs(width);
        var heightAbs = Math.abs(height);

        var rect = new Rectangle(x, y, widthAbs, heightAbs);

        var selectedNotes = NoteHelper.getAllSelectedNotes(MainController.Instance.notes);

        if (MainController.Instance.hasFocusRect.get() && !selectedNotes.isEmpty() || MainController.Instance.isDragging.get()) {
            if (MainController.Instance.isAtRightEdge.get()) {
                for (var note : selectedNotes) {
                    if (note instanceof GroupNote groupNote) {
                        groupNote.setDuration((int) xDiff);
                    }

                    if (note instanceof BasicNote basicNote) {
                        basicNote.setDuration(Math.max(10, note.getDuration() + (int) xDiff));
                    }
                }
            } else {
                MainController.Instance.isDragging.set(true);

                for (var note : selectedNotes) {
                    var pitch = note.getRawPitch();
                    var offset = note.getOffset();

                    if (yDiff < 0) {
                        if (note instanceof GroupNote groupNote)
                            pitch = NoteHelper.getMaxPitchNote(groupNote.getChildren()).getRawPitch();
                    } else {
                        if (note instanceof GroupNote groupNote)
                            pitch = NoteHelper.getMinPitchNote(groupNote.getChildren()).getRawPitch();
                    }

                    if (xDiff < 0) {
                        if (note instanceof GroupNote groupNote)
                            offset = NoteHelper.getFirstNote(groupNote.getChildren()).getOffset();
                    } else {
                        if (note instanceof GroupNote groupNote)
                            offset = NoteHelper.getLastFinishNote(groupNote.getChildren()).getOffset();
                    }

                    var newOffset = Math.max(0, Math.min(2000, offset + (int) xDiff));
                    var newPitch = Math.min(1270, Math.max(0, pitch + (int) yDiff));


                    if (note instanceof GroupNote groupNote) {
                        if (newOffset == 2000 || newOffset == 0) {
                            groupNote.setOffset(0);
                        } else {
                            groupNote.setOffset((int) xDiff);
                        }

                        if (newPitch == 1270 || newPitch == 0) {
                            groupNote.setPitch(0);
                        } else {
                            groupNote.setPitch((int) yDiff);
                        }
                    }

                    if (note instanceof BasicNote basicNote) {
                        basicNote.setOffset(newOffset);
                        basicNote.setPitch(newPitch);
                    }
                }
            }

            return;
        }

        if (width < 0)
            MainController.Instance.selectionRectangle.setTranslateX(mouseEvent.getX());
        if (height < 0)
            MainController.Instance.selectionRectangle.setTranslateY(mouseEvent.getY());

        MainController.Instance.selectionRectangle.setWidth(rect.getWidth());
        MainController.Instance.selectionRectangle.setHeight(rect.getHeight());

        NoteHelper.refreshSelectionUseRect(MainController.Instance.notes, rect);
    }
}
