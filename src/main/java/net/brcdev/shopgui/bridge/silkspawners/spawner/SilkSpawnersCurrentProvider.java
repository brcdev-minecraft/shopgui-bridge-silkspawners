package net.brcdev.shopgui.bridge.silkspawners.spawner;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import de.dustplanet.silkspawners.SilkSpawners;
import de.dustplanet.silkspawners.compat.api.NMSProvider;
import de.dustplanet.util.SilkUtil;
import net.brcdev.shopgui.bridge.silkspawners.nms.NmsUtils;
import net.brcdev.shopgui.bridge.silkspawners.nms.NmsVersion;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SilkSpawnersCurrentProvider implements SilkSpawnersProvider {

  private SilkUtil silkUtil;

  private BiMap<EntityType, String> entityTypesMap;

  public void hookIntoSilkSpawners(Plugin silkSpawnersPlugin) {
    this.silkUtil = new SilkUtil((SilkSpawners) silkSpawnersPlugin);
    loadEntityTypes();
  }

  public String getName() {
    return "SilkSpawners";
  }

  public ItemStack getSpawnerItem(EntityType entityType) {
    ItemStack itemStack = null;
    String entityTypeName = this.entityTypesMap.containsKey(entityType) ? this.entityTypesMap.get(entityType) :
      entityType.name().toLowerCase();
    try {
      Method method = SilkUtil.class.getMethod("newSpawnerItem", String.class, String.class, int.class, boolean.class);
      itemStack = (ItemStack) method.invoke(this.silkUtil, entityTypeName, null, 1, false);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return itemStack;
  }

  public EntityType getSpawnerEntityType(ItemStack itemStack) {
    EntityType entityType = null;
    try {
      Method method = NMSProvider.class.getMethod("getSilkSpawnersNBTEntityID", ItemStack.class);
      String entityTypeName = ((String) method.invoke(this.silkUtil.nmsProvider, itemStack)).toUpperCase();
      if (this.entityTypesMap.containsValue(entityTypeName.toLowerCase())) {
        entityType = this.entityTypesMap.inverse().get(entityTypeName.toLowerCase());
      } else {
        try {
          entityType = EntityType.valueOf(entityTypeName);
        } catch (IllegalArgumentException ex) {
          entityType = getEntityTypeFromStringWithoutUnderScores(entityTypeName);
        }
      }
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return entityType;
  }

  private EntityType getEntityTypeFromStringWithoutUnderScores(String entityTypeName) {
    for (EntityType entityType : EntityType.values()) {
      if (entityType.name().replace("_", "").equalsIgnoreCase(entityTypeName)) return entityType;
    }
    return null;
  }

  private void loadEntityTypes() {
    this.entityTypesMap = HashBiMap.create();
    if (NmsUtils.isNmsVersionLowerThan(NmsVersion.v1_16)) {
      this.entityTypesMap.put(EntityType.valueOf("PIG_ZOMBIE"), "zombie_pigman");
    }
    this.entityTypesMap.put(EntityType.MUSHROOM_COW, "mooshroom");
    if (NmsUtils.isNmsVersionLowerThan(NmsVersion.v1_11)) {
      this.entityTypesMap.put(EntityType.IRON_GOLEM, "VillagerGolem");
    } else if (NmsUtils.isNmsVersionLowerThan(NmsVersion.v1_13)) {
      this.entityTypesMap.put(EntityType.IRON_GOLEM, "villager_golem");
    }
  }
}