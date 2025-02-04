package com.jsorrell.skyblock.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;

import com.jsorrell.skyblock.SkyBlockSettings;

@Mixin(FoxEntity.class)
public abstract class FoxEntityMixin extends Entity {
  private FoxEntityMixin(EntityType<?> type, World world) {
    super(type, world);
  }

  // Chance given the 20% chance a fox is spawned holding any item
  private static final float BERRY_CHANCE = 0.3f;

  @Inject(
      method = "initEquipment",
      cancellable = true,
      at = @At(value = "INVOKE", target = "Ljava/util/Random;nextFloat()F", ordinal = 1))
  private void addFoxHeldItem(LocalDifficulty difficulty, CallbackInfo ci) {
    if (SkyBlockSettings.foxesSpawnWithBerries) {
      float f = this.random.nextFloat();
      ItemStack equippedItem;
      if (f < BERRY_CHANCE) {
         equippedItem = new ItemStack(f < BERRY_CHANCE / 2 ? Items.GLOW_BERRIES : Items.SWEET_BERRIES);
         this.equipStack(EquipmentSlot.MAINHAND, equippedItem);
         ci.cancel();
      }
    }
  }
}
