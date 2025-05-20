package it.polimi.ingsw.view.graphic;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class GUIController {

    @FXML
    private TextField numberField;

    @FXML
    private Button submitButton;

    @FXML
    public void initialize() {
        // logica iniziale dopo il caricamento dell'FXML
    }

    @FXML
    private void onSubmit() {
        String input = numberField.getText().trim();
        System.out.println("Numero inserito: " + input);
        // Puoi anche invocare setCurrentEvent(...) da qui se hai un riferimento a GUI
    }
}