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
    private final ToggleGroup toggleGroup = new ToggleGroup();


    @FXML private ToggleButton banner1;
    @FXML private ToggleButton banner2;
    @FXML private ToggleButton banner3;
    @FXML private ToggleButton banner4;
    private final ToggleGroup toggleBannerGroup = new ToggleGroup();


    @FXML private StackPane picChooserPane;

    private GridPane blurredPane;
    private profileController mainController;


    public void initialize() {
        //Toggles per l'immagine profilo
        pic1.setToggleGroup(toggleGroup);
        pic2.setToggleGroup(toggleGroup);
        pic3.setToggleGroup(toggleGroup);
        pic4.setToggleGroup(toggleGroup);

        pic1.setUserData("@images/chr_icon_1052.png");
        pic2.setUserData("@images/chr_icon_1007.png");
        pic3.setUserData("@images/chr_icon_1025.png");
        pic4.setUserData("@images/chr_icon_1053.png");

        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                toggleGroup.selectToggle(oldToggle);
            }
        });

        //Toggles per il banner
        banner1.setToggleGroup(toggleBannerGroup);
        banner2.setToggleGroup(toggleBannerGroup);
        banner3.setToggleGroup(toggleBannerGroup);
        banner4.setToggleGroup(toggleBannerGroup);

        banner1.setUserData("@images/Banner1.png");
        banner2.setUserData("@images/Banner2.png");
        banner3.setUserData("@images/Banner3.jpg");
        banner4.setUserData("@images/Banner4.jpg");

        toggleBannerGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                toggleBannerGroup.selectToggle(oldToggle);
            }
        });
    }

    public void initData(profileController mainController, GridPane mainContentPane, String currentAvatarUrl, String currentBannerUrl) {
        this.mainController = mainController;
        this.blurredPane = mainContentPane;

        //serve ad avere sempre selezionata la propria immagine profilo
        for (Toggle toggle : toggleGroup.getToggles()) {
            ToggleButton button = (ToggleButton) toggle;
            String buttonUrl = (String) button.getUserData();

            if (buttonUrl != null && buttonUrl.equals(currentAvatarUrl)) {
                button.setSelected(true);
                break;
            }
        }

        //serve ad avere sempre selezionato il proprio banner
        for (Toggle toggle : toggleBannerGroup.getToggles()) {
            ToggleButton button = (ToggleButton) toggle;
            String buttonUrl = (String) button.getUserData();

            if (buttonUrl != null && buttonUrl.equals(currentBannerUrl)) {
                button.setSelected(true);
                break;
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
        }

        // Rimuove l'effetto blur e riattiva il pannello principale
        if (blurredPane != null) {
            blurredPane.setEffect(null);
            blurredPane.setDisable(false);
        }

        StackPane parentPane = (StackPane) picChooserPane.getParent();
        parentPane.getChildren().remove(picChooserPane);
    }

    @FXML
    private void handleConfirmBannerClick(ActionEvent event) {

        ToggleButton selected = (ToggleButton) toggleBannerGroup.getSelectedToggle();

        if (selected != null) {
            String imageUrl = (String) selected.getUserData();
            mainController.updateBannerPicture(imageUrl);

            //salva l'immagine
            Preferences prefs = Preferences.userNodeForPackage(profileController.class);
            prefs.put("banner_url", imageUrl);
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
