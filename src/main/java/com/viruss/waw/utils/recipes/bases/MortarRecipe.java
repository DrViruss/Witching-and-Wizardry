package com.viruss.waw.utils.recipes.bases;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.viruss.waw.Main;
import com.viruss.waw.common.tile.MortarTE;
import com.viruss.waw.utils.ModUtils;
import com.viruss.waw.utils.recipes.RecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nullable;
import java.util.*;

//@SuppressWarnings("all")
public class MortarRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final NonNullList<Ingredient> ingredients;
    private final int hits;
    private final ItemStack result;

    public MortarRecipe(ResourceLocation id,int hits, ItemStack result,Ingredient... ingredients) {
        if(ingredients.length>4) throw new ArrayIndexOutOfBoundsException("Mortar max supports 4 ingredients");

        this.id = id;
        this.ingredients = fromArray(ingredients);
        this.hits = hits;
        this.result = result;
    }

    private NonNullList<Ingredient> fromArray(Ingredient... ingredients){
        NonNullList<Ingredient> result = NonNullList.withSize(4,Ingredient.EMPTY);
        for(Ingredient ingredient : ingredients)
            for(int i = 0; i < result.size(); ++i){
                if(result.get(i).isEmpty()) {
                    result.set(i, ingredient);
                    break;
                }
            }
        return result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public String toString() {
        return "MortarRecipe{" +
            ", id=" + id +
            ", ingredients=" + ingredients.toString() +
            ", hits=" + hits +
            ", result=" + result +
            '}';
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        System.out.println(this);
        for(int i=0; i< ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            for (int j = 0; j < ingredient.getItems().length; i++) {
                ItemStack item = container.getItem(i);
                if (!item.isEmpty() && !ingredient.test(item))
                    return false;
            }
        }

        return true;
    }

    public int getHits() {
        return hits;
    }

    @Override
    public ItemStack assemble(SimpleContainer p_44001_) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return result;
    }

    @Override
    public ResourceLocation getId() {return id;}

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeTypes.Mortar.SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypes.Mortar.TYPE;
    }


    public static class FinishedRecipe implements net.minecraft.data.recipes.FinishedRecipe {
        private final ResourceLocation id;
        private final ItemStack output;
        private final Ingredient[] input;
        private final int hits;

        public FinishedRecipe(int hits, Item result, Ingredient... ingredients) {
            if(ingredients.length>4) throw new ArrayIndexOutOfBoundsException("Mortar max supports 4 ingredients");

            this.id = new ResourceLocation(Main.MOD_ID,"mortar/"+Objects.requireNonNull(result.getRegistryName()).getPath());
            this.input = ingredients;
            this.hits = hits;
            this.output = result.getDefaultInstance();
        }

        public FinishedRecipe(ResourceLocation id, ItemStack output, int hits, Ingredient... input) {
            if(input.length>4) throw new ArrayIndexOutOfBoundsException("Mortar max supports 4 ingredients");

            this.id = id;
            this.output = output;
            this.input = input;
            this.hits = hits;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("result", ModUtils.Json.serializeItemStack(output));
            json.add("hits", new JsonPrimitive(hits));

            JsonArray ingredients = new JsonArray();
            for(Ingredient ingredient : input)
                ingredients.add(ingredient.toJson());

            json.add("ingredients", ingredients);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RecipeTypes.Mortar.SERIALIZER;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<MortarRecipe> {

        public Serializer() {
            setRegistryName(Main.MOD_ID, "mortar");
        }

        @Override
        public MortarRecipe fromJson(ResourceLocation id, JsonObject json) {
            int hits = json.get("hits").getAsInt();
            ItemStack result = ShapedRecipe.itemFromJson(GsonHelper.getAsJsonObject(json, "result")).getDefaultInstance();

            JsonArray ingArr = json.get("ingredients").getAsJsonArray();
            Ingredient[] ingredients = new Ingredient[ingArr.size()];

            for(int i=0; i<ingArr.size(); i++)
                ingredients[i] = Ingredient.fromJson(ingArr.get(i));


            return new MortarRecipe(id, hits, result,ingredients);
        }

        @Nullable
        @Override
        public MortarRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Ingredient[] ingredients = new Ingredient[buffer.readVarInt()];
            for(int i=0; i<ingredients.length; i++)
                ingredients[i] = Ingredient.fromNetwork(buffer);
            int hits = buffer.readByte();
            ItemStack result = buffer.readItem();
            return new MortarRecipe(id, hits, result,ingredients);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, MortarRecipe recipe) {
            buffer.writeVarInt(recipe.ingredients.size());

            for(Ingredient ingredient : recipe.ingredients)
                ingredient.toNetwork(buffer);

            buffer.writeByte(recipe.hits);
            buffer.writeItem(recipe.result);
        }
    }
}
