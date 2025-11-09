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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class bossController {

    // Inietta gli elementi dall'FXML
    @FXML private Pane flashPane;
    @FXML private Button battleButton;

    // La durata di ogni singolo flash (in millisecondi)
    private double FLASH_DURATION_MS = 80;

    // Durata dell'ultimo flash nero (1 secondo)
    private double LAST_FLASH_DURATION_MS = 1000;

    /**
     * Questo metodo è chiamato dal pulsante "Battaglia" (definito in onAction).
     */
    @FXML
    void handleBattleButton(ActionEvent event) {

        battleButton.setDisable(true);
        flashPane.setVisible(true);

        double time1 = FLASH_DURATION_MS; // T=80ms
        double time2 = time1 + FLASH_DURATION_MS; // T=160ms
        double time3 = time2 + FLASH_DURATION_MS; // T=240ms (inizio ultimo flash)
        double time4_fine = time3 + LAST_FLASH_DURATION_MS; // T=240 + 1000 = 1240ms

        // 3. Crea la Timeline per l'animazione
        Timeline timeline = new Timeline(

                // T=0ms : Imposta a BIANCO
                new KeyFrame(Duration.ZERO, e -> {
                    flashPane.setStyle("-fx-background-color: white;");
                    flashPane.setBlendMode(BlendMode.DIFFERENCE); // <-- 2. ECCO L'INVERSIONE!
                }),

                // T=80ms : NERO (Resettando il BlendMode)
                new KeyFrame(Duration.millis(time1), e -> {
                    flashPane.setBlendMode(null); // <-- 3. RESETTA IL BLENDMODE
                    flashPane.setStyle("-fx-background-color: black;");
                }),

                // T=160ms (80*2) : BIANCO (Normale)
                new KeyFrame(Duration.millis(time2), e -> {
                    // Il BlendMode è già null, quindi questo è un flash bianco normale
                    flashPane.setStyle("-fx-background-color: white;");
                }),

                // T=240ms (80*3) : NERO (Normale)
                new KeyFrame(Duration.millis(time3), e -> {
                    flashPane.setStyle("-fx-background-color: black;");
                }),

                new KeyFrame(Duration.millis(time4_fine))
        );

        // 4. Cosa fare QUANDO la timeline è finita
        timeline.setOnFinished(e -> {
            // Nascondi il pannello
            flashPane.setVisible(false);
            flashPane.setBlendMode(null);

            // Esegui la logica di transizione alla battaglia
            startBattle();
        });

        // 5. Avvia l'animazione!
        timeline.setDelay(Duration.millis(100));
        timeline.play();
    }

    /**
     * Metodo fittizio per la transizione
     */
    private void startBattle() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("bossBattle.fxml"));
            Parent bossBattleRoot = loader.load();

            // 2. IMPOSTA LA NUOVA SCENA COME TRASPARENTE (OPACITÀ = 0)
            bossBattleRoot.setOpacity(0.0);

            // 3. Ottieni la finestra (Stage) corrente
            Stage stage = (Stage) flashPane.getScene().getWindow();
            Scene bossScene = new Scene(bossBattleRoot);
            bossScene.getStylesheets().addAll(flashPane.getScene().getStylesheets());

            // 4. IMPOSTA LA NUOVA SCENA SULLO STAGE
            // Lo stage ora mostra una scena trasparente, quindi appare ancora nero
            stage.setScene(bossScene);
            stage.show();

            // 5. CREA E AVVIA L'ANIMAZIONE DI FADE-IN
            // Scegli una durata che ti piace (es. 700 millisecondi)
            FadeTransition fadeIn = new FadeTransition(Duration.millis(700), bossBattleRoot);
            fadeIn.setFromValue(0.0); // Opacità iniziale
            fadeIn.setToValue(1.0);   // Opacità finale
            fadeIn.play();

        } catch (IOException e) {
            System.err.println("Errore: Impossibile caricare bossbattle.fxml");
            e.printStackTrace();
            // Se fallisce, riabilita almeno il pulsante per riprovare
            battleButton.setDisable(false);
        }
    }
}

