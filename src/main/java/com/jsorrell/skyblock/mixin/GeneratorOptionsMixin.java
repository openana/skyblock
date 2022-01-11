package com.jsorrell.skyblock.mixin;

import java.util.Properties;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

import com.jsorrell.skyblock.gen.SkyBlockGenerationSettings;

@Mixin(GeneratorOptions.class)
public class GeneratorOptionsMixin {

  @Inject(
      method = "fromProperties",
      at = @At("RETURN"),
      locals = LocalCapture.CAPTURE_FAILHARD,
      cancellable = true)
  private static void addSkyBlockGeneratorOptionWhenLoadingProperties(
      DynamicRegistryManager dynamicRegistryManager,
      Properties properties,
      CallbackInfoReturnable<GeneratorOptions> cir,
      String string,
      String string2,
      boolean generateStructures,
      String string4,
      String generatorSettingsName,
      long seed,
      Registry<DimensionType> register,
      Registry<Biome> register2,
      SimpleRegistry<ChunkGeneratorSettings> register3) {
    if (SkyBlockGenerationSettings.NAME.equals(generatorSettingsName)) {
      Registry<DimensionType> dimensionTypeRegistry = dynamicRegistryManager.get(Registry.DIMENSION_TYPE_KEY);
      Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry = dynamicRegistryManager.get(Registry.NOISE_WORLDGEN);
      Registry<Biome> biomeRegistry = dynamicRegistryManager.get(Registry.BIOME_KEY);
      Registry<ChunkGeneratorSettings> settingsRegistry = dynamicRegistryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
      SimpleRegistry<DimensionOptions> dimensionOptions =
          SkyBlockGenerationSettings.getSkyBlockDimensionOptions(
              dimensionTypeRegistry, noiseRegistry, biomeRegistry, settingsRegistry, seed);
      GeneratorOptions generatorOptions =
          new GeneratorOptions(seed, generateStructures, false, dimensionOptions);
      cir.setReturnValue(generatorOptions);
    }
  }
}
