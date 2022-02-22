package com.viruss.waw.utils.recipes.bases;

import com.electronwill.nightconfig.core.conversion.InvalidValueException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.viruss.waw.Main;
import com.viruss.waw.common.objects.items.Chalk;
import com.viruss.waw.common.rituals.actions.AbstractRitualAction;
import com.viruss.waw.utils.ModUtils;
import com.viruss.waw.utils.recipes.RecipeTypes;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;
import oshi.util.tuples.Pair;

import javax.annotation.Nullable;
import java.util.*;

@SuppressWarnings("all")
public class RitualRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;
    private final Pair<String,Integer>[] circles;
    private final List<Pair<Integer,Integer>> candles;
    private final int time;
    private final int cost;
    private final AbstractRitualAction action;

    public RitualRecipe(ResourceLocation id, ItemStack result, AbstractRitualAction action, int time , int cost , Pair<String,Integer>[] circles, List<Pair<Integer,Integer>> candles, Ingredient... ingredients) {
        if(action == null) throw new InvalidValueException("Please add ACTION to ritual"+id.toString());
        this.id = id;
        this.ingredients = fromArray(ingredients);
        List<Pair<String,Integer>> tmp = Arrays.stream(circles).filter(Objects::nonNull).toList();
        this.circles = tmp.toArray(Arrays.copyOf(circles,tmp.size()));
        this.candles = candles;
        this.result = result;
        this.time = time;
        this.cost = cost;
        this.action = action;
    }

    public AbstractRitualAction getAction() {
        return action;
    }

    private NonNullList<Ingredient> fromArray(Ingredient... ingredients){
        NonNullList<Ingredient> result = NonNullList.create();
        Collections.addAll(result, ingredients);
        return result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        Ingredient ingredients = Ingredient.merge(this.ingredients);
        for (int j = 0; j < container.getContainerSize(); j++) {
            ItemStack item = container.getItem(j);
            if (!item.isEmpty() && !ModUtils.Ingredient.testItem(ingredients,item) || item.getItem() instanceof Chalk && !testChalk(ingredients.getItems(),item))
                return false;
        }
        return true;
    }

    public boolean testAdditional(Level level, Player player, BlockPos centerPos){
        //TODO: add: altar, familiar, sacrifice

        return testArea(level,centerPos) && (candles == null || testCandles(level, centerPos));
    }

    private boolean testArea(Level level, BlockPos centerPos){
        for(Pair<String,Integer> circle : circles)
            if(!ModUtils.Rituals.checkChalk(level,ModUtils.Rituals.getBorderCoords(centerPos,ModUtils.Rituals.circleByName(circle.getA())),this.getCircleColor(circle.getA())))
                return false;
        return true;
    }

    private boolean testCandles(Level level, BlockPos centerPos){
        List<BlockPos> poses = new ArrayList<>();
        for(Pair<String,Integer> circle : circles)
           poses.addAll(Arrays.stream(ModUtils.Rituals.candlesPoses(ModUtils.Rituals.getBorderCoords(centerPos, ModUtils.Rituals.circleByName(circle.getA())))).toList());

        return ModUtils.Rituals.checkCandles(level, poses, candles);
    }

    private boolean testChalk(ItemStack[] ingredients,ItemStack containerItem){
        for(ItemStack stack : ingredients){
            if(!(stack.getItem() instanceof Chalk))
                continue;
            int color0 = stack.getTag().contains("color") ? stack.getTag().getInt("color") : 0;
            int color1 = containerItem.getTag().contains("color") ? containerItem.getTag().getInt("color") : 0;
            if(color0 == color1)
                return testDurability(containerItem,stack.getDamageValue());
        }
        return false;
    }

    private boolean testDurability(ItemStack stack,int durability){
        return stack.getDamageValue() <= durability;
    }

    @Override
    public ItemStack assemble(SimpleContainer inventory) {
        inventory.clearContent();
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

    public int getCircleColor(String type) {
        for(Pair<String,Integer> circle : circles)
            if(Objects.equals(circle.getA(), type))
                return circle.getB();
        return Integer.MAX_VALUE;
    }

    public List<Pair<Integer, Integer>> getCandles() {
        return candles;
    }

    public int getTime() {
        return time;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public ResourceLocation getId() {return id;}

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeTypes.Rituals.SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypes.Rituals.TYPE;
    }

    @Override
    public String getGroup() {
        return Main.MOD_ID+"ritual";
    }

    public static class FinishedRecipe implements net.minecraft.data.recipes.FinishedRecipe {
        private final ResourceLocation id;
        private final ItemStack output;
        private final Ingredient[] input;
        private final Pair<String,Integer>[] circles;
        private final List<Pair<Integer,Integer>> candles;
        private final int time;
        private final int cost;
        private final AbstractRitualAction action;


        public FinishedRecipe(Item result,AbstractRitualAction action ,int time,int cost,Pair<String,Integer>[] circles,List<Pair<Integer,Integer>> candles, Ingredient... ingredients) {
            this.id = new ResourceLocation(Main.MOD_ID,"rituals/"+ Objects.requireNonNull(result.getRegistryName()).getPath());
            this.input = ingredients;
            this.circles=circles;
            this.candles=candles;
            this.time=time;
            this.cost=cost;
            this.output = result.getDefaultInstance();
            this.action = action;
        }

        public FinishedRecipe(ItemStack result,AbstractRitualAction action ,int time,int cost,Pair<String,Integer>[] circles,List<Pair<Integer,Integer>> candles, Ingredient... ingredients) {
            this.id = new ResourceLocation(Main.MOD_ID,"rituals/"+Objects.requireNonNull(result.getItem().getRegistryName()).getPath());
            this.input = ingredients;
            this.output = result;
            this.circles=circles;
            this.candles=candles;
            this.time=time;
            this.cost=cost;
            this.action = action;
        }

        public FinishedRecipe(AbstractRitualAction action, int time,int cost,Pair<String,Integer>[] circles,List<Pair<Integer,Integer>> candles, Ingredient... ingredients) {
            this.id = new ResourceLocation(Main.MOD_ID,"rituals/"+ Objects.requireNonNull(action.getRegistryName()).getPath().replaceFirst("_action",""));
            this.input = ingredients;
            this.circles=circles;
            this.candles=candles;
            this.time=time;
            this.cost=cost;
            this.output = ItemStack.EMPTY;
            this.action = action;
        }

        public FinishedRecipe(AbstractRitualAction action,int cost,Pair<String,Integer>[] circles,List<Pair<Integer,Integer>> candles, Ingredient... ingredients) {
            this.id = new ResourceLocation(Main.MOD_ID,"rituals/"+ Objects.requireNonNull(action.getRegistryName()).getPath().replaceFirst("_action",""));
            this.input = ingredients;
            this.circles=circles;
            this.candles=candles;
            this.time=0;
            this.cost=cost;
            this.output = ItemStack.EMPTY;
            this.action = action;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            if(!output.isEmpty())
                json.add("result", ModUtils.Json.serializeItemStack(output));
            json.add("action",new JsonPrimitive(action.getRegistryName().getPath()));
            if(this.time >0)
                json.add("time", new JsonPrimitive(this.time));
            json.add("cost", new JsonPrimitive(this.cost));

            //Circles
            JsonObject tmp = new JsonObject();
            JsonArray j_arr;

            for (Pair<String, Integer> circle : circles)
                tmp.add(circle.getA(), new JsonPrimitive(circle.getB()));
            json.add("circles",tmp);

            //Candles
            if(candles!=null) {
                j_arr = new JsonArray();
                for (Pair<Integer, Integer> candle : candles) {
                    JsonObject c = new JsonObject();
                    c.add("color", new JsonPrimitive(candle.getA()));
                    c.add("amount", new JsonPrimitive(candle.getB()));
                    j_arr.add(c);
                }
                json.add("candles", j_arr);
            }


            j_arr = new JsonArray();
            for(Ingredient ingredient : input)
                if(ingredient.getItems().length==1)
                    j_arr.add(ModUtils.Json.serializeItemStack(ingredient.getItems()[0]));
                else
                    j_arr.add(ingredient.toJson());

            json.add("ingredients", j_arr);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RecipeTypes.Rituals.SERIALIZER;
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

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<RitualRecipe> {

        public Serializer() {
            setRegistryName(Main.MOD_ID, "rituals");
        }

        @Override
        public RitualRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack result = ItemStack.EMPTY;
            if(json.has("result"))
                result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            int cost = json.get("cost").getAsInt();

            ArrayList<Pair<Integer,Integer>> candles = null;
            JsonObject tmp;
            if(json.has("candles")) {
                candles = new ArrayList<>();
                for(JsonElement c : json.get("candles").getAsJsonArray()){
                    JsonObject o = c.getAsJsonObject();
                    candles.add(new Pair<>(o.get("color").getAsInt(), o.get("amount").getAsInt()));
                }
            }
            JsonArray ingArr = json.get("ingredients").getAsJsonArray();
            Ingredient[] ingredients = new Ingredient[ingArr.size()];
            for(int i=0; i<ingArr.size(); i++) {
                JsonObject element = (JsonObject) ingArr.get(i);
                if (element.has("count"))
                    ingredients[i] = Ingredient.of(ShapedRecipe.itemStackFromJson(element));
                else
                    ingredients[i] = Ingredient.fromJson(ingArr.get(i));
            }

            AbstractRitualAction action =null;
            for(RegistryObject<AbstractRitualAction> act_RO : ModRegistry.RITUAL_REGISTER.getEntries())
                if(act_RO.getId().getPath().equals(json.get("action").getAsString())) {
                    action = act_RO.get();
                    break;
                }
            tmp = json.getAsJsonObject("circles");

            return new RitualRecipe(
                id,
                result,
                action,
                json.has("time") ? json.get("time").getAsInt() : 0,
                cost,
                new Pair[]{
                    tmp.has("small") ? new Pair<>("small", tmp.get("small").getAsInt()) : null,
                    tmp.has("medium") ? new Pair<>("medium", tmp.get("medium").getAsInt()) : null,
                    tmp.has("large") ? new Pair<>("large", tmp.get("large").getAsInt()) : null,
                },
                candles,
                ingredients);
        }

        @Nullable
        @Override
        public RitualRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Ingredient[] ingredients = new Ingredient[buffer.readVarInt()];
            for(int i=0; i<ingredients.length; i++)
                ingredients[i] = Ingredient.fromNetwork(buffer);

            CompoundTag tag = buffer.readNbt();

            AbstractRitualAction action =null;
            for(RegistryObject<AbstractRitualAction> act_RO : ModRegistry.RITUAL_REGISTER.getEntries())
                if(act_RO.getId().toString().equals(buffer.readResourceLocation().toString())) {
                    action = act_RO.get();
                    break;
                }

            ArrayList<Pair<Integer, Integer>> candles = null;
            if(tag.contains("candles")) {
                candles = new ArrayList<>();
                for (int i = 0; i < tag.getInt("candles"); i++){
                    candles.add(new Pair<>(tag.getInt("color_"+i),tag.getInt("amount_"+i)));
                }
            }

            return new RitualRecipe(
                id,
                buffer.readItem(),
                action,
                buffer.readInt(),
                buffer.readByte(),
                new Pair[]{
                    tag.contains("small") ? new Pair<>("small", tag.getInt("small")) : null,
                    tag.contains("medium") ? new Pair<>("medium", tag.getInt("medium")) : null,
                    tag.contains("large") ? new Pair<>("large", tag.getInt("large")) : null,
                },
                candles,
                ingredients);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RitualRecipe recipe) {
            buffer.writeItem(recipe.result);
            buffer.writeResourceLocation(Objects.requireNonNull(recipe.action.getRegistryName()));
            buffer.writeInt(recipe.time);
            buffer.writeByte(recipe.cost);

            CompoundTag tag = new CompoundTag();
            if(recipe.getCircleColor("small") != Integer.MAX_VALUE)
                tag.putInt("small",recipe.getCircleColor("small"));
            if(recipe.getCircleColor("medium") != Integer.MAX_VALUE)
                tag.putInt("medium",recipe.getCircleColor("medium"));
            if(recipe.getCircleColor("large") != Integer.MAX_VALUE)
                tag.putInt("large",recipe.getCircleColor("large"));


            if(recipe.getCandles() != null) {
                List<Pair<Integer,Integer>> candles = recipe.getCandles();
                tag.putInt("candles",candles.size());
                for(int i=0; i < candles.size(); i++) {
                    tag.putInt("color_"+i, candles.get(i).getA());
                    tag.putInt("amount_"+i, candles.get(i).getB());
                }
            }
            buffer.writeNbt(tag);


            buffer.writeVarInt(recipe.ingredients.size());
            for(Ingredient ingredient : recipe.ingredients)
                ingredient.toNetwork(buffer);
        }
    }
}