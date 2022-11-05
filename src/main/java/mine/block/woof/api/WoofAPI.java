package mine.block.woof.api;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.function.Function;

public class WoofAPI {
    public static final SimpleRegistry<Variant> VARIANT_REGISTRY = FabricRegistryBuilder.createSimple(Variant.class, new Identifier("woof_variants")).buildAndRegister();

    /**
     * Register a new wolf variant.
     * @param identifier The identifier of the variant.
     * @param biomePredicate Biome predicate stating which biome it should be in.
     * @return The variant registered.
     */
    public static Variant registerWolfVariant(Identifier identifier, Function<RegistryEntry<Biome>, Boolean> biomePredicate) {
        return Registry.register(VARIANT_REGISTRY, identifier, new Variant(identifier, biomePredicate));
    }

    @ApiStatus.Internal
    public static void initialize() {
        for (Map.Entry<RegistryKey<Variant>, Variant> variantRegistryKey : WoofAPI.VARIANT_REGISTRY.getEntrySet()) {
            if(variantRegistryKey.getValue().identifier().getPath().equals("default")) continue;
            BiomeModifications.addSpawn(biomeSelectionContext -> variantRegistryKey.getValue().canSpawnIn(biomeSelectionContext.getBiomeRegistryEntry()), SpawnGroup.CREATURE, EntityType.WOLF, 5, 1, 2);
        }
    }
}
