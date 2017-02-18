package org.zrgs.errorprone;

import java.util.HashSet;
import java.util.Set;

/**
 * ShortSet is a demonstration class to show what kinds of problems the errorprone
 * compiler can identify and report with existing Java code
 */
public class ShortSet {
  public static void main (String[] args) {
    Set<Short> s = new HashSet<>();

    for (short i = 0; i < 100; i++) {
      s.add(i);
      s.remove(i - 1);
    }

    System.out.println(s.size());
  }
}
