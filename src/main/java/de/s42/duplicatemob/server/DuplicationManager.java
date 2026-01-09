package de.s42.duplicatemob.server;

import de.s42.duplicatemob.Duplicatemob;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class DuplicationManager {
    private static final double SPAWN_RADIUS = 1.0;
    private static final int MAX_SPAWN_ATTEMPTS = 6;

    public static void duplicateEntity(ServerLevel level, LivingEntity original) {
        if (level == null || original == null) return;
        if (original.isRemoved() || original.isDeadOrDying()) return;

        EntityType<?> type = original.getType();

        Vec3 originalPos = original.position();
        boolean spawned = false;

        for (int attempt = 0; attempt < MAX_SPAWN_ATTEMPTS; attempt++) {
            double dx = (level.getRandom().nextDouble() * 2.0 - 1.0) * SPAWN_RADIUS;
            double dz = (level.getRandom().nextDouble() * 2.0 - 1.0) * SPAWN_RADIUS;
            Vec3 spawnPos = originalPos.add(dx, 0.0, dz);

            Entity newEntity = type.spawn(level, BlockPos.containing(spawnPos), null);

            if (newEntity instanceof LivingEntity duplicate) {
                duplicate.setYRot(original.getYRot());
                duplicate.setXRot(original.getXRot());
                duplicate.yHeadRot = original.yHeadRot;
                duplicate.yBodyRot = original.yBodyRot;

                spawned = true;
                Duplicatemob.LOGGER.info("Successfully duplicated entity: " + type);
                break;
            } else if (newEntity != null) {
                newEntity.discard();
            }
        }

        if (!spawned) {
            Vec3 fallbackPos = originalPos.add(0, 1.0, 0);
            Entity newEntity = type.spawn(level, BlockPos.containing(fallbackPos), null);

            if (newEntity instanceof LivingEntity duplicate) {
                duplicate.setYRot(original.getYRot());
                duplicate.setXRot(original.getXRot());
                Duplicatemob.LOGGER.info("Spawned duplicate at fallback position: " + type);
            } else {
                Duplicatemob.LOGGER.error("Failed to spawn duplicate entity: " + type);
                if (newEntity != null) {
                    newEntity.discard();
                }
            }
        }
    }
}