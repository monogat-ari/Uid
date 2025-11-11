package com.example.profile;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class closetController implements Initializable {

    // Campi FXML da "Closet.fxml"
    @FXML private BorderPane closetRootPane;
    @FXML private Button BackButton;

    // Campi FXML dalle pagine "page_*.fxml" (cercati via lookup)
    @FXML private ToggleButton btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;

    // Variabili di stato
    private Scene homeScene; // Scena principale per tornare "Home"
    private final Map<String, Parent> pageCache = new HashMap<>();
    private final Map<String, String> idToFxml = Map.of(
            "crownBtn", "/com/example/profile/page_crown.fxml",
            "shirtBtn", "/com/example/profile/page_shirt.fxml",
            "talkBtn", "/com/example/profile/page_hair.fxml",
            "artBtn", "/com/example/profile/page_art.fxml"
    );

    /**
     * Metodo Inizializzatore.
     * Ascolta i cambiamenti dello sfondo per aggiornare il BORDERPANE dell'armadio.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Ascolta cambiamenti del BackgroundService
        BackgroundService.getInstance().backgroundProperty().addListener((obs, oldImg, newImg) -> {
            if (newImg != null) {
                applyBackground(closetRootPane, newImg);
            }
        });

        // Applica sfondo iniziale
        Image started = BackgroundService.getInstance().getBackground();
        if (started != null) {
            applyBackground(closetRootPane, started);
        }
    }

    /**
     * Metodo pubblico per "iniettare" la scena Home dal controller principale.
     * Questo è FONDAMENTALE per far funzionare il tasto "Indietro".
     */
    public void setHomeScene(Scene scene) {
        this.homeScene = scene;
    }

    /**
     * Gestisce i clic sul menu laterale (Corone, Maglie, ecc.)
     */
    @FXML
    private void handleMenu(ActionEvent event) {
        if (!(event.getSource() instanceof Node node)) return;
        String id = node.getId();
        if (id == null) return;

        String fxmlPath = idToFxml.get(id);
        if (fxmlPath != null) setCenterFromFxml(fxmlPath);
    }

    /**
     * Gestisce il clic sul pulsante "Indietro" (Home).
     */
    @FXML
    public void Home(ActionEvent event) {
        if (homeScene != null) {
            Stage currentStage = (Stage) BackButton.getScene().getWindow();
            currentStage.setScene(homeScene);
        } else {
            System.err.println("HomeScene non è stata impostata in ClosetController!");
        }
    }

    /**
     * Carica la pagina FXML nell'area centrale, usando la cache.
     */
    private void setCenterFromFxml(String resourcePath) {
        try {
            Parent page = pageCache.get(resourcePath);
            if (page == null) {
                var resource = getClass().getResource(resourcePath);
                if (resource == null) {
                    System.err.println("FXML non trovato: " + resourcePath);
                    return;
                }
                FXMLLoader loader = new FXMLLoader(resource);
                page = loader.load();
                pageCache.put(resourcePath, page);
            }

            // Cerca e assegna i ToggleButton dalla pagina caricata
            // Questo pezzo (il lookup) è ancora necessario perché
            // il controller dell'armadio gestisce anche i suoi sotto-componenti.
            try {
                Node n;
                n = page.lookup("#btn1"); if (n instanceof ToggleButton) btn1 = (ToggleButton) n;
                n = page.lookup("#btn2"); if (n instanceof ToggleButton) btn2 = (ToggleButton) n;
                n = page.lookup("#btn3"); if (n instanceof ToggleButton) btn3 = (ToggleButton) n;
                n = page.lookup("#btn4"); if (n instanceof ToggleButton) btn4 = (ToggleButton) n;
                n = page.lookup("#btn5"); if (n instanceof ToggleButton) btn5 = (ToggleButton) n;
                n = page.lookup("#btn6"); if (n instanceof ToggleButton) btn6 = (ToggleButton) n;
                n = page.lookup("#btn7"); if (n instanceof ToggleButton) btn7 = (ToggleButton) n;
                n = page.lookup("#btn8"); if (n instanceof ToggleButton) btn8 = (ToggleButton) n;
                n = page.lookup("#btn9"); if (n instanceof ToggleButton) btn9 = (ToggleButton) n;
            } catch (Exception ex) { ex.printStackTrace(); }

            // Imposta la pagina nel centro del BorderPane
            if (closetRootPane != null) closetRootPane.setCenter(page);

            // Aggiunge i listener ai bottoni trovati
            attachListenersToButtonsIfPresent();
        } catch (IOException e) { e.printStackTrace(); }
    }


    // --- Logica per i ToggleButton (btn1, btn2...) ---

    private void attachListenersToButtonsIfPresent() {
        ToggleButton[] buttons = {btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9};
        for (ToggleButton tb : buttons) {
            if (tb == null) continue;

            ImageView iv = findImageView(tb.getGraphic());
            if (iv != null) iv.setMouseTransparent(true);

            Object already = tb.getProperties().get("bg-listener-attached");
            if (!Boolean.TRUE.equals(already)) {
                attachSelectionListener(tb);
                tb.getProperties().put("bg-listener-attached", Boolean.TRUE);
            }
        }
    }

    private void attachSelectionListener(ToggleButton tb) {
        tb.selectedProperty().addListener((obs, oldVal, newVal) -> {
            ImageView iv = findImageView(tb.getGraphic());
            if (iv != null) {
                iv.setCache(false);
                if (newVal) {
                    // Applica effetto "scuro" per selezione
                    ColorAdjust darken = new ColorAdjust();
                    darken.setBrightness(-0.6);
                    iv.setEffect(darken);

                    Image img = iv.getImage();
                    if (img != null) {
                        // LA NUOVA RESPONSABILITÀ:
                        // Notifica il servizio che lo sfondo è cambiato.
                        // Sarà poi HelloController (e questo stesso controller)
                        // a reagire a questo cambiamento.
                        try {
                            BackgroundService.getInstance().setBackground(img);
                        } catch (Throwable ignored) {}
                    }
                } else {
                    // Rimuovi effetto
                    iv.setEffect(null);
                }
            }
        });
    }

    private ImageView findImageView(Node node) {
        if (node instanceof ImageView iv) return iv;
        if (node instanceof Parent p) {
            for (Node child : p.getChildrenUnmodifiable()) {
                ImageView result = findImageView(child);
                if (result != null) return result;
            }
        }
        return null;
    }

    // --- Metodi Utilità ---

    private void applyBackground(Region region, Image image) {
        if (region == null || image == null) return;
        BackgroundSize bs = new BackgroundSize(1.0, 1.0, true, true, false, true);
        BackgroundImage bi = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bs);
        region.setBackground(new Background(bi));
    }
}