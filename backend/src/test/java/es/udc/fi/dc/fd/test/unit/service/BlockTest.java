package es.udc.fi.dc.fd.test.unit.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import es.udc.fi.dc.fd.service.Block;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class BlockTest {

  @Test
  public void testHashCode() {
    final Block block1 = new Block<>();
    final Block block2 = new Block<>();

    assertEquals(block1.hashCode(), block2.hashCode());
  }

  @Test
  public void testEquals() {
    final Block<String> block1 = new Block<>();
    final Block<String> block2 = new Block<>();

    final List<String> items1 = new ArrayList<>();
    final List<String> items2 = new ArrayList<>();
    items1.add("tests");
    items2.add("aaaaaaa");
    block2.setElements(items1);
    assertFalse(block1.equals(block2));
    block1.setElements(items2);
    assertFalse(block1.equals(block2));
    block1.setElements(items1);
    assertTrue(block1.equals(block2));
  }
}
