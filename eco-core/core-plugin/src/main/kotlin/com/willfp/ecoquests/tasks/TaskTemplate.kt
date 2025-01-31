package com.willfp.ecoquests.tasks

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.registry.KRegistrable
import com.willfp.ecoquests.quests.Quest
import com.willfp.libreforge.ViolationContext
import com.willfp.libreforge.counters.Counters
import com.willfp.libreforge.effects.Effects

class TaskTemplate(
    private val plugin: EcoPlugin,
    override val id: String,
    val config: Config
) : KRegistrable {
    val xpGainMethods = config.getSubsections("xp-gain-methods").mapNotNull {
        Counters.compile(it, ViolationContext(plugin, "task $id tasks"))
    }

    val onComplete = Effects.compileChain(
        config.getSubsections("on-complete"),
        ViolationContext(plugin, "task $id on-complete")
    )

    fun create(quest: Quest, xpExpr: String, order: Int) =
        Task(plugin, this, quest, xpExpr, order)
}
