package whitman.cs370proj.composer.Controllers;

import javafx.fxml.FXML;

public class MenuController {
    public static MenuController Instance;

    @FXML
    public void initialize() {
        Instance = this;
    }

    public void handlePlay() {
        MainController.Instance.play();
    }

    public void handlePlaySelected() {
        MainController.Instance.playSelected();
    }

    public void handleStop() {
        MainController.Instance.stop();
    }

    public void handleExit() {
        MainController.Instance.exit();
    }

    public void handleSelectAll() {
        MainController.Instance.selectAll();
    }

    public void handleDelete() {
        MainController.Instance.delete();
    }

    public void handleInstrumentChange() {
        MainController.Instance.instrumentChange();
    }

    public void handleGroup() {
        MainController.Instance.group();
    }

    public void handleUngroup() {
        MainController.Instance.ungroup();
    }

    public void handleCut() {
        MainController.Instance.cut();
    }

    public void handleClearAll() {
        MainController.Instance.clearAll();
    }

    public void handleCopy() {
        MainController.Instance.copy();
    }

    public void handlePaste() {
        MainController.Instance.paste();
    }

    public void handleAbout() {
        MainController.Instance.about();
    }

    public void handleNew() {
        MainController.Instance.newFile();
    }

    public void handleOpen() {
        MainController.Instance.open();
    }

    public void handleSave() {
        MainController.Instance.save();
    }

    public void handleSaveAs() {
        MainController.Instance.saveAs();
    }

    public void handleUndo() {
        MainController.Instance.undo();
    }

    public void handleRedo() {
        MainController.Instance.redo();
    }

    public void handleGitLink() {
        MainController.Instance.gitLink();
    }

    public void handleVolume() {
        MainController.Instance.volume();
    }

}
