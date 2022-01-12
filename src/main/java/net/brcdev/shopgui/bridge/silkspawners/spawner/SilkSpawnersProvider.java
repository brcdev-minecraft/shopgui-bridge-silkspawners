package net.brcdev.shopgui.bridge.silkspawners.spawner;

import net.brcdev.shopgui.spawner.external.provider.ExternalSpawnerProvider;
import org.bukkit.plugin.Plugin;

public interface SilkSpawnersProvider extends ExternalSpawnerProvider {

  void hookIntoSilkSpawners(Plugin paramPlugin);

}
