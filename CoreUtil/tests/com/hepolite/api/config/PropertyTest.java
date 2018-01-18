package com.hepolite.api.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.hepolite.api.config.IProperty;
import com.hepolite.api.config.Property;

class PropertyTest
{
	@Test
	void testConstructor()
	{
		new Property();
		new Property("property");
		new Property("root.property");
		new Property("", "property");
		new Property("root", "property");

		assertThrows(IllegalArgumentException.class, () -> new Property(null));
		assertThrows(IllegalArgumentException.class, () -> new Property("."));
		assertThrows(IllegalArgumentException.class, () -> new Property(null, "property"));
		assertThrows(IllegalArgumentException.class, () -> new Property(".", "property"));
		assertThrows(IllegalArgumentException.class, () -> new Property("root", null));
		assertThrows(IllegalArgumentException.class, () -> new Property("root", "."));
	}

	@Test
	void testGetPath()
	{
		final Property propertyA = new Property();
		final Property propertyB = new Property("property");
		final Property propertyC = new Property("root.property");
		final Property propertyD = new Property("longer.path.to", "property");

		assertEquals("", propertyA.getPath());
		assertEquals("property", propertyB.getPath());
		assertEquals("root.property", propertyC.getPath());
		assertEquals("longer.path.to.property", propertyD.getPath());
	}
	@Test
	void testGetRoot()
	{
		final Property propertyA = new Property();
		final Property propertyB = new Property("property");
		final Property propertyC = new Property("root.property");
		final Property propertyD = new Property("longer.path.to", "property");

		assertEquals("", propertyA.getRoot());
		assertEquals("", propertyB.getRoot());
		assertEquals("root", propertyC.getRoot());
		assertEquals("longer.path.to", propertyD.getRoot());
	}
	@Test
	void testGetName()
	{
		final Property propertyA = new Property();
		final Property propertyB = new Property("property");
		final Property propertyC = new Property("root.property");
		final Property propertyD = new Property("longer.path.to", "property");

		assertEquals("", propertyA.getName());
		assertEquals("property", propertyB.getName());
		assertEquals("property", propertyC.getName());
		assertEquals("property", propertyD.getName());
	}

	@Test
	void testGetParent()
	{
		final Property propertyA = new Property();
		final Property propertyB = new Property("root.property");
		final Property propertyC = new Property("longer.path.to", "property");

		assertEquals("", propertyA.parent().getPath());
		assertEquals("root", propertyB.parent().getPath());
		assertEquals("longer.path.to", propertyC.parent().getPath());
	}
	@Test
	void testGetChild()
	{
		final Property property = new Property("property");
		final Property childA = new Property("childA");
		final Property childB = new Property("root.childB");

		assertEquals("property.childA", property.child(childA).getPath());
		assertEquals("property.childC", property.child("childC").getPath());
		assertThrows(IllegalArgumentException.class, () -> property.child((String) null));
		assertThrows(IllegalArgumentException.class, () -> property.child((IProperty) null));
		assertThrows(IllegalArgumentException.class, () -> property.child(childB));
		assertThrows(IllegalArgumentException.class, () -> property.child(""));
		assertThrows(IllegalArgumentException.class, () -> property.child("."));
	}
}
