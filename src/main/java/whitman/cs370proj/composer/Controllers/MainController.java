package whitman.cs370proj.composer.Controllers;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import whitman.cs370proj.composer.Handlers.OnMouseDraggedEventHandler;
import whitman.cs370proj.composer.Handlers.OnMousePressedEventHandler;
import whitman.cs370proj.composer.Handlers.OnMouseReleasedEventHandler;
import whitman.cs370proj.composer.Helpers.*;
import whitman.cs370proj.composer.Interfaces.IComposable;
import whitman.cs370proj.composer.Interfaces.INote;
import whitman.cs370proj.composer.MainApplication;
import whitman.cs370proj.composer.MidiPlayer;
import whitman.cs370proj.composer.Models.BasicNote;
import whitman.cs370proj.composer.Models.GroupNote;
import whitman.cs370proj.composer.Models.InstrumentType;
import whitman.cs370proj.composer.Models.TimeLineAnimationTimer;

import javax.sound.midi.ShortMessage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Sets up main controller.
 */
public class MainController {
    public static MainController Instance;
    private static int VOLUME = 100;
    /**
     * Sets up toggle group, an array list to hold notes, and a midi note sequence player to read standard midi files
     * and emit realtime JSON events.
     */
    public final OperationHistoryHelper operationHistoryHelper = new OperationHistoryHelper();
    public final List<INote> notes = new ArrayList<>();
    public final Rectangle selectionRectangle = new Rectangle(0, 0, Color.TRANSPARENT);
    public final AtomicBoolean isDragging = new AtomicBoolean(false);
    public final AtomicBoolean hasFocusRect = new AtomicBoolean(false);
    public final AtomicBoolean isAtRightEdge = new AtomicBoolean(false);
    public final Point lastDragPos = new Point();
    private final MidiPlayer player = new MidiPlayer(100, 60);
    public Clipboard systemClipboard = Clipboard.getSystemClipboard();
    /**
     * Defines menu items in dropdown.
     */
    @FXML
    public Pane notePane, animationPane, linePane, labelPane, tagPane;
    public Line timeLine;
    @FXML
    public Slider volumeSlider;
    private boolean isContentChanged;
    private File openedFile;
    private InstrumentType selectedInstrument = InstrumentType.Piano;// = InstrumentsController.Instance.getSelectedInstrument();
    private boolean isAnimationRunning;
    private AnimationTimer timer;

    public void setSelectedInstrument(InstrumentType type) {
        selectedInstrument = type;
    }

    /**
     * Initializes and plays animation.
     */
    @FXML
    private void beginTimeLine() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }

        timer = new TimeLineAnimationTimer();

        // Initializes red time-line.
        timeLine = new Line(0, 0, 0, 1270);

        timeLine.setStroke(Color.RED);

        // Adds time-line to composition panel.
        animationPane.getChildren().add(timeLine);

        var lastNote = NoteHelper.getLastFinishNote(notes);
        var trans = new TranslateTransition();
        var offset = lastNote.getOffset() + lastNote.getDuration();

        // Creates a translation animation that spans duration. Updates x variable of the line node at regular intervals
        // by setting x variable equal to the offset variable. Removes line once final note in composition is played.
        trans.setInterpolator(Interpolator.LINEAR);
        trans.setNode(timeLine);
        trans.setDuration(Duration.seconds(offset / 100d));
        trans.setFromX(0);
        trans.setToX(offset);
        trans.setOnFinished(e -> {
            animationPane.getChildren().remove(timeLine);
            isAnimationRunning = false;
            timer.stop();
            NoteHelper.unselectAll(notes);
            NoteHelper.unhideAll(notes);
        });

        // Animates line.
        isAnimationRunning = true;
        trans.play();
        timer.start();
    }

    /**
     * Adds note to composition panel.
     *
     * @param event mouse click event.
     */
    @FXML
    public void addNote(MouseEvent event) {
        if (isAnimationRunning) return;
        if (event.getButton() != MouseButton.PRIMARY || event.getClickCount() != 1) return;

        var posX = (int) event.getX();
        var posY = (int) event.getY();
        var posYRound = posY - (posY % 10) + 10;

        var note = NoteHelper.getNote(selectedInstrument, posYRound, posX, 100);

        NoteHelper.unselectAll(notes);
        note.select();

        notePane.getChildren().add(note.getNode());
        notes.add(note);

        isContentChanged = true;
        operationHistoryHelper.setCurrent(notes);
    }

    /**
     * Handles selection of play button.
     */
    @FXML
    public void play() {
        if (notes.isEmpty()) return;
        if (player.isRunning()) {
            player.stop();
        }

        setupNotes(notes);
        player.play();
        beginTimeLine();
    }

    /**
     * Plays only the selected notes.
     */
    @FXML
    public void playSelected() {
        var selectedNotes = NoteHelper.getAllSelectedNotes(notes);

        NoteHelper.hideAll(notes);
        NoteHelper.unhideAll(selectedNotes);

        if (selectedNotes.isEmpty()) return;
        if (player.isRunning()) {
            player.stop();
        }

        setupNotes(selectedNotes);
        player.play();
        beginTimeLine();
    }


    /**
     * Handles note dragging, through mouse dragging of rectangles. Sets new rectangle position to last dragged
     * position.
     */
    private void setupDrag() {

        selectionRectangle.setStroke(Color.BLACK);

        notePane.setOnMousePressed(new OnMousePressedEventHandler());
        notePane.setOnMouseDragged(new OnMouseDraggedEventHandler());
        notePane.setOnMouseReleased(new OnMouseReleasedEventHandler());
    }

    /**
     * Sets up user interface.
     */
    private void setupUI() {
        var rect = new Rectangle(2000, 1280);
        rect.setFill(Color.TRANSPARENT);

        linePane.getChildren().add(rect);

        for (var i = 1; i <= 128; i++) {
            var line = new Line();

            line.setTranslateY(i * 10);
            line.setEndX(2000);

            linePane.getChildren().add(line);
        }
    }

    private void setupNoteLabels() {
        var rect = new Rectangle(2000, 1280);
        rect.setFill(Color.TRANSPARENT);

        for (int n = 0; n < 10; n++) {
            Rectangle F_sharp = new Rectangle(0, 10 + (120 * n), 40, 10);
            labelPane.getChildren().add(F_sharp);
            Rectangle D_sharp = new Rectangle(0, 40 + (120 * n), 40, 10);
            labelPane.getChildren().add(D_sharp);
            Rectangle C_sharp = new Rectangle(0, 60 + (120 * n), 40, 10);
            labelPane.getChildren().add(C_sharp);
            Rectangle A_sharp = new Rectangle(0, 90 + (120 * n), 40, 10);
            labelPane.getChildren().add(A_sharp);
            Rectangle G_sharp = new Rectangle(0, 110 + (120 * n), 40, 10);
            labelPane.getChildren().add(G_sharp);
        }

        Rectangle F_sharp = new Rectangle(0, 10 + (120 * 10), 40, 10);
        labelPane.getChildren().add(F_sharp);
        Rectangle D_sharp = new Rectangle(0, 40 + (120 * 10), 40, 10);
        labelPane.getChildren().add(D_sharp);
        Rectangle C_sharp = new Rectangle(0, 60 + (120 * 10), 40, 10);
        labelPane.getChildren().add(C_sharp);
    }


    /**
     * Adds keyboard shortcuts and horizontal pitch lines to composition panel.
     */
    @FXML
    public void initialize() {
        setupDrag();
        setupUI();
        setupNoteLabels();

        Instance = this;
    }

    /**
     * Handles selection of Select All button.
     */
    @FXML
    public void selectAll() {
        NoteHelper.selectAll(notes);
    }


    /**
     * Handles selection of delete button.
     */
    @FXML
    public void delete() {
        var selectedNotes = NoteHelper.getAllSelectedNotes(notes);

        if (selectedNotes.isEmpty()) return;

        for (var note : selectedNotes) {
            deleteNote(note);
        }

        operationHistoryHelper.setCurrent(notes);
    }

    private void deleteNote(INote note) {
        notes.remove(note);
        notePane.getChildren().remove(note.getNode());
        if (note instanceof GroupNote groupNote) {
            for (var innerNote : groupNote.getChildren()) {
                deleteNote(innerNote);
            }
        }

        isContentChanged = true;
    }

    /**
     * Handles instrument change.
     */
    @FXML
    public void instrumentChange() {
        var selectedNotes = NoteHelper.getAllSelectedNotes(notes).stream().filter(n -> n instanceof BasicNote).toList();

        for (var note : selectedNotes) {
            notes.remove(note);
            notePane.getChildren().remove(note.getNode());
        }

        for (var note : NoteHelper.changeInstrument(selectedNotes, selectedInstrument)) {
            notes.add(note);
            notePane.getChildren().add(note.getNode());
        }

        NoteHelper.unselectAll(selectedNotes);
        isContentChanged = true;

        operationHistoryHelper.setCurrent(notes);
    }

    /**
     * Handles selection of stop button.
     */
    @FXML
    public void stop() {
        if (timeLine == null) return;
        if (timer != null)
            timer.stop();

        animationPane.getChildren().remove(timeLine);
        NoteHelper.unhideAll(notes);
        NoteHelper.unselectAll(notes);
        player.stop();
    }

    /**
     * Handles selection of exit button
     */
    @FXML
    public void exit() {
        var exit = new Alert(Alert.AlertType.CONFIRMATION);
        exit.setTitle("Exit");
        exit.setHeaderText("Do you want to save before exiting?");

        var okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        var noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        var cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        exit.getButtonTypes().setAll(okButton, noButton, cancelButton);
        exit.showAndWait().ifPresent(type -> {
            if (type == okButton) {
                save();
                exit();
                return;
            }

            if (type == noButton) {
                System.exit(0);
            }
        });
    }


    /**
     * Handles group the notes.
     */
    @FXML
    public void group() {
        var selectedNotes = NoteHelper.getAllSelectedNotes(notes);

        if (selectedNotes.isEmpty()) return;

        var group = new GroupNote(selectedNotes);

        group.select();

        notes.add(group);
        notePane.getChildren().add(group.getNode());

        isContentChanged = true;

        operationHistoryHelper.setCurrent(notes);
    }

    /**
     * Handles ungroup the notes.
     */
    @FXML
    public void ungroup() {
        var selectedNotes = NoteHelper.getAllSelectedNotes(notes);

        if (selectedNotes.isEmpty()) return;

        for (var note : selectedNotes) {
            if (!(note instanceof GroupNote groupNote)) continue;

            for (var gNote : groupNote.getChildren()) {
                ((IComposable) gNote).setIsInGroup(false);
                gNote.unselect();
            }

            for (var gNote : groupNote.getChildren()) {
                gNote.select();
            }

            notePane.getChildren().remove(note.getNode());
            notes.remove(note);
        }

        isContentChanged = true;

        operationHistoryHelper.setCurrent(notes);
    }

    public void copy() {
        var selectedNotes = NoteHelper.getAllSelectedNotes(notes);
        var jsonStr = JsonHelper.ToJson(selectedNotes);

        ClipboardContent content = new ClipboardContent();
        content.putString(jsonStr);

        systemClipboard.setContent(content);
    }

    public void cut() {
        copy();
        delete();
    }

    public void clearAll() {
        selectAll();
        delete();
    }

    private void addNote(INote note) {
        note.restoreTypeInfo();
        notes.add(note);

        if (note instanceof GroupNote groupNote) {
            for (var innerNote : groupNote.getChildren()) {
                addNote(innerNote);
            }
        }

        notePane.getChildren().add(note.getNode());
        isContentChanged = true;
    }

    public void paste() {
        var selectedNotes = NoteHelper.getAllSelectedNotes(notes);
        for (var note : selectedNotes) {
            note.unselect();
        }

        var clipboardNotes = systemClipboard.getString();

        if (clipboardNotes != null && clipboardNotes.length() != 0) {
            var newNotes = JsonHelper.ToObject(clipboardNotes, INote[].class);

            if (newNotes == null || newNotes.length == 0) return;

            for (var note : newNotes) {
                addNote(note);
                note.select();
            }
        }

        operationHistoryHelper.setCurrent(notes);
    }


    public void about() {
        AlertHelper.show(Alert.AlertType.INFORMATION, "About", "Paul, Tina, Flora, and Jess", "Compose a musical tune!");
    }

    private void saveToFile(File file, String content) {
        var result = FileHelper.writeString(file, content);

        if (!result)
            AlertHelper.show(Alert.AlertType.ERROR, "Failed to save file", "Can not save the file.", "Failed to save the file to disk.");
    }

    private void resetToDefault() {
        openedFile = null;
        notes.clear();
        notePane.getChildren().clear();
    }

    public void newFile() {
        if (!isContentChanged) {
            resetToDefault();
            operationHistoryHelper.reset();
            return;
        }

        var dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Exit");
        dialog.setHeaderText("Do you want to save before create a new one?");

        var okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        var noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

        dialog.getButtonTypes().setAll(okButton, noButton);
        dialog.showAndWait().ifPresent(type -> {
            if (type == okButton) {
                save();
                return;
            }

            if (type == noButton) {
                resetToDefault();
                operationHistoryHelper.reset();
            }
        });
    }

    public void open() {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Files", "*.composer"));
        var file = fileChooser.showOpenDialog(MainApplication.stage);

        try {
            var content = FileHelper.readAllContent(file);
            var notesList = JsonHelper.ToObject(content, INote[].class);

            resetToDefault();
            operationHistoryHelper.reset();
            openedFile = file;

            for (var note : notesList) {
                addNote(note);
            }
        } catch (IOException | NullPointerException e) {
            AlertHelper.show(Alert.AlertType.ERROR, "Can not open the file", "", "Failed to open the file from disk.");
        }
    }

    public void save() {
        if (openedFile == null || !openedFile.canWrite()) {
            saveAs();
            return;
        }

        saveToFile(openedFile, JsonHelper.ToJson(notes));

        isContentChanged = false;
    }

    public void saveAs() {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Save As");
        fileChooser.setInitialFileName("no_name.composer");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Files", "*.composer"));
        var file = fileChooser.showSaveDialog(MainApplication.stage);

        if (file != null) {
            saveToFile(file, JsonHelper.ToJson(notes));
        }

        openedFile = file;
        isContentChanged = false;
    }

    public void undo() {
        var lastState = operationHistoryHelper.undo();

        notes.clear();
        notePane.getChildren().clear();

        if (lastState == null) return;

        for (var note : lastState) {
            if (note instanceof BasicNote basicNote)
                if (basicNote.getIsInGroup())
                    continue;

            if (note.getIsSelected())
                note.select();
            else
                note.unselect();

            addNote(note);
        }
    }

    public void redo() {
        var lastState = operationHistoryHelper.redo();

        notes.clear();
        notePane.getChildren().clear();

        if (lastState == null) return;

        for (var note : lastState) {
            if (note instanceof BasicNote basicNote)
                if (basicNote.getIsInGroup())
                    continue;

            if (note.getIsSelected())
                note.select();
            else
                note.unselect();

            addNote(note);
        }
    }

    /**
     * Sets channel of notes.
     */
    private void setChannel(INote note) {
        player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + note.getChannel(), note.getInstrument(), 0, note.getOffset(), 0);
    }


    @FXML
    public void volume() {
        TextInputDialog volume = new TextInputDialog("");
        volume.setTitle("Volume Selector");
        volume.setHeaderText("Enter the Volume Level (0-127)");
        volume.showAndWait();
        String x = volume.getResult();
        try {
            if (!Objects.equals(x, ""))
                VOLUME = Integer.parseInt(x);
        } catch (NumberFormatException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("Not a valid input, please type a number between 0-127");
            error.setHeaderText("ERROR");
            error.show();
        }
    }


    /**
     * Adds notes to composition.
     */
    private void setupNotes(List<INote> notes) {
        player.clear();

        for (var note : notes) {
            if (!(note instanceof BasicNote basicNote)) continue;

            setChannel(basicNote);
            player.addNote(basicNote.getPitch(), VOLUME, basicNote.getOffset(),
                    basicNote.getDuration(), basicNote.getChannel(), 0);
        }
    }


    public void gitLink() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/WhitmanCS370/project-3-PaulTinaFloraJess"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void volumeSlider() {
        volumeSlider.valueProperty().addListener((observableValue, number, t1) -> {
            System.out.println(VOLUME);
            VOLUME = (int) volumeSlider.getValue();
        });
    }
}
