package de.andre.foe.ui.data;

import java.awt.Color;
import lombok.Data;

@Data
public class BuildingType {
  private String name;
  private int width = 1;
  private int height = 1;
  private int layer = 1;
  private Color fillColor = Color.cyan;
  private Color borderColor = null;
  private boolean showGrid = false;
  private Color gridColor = null;
}
