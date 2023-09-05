package com.willfp.ecoquests.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.eco.util.StringUtils
import com.willfp.ecoquests.quests.Quests
import com.willfp.ecoquests.tasks.Tasks
import org.bukkit.command.CommandSender
import org.bukkit.util.StringUtil

class CommandGiveTaskXp(plugin: EcoPlugin) : PluginCommand(
    plugin,
    "givetaskxp",
    "ecoquests.command.givetaskxp",
    false
) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        val player = notifyPlayerRequired(args.getOrNull(0), "invalid-player")
        val quest = notifyNull(Quests[args.getOrNull(1)], "invalid-quest")
        val template = notifyNull(Tasks[args.getOrNull(2)], "invalid-task")
        val task = notifyNull(quest.getTask(template), "invalid-task")
        val xp = notifyNull(args.getOrNull(3)?.toDouble(), "invalid-command")

        task.giveExperience(player, xp)

        sender.sendMessage(
            plugin.langYml.getMessage("task-xp-given-player", StringUtils.FormatOption.WITHOUT_PLACEHOLDERS)
                .replace("%quest%", quest.name)
                .replace("%task%", template.id)
                .replace("%player%", player.name)
                .replace("%xp%", xp.toString())
        )
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        val completions = mutableListOf<String>()

        if (args.size == 1) {
            StringUtil.copyPartialMatches(
                args[0],
                plugin.server.onlinePlayers.map { it.name },
                completions
            )
        }

        if (args.size == 2) {
            StringUtil.copyPartialMatches(
                args[1],
                Quests.values().map { it.id },
                completions
            )
        }

        if (args.size == 3) {
            val quest = Quests[args.getOrNull(1)] ?: return completions

            StringUtil.copyPartialMatches(
                args[2],
                quest.tasks.map { task -> task.template.id },
                completions
            )
        }

        return completions
    }
}

