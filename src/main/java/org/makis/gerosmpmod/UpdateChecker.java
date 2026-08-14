package org.makis.gerosmpmod;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.fabricmc.loader.api.FabricLoader;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class UpdateChecker {
    public static void checkForUpdates() {
        CompletableFuture.runAsync(() -> {
           try {
               HttpClient client = HttpClient.newBuilder()
                       .connectTimeout(Duration.ofSeconds(5))
                       .build();
               HttpRequest request = HttpRequest.newBuilder()
                       .uri(URI.create("https://api.github.com/repos/" + GeroSmpMod.GITHUB_REPO + "/releases/latest"))
                       .header("Accept", "application/vnd.github.v3_json")
                       .header("User-Agent", GeroSmpMod.MOD_ID + "-UpdateChecker")
                       .GET()
                       .build();
               HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
               if (response.statusCode() == 200) {
                   JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                   String latestTag = json.get("tag_name").getAsString().replace("v", "");
                   String releaseUrl = json.get("html_url").getAsString();

                   String currentVersion = FabricLoader.getInstance()
                           .getModContainer(GeroSmpMod.MOD_ID)
                           .map(c -> c.getMetadata().getVersion().getFriendlyString())
                           .orElse("0.0.0");

                   if (isNewerVersion(currentVersion, latestTag)) {
                       int openLink = TinyFileDialogs.tinyfd_messageBox(
                               "Mod Update Available",
                               "A new version of GeroMod (v" + latestTag + ") Open release page?",
                               "okcancel",
                               "info",
                               1
                       );
                       if (openLink != 0) {
                           net.minecraft.util.Util.getPlatform().openUri(URI.create(releaseUrl));
                       }
                   }
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
        });
    }
    private static boolean isNewerVersion(String current, String latest) {
        String[] currentParts = current.split("\\.");
        String[] latestParts = current.split("\\.");

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
