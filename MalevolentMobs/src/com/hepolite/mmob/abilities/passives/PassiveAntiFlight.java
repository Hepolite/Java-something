package com.hepolite.mmob.abilities.passives;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hepolite.mmob.MMobCompatibility;
import com.hepolite.mmob.abilities.PassiveAura;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;

import net.md_5.bungee.api.ChatColor;

public class PassiveAntiFlight extends PassiveAura
{
	private float exhaustion = 0.0f;
	
	private final Set<UUID> warnedPlayers = new HashSet<>();

	public PassiveAntiFlight(MalevolentMob mob, float scale)
	{
		super(mob, "Anti Flight", scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		exhaustion = settings.getScaledValue(alternative, "Exhaustion", scale, 0.0f);
	}

	@Override
	protected void applyAuraEffect(LivingEntity entity)
	{
		if (!(entity instanceof Player))
			return;
		Player player = (Player) entity;
		if (!player.isFlying())
			return;
		UUID uuid = player.getUniqueId();
		if (!warnedPlayers.contains(uuid))
		{
			player.sendMessage(ChatColor.RED + "An invisible force makes flight incredibly difficult!");
			warnedPlayers.add(uuid);
		}

		if (MMobCompatibility.hasPangaea())
			MMobCompatibility.pangaeaChangePlayerExhaustion(player, exhaustion);
		else
			player.setFlying(false);
	}

	@Override
	protected void displayAura(Location location, float range)
	{}
}
