package net.brcdev.shopgui.bridge.silkspawners.spawner;

import de.dustplanet.silkspawners.SilkSpawners;
import de.dustplanet.silkspawners.compat.api.NMSProvider;
import de.dustplanet.util.SilkUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SilkSpawnersLegacyProvider implements SilkSpawnersProvider {

  private SilkUtil silkUtil;

  public void hookIntoSilkSpawners(Plugin silkSpawnersPlugin) {
    this.silkUtil = new SilkUtil((SilkSpawners) silkSpawnersPlugin);
  }

  public String getName() {
    return "SilkSpawners";
  }

  public ItemStack getSpawnerItem(EntityType entityType) {
    ItemStack itemStack = null;
    try {
      Method method = SilkUtil.class.getMethod("newSpawnerItem", short.class, String.class, int.class, boolean.class);
      itemStack = (ItemStack) method.invoke(this.silkUtil, entityType.getTypeId(), null, 1, false);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return itemStack;
  }

  public EntityType getSpawnerEntityType(ItemStack itemStack) {
    EntityType entityType = null;
    try {
      Method method = NMSProvider.class.getMethod("getSilkSpawnersNBTEntityID", ItemStack.class);
      entityType = EntityType.fromId((short) method.invoke(this.silkUtil.nmsProvider, itemStack));
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return entityType;
  }
}
