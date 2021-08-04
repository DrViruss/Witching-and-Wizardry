package com.viruss.waw.client.models;

import com.viruss.waw.common.entities.BroomEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

@SuppressWarnings("all")
public class BroomModel<T extends BroomEntity> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart ring;
    private final ModelPart bristle1;
    private final ModelPart bristle2;
    private final ModelPart bristle3;
    private final ModelPart bristle4;
    private final ModelPart bristle5;
    private final ModelPart bristle6;
    private final ModelPart bristle7;
    private final ModelPart bristle8;
    private final ModelPart bristle9;
    private final ModelPart bristle10;
    private final ModelPart bristle11;
    private final ModelPart bb_main;

    public BroomModel(ModelPart main) {
        root = main;
        ring =main.getChild("ring");
        bristle1 = main.getChild("bristle1");
        bristle2 = main.getChild("bristle2");
        bristle3 = main.getChild("bristle3");
        bristle4 = main.getChild("bristle4");
        bristle5 = main.getChild("bristle5");
        bristle6 = main.getChild("bristle6");
        bristle7 = main.getChild("bristle7");
        bristle8 = main.getChild("bristle8");
        bristle9 = main.getChild("bristle9");
        bristle10 = main.getChild("bristle10");
        bristle11 = main.getChild("bristle11");
        bb_main = main.getChild("bb_main");

        setRotationAngle(bristle1, 0.0F, -0.3054F, -2.6616F);
        setRotationAngle(bristle2, 0.0F, -0.3054F, -1.9635F);
        setRotationAngle(bristle3, 0.0F, -0.3054F, -1.2654F);
        setRotationAngle(bristle4, 0.0F, -0.3054F, -0.5236F);
        setRotationAngle(bristle5, 0.0F, -0.3054F, 1.7453F);
        setRotationAngle(bristle6, 0.0F, -0.3491F, 0.9599F);
        setRotationAngle(bristle7, 0.0436F, -0.3054F, 0.1745F);
        setRotationAngle(bristle8, 0.0F, -0.0436F, 1.3963F);
        setRotationAngle(bristle9, 0.0F, 0.1309F, 1.1345F);
        setRotationAngle(bristle10, 0.2182F, 0.0F, 0.4363F);
        setRotationAngle(bristle11, 0.0F, -0.3054F, 2.5744F);
    }

    @Override
    public void setupAnim(BroomEntity p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) {
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition modelDefinition = new MeshDefinition();
        PartDefinition def = modelDefinition.getRoot();

        def.addOrReplaceChild("ring",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-2.0F, -4.0F, 12.0F, 1.0F, 1.0F, 1.0F)
                .addBox(-1.0F, -5.0F, 12.0F, 1.0F, 1.0F, 1.0F)
                .addBox(-1.0F, -3.0F, 12.0F, 1.0F, 1.0F, 1.0F)
                .addBox(0.0F, -4.0F, 12.0F, 1.0F, 1.0F, 1.0F),
            PartPose.offset(0.0F, 7F, 0.0F));

        def.addOrReplaceChild("bristle1",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-1.5F, -0.5F, -3.5F, 1.0F, 1.0F, 7.0F),
            PartPose.offset(-0.5F, 3.5F, 15.5F));

        def.addOrReplaceChild("bristle2",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5F, -0.5F, -3.5F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(-0.5F, 3.5F, 15.5F));

        def.addOrReplaceChild("bristle3",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5F, -0.5F, -3.5F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(-0.5F, 3.5F, 15.5F));

        def.addOrReplaceChild("bristle4",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5F, -0.5F, -3.5F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(-0.5F, 3.5F, 15.5F));

        def.addOrReplaceChild("bristle5",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5F, -0.5F, -3.5F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(-0.5F, 3.5F, 15.5F));

        def.addOrReplaceChild("bristle6",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5F, -0.5F, -3.5F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(-0.5F, 3.5F, 15.5F));

        def.addOrReplaceChild("bristle7",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5F, -0.5F, -3.5F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(-0.5F, 3.5F, 15.5F));

        def.addOrReplaceChild("bristle8",
                CubeListBuilder.create()
                        .texOffs(1, 1)
                        .addBox(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offset(-0.9873F, 3.0285F, 15.8463F));

        def.addOrReplaceChild("bristle9",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -0.5F, -4.0F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(-0.5F, 3.3496F, 15.9769F));

        def.addOrReplaceChild("bristle10",
                CubeListBuilder.create()
                        .texOffs(1, 1)
                        .addBox(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offset(-0.5F, 3.6082F, 15.9881F));

        def.addOrReplaceChild("bristle11",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5F, -0.5F, -3.5F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(-0.5F, 3.5F, 15.5F));

        def.addOrReplaceChild("bb_main",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.0F, -4.0F, -7.0F, 1.0F, 1.0F, 19.0F),
                PartPose.offset(0.0F, 7F, 0.0F));



        return LayerDefinition.create(modelDefinition, 32, 32);
    }


    private void setRotationAngle(ModelPart ModelPart, float x, float y, float z) {
        ModelPart.xRot = x;
        ModelPart.yRot = y;
        ModelPart.zRot = z;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
