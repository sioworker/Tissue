package org.bukkit.potion;

import net.minecraft.core.registries.BuiltInRegistries;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AllFeatures
public class PotionTest {

    @Test
    public void testEffectType() {
        BuiltInRegistries.MOB_EFFECT.listElements().forEach(reference -> {
            PotionEffectType bukkit = CraftPotionEffectType.minecraftHolderToBukkit(reference);

            assertNotNull(bukkit, "No Bukkit type for " + reference.key());

            PotionEffectType byName = PotionEffectType.getByName(org.bukkit.craftbukkit.legacy.FieldRename.convertPotionEffectTypeName(org.bukkit.craftbukkit.util.ApiVersion.CURRENT, bukkit.getName()));
            assertEquals(bukkit, byName, "Same type not returned by name " + reference.key());
        });
    }
}
