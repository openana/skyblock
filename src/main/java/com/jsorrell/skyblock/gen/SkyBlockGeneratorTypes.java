package com.jsorrell.skyblock.gen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;

@Environment(EnvType.CLIENT)
public class SkyBlockGeneratorTypes {
  public static final GeneratorType SKYBLOCK =
      new GeneratorType("skyblock") {
        @Override
        protected ChunkGenerator getChunkGenerator(DynamicRegistryManager registryManager, long seed) {
          return SkyBlockGenerationSettings.createOverworldGenerator(registryManager, seed);
        }

        @Override
        public GeneratorOptions createDefaultOptions(
            DynamicRegistryManager registryManager,
            long seed,
            boolean generateStructures,
            boolean bonusChest) {
          return new GeneratorOptions(
              seed,
              generateStructures,
              bonusChest,
              SkyBlockGenerationSettings.getSkyBlockDimensionOptionsRegistry(registryManager, seed));
        }
      };
}
