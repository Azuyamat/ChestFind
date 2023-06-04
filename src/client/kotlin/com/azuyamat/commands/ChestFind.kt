package com.azuyamat.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.particle.ParticleTypes
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

object ChestFind {
    fun register(name : String) {
        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher: CommandDispatcher<FabricClientCommandSource?>, registryAccess: CommandRegistryAccess? ->
            dispatcher.register(
                ClientCommandManager.literal(name)
                    .then(ClientCommandManager.argument("item", StringArgumentType.string()))
                    .executes { context: CommandContext<FabricClientCommandSource> -> findChests(context)}
            )
        })
    }
}

fun findChests(context: CommandContext<FabricClientCommandSource>) : Int{
    val player: ClientPlayerEntity = context.source.player
    val radius = 10
    val playerPos: Vec3d = player.pos
    val playerBlockPos: BlockPos = BlockPos(playerPos.x.toInt(), playerPos.y.toInt(), playerPos.z.toInt())
    val chests: MutableList<BlockPos> = mutableListOf()
    for (x in -radius..radius) {
        for (y in -radius..radius) {
            for (z in -radius..radius) {
                val blockPos = playerBlockPos.add(x, y, z)
                val block: Block? = player.world.getBlockState(blockPos)?.block
                if (block == Blocks.CHEST) {
                    val chestEntity = player.world?.getBlockEntity(blockPos)
                    if (chestEntity is ChestBlockEntity) {
                        player.world.addParticle(ParticleTypes.ASH, x.toDouble(), y.toDouble(), z.toDouble(), 0.00,
                            1.00, 0.00)
                        player.sendMessage(Text.literal("Chest found ${chestEntity.pos}"))
                        chests.add(blockPos)
                    }
                }
            }
        }
    }

    return 1
}