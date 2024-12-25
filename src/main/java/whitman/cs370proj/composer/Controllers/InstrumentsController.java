package whitman.cs370proj.composer.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import whitman.cs370proj.composer.Models.InstrumentType;

/**
 * Sets up instrument controller.
 */
public class InstrumentsController {
    public static InstrumentsController Instance;
    private final ToggleGroup toggleGroup = new ToggleGroup();

    /**
     * Defines instrument buttons.
     */
    @FXML
    public RadioButton FrenchHorn, Violin, Guitar, Accordion, ChurchOrgan, Marimba, Harpsichord, Piano;

    /**
     * Initializes toggle group.
     */
    @FXML
    public void initialize() {
        setupToggleGroup();

        Instance = this;
    }


    /**
     * Sets up toggle between instruments.
     */
    public void setupToggleGroup() {
        Piano.setToggleGroup(toggleGroup);
        Harpsichord.setToggleGroup(toggleGroup);
        Marimba.setToggleGroup(toggleGroup);
        ChurchOrgan.setToggleGroup(toggleGroup);
        Accordion.setToggleGroup(toggleGroup);
        Guitar.setToggleGroup(toggleGroup);
        Violin.setToggleGroup(toggleGroup);
        FrenchHorn.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            var rb = (RadioButton) toggleGroup.getSelectedToggle();
            var selectedInstrument = InstrumentType.valueOf(rb.getId());

            MainController.Instance.setSelectedInstrument(selectedInstrument);
        });
    }
}
