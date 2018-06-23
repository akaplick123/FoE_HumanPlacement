package de.andre.foe.ui.data;

import java.awt.Color;
import lombok.Data;

/**
 * Model of a building type
 * 
 * @author Andre
 */
@Data
public class BuildingType {
  private String name;
  
  private int money = 0;
  private int culture = 0;
  private int resources = 0;
  private int people = 0;
  
  private int width = 1;
  private int height = 1;
  private int layer = 1;
  private Color fillColor = Color.cyan;
  private Color borderColor = null;
  private boolean showGrid = false;
  private Color gridColor = null;
}
