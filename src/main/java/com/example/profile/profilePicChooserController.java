package com.example.profile;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.prefs.Preferences;

public class profilePicChooserController {

    @FXML private ToggleButton pic1;
    @FXML private ToggleButton pic2;
    @FXML private ToggleButton pic3;
    @FXML private ToggleButton pic4;

    @FXML private StackPane picChooserPane;

    private GridPane blurredPane;
    private profileController mainController;
    private final ToggleGroup toggleGroup = new ToggleGroup();

    public void initialize() {
        pic1.setToggleGroup(toggleGroup);
        pic2.setToggleGroup(toggleGroup);
        pic3.setToggleGroup(toggleGroup);
        pic4.setToggleGroup(toggleGroup);

        pic1.setUserData("@images/chr_icon_1052.png");
        pic2.setUserData("@images/chr_icon_1007.png");
        pic3.setUserData("@images/chr_icon_1025.png");
        pic4.setUserData("@images/chr_icon_1053.png");
    }

    public void initData(profileController mainController, GridPane mainContentPane, String currentAvatarUrl) {
        this.mainController = mainController;
        this.blurredPane = mainContentPane;

        //serve ad avere sempre selezionata la propria immagine profilo
        for (Toggle toggle : toggleGroup.getToggles()) {
            ToggleButton button = (ToggleButton) toggle;
            String buttonUrl = (String) button.getUserData();

            if (buttonUrl != null && buttonUrl.equals(currentAvatarUrl)) {
                // Seleziona il bottone corrispondente
                button.setSelected(true);
                break; // Trovato, esci dal ciclo
            }
        }
    }

    @FXML
    private void handleConfirmClick(ActionEvent event) {

        ToggleButton selected = (ToggleButton) toggleGroup.getSelectedToggle();

        if (selected != null) {
            String imageUrl = (String) selected.getUserData();
            mainController.updateProfilePicture(imageUrl);

            //salva l'immagine
            Preferences prefs = Preferences.userNodeForPackage(profileController.class);
            prefs.put("avatar_url", imageUrl);
            mainController.updateProfilePicture(imageUrl);
        }

        // Rimuove l'effetto blur e riattiva il pannello principale
        if (blurredPane != null) {
            blurredPane.setEffect(null);
            blurredPane.setDisable(false);
        }

        StackPane parentPane = (StackPane) picChooserPane.getParent();
        parentPane.getChildren().remove(picChooserPane);
    }
}
