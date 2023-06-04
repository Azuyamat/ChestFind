package com.azuyamat

import com.azuyamat.commands.ChestFind
import net.fabricmc.api.ClientModInitializer

object ExampleModClient : ClientModInitializer {

	override fun onInitializeClient() {
		registerCommands()
	}

	private fun registerCommands(){
		ChestFind.register("findchests")
	}
}