package net.brcdev.shopgui.bridge.silkspawners.nms;

import net.brcdev.shopgui.exception.UnsupportedMinecraftVersionException;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NmsUtils {

  private static final Pattern MINECRAFT_SHORT_VERSION_PATTERN = Pattern.compile("(v\\d_\\d+)");
  private static final Pattern NMS_RELEASE_NUMBER_PATTERN = Pattern.compile("R(\\d+)");

  public static boolean isNmsVersionAtLeast(NmsVersion requiredNmsVersion) {
    NmsVersion currentVersion = readNmsVersion();

    return currentVersion.extractVersionNumber() >= requiredNmsVersion.extractVersionNumber();
  }

  public static boolean isNmsVersionLowerThan(NmsVersion maximalNmsVersion) {
    return !isNmsVersionAtLeast(maximalNmsVersion);
  }

  private static NmsVersion readNmsVersion() {
    NmsVersion version = null;

    try {
      // Try to use Paper's method on Paper and for 1.20.6+ only
      String minecraftVersion = getPaperMinecraftVersion();

      switch (minecraftVersion) {
        case "1.20.6":
          version = parseShortNmsVersion("v1_20");
          break;
        case "1.21":
        case "1.21.1":
        case "1.21.2":
        case "1.21.3":
        case "1.21.4":
        case "1.21.5":
        case "1.21.6":
        case "1.21.7":
        case "1.21.8":
          version = parseShortNmsVersion("v1_21");
          break;
      }
    } catch (ReflectiveOperationException e) {
      // Not Paper or older than 1.16.5 where the method was added
      version = parseShortNmsVersion(extractNmsVersion(MINECRAFT_SHORT_VERSION_PATTERN));
    }

    if (version == null) {
      // Paper version between 1.16.5 <= version < 1.20.6
      version = parseShortNmsVersion(extractNmsVersion(MINECRAFT_SHORT_VERSION_PATTERN));
    }

    return version;
  }

  /**
   * Paper 1.16.5 added the getMinecraftVersion() method.
   *
   * @return The current Minecraft version. (Example: 1.20.6)
   * @throws ReflectiveOperationException This method only works on Paper servers. Spigot will throw an error.
   */
  private static String getPaperMinecraftVersion() throws ReflectiveOperationException {
    Server server = Bukkit.getServer();
    Class<? extends Server> serverClass = server.getClass();
    Method method_getMinecraftVersion = serverClass.getMethod("getMinecraftVersion");
    return (String) method_getMinecraftVersion.invoke(server);
  }

  private static String extractNmsVersion(Pattern pattern) {
    String nmsClasspath = Bukkit.getServer().getClass().getPackage().getName();
    Matcher matcher = pattern.matcher(nmsClasspath);

    if (matcher.find()) {
      return matcher.group();
    } else {
      throw new UnsupportedMinecraftVersionException();
    }
  }

  private static NmsVersion parseShortNmsVersion(String version) {
    NmsVersion nmsVersion;
    try {
      nmsVersion = NmsVersion.valueOf(version);
    } catch (IllegalArgumentException e) {
      throw new UnsupportedMinecraftVersionException();
    }

    return nmsVersion;
  }

}