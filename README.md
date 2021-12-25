# MinecraftPlayerAPI

With this API you can easily get informations about any Minecraft account.
This API is using the [Mojang API](https://wiki.vg/Mojang_API) so only 600 requests per 10 minutes per IP address are possible

### Current Features

- Getting name (by uuid)
- Getting uuid (by name)
- Getting skinURL
- Getting Base64 encoded skin texture

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
            <version>1.0</version>
        </dependency>
    </dependencies>
```

#### Gradle

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.ferdithedev:MCPlayerAPI:1.0'
}
```

## Usage

Create an MinecraftPlayer object, using the name or UUID:

#### By Name

```java
MinecraftPlayer minecraftPlayer = new MinecraftPlayer("Ferdi_the_best");
System.out.println("UUID: " + minecraftPlayer.getUUID());
System.out.println("SkinURL: " + minecraftPlayer.getSkinURL());
```

#### By UUID

```java
MinecraftPlayer minecraftPlayer = new MinecraftPlayer(UUID.fromString("5c3837ff-cbb7-4911-9a97-dfc3f6bbdb87"));
System.out.println("Name: " + minecraftPlayer.getName());
```

or

```java
MinecraftPlayer minecraftPlayer = new MinecraftPlayer(MinecraftPlayerAPI.fromTrimmed("5c3837ffcbb749119a97dfc3f6bbdb87"));
```
