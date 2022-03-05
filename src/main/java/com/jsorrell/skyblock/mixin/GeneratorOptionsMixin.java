package com.jsorrell.skyblock.mixin;

import com.jsorrell.skyblock.gen.SkyBlockGenerationSettings;
import net.minecraft.server.dedicated.ServerPropertiesHandler.WorldGenProperties;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.random.RandomSeed;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.OptionalLong;
import java.util.Properties;

@Mixin(GeneratorOptions.class)
public class GeneratorOptionsMixin {

  @Inject(
      method = "fromProperties",
      at = @At("RETURN"),
      locals = LocalCapture.CAPTURE_FAILHARD,
      cancellable = true)
  private static void addSkyBlockGeneratorOptionWhenLoadingProperties(
      DynamicRegistryManager registryManager,
      WorldGenProperties worldGenProperties,
      CallbackInfoReturnable<GeneratorOptions> cir
  ) {
    if (SkyBlockGenerationSettings.NAME.equals(worldGenProperties.levelType())) {
      long seed = RandomSeed.getSeed();
      OptionalLong optionalSeed = GeneratorOptions.parseSeed(worldGenProperties.levelSeed());
      if (optionalSeed.isPresent()) {
        seed = optionalSeed.getAsLong();
      }
      GeneratorOptions x = new GeneratorOptions(
          seed,
          worldGenProperties.generateStructures(),
          false,
          SkyBlockGenerationSettings.getSkyBlockDimensionOptionsRegistry(registryManager, seed)
      );
      cir.setReturnValue(x);
    }
  }
}
