package com.viruss.waw.common.rituals.actions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Predicate;

public abstract class AbstractRitualAction extends ForgeRegistryEntry<AbstractRitualAction> {
    public final ParticleType<?> particle;
    public final Predicate<LivingEntity> sacrifice;

    public AbstractRitualAction(ParticleType<?> particle, Predicate<LivingEntity> sacrifice) {
        this.particle = particle;
        this.sacrifice = sacrifice;
    }
    public AbstractRitualAction(ParticleType<?> particle) {
        this.particle = particle;
        this.sacrifice = null;
    }

    private TranslatableComponent getInvalidMessage() {
        return new TranslatableComponent("waw.ritual.condition.sacrifice");
    }

    public boolean isValid(Level level, BlockPos pos, Player player/*,Entity sacrifice*/) {
//        if(sacrifice == null){
//            player.displayClientMessage(getInvalidMessage(),true);
//            return false;
//        }
        return true;
    }

    public ActionResult start(Level level, BlockPos centerPos, Container inventory, Player player /*,boolean familiar , Entity sacrifice*/) {
        if(isValid(level,centerPos,player)) {
            inventory.clearContent();
            return ActionResult.PASS;
        }

        Containers.dropContents(level,centerPos,inventory);
        return ActionResult.FAIL;
    }

    public abstract ActionResult tick(Level level, BlockPos centerPos/*, boolean familiar*/);

    public enum ActionResult {
        SUCCESS,
        PASS,
        FAIL;

        public boolean isFinished(){
            return this == SUCCESS || this == FAIL;
        }
    }
}
