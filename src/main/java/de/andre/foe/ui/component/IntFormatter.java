package de.andre.foe.ui.component;

public class IntFormatter {

  public static String intToText(int value) {
    if (value % 100_000 == 0 && value >= 1_000_000) {
      // e.g. 1500000 -> 1.5M
      return Integer.toString(value / 1_000_000) + "M";
    }
    if (value % 1_000 == 0 && value >= 1_000) {
      // e.g. 1500 -> 1.5K
      return Integer.toString(value / 1_000) + "K";
    }

    return Integer.toString(value);
  }

  public static int textToInt(String text) {
    String txt = text.replace(" ", "");
    int factor = 1;
    if (txt.endsWith("K") || txt.endsWith("k")) {
      factor = 1_000;
    }
    if (txt.endsWith("M") || txt.endsWith("m")) {
      factor = 1_000_000;
    }

    txt = txt.replaceAll("[^0-9-.]+", "");
    try {
      return Integer.parseInt(txt) * factor;
    } catch (Exception ex) {
      return 0;
    }
  }

}
