package de.s42.duplicatemob.mixin;

import de.s42.duplicatemob.server.DuplicationManager;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class EntityDamageMixin {
    @Inject(method = "hurtServer", at = @At("HEAD"))
    private void onDamage(ServerLevel serverLevel, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity)(Object)this;

        if (self instanceof Player) {
            return;
        }

        try {
            DuplicationManager.duplicateEntity(serverLevel, self);
        }
        catch (Exception ex) {
            System.out.println("DuplicateMob: failed to duplicate entity: " + ex.getMessage());
        }
    }
}


