package com.example.profile;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.prefs.Preferences;

public class UserProfileService {

    private static final UserProfileService instance = new UserProfileService();

    // Usiamo StringProperty per gli URL, come nel tuo codice
    private final StringProperty profileImageUrl = new SimpleStringProperty();
    private final StringProperty bannerImageUrl = new SimpleStringProperty();

    private UserProfileService() {
        // Carica gli URL salvati all'avvio
        Preferences prefs = Preferences.userNodeForPackage(profileController.class);
        String savedAvatar = prefs.get("avatar_url", "@images/chr_icon_1052.png"); // Default
        String savedBanner = prefs.get("banner_url", "@images/Banner1.png"); // Default

        profileImageUrl.set(savedAvatar);
        bannerImageUrl.set(savedBanner);
    }

    public static UserProfileService getInstance() {
        return instance;
    }

    // --- Metodi per FOTO PROFILO ---
    public StringProperty profileImageUrlProperty() {
        return profileImageUrl;
    }
    public String getProfileImageUrl() {
        return profileImageUrl.get();
    }
    public void setProfileImageUrl(String url) {
        profileImageUrl.set(url);
    }

    // --- Metodi per BANNER ---
    public StringProperty bannerImageUrlProperty() {
        return bannerImageUrl;
    }
    public String getBannerImageUrl() {
        return bannerImageUrl.get();
    }
    public void setBannerImageUrl(String url) {
        bannerImageUrl.set(url);
    }
}