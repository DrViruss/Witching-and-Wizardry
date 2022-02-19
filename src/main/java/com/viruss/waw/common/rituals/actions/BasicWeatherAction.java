package com.viruss.waw.common.rituals.actions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class BasicWeatherAction extends AbstractRitualAction{
    final Consumer<Level> type;
    public BasicWeatherAction(ParticleType<?> particle, Consumer<Level> type) {
        super(particle);
        this.type = type;
    }

    @Override
    public ActionResult tick(Level level, BlockPos centerPos) {return ActionResult.PASS;}

    @Override
    public ActionResult start(Level level, BlockPos centerPos, Container inventory, Player player) {
        super.start(level, centerPos, inventory, player);
        type.accept(level);
        return ActionResult.SUCCESS;
    }
}
