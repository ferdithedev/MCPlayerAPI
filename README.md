# MinecraftPlayerAPI

With this API you can easily get informations about any Minecraft account.
This API is using the [Mojang API](https://wiki.vg/Mojang_API) so only 600 requests per 10 minutes per IP address are possible

### Current Features

- Getting name (by uuid)
- Getting uuid (by name)
- Getting skinURL
- Getting skin texture value
- Getting skin texture signature
- Checking availability of Minecraft name and if a UUID is taken

## How to implement

#### Maven

```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>com.github.ferdithedev</groupId>
            <artifactId>MCPlayerAPI</artifactId>
            <version>1.4</version>
        </dependency>
    </dependencies>
```

#### Gradle

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.ferdithedev:MCPlayerAPI:1.4'
}
```

## Usage

### Player Informations

Create an MinecraftPlayer object, using the name or UUID:

#### By Name

```java
MinecraftPlayer mcplayer = new MinecraftPlayer("Ferdi_the_best");
System.out.println("Name: " + mcplayer.getName());
System.out.println("SkinURL: " + mcplayer.getSkinURL());
System.out.println("UUID: " + mcplayer.getUUID());
System.out.println("UUIDTrimmed: " + mcplayer.getUUIDTrimmed());
System.out.println("TextureValue: " + mcplayer.getTextureValue());
System.out.println("TextureSignature: " + mcplayer.getTextureSignature());
```

#### By UUID

```java
MinecraftPlayer mcplayer = new MinecraftPlayer("5c3837ff-cbb7-4911-9a97-dfc3f6bbdb87");
```

or

```java
MinecraftPlayer mcplayer = new MinecraftPlayer("5c3837ffcbb749119a97dfc3f6bbdb87");
```

### Checking Availability Of Minecraft Name/UUID

```java
System.out.println(MinecraftPlayerAPI.isMinecraftName("Ferdi_the_best"));
System.out.println(MinecraftPlayerAPI.isUUID("5c3837ffcbb749119a97dfc3f6bbdb87"));
```
