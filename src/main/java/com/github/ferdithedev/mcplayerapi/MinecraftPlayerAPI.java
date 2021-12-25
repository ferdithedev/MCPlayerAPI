package com.github.ferdithedev.mcplayerapi;

public class MinecraftPlayerAPI {

public static class NoSuchPlayerException extends Exception{
        public NoSuchPlayerException(String message) {
            super(message);
        }
    }
}
