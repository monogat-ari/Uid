package com.example.profile;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class bossController {

    // Inietta gli elementi dall'FXML
    @FXML private Button backButton;
    @FXML private Pane flashPane;
    @FXML private Button battleButton;

    @FXML private ImageView profilePicImageView;

    private Scene homeScene;

    private double FLASH_DURATION_MS = 120; // Durata minima singolo flash
    private double LAST_FLASH_DURATION_MS = 1000; // Durata dell'ultimo flash nero (1 secondo)

    @FXML
    private void initialize() {
        // 1. Imposta l'immagine iniziale caricandola dall'URL nel service
        String initialUrl = UserProfileService.getInstance().getProfileImageUrl();
        if (initialUrl != null) {
            // Nota: uso substring(1) per rimuovere la "@" iniziale e mettere "/"
            Image initialImage = new Image(getClass().getResourceAsStream(initialUrl.substring(1)));
            profilePicImageView.setImage(initialImage);
        }

        // 2. Mettiti in ascolto per futuri cambiamenti
        UserProfileService.getInstance().profileImageUrlProperty().addListener((obs, oldUrl, newUrl) -> {
            if (newUrl != null && !newUrl.isEmpty()) {
                try {
                    // Rimuovi la "@" e caricala come risorsa
                    Image newImage = new Image(getClass().getResourceAsStream(newUrl.substring(1)));
                    profilePicImageView.setImage(newImage);
                } catch (Exception e) {
                    System.err.println("Impossibile caricare l'immagine: " + newUrl);
                }
            }
        });
    }

    @FXML
    void handleBattleButton(ActionEvent event) {

        battleButton.setDisable(true);
        flashPane.setVisible(true);

        double time1 = FLASH_DURATION_MS; // T=120ms
        double time2 = time1 + FLASH_DURATION_MS; // T=200ms
        double time3 = time2 + FLASH_DURATION_MS; // T=280ms (inizio ultimo flash)
        double time4_fine = time3 + LAST_FLASH_DURATION_MS; // T=280 + 1000 = 1280ms

        //Timeline per l'animazione
        Timeline timeline = new Timeline(

                new KeyFrame(Duration.ZERO, e -> {
                    flashPane.setStyle("-fx-background-color: white;");
                    flashPane.setBlendMode(BlendMode.DIFFERENCE); // inversione colori
                }),

                new KeyFrame(Duration.millis(time1), e -> {
                    flashPane.setBlendMode(null); // reset inversione colori
                    flashPane.setStyle("-fx-background-color: black;");
                }),

                new KeyFrame(Duration.millis(time2), e -> {
                    flashPane.setStyle("-fx-background-color: white;");
                }),

                new KeyFrame(Duration.millis(time3), e -> {
                    flashPane.setStyle("-fx-background-color: black;");
                }),

                new KeyFrame(Duration.millis(time4_fine))
        );

        // fine timeline
        timeline.setOnFinished(e -> {
            flashPane.setVisible(false);
            flashPane.setBlendMode(null);

            startBattle();
        });

        timeline.setDelay(Duration.millis(100));
        timeline.play();
    }

    //cambio scena dopo la timeline
    private void startBattle() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("bossBattle.fxml"));
            Parent bossBattleRoot = loader.load();

            bossBattleRoot.setOpacity(0.0);

            Stage stage = (Stage) flashPane.getScene().getWindow();
            Scene bossScene = new Scene(bossBattleRoot);
            bossScene.getStylesheets().addAll(flashPane.getScene().getStylesheets());

            stage.setScene(bossScene);
            stage.show();

            FadeTransition fadeIn = new FadeTransition(Duration.millis(700), bossBattleRoot);
            fadeIn.setFromValue(0.0); // Opacità iniziale
            fadeIn.setToValue(1.0);   // Opacità finale
            fadeIn.play();

        } catch (IOException e) {
            System.err.println("Errore: Impossibile caricare bossBattle.fxml");
            e.printStackTrace();
            battleButton.setDisable(false);
        }
    }

    public void setHomeScene(Scene scene) {
        this.homeScene = scene;
    }

    @FXML
    public void Home() {
        if (homeScene != null) {
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.setScene(homeScene);
        } else {
            System.err.println("⚠ Nessuna scena Home disponibile!");
        }
    }
}

