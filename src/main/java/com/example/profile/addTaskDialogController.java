package com.example.profile;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller per la finestra 'add-task-dialog.fxml'.
 * Contiene un campo di testo e bottoni "Conferma" e "Annulla".
 * * NOTA: Il nome di questa classe dovrebbe essere 'AddTaskDialogController'
 * (con la 'A' maiuscola) per seguire le convenzioni Java.
 */
public class addTaskDialogController { // <-- Dovrebbe chiamarsi AddTaskDialogController

    @FXML private TextField taskInput;

    // NOTA: Il nome della classe qui deve corrispondere al tuo file:
    //       'AddTaskController' (con la 'A' maiuscola).
    private addTaskController listController;

    private Stage dialogStage;

    /**
     * Metodo chiamato da AddTaskController per passare un riferimento
     * al controller della lista.
     */
    public void setListController(addTaskController listController) {
        this.listController = listController;
    }

    /**
     * Metodo chiamato da AddTaskController per passare un riferimento
     * allo Stage (la finestra) del dialogo, per poterla chiudere.
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize() {
        // Vuoto, nessuna inizializzazione speciale richiesta
    }

    /**
     * Chiamato quando l'utente clicca "Conferma".
     */
    @FXML
    private void handleConfirm() {
        String taskName = taskInput.getText();

        if (taskName != null && !taskName.trim().isEmpty()) {
            // Ora puoi chiamare 'addTask' direttamente
            if (listController != null) {
                listController.addTask(taskName.trim());
            } else {
                System.err.println("Errore: ListController non impostato.");
            }
            dialogStage.close(); // Chiude la finestra popup
        }
    }

    /**
     * Chiamato quando l'utente clicca "Annulla".
     */
    @FXML
    private void handleCancel() {
        dialogStage.close(); // Chiude la finestra popup
    }
}