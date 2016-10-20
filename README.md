# KotlinFun
Kotlin Library for Bukkit and BungeeCord plugins

## Adding to your project

### Gradle
Add this to your `build.gradle`

```gradle
allprojects {
  repositories {
    maven { url "https://jitpack.io" }
  }
}
```

Now add one of compile line below:
```gradle
dependencies {

  // For 0.1 Bukkit:
  compile 'com.github.GameModsBR.KotlinFun:BukkitPlugin:0.1'
  
  // For 0.1 BungeeCord:
  compile 'com.github.GameModsBR.KotlinFun:BungeePlugin:0.1'
  
  // For 0.1 Universal:
  compile 'com.github.GameModsBR.KotlinFun:UniversalPlugin:0.1'
  
  // For 0.2-SNAPSHOT (includes everything)
  compile 'com.github.GameModsBR:KotlinFun:master-SNAPSHOT'
  
}
```

### Maven
Add this repository to your main `pom.xml`

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
	      <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Now add one of the depedencies below:
```xml
<dependencies>

    <!-- For 0.1 Bukkit -->
    <dependency>
        <groupId>com.github.GameModsBR.KotlinFun</groupId>
        <artifactId>BukkitPlugin</artifactId>
        <version>0.1</version>
    </dependency>
    
    <!-- For 0.1 BungeeCord -->
    <dependency>
        <groupId>com.github.GameModsBR.KotlinFun</groupId>
        <artifactId>BungeePlugin</artifactId>
        <version>0.1</version>
    </dependency>
    
    <!-- For 0.1 Universal -->
    <dependency>
        <groupId>com.github.GameModsBR.KotlinFun</groupId>
        <artifactId>UniversalPlugin</artifactId>
        <version>0.1</version>
    </dependency>
    
    <!-- For 0.2-SNAPSHOT (includes everything) -->
    <dependency>
        <groupId>com.github.GameModsBR</groupId>
        <artifactId>KotlinFun</artifactId>
        <version>master-SNAPSHOT</version>
    </dependency>
    
</dependencies>
```