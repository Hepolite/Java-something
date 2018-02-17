package com.hepolite.api.chat;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BuilderTest
{
	@Test
	void testBuilder()
	{
		final Builder builderA = new Builder("Hello World");
		final Builder builderB = new Builder("Formatted!", Color.AQUA, Format.BOLD);
		final Builder builderC = new Builder("").addTranslatedText("key", "%2$s:%1$s");
		
		assertEquals("§fHello World", builderA.build().toString());
		assertEquals("§b§lFormatted!", builderB.build().toString());
		assertEquals("§f§f3.1415§f:§ftext", builderC.build().translate("key", "text", 3.1415f).toString());
	}
}
