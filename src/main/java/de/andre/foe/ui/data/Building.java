package de.andre.foe.ui.data;

import lombok.Data;

@Data
public class Building {
  private static int id_sequence = 1;

  private int id;
  private int x;
  private int y;
  private BuildingType type;
  
  public Building() {
    this.id = (id_sequence++);
  }
}
