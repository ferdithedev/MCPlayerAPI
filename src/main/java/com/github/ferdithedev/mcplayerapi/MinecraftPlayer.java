package com.github.ferdithedev.mcplayerapi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class MinecraftPlayer {

    private final String name;
    private String trimmeduuid;
    private final String textureValue;
    private final String textureSignature;
    private final String skinURL;

    private static final String apiurl1 = "https://api.mojang.com/users/profiles/minecraft/%name%";
    private static final String apiurl2 = "https://api.mojang.com/user/profiles/%uuid%/names";
    private static final String apiurl3 = "https://sessionserver.mojang.com/session/minecraft/profile/%uuid%?unsigned=false";

    public MinecraftPlayer(String arg) throws MinecraftPlayerAPI.NoSuchPlayerException {
        if(arg == null ||arg.isEmpty()) throw new MinecraftPlayerAPI.NoSuchPlayerException("Name/UUID can't be null or empty");
        if(isUUID(arg)) {
            this.trimmeduuid = arg;
            this.name = getName(call(apiurl2.replace("%uuid%",trimmeduuid)));
        } else {
            this.name = arg;
            this.trimmeduuid = getUUID(call(apiurl1.replace("%name%",arg)));
        }

        if(name == null || name.isEmpty()) throw new MinecraftPlayerAPI.NoSuchPlayerException("Failed to get player with uuid '" + arg + "'. Please check your spelling");

        if(trimmeduuid != null) {
            this.trimmeduuid = trimmeduuid.replaceAll("-","");
            String textureJson = call(apiurl3.replace("%uuid%",this.trimmeduuid));
            this.textureValue = getTextureProperty(textureJson, "value");
            this.textureSignature = getTextureProperty(textureJson, "signature");
        } else {
            throw new MinecraftPlayerAPI.NoSuchPlayerException("Failed to get player with name '" + arg + "'. Please check your spelling");
        }

        this.skinURL = getURLOfDecodedJSONObject(textureValue);
    }

    private static HttpURLConnection connection;

    public static boolean isUUID(String uuid) {
        String name = getName(call(apiurl2.replace("%uuid%", uuid)));
        return name != null && !name.isEmpty();
    }

    private static String call(String urlS) {
        BufferedReader reader;
        String line;
        StringBuilder responseContent = new StringBuilder();
        try {
            URL url = new URL(urlS);
            connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            if(connection.getResponseCode() > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }

            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();

            return responseContent.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return null;
    }

    private String getTextureProperty(String json, String which) {
        if(isJSONObject(json)) {
            JSONObject jsonObject = new JSONObject(json);
            if(jsonObject.has("properties")) {
                JSONArray jsonArray = jsonObject.getJSONArray("properties");
                if(jsonArray.getJSONObject(0) != null) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    if(jsonObject1.has(which)) {
                        return jsonObject1.getString(which);
                    }
                }
            }
        }
        return null;
    }

    private String getUUID(String json) {
        if(isJSONObject(json)) {
            JSONObject jsonObject = new JSONObject(json);
            if(jsonObject.has("id")) {
                return jsonObject.getString("id");
            }
        }
        return null;
    }

    private static String getName(String json) {
        if(isJSONArray(json)) {
            JSONArray jsonArray = new JSONArray(json);
            List<JSONObject> list = new ArrayList<>();
            for (int i=0;i<jsonArray.length();i++){
                list.add(jsonArray.getJSONObject(i));
            }

            Collections.reverse(list);
            JSONObject newest = list.get(0);
            if(newest.has("name")) {
                return newest.getString("name");
            }
        }
        return "";
    }

    private static String getURLOfDecodedJSONObject(String value) {
        byte[] decode = Base64.getDecoder().decode(value);
        return new JSONObject(new String(decode)).getJSONObject("textures").getJSONObject("SKIN").getString("url");
    }

    private static boolean isJSONObject(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    private static boolean isJSONArray(String test) {
        try {
            new JSONArray(test);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public String getUUIDTrimmed() {
        return trimmeduuid;
    }

    public String getUUID() {
        return fromTrimmed(trimmeduuid);
    }

    public String getTextureValue() {
        return textureValue;
    }

    public String getSkinURL() {
        return skinURL;
    }

    public String getTextureSignature() {
        return textureSignature;
    }

    public static String fromTrimmed(String trimmedUUID) throws IllegalArgumentException{
        if(trimmedUUID == null) throw new IllegalArgumentException();

        try {
            UUID.fromString(trimmedUUID);
            return trimmedUUID;
        } catch (IllegalArgumentException ignored) { }

        StringBuilder builder = new StringBuilder(trimmedUUID.trim());
        try {
            builder.insert(8, "-");
            builder.insert(12, "-");
            builder.insert(16, "-");
            builder.insert(20, "-");
        } catch (StringIndexOutOfBoundsException e){
            throw new IllegalArgumentException();
        }

        return builder.toString();
    }
}
