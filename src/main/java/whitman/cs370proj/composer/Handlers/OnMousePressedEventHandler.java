package whitman.cs370proj.composer.Handlers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import whitman.cs370proj.composer.Controllers.MainController;
import whitman.cs370proj.composer.Helpers.NoteHelper;

import java.awt.*;

/**
 * Class to handle mouse press event.
 */
public class OnMousePressedEventHandler implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent mouseEvent) {
        var selectedNotes = NoteHelper.getAllSelectedNotes(MainController.Instance.notes);

        MainController.Instance.notePane.getChildren().remove(MainController.Instance.selectionRectangle);

        MainController.Instance.selectionRectangle.setWidth(0);
        MainController.Instance.selectionRectangle.setHeight(0);
        MainController.Instance.selectionRectangle.setUserData(new Point((int) mouseEvent.getX(), (int) mouseEvent.getY()));
        MainController.Instance.notePane.getChildren().add(MainController.Instance.selectionRectangle);
        MainController.Instance.selectionRectangle.setTranslateX(mouseEvent.getX());
        MainController.Instance.selectionRectangle.setTranslateY(mouseEvent.getY());

        MainController.Instance.lastDragPos.setLocation(mouseEvent.getX(), mouseEvent.getY());

        var rect = new Rectangle(mouseEvent.getX(), mouseEvent.getY(), 3, 3);
        var inRangeRect = NoteHelper.getInRectNotes(MainController.Instance.notes, rect);

        if (inRangeRect.isEmpty()) {
            MainController.Instance.isAtRightEdge.set(false);
        } else {
            var xOffset = inRangeRect.get(0).getOffset() + inRangeRect.get(0).getDuration();
            var flag = xOffset - 10 <= mouseEvent.getX() && mouseEvent.getX() <= xOffset;

            MainController.Instance.isAtRightEdge.set(flag);
        }

        MainController.Instance.hasFocusRect.set(!inRangeRect.isEmpty());

        if (!inRangeRect.isEmpty()) {
            for (var note : inRangeRect)
                note.select();

            MainController.Instance.notePane.getChildren().remove(MainController.Instance.selectionRectangle);
            MainController.Instance.isDragging.set(false);
        }
    }
}
