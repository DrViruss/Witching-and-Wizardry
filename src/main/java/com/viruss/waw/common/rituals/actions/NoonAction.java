package com.viruss.waw.common.rituals.actions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public class NoonAction extends AbstractRitualAction{
    public NoonAction(ParticleType<?> particle) {
        super(particle);
    }

    @Override
    public ActionResult tick(Level level, BlockPos centerPos) {
        if (level.getDayTime() % 24000 < 3000 || level.getDayTime() % 24000 >= 12000) { //thanks Elucent! (https://github.com/elucent/eidolon)
                ((ServerLevel) level).setDayTime(level.getDayTime() + 100);
                for (ServerPlayer player : ((ServerLevel) level).players()) {
                    player.connection.send(new ClientboundSetTimePacket(level.getGameTime(), level.getDayTime(), level.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)));
                }
            return ActionResult.PASS;
        }
        return ActionResult.SUCCESS;
    }

    private TranslatableComponent getInvalidMessage() {
        return new TranslatableComponent("waw.ritual.condition.night");
    }

    @Override
    public boolean isValid(Level level, BlockPos pos, Player player) {
        if( !(level.getDayTime() % 24000 < 3000 || level.getDayTime() % 24000 >= 12000) ){
            player.displayClientMessage(getInvalidMessage(),true);
            return false;
        }
        return super.isValid(level, pos, player);
    }
}
