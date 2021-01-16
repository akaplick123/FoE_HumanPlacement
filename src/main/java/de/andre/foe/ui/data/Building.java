package de.andre.foe.ui.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Model of a building
 *
 * @author Andre
 */
@Data
@EqualsAndHashCode(of = "id")
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
