package com.santipdr.winterfall.registry;

import com.santipdr.winterfall.WinterFall;
import com.santipdr.winterfall.salvage.ScrapperBlock;
import com.santipdr.winterfall.salvage.WorkbenchBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WinterFall.MOD_ID);
    public static final RegistryObject<Block> SCRAPPER = BLOCKS.register("scrapper",
            () -> new ScrapperBlock(BlockBehaviour.Properties.of().strength(3.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> WORKBENCH = BLOCKS.register("workbench",
            () -> new WorkbenchBlock(BlockBehaviour.Properties.of().strength(3.5F, 6.0F).sound(SoundType.WOOD).requiresCorrectToolForDrops()));

    private ModBlocks() {}
}
