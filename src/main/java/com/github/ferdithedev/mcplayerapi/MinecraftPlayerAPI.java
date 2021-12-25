package com.github.ferdithedev.mcplayerapi;

import static com.github.ferdithedev.mcplayerapi.MinecraftPlayer.*;

public class MinecraftPlayerAPI {

    public static boolean isUUID(String uuid) {
        String name = getName(call(apiurl2.replace("%uuid%", uuid)));
        return name != null && !name.isEmpty();
    }

    public static boolean isMinecraftName(String name) {
        String uuid = getUUID(call(apiurl1.replace("%name%",name)));
        return uuid != null && !uuid.isEmpty();
    }

    public static class NoSuchPlayerException extends Exception{
        public NoSuchPlayerException(String message) {
            super(message);
        }
    }
}
