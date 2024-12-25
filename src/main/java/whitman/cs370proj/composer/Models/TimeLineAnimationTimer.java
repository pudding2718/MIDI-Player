package whitman.cs370proj.composer.Models;

import javafx.animation.AnimationTimer;
import whitman.cs370proj.composer.Controllers.MainController;

public class TimeLineAnimationTimer extends AnimationTimer {
    @Override
    public void handle(long time) {
        for (var note : MainController.Instance.notes) {
            if (note.isInRect(MainController.Instance.timeLine)) {
                note.select();
                continue;
            }

            note.unselect();
        }
    }
}
