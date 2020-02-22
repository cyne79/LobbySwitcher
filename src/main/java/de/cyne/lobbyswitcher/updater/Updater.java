package de.cyne.lobbyswitcher.updater;

import de.cyne.lobbyswitcher.LobbySwitcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Updater {

    private long resourceId;
    private String latestVersion;
    private String currentVersion;
    private UpdateResult updateResult;

    public Updater(long resourceId) {
        this.resourceId = resourceId;
        this.currentVersion = LobbySwitcher.getInstance().getDescription().getVersion();
    }

    public enum UpdateResult {
        UPDATE_AVAILABLE, NO_UPDATE, CONNECTION_ERROR
    }

    public void checkLatestVersion() {
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId)
                    .openConnection();
            this.latestVersion = new BufferedReader(new InputStreamReader(httpConnection.getInputStream())).readLine();
        } catch (IOException e) {
            this.setUpdateResult(UpdateResult.CONNECTION_ERROR);
        }
    }

    public void compareVersions() {
        long currentVersionCompact = Long.parseLong(currentVersion.replace(".", ""));
        long latestVersionCompact = Long.parseLong(latestVersion.replace(".", ""));

        if (currentVersionCompact == latestVersionCompact) {
            this.setUpdateResult(UpdateResult.NO_UPDATE);
            return;
        }
        this.setUpdateResult(UpdateResult.UPDATE_AVAILABLE);
        return;
    }

    public void run() {
        LobbySwitcher.getInstance().getLogger().info("Searching for an update on 'spigotmc.org'..");
        checkLatestVersion();
        compareVersions();
        switch (this.updateResult) {
            case UPDATE_AVAILABLE:
                LobbySwitcher.getInstance().getLogger().info("There was a new version found. It is recommended to update. (Visit spigotmc.org)");
                LobbySwitcher.updateAvailable = true;
                break;

            case NO_UPDATE:
                LobbySwitcher.getInstance().getLogger().info("The plugin is up to date.");
                LobbySwitcher.updateAvailable = false;
                break;

            case CONNECTION_ERROR:
                LobbySwitcher.getInstance().getLogger().warning("Could not connect to spigotmc.org. Retrying soon.");
                LobbySwitcher.updateAvailable = false;
                break;

            default:
                LobbySwitcher.getInstance().getLogger().warning("Could not connect to spigotmc.org. Retrying soon.");
                LobbySwitcher.updateAvailable = false;
                break;
        }
    }

    public String getLatestVersion() {
        return this.latestVersion;
    }

    public String getCurrentVersion() {
        return this.currentVersion;
    }

    public void setUpdateResult(UpdateResult updateResult) {
        this.updateResult = updateResult;
    }

}
