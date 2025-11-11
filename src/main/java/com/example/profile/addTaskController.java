package com.example.profile;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.swing.*;


/**
 * Controller per la finestra 'add-task-view.fxml'.
 * Mostra la lista dei task e ha un bottone per aggiungerne di nuovi.
 */
public class addTaskController {

    @FXML private VBox taskContainer;  // Il VBox che contiene i CheckBox
    @FXML private Label emptyLabel;    // L'etichetta "Nessun task"
    @FXML private Button backButton;    // Il bottone "Aggiungi"
    private Scene homeScene;
    @FXML private BorderPane rootPane;


    @FXML private StackPane addTaskRootPane;
    private StackPane parentStack;
    private BorderPane homeRootPane;

    public void setParentStack(StackPane stack) {
        this.parentStack = stack;
    }

    public void setHomeScene(Scene scene) {
        this.homeScene = scene;
    }

    @FXML
    private void initialize() {
        // Controlla subito se la lista è vuota
        updateEmptyLabel();
    }

    @FXML
    private void handleAddButton() {
        try {
            // 1. Carica l'FXML del dialogo DA SOLO
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-task-dialog.fxml"));
            Parent root = loader.load();

            // 2. Ottieni il controller del dialogo
            addTaskDialogController dialogController = loader.getController(); // 'A' Maiuscola

            // 3. Passa QUESTO controller (this) al dialogo
            dialogController.setListController(this);

            // 4. Crea e mostra la nuova finestra (Stage)
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Aggiungi Nuovo Task");
            dialogStage.setScene(new Scene(root));

            // 5. Passa lo Stage al dialogo (per permettergli di chiudersi)
            dialogController.setDialogStage(dialogStage);

            dialogStage.showAndWait(); // Mostra e aspetta

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setParentComponents(StackPane stack, BorderPane homePane) {
        this.parentStack = stack;
        this.homeRootPane = homePane; // <-- Salva il riferimento
    }

    public void addTask(String taskName) {
        if (taskName != null && !taskName.isBlank()) {

            // Crea un nuovo CheckBox per il task
            CheckBox checkBox = new CheckBox(taskName);

            // Imposta uno stile (testo nero) per evitare che sia bianco su sfondo bianco
            checkBox.setStyle("-fx-text-fill: black; -fx-font-size: 14px;");

            // Aggiungi alla lista
            this.taskContainer.getChildren().add(checkBox);

            // Aggiorna lo stato dell'etichetta "vuoto"
            updateEmptyLabel();
        }
    }

    /**
     * Controlla se ci sono task e mostra/nasconde l'etichetta "vuoto".
     */
    private void updateEmptyLabel() {
        // Controlla se ci sono più figli oltre all'etichetta stessa.
        // (Questo presume che 'emptyLabel' sia un figlio di 'taskContainer')
        boolean hasTasks = taskContainer.getChildren().size() > 1;

        emptyLabel.setVisible(!hasTasks);
        emptyLabel.setManaged(!hasTasks);
    }

    @FXML
    private void setBackButton(ActionEvent event) {
        // 3. USA LA VARIABILE CORRETTA
        if(homeRootPane != null){
            homeRootPane.setEffect(null);  // Rimuovi il blur
            homeRootPane.setDisable(false); // Riabilita i click
        } else {
            System.err.println("homeRootPane è null!");
        }

        // Questa parte ora usa le variabili corrette
        if (parentStack != null) {
            parentStack.getChildren().remove(addTaskRootPane);
        } else {
            System.err.println("parentStack è null!");
        }
    }
}
