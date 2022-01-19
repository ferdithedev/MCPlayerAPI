package com.github.ferdithedev.mcplayerapi;

public class MinecraftPlayerAPI {

    public static boolean isUUID(String uuid) {
        String name = MinecraftPlayer.getName(MinecraftPlayer.call(MinecraftPlayer.apiURL_2.replace("%uuid%", uuid)));
        return name != null && !name.isEmpty();
    }

    public static boolean isMinecraftName(String name) {
        String uuid = MinecraftPlayer.getUUID(MinecraftPlayer.call(MinecraftPlayer.apiURL_1.replace("%name%",name)));
        return uuid != null && !uuid.isEmpty();
    }

    public static class NoSuchPlayerException extends Exception{
        public NoSuchPlayerException(String message) {
            super(message);
        }
    }
}
