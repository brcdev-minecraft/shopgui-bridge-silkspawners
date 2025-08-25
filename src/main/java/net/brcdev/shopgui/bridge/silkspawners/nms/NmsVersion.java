package net.brcdev.shopgui.bridge.silkspawners.nms;

import net.brcdev.shopgui.exception.UnsupportedMinecraftVersionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum NmsVersion {
  v1_7,
  v1_8,
  v1_9,
  v1_10,
  v1_11,
  v1_12,
  v1_13,
  v1_14,
  v1_15,
  v1_16,
  v1_17,
  v1_18,
  v1_19,
  v1_20,
  v1_21;

  private static final Pattern ONE_POINT_VERSION_NUMBER_PATTERN = Pattern.compile("v\\d_(\\d+)");

  public int extractVersionNumber() {
    Matcher matcher = ONE_POINT_VERSION_NUMBER_PATTERN.matcher(this.name());
    matcher.find();

    if (matcher.groupCount() < 1) {
      throw new UnsupportedMinecraftVersionException();
    }

    return Integer.parseInt(matcher.group(1));
  }
}
