package org.zrgs.errorprone;

import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ShortSetTest is a demonstration class to show what kinds of problems the errorprone
 * compiler can identify and report with existing Java code
 *
 * the code below compiles, but it has been modified from the original example
 *
 * what didn't compile was the statement
 *
 * s.remove(i - 1);
 *
 * as it resolved to int and not to short
 */
public class ShortSetTest
{
  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Test
  public void testShortSet()
  {

    Set<Short> s = new HashSet<>();

    for (short i = 0; i < 100; i++)
    {
      s.add(i);
      s.remove(i - 1);
    }

    log.info("the size  of the set is " + s.size());

    assertThat(s.size()).isEqualTo(1);
  }
}
