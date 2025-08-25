package net.brcdev.shopgui.bridge.silkspawners;

import me.nahu.scheduler.wrapper.FoliaWrappedJavaPlugin;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.bridge.silkspawners.spawner.SilkSpawnersCurrentProvider;
import net.brcdev.shopgui.bridge.silkspawners.spawner.SilkSpawnersLegacyProvider;
import net.brcdev.shopgui.bridge.silkspawners.spawner.SilkSpawnersProvider;
import net.brcdev.shopgui.exception.api.ExternalSpawnerProviderNameConflictException;
import org.bukkit.Bukkit;

public class ShopGuiBridgeSilkSpawnersPlugin extends FoliaWrappedJavaPlugin {

  private SilkSpawnersProvider spawnerProvider;

  public void onEnable() {
    hookIntoSilkSpawners();
    hookIntoShopGui();
  }

  private void hookIntoSilkSpawners() {
    if (usingLegacySilkSpawners()) {
      this.spawnerProvider = new SilkSpawnersLegacyProvider();
    } else {
      this.spawnerProvider = new SilkSpawnersCurrentProvider();
    }
    this.spawnerProvider.hookIntoSilkSpawners(Bukkit.getPluginManager().getPlugin("SilkSpawners"));
  }

  private void hookIntoShopGui() {
    try {
      ShopGuiPlusApi.registerSpawnerProvider(this.spawnerProvider);
    } catch (ExternalSpawnerProviderNameConflictException e) {
      getLogger().warning("Failed to hook into ShopGUI+: " + e.getMessage());
    }
  }

  private boolean usingLegacySilkSpawners() {
    int versionMajorNumber;
    String version = Bukkit.getPluginManager().getPlugin("SilkSpawners").getDescription().getVersion();
    try {
      versionMajorNumber = Integer.parseInt(String.valueOf(version.charAt(0)));
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
      return true;
    }
    return (versionMajorNumber < 6);
  }
}

