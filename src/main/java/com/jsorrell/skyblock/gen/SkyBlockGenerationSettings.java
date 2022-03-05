package com.jsorrell.skyblock.gen;

import com.mojang.serialization.Lifecycle;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class SkyBlockGenerationSettings {

  public static final String NAME = "skyblock";

  public static Registry<DimensionOptions> getSkyBlockDimensionOptionsRegistry(
      DynamicRegistryManager registryManager,
      long seed) {
    SimpleRegistry<DimensionOptions> dimensionOptionsRegistry = new SimpleRegistry<>(Registry.DIMENSION_KEY, Lifecycle.experimental(), null);

    Registry<DimensionType> dimensionTypeRegistry = registryManager.get(Registry.DIMENSION_TYPE_KEY);

    dimensionOptionsRegistry.add(
        DimensionOptions.OVERWORLD,
        new DimensionOptions(
            new RegistryEntry.Direct<>(dimensionTypeRegistry.getOrThrow(DimensionType.OVERWORLD_REGISTRY_KEY)),
            createOverworldGenerator(registryManager, seed)),
        Lifecycle.stable());

    dimensionOptionsRegistry.add(
        DimensionOptions.NETHER,
        new DimensionOptions(
            new RegistryEntry.Direct<>(dimensionTypeRegistry.getOrThrow(DimensionType.THE_NETHER_REGISTRY_KEY)),
            createNetherGenerator(registryManager, seed)),
        Lifecycle.stable());

    dimensionOptionsRegistry.add(
        DimensionOptions.END,
        new DimensionOptions(
            new RegistryEntry.Direct<>(dimensionTypeRegistry.getOrThrow(DimensionType.THE_END_REGISTRY_KEY)),
            createEndGenerator(registryManager, seed)),
        Lifecycle.stable());
    return dimensionOptionsRegistry;
  }

  public static SkyBlockChunkGenerator createOverworldGenerator(DynamicRegistryManager registryManager, long seed) {
    Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry = registryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
    return new SkyBlockChunkGenerator(
        registryManager.get(Registry.STRUCTURE_SET_KEY),
        registryManager.get(Registry.NOISE_WORLDGEN),
        MultiNoiseBiomeSource.Preset.OVERWORLD.getBiomeSource(registryManager.get(Registry.BIOME_KEY)).withSeed(seed),
        seed,
        chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.OVERWORLD).getInstance());
  }

  public static SkyBlockChunkGenerator createNetherGenerator(DynamicRegistryManager registryManager, long seed) {
    Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry = registryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
    return new SkyBlockChunkGenerator(
        registryManager.get(Registry.STRUCTURE_SET_KEY),
        registryManager.get(Registry.NOISE_WORLDGEN),
        MultiNoiseBiomeSource.Preset.NETHER.getBiomeSource(registryManager.get(Registry.BIOME_KEY)).withSeed(seed),
        seed,
        chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.NETHER).getInstance());
  }

  public static SkyBlockChunkGenerator createEndGenerator(DynamicRegistryManager registryManager, long seed) {
    Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry = registryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
    return new SkyBlockChunkGenerator(
        registryManager.get(Registry.STRUCTURE_SET_KEY),
        registryManager.get(Registry.NOISE_WORLDGEN),
        new TheEndBiomeSource(registryManager.get(Registry.BIOME_KEY), seed),
        seed,
        chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.END).getInstance());
  }
}
