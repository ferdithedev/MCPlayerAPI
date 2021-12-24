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

    private final String name, uuid, base64PropertiesValue, skinURL;

    private static final String apiurl1 = "https://api.mojang.com/users/profiles/minecraft/%name%";
    private static final String apiurl2 = "https://api.mojang.com/user/profiles/%uuid%/names";
    private static final String apiurl3 = "https://sessionserver.mojang.com/session/minecraft/profile/%uuid%";

    /**
     * @param name the name of the Minecraft player
     * @throws MinecraftPlayerAPI.FailedCallException failing call when name is misspelled
     */
    public MinecraftPlayer(String name) throws MinecraftPlayerAPI.FailedCallException {
        this.name = name;
        this.uuid = MinecraftPlayerAPI.fromTrimmed(getUUID(call(apiurl1.replace("%name%",name)))).toString();
        if(uuid != null) {
            this.base64PropertiesValue = getValue(call(apiurl3.replace("%uuid%",uuid)));
        } else {
            throw new MinecraftPlayerAPI.FailedCallException("Failed to get uuid by '" + name + "'. Please check your spelling");
        }

        this.skinURL = getURLOfDecodedJSONObject(base64PropertiesValue);
    }

    /**
     * @param uuid the uuid of the Minecraft player as a UUID
     * @see UUID
     */
    public MinecraftPlayer(UUID uuid) throws MinecraftPlayerAPI.FailedCallException {
        this.uuid = uuid.toString();
        this.name = getName(call(apiurl2.replace("%uuid%",uuid.toString())));
        if(name == null || name.isEmpty()) throw new MinecraftPlayerAPI.FailedCallException("Failed to get name by uuid '" + uuid + "'. Please check your spelling");
        this.base64PropertiesValue = getValue(call(apiurl3.replace("%uuid%",uuid.toString())));
        this.skinURL = getURLOfDecodedJSONObject(base64PropertiesValue);
    }

    private static HttpURLConnection connection;

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

    private String getValue(String json) {
        if(isJSONValid(json)) {
            JSONObject jsonObject = new JSONObject(json);
            if(jsonObject.has("properties")) {
                JSONArray jsonArray = jsonObject.getJSONArray("properties");
                if(jsonArray.getJSONObject(0) != null) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    if(jsonObject1.has("value")) {
                        return jsonObject1.getString("value");
                    }
                }
            }
        }
        return null;
    }

    private String getUUID(String json) {
        if(isJSONValid(json)) {
            JSONObject jsonObject = new JSONObject(json);
            if(jsonObject.has("id")) {
                return jsonObject.getString("id");
            }
        }
        return null;
    }

    private String getName(String json) {
        if(isJSONValid(json)) {
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

    private static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the current display name of the player
     *
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Get the current trimmed UUID as a string of the player
     *
     * @return the trimmed uuid of the player as string
     */
    public String getUUIDTrimmed() {
        return uuid.replaceAll("-","");
    }

    /**
     * Get the current UUID of the player
     *
     * @return the uuid of the player
     */
    public UUID getUUID() {
        return UUID.fromString(uuid);
    }

    /**
     * get the base64 decoded texture properties value of the player
     *
     * @return the base64 decoded texture properties value of the player
     */
    public String getBase64PropertiesValue() {
        return base64PropertiesValue;
    }

    /**
     * get the textures.minecraft.net skin url of the player
     *
     * @return the skin url of the player
     */
    public String getSkinURL() {
        return skinURL;
    }

}
