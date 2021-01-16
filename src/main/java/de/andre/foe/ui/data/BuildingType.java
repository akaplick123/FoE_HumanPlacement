package de.andre.foe.ui.data;

import java.awt.Color;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model of a building type
 *
 * @author Andre
 */
@Data
@Builder
public class BuildingType {

  private String name;

  @Builder.Default
  private int money = 0;
  @Builder.Default
  private int culture = 0;
  @Builder.Default
  private int resources = 0;
  @Builder.Default
  private int people = 0;

  @Builder.Default
  private int width = 1;
  @Builder.Default
  private int height = 1;
  @Builder.Default
  private int layer = 1;
  @Builder.Default
  private Color fillColor = Color.cyan;
  private Color borderColor;
  @Builder.Default
  private boolean showGrid = false;
  private Color gridColor;

  public BuildingType doClone() {
    return BuildingType.builder()
        .name(this.name)
        .money(this.money)
        .culture(this.culture)
        .resources(this.resources)
        .people(this.people)
        .width(this.width)
        .height(this.height)
        .layer(this.layer)
        .fillColor(this.fillColor)
        .borderColor(this.borderColor)
        .showGrid(this.showGrid)
        .gridColor(this.gridColor)
        .build();
  }
}
