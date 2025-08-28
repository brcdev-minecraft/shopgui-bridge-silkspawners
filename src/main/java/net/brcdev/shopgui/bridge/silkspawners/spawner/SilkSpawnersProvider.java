package net.brcdev.shopgui.bridge.silkspawners.spawner;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import de.dustplanet.util.SilkUtil;
import net.brcdev.shopgui.bridge.silkspawners.nms.NmsUtils;
import net.brcdev.shopgui.bridge.silkspawners.nms.NmsVersion;
import net.brcdev.shopgui.spawner.external.provider.ExternalSpawnerProvider;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class SilkSpawnersProvider implements ExternalSpawnerProvider {

  private SilkUtil silkUtil;

  private BiMap<EntityType, String> entityTypesMap;

  public void hookIntoSilkSpawners() {
    this.silkUtil = SilkUtil.hookIntoSilkSpanwers();
    loadEntityTypes();
  }

  public String getName() {
    return "SilkSpawners";
  }

  public ItemStack getSpawnerItem(EntityType entityType) {
    String entityTypeName = this.entityTypesMap.containsKey(entityType) ? this.entityTypesMap.get(entityType) :
      entityType.name().toLowerCase();
    String customName = silkUtil.getCustomSpawnerName(entityTypeName);

    return silkUtil.newSpawnerItem(entityTypeName, customName, 1, false);
  }

  public EntityType getSpawnerEntityType(ItemStack itemStack) {
    EntityType entityType;

    String entityTypeName = Objects.toString(silkUtil.getStoredSpawnerItemEntityID(itemStack), "");

    if (this.entityTypesMap.containsValue(entityTypeName.toLowerCase())) {
      entityType = this.entityTypesMap.inverse().get(entityTypeName.toLowerCase());
    } else {
      try {
        entityType = EntityType.valueOf(entityTypeName);
      } catch (IllegalArgumentException ex) {
        entityType = getEntityTypeFromStringWithoutUnderScores(entityTypeName);
      }
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