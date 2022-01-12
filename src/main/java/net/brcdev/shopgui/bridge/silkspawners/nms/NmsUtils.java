package net.brcdev.shopgui.bridge.silkspawners.nms;

import net.brcdev.shopgui.exception.UnsupportedMinecraftVersionException;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NmsUtils {

  private static final Pattern MINECRAFT_SHORT_VERSION_PATTERN = Pattern.compile("(v\\d_\\d+)");

  public static boolean isNmsVersionAtLeast(NmsVersion requiredNmsVersion) {
    NmsVersion currentVersion = readNmsVersion();
    return (currentVersion.extractVersionNumber() >= requiredNmsVersion.extractVersionNumber());
  }

  public static boolean isNmsVersionLowerThan(NmsVersion maximalNmsVersion) {
    return !isNmsVersionAtLeast(maximalNmsVersion);
  }

  private static NmsVersion readNmsVersion() {
    return parseShortNmsVersion(extractNmsVersion(MINECRAFT_SHORT_VERSION_PATTERN));
  }

  private static String extractNmsVersion(Pattern pattern) {
    String nmsClasspath = Bukkit.getServer().getClass().getPackage().getName();
    Matcher matcher = pattern.matcher(nmsClasspath);
    if (matcher.find())
      return matcher.group();
    throw new UnsupportedMinecraftVersionException();
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
