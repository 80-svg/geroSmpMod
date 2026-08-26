package org.makis.gerosmpmod;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class UpdateChecker {
    public static void checkForUpdates() {
        GeroSmpMod.LOGGER.info("Starting update check for {}", GeroSmpMod.GITHUB_REPO);
        CompletableFuture.runAsync(() -> {
           String apiUrl = "https://api.github.com/repos/" + GeroSmpMod.GITHUB_REPO + "/releases/latest";
           try {
               GeroSmpMod.LOGGER.debug("Update check running on thread {}", Thread.currentThread().getName());
               HttpClient client = HttpClient.newBuilder()
                       .connectTimeout(Duration.ofSeconds(5))
                       .build();
               HttpRequest request = HttpRequest.newBuilder()
                       .uri(URI.create(apiUrl))
                       .header("Accept", "application/vnd.github.v3_json")
                       .header("User-Agent", GeroSmpMod.MOD_ID + "-UpdateChecker")
                       .GET()
                       .build();
               GeroSmpMod.LOGGER.debug("Requesting latest release from {}", apiUrl);
               HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
               GeroSmpMod.LOGGER.debug("GitHub responded with HTTP {} and {} bytes", response.statusCode(), response.body().length());
               if (response.statusCode() == 200) {
                   GeroSmpMod.LOGGER.trace("GitHub response body: {}", response.body());
                   JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                   if (!json.has("tag_name") || !json.has("html_url")) {
                       GeroSmpMod.LOGGER.warn("GitHub response did not contain tag_name and html_url: {}", response.body());
                       return;
                   }
                   String latestTag = json.get("tag_name").getAsString().replaceFirst("^[vV]", "");
                   String releaseUrl = json.get("html_url").getAsString();

                   String currentVersion = GeroSmpMod.getModVersion();

                   boolean newer = isNewerVersion(currentVersion, latestTag);
                   GeroSmpMod.LOGGER.info("Update comparison: current={}, latest={}, newer={}", currentVersion, latestTag, newer);
                   if (newer) {
                       GeroSmpMod.LOGGER.info("Showing update dialog for release {}", releaseUrl);
                       int openLink = TinyFileDialogs.tinyfd_messageBox(
                               "Mod Update Available",
                               "A new version of GeroMod (v" + latestTag + ") Open release page?",
                               "okcancel",
                               "info",
                               1
                       );
                       GeroSmpMod.LOGGER.debug("Update dialog result: {}", openLink);
                       if (openLink != 0) {
                           GeroSmpMod.LOGGER.info("Opening release URL: {}", releaseUrl);
                           net.minecraft.util.Util.getPlatform().openUri(URI.create(releaseUrl));
                       }
                   } else {
                       GeroSmpMod.LOGGER.debug("No update is available.");
                   }
               } else {
                   GeroSmpMod.LOGGER.warn("GitHub update request failed: HTTP {}. Response body: {}",
                           response.statusCode(), response.body());
               }
           } catch (Exception e) {
               GeroSmpMod.LOGGER.error("Update check failed for {}", apiUrl, e);
           }
        });
    }
    private static boolean isNewerVersion(String current, String latest) {
        String[] currentParts = current.split("\\.");
        String[] latestParts = latest.split("\\.");

        GeroSmpMod.LOGGER.debug("Comparing version parts: current={}, latest={}",
                Arrays.toString(currentParts), Arrays.toString(latestParts));

        int length = Math.max(currentParts.length, latestParts.length);
        for (int i = 0; i < length; i++) {
            int curr = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;
            int l = i < latestParts.length ? Integer.parseInt(latestParts[i]) : 0;

            if (l > curr) return true;
            if (curr > l) return false;
        }
        return false;
    }
}
