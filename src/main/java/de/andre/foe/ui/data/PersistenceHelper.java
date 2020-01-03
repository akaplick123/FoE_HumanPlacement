package de.andre.foe.ui.data;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import lombok.Data;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
public class PersistenceHelper {

  public void storeToFile(File file, Datacenter datacenter) {
    // ensure directory exists
    if (file.getParentFile() != null) {
      file.getParentFile().mkdirs();
    }

    // convert datacenter to storable data structure
    PDatacenter pmodel = convertToPModel(datacenter);

    String json = new GsonBuilder().setPrettyPrinting().create().toJson(pmodel);
    FileWriter fw = null;
    try {
      fw = new FileWriter(file);
      fw.write(json);
      log.info("Write gamefield to file " + file.getAbsolutePath() + ".");
    } catch (IOException e) {
      log.error("File creation failed.", e);
    } finally {
      try {
        if (fw != null) {
          fw.close();
        }
      } catch (IOException e) {
        log.error("File closing failed.", e);
      }
    }
  }

  public void loadFromFile(File file, Datacenter datacenter) {
    if (!file.exists()) {
      return;
    }

    try {
      PDatacenter pmodel =
          new GsonBuilder().create().fromJson(new FileReader(file), PDatacenter.class);

      // remove all old artifacts
      for (Building old : new ArrayList<>(datacenter.getBuildings())) {
        datacenter.remove(old);
      }
      for (BuildingType old : new ArrayList<>(datacenter.getBuildingTypes())) {
        datacenter.remove(old);
      }

      // add artifacts from file
      HashMap<Integer, BuildingType> idToBuildingType = new HashMap<>();
      for (PBuildingType pBuildingType : pmodel.buildingTypes) {
        BuildingType buildingType = convertTotModel(pBuildingType);
        idToBuildingType.put(pBuildingType.id, buildingType);
        datacenter.add(buildingType);
      }

      for (PBuilding pBuilding : pmodel.buildings) {
        Building building = convertToModel(pBuilding, idToBuildingType);
        datacenter.add(building);
      }
    } catch (JsonParseException e) {
      log.error("File reading failed.", e);
    } catch (FileNotFoundException e) {
      log.error("File reading failed.", e);
    }
  }

  private Building convertToModel(PBuilding pBuilding,
      HashMap<Integer, BuildingType> idToBuildingType) {
    Building building = new Building();
    building.setType(idToBuildingType.get(pBuilding.buildingTypeId));
    building.setX(pBuilding.x);
    building.setY(pBuilding.y);
    return building;
  }

  private BuildingType convertTotModel(PBuildingType pBuildingType) {
    BuildingType buildingType = new BuildingType();
    buildingType.setName(pBuildingType.name);
    buildingType.setMoney(pBuildingType.money);
    buildingType.setCulture(pBuildingType.culture);
    buildingType.setResources(pBuildingType.resources);
    buildingType.setPeople(pBuildingType.people);
    buildingType.setWidth(pBuildingType.width);
    buildingType.setHeight(pBuildingType.height);
    buildingType.setLayer(pBuildingType.layer);
    buildingType.setFillColor(convertToModel(pBuildingType.fillColor));
    buildingType.setShowGrid(
        pBuildingType.showGrid == null ? false : pBuildingType.showGrid.booleanValue());
    buildingType.setGridColor(convertToModel(pBuildingType.gridColor));
    buildingType.setBorderColor(convertToModel(pBuildingType.borderColor));

    return buildingType;
  }


  private Color convertToModel(PColor color) {
    if (color == null) {
      return null;
    }
    return new Color(color.r, color.g, color.b, color.a);
  }

  private PDatacenter convertToPModel(Datacenter datacenter) {
    HashMap<BuildingType, Integer> buildingTypeToId = new HashMap<>();
    PDatacenter pDatacenter = new PDatacenter();

    int id = 1;
    for (BuildingType buildingType : datacenter.getBuildingTypes()) {
      PBuildingType pBuildingType = convertToPModel(buildingType, id++);
      buildingTypeToId.put(buildingType, pBuildingType.id);
      pDatacenter.buildingTypes.add(pBuildingType);
    }

    for (Building building : datacenter.getBuildings()) {
      PBuilding pBuilding = convertToPModel(building, buildingTypeToId);
      pDatacenter.buildings.add(pBuilding);
    }

    return pDatacenter;
  }

  private PBuilding convertToPModel(Building building,
      HashMap<BuildingType, Integer> buildingTypeToId) {
    PBuilding pBuilding = new PBuilding();
    pBuilding.x = building.getX();
    pBuilding.y = building.getY();
    pBuilding.buildingTypeId = buildingTypeToId.get(building.getType());
    return pBuilding;
  }

  private PBuildingType convertToPModel(BuildingType buildingType, int id) {
    PBuildingType pBuildingType = new PBuildingType();
    pBuildingType.id = id;
    pBuildingType.name = buildingType.getName();
    pBuildingType.money = buildingType.getMoney();
    pBuildingType.culture = buildingType.getCulture();
    pBuildingType.resources = buildingType.getResources();
    pBuildingType.people = buildingType.getPeople();
    pBuildingType.width = buildingType.getWidth();
    pBuildingType.height = buildingType.getHeight();
    pBuildingType.layer = buildingType.getLayer();
    pBuildingType.fillColor = convertToPModel(buildingType.getFillColor());
    pBuildingType.showGrid = (buildingType.isShowGrid() ? true : null);
    pBuildingType.gridColor = convertToPModel(buildingType.getGridColor());
    pBuildingType.borderColor = convertToPModel(buildingType.getBorderColor());

    return pBuildingType;
  }

  private PColor convertToPModel(Color color) {
    if (color == null) {
      return null;
    }

    PColor pColor = new PColor();
    pColor.r = color.getRed();
    pColor.g = color.getGreen();
    pColor.b = color.getBlue();
    pColor.a = color.getAlpha();
    return pColor;
  }

  /**
   * Persistence model of all data (JSON representation)
   * @author Andre
   */
  @Data
  private static class PDatacenter {
    private List<PBuildingType> buildingTypes = new ArrayList<>();
    private List<PBuilding> buildings = new ArrayList<>();
  }

  /**
   * Persistence model of a building type (JSON representation)
   * @author Andre
   */
  @Data
  private static class PBuildingType {
    int id;
    String name;
    int money = 0;
    int culture = 0;
    int resources = 0;
    int people = 0;
    int width;
    int height;
    int layer = 1;
    PColor fillColor;
    PColor borderColor;
    Boolean showGrid;
    PColor gridColor;
  }

  /**
   * Persistence model of a building (JSON representation)
   * @author Andre
   */
  @Data
  private static class PBuilding {
    int x;
    int y;
    int buildingTypeId;
  }

  /**
   * Persistence model of an color (JSON representation)
   * @author Andre
   */
  @Data
  private static class PColor {
    int r, g, b, a;
  }
}
