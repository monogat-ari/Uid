package com.example.profile;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class homeController implements Initializable {

    @FXML private StackPane rootStack;
    @FXML private BorderPane rootPane;
    @FXML private javafx.scene.control.Label soldiLabel;
    @FXML private ImageView backgroundImageView;

    @FXML private ImageView profilePicImageView;

    private Image currentBackgroundImage = null;

    private HelloApplication mainApp;
    private Stage primaryStage;

    public void setMainApp(HelloApplication mainApp) {
        this.mainApp = mainApp;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Image started = BackgroundService.getInstance().getBackground();
        if (started != null) {
            applyBackground(rootStack != null ? rootStack : rootPane, started);
            applyBackground(backgroundImageView, started);
            currentBackgroundImage = started;
        }

        // Ascolta cambiamenti del BackgroundService
        BackgroundService.getInstance().backgroundProperty().addListener((obs, oldImg, newImg) -> {
            if (newImg != null) {
                Region target = (rootStack != null) ? rootStack : rootPane;
                applyBackground(target, newImg);
                applyBackground(backgroundImageView, newImg);
            }
        });

        //Imposta l'immagine iniziale caricandola dall'URL nel service
        String initialUrl = UserProfileService.getInstance().getProfileImageUrl();
        if (initialUrl != null) {
            // Nota: uso substring(1) per rimuovere la "@" iniziale e mettere "/"
            Image initialImage = new Image(getClass().getResourceAsStream(initialUrl.substring(1)));
            profilePicImageView.setImage(initialImage);
        }

        // Si mette in ascolto per futuri cambiamenti
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
    public void showAddTask(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-task-view.fxml"));
            Parent addTaskRoot = loader.load();

            addTaskController atc = loader.getController();
            atc.setParentComponents(this.rootStack, this.rootPane);

            GaussianBlur blur = new GaussianBlur(10);
            rootPane.setEffect(blur);

            rootPane.setDisable(true);
            rootStack.getChildren().add(addTaskRoot);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showBoss(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("boss.fxml"));
            Parent bossRoot = loader.load();

            bossController bc = loader.getController();
            Scene homeScene = ((Node) event.getSource()).getScene();
            bc.setHomeScene(homeScene);

            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(new Scene(bossRoot));
            appStage.show();

        } catch (IOException e) {
            System.err.println("Errore durante il caricamento di profile.fxml");
            e.printStackTrace();
        } catch (Exception e) {
            // catch generico in caso 'ProfileController' sia null o altro
            System.err.println("Errore generico in showProfile:");
            e.printStackTrace();
        }
    }


    @FXML
    public void showShop(ActionEvent event) throws IOException {
        PauseTransition delay = new PauseTransition(javafx.util.Duration.millis(0)); // 100ms
        delay.setOnFinished(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("shop.fxml"));
                Parent shopRoot = loader.load();

                shopController sc = loader.getController();
                Scene homeScene = ((Node) event.getSource()).getScene();
                sc.setHomeScene(homeScene);

                Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                appStage.setScene(new Scene(shopRoot));
                appStage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        delay.play();
    }

    @FXML
    public void showProfile(ActionEvent event) throws IOException {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("profile.fxml"));
            Parent profileRoot = loader.load();


            profileController pc = loader.getController();
            Scene homeScene = ((Node) event.getSource()).getScene();
            pc.setHomeScene(homeScene);

            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(new Scene(profileRoot));
            appStage.show();

        } catch (IOException e) {
            System.err.println("Errore durante il caricamento di profile.fxml");
            e.printStackTrace();
        } catch (Exception e) {
            // Aggiungo un catch generico in caso 'ProfileController' sia null o altro
            System.err.println("Errore generico in showProfile:");
            e.printStackTrace();
        }
    }

    @FXML
    public void showCloset(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("closet.fxml"));
            Parent closetRoot = loader.load();


            closetController cc = loader.getController();
            Scene homeScene = ((Node) event.getSource()).getScene();
            cc.setHomeScene(homeScene);

            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(new Scene(closetRoot));
            appStage.show();

        } catch (IOException e) {
            System.err.println("Errore durante il caricamento di profile.fxml");
            e.printStackTrace();
        } catch (Exception e) {
            // catch generico in caso 'ProfileController' sia null o altro
            System.err.println("Errore generico in showProfile:");
            e.printStackTrace();
        }
    }

    private void applyBackground(ImageView imageView, Image image) {
        if (imageView != null && image != null) {
            imageView.setImage(image);
            currentBackgroundImage = image;
        }
    }


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
