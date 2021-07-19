package com.viruss.waw.common.objects.blocks.bases;

import com.viruss.waw.common.objects.blocks.SignObject;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntityType;

public class AbstractSignTileEntity extends SignTileEntity {
    private final TileEntityType<?> signObject;
    public AbstractSignTileEntity(SignObject  signObject) {
        this.signObject = signObject.getTile();
    }
    public AbstractSignTileEntity(TileEntityType<?>  type) {
        this.signObject = type;
    }



    @Override
    public TileEntityType<?> getType() {
        return signObject;
    }
}
