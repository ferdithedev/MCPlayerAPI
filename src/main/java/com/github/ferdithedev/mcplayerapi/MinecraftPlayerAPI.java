package com.github.ferdithedev.mcplayerapi;

import java.util.UUID;

public class MinecraftPlayerAPI {

    public static UUID fromTrimmed(String trimmedUUID) throws IllegalArgumentException{
        if(trimmedUUID == null) throw new IllegalArgumentException();
        StringBuilder builder = new StringBuilder(trimmedUUID.trim());
        try {
            builder.insert(20, "-");
            builder.insert(16, "-");
            builder.insert(12, "-");
            builder.insert(8, "-");
        } catch (StringIndexOutOfBoundsException e){
            throw new IllegalArgumentException();
        }

        return UUID.fromString(builder.toString());
    }

    public static class FailedCallException extends Exception{
        public FailedCallException(String message) {
            super(message);
        }
    }
}
