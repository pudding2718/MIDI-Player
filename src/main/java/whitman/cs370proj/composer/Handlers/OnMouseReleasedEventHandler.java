package whitman.cs370proj.composer.Handlers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import whitman.cs370proj.composer.Controllers.MainController;
import whitman.cs370proj.composer.Helpers.NoteHelper;
import whitman.cs370proj.composer.Models.GroupNote;

/**
 * Class to handle mouse release event.
 */
public class OnMouseReleasedEventHandler implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent mouseEvent) {
        var selectedNotes = NoteHelper.getAllSelectedNotes(MainController.Instance.notes);
        var rect = new Rectangle(MainController.Instance.selectionRectangle.getTranslateX(), MainController.Instance.selectionRectangle.getTranslateY(), 3, 3);
        var inRectNotes = NoteHelper.getInRectNotes(MainController.Instance.notes, rect);

        var changed = false;
        for (var note : selectedNotes) {
            var rawPitch = note.getRawPitch();

            var newPitch = rawPitch - (rawPitch % 10);

            if (note instanceof GroupNote groupNote) {
                newPitch = newPitch - groupNote.getRawPitch();
            }

            if (note.getRawPitch() != newPitch)
                changed = true;
            note.setPitch(newPitch);
        }

        if (!inRectNotes.isEmpty()) {
            NoteHelper.unselectAll(MainController.Instance.notes);
            for (var note : inRectNotes)
                note.select();

            MainController.Instance.notePane.getChildren().remove(MainController.Instance.selectionRectangle);
            MainController.Instance.isDragging.set(false);

            return;
        }

        if (MainController.Instance.selectionRectangle.getWidth() == 0 &&
                MainController.Instance.selectionRectangle.getHeight() == 0 &&
                !MainController.Instance.isDragging.get()) {
            MainController.Instance.addNote(mouseEvent);
        }

        MainController.Instance.notePane.getChildren().remove(MainController.Instance.selectionRectangle);
        MainController.Instance.isDragging.set(false);
        if (changed)
            MainController.Instance.operationHistoryHelper.setCurrent(MainController.Instance.notes);
    }
}
