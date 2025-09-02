package net.brcdev.shopgui.bridge.silkspawners;

import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.bridge.silkspawners.spawner.SilkSpawnersProvider;
import net.brcdev.shopgui.exception.api.ExternalSpawnerProviderNameConflictException;
import org.bukkit.plugin.java.JavaPlugin;

public class ShopGuiBridgeSilkSpawnersPlugin extends JavaPlugin {

  private SilkSpawnersProvider spawnerProvider;

  public void onEnable() {
    hookIntoSilkSpawners();
    hookIntoShopGui();
  }

  private void hookIntoSilkSpawners() {
    this.spawnerProvider = new SilkSpawnersProvider();
    this.spawnerProvider.hookIntoSilkSpawners();
  }

  private void hookIntoShopGui() {
    try {
      ShopGuiPlusApi.registerSpawnerProvider(this.spawnerProvider);
    } catch (ExternalSpawnerProviderNameConflictException e) {
      getLogger().warning("Failed to hook into ShopGUI+: " + e.getMessage());
    }
  }
}

