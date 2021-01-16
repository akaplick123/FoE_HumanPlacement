package de.andre.foe.ui.data;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import javax.swing.event.EventListenerList;
import org.springframework.stereotype.Component;

@Component
public class Datacenter {

  private final EventListenerList eventListeners = new EventListenerList();
  private final List<BuildingType> buildingTypes = new ArrayList<>();
  private final List<Building> buildings = new ArrayList<>();
  private File file;

  public Datacenter() {
    // add some basic BuildingTypes ...
    BuildingType b;

    b = BuildingType.builder()
        .name("Grid (4x4)")
        .width(4)
        .height(4)
        .fillColor(Color.white)
        .gridColor(Color.lightGray)
        .showGrid(true)
        .build();
    buildingTypes.add(b);
  }

  public void add(BuildingType buildingType) {
    this.buildingTypes.add(buildingType);
    fireBuildingTypeAddedEvent(buildingType);
  }

  public void remove(BuildingType buildingType) {
    this.buildingTypes.remove(buildingType);
    fireBuildingTypeRemovedEvent(buildingType);
  }

  public List<BuildingType> getBuildingTypes() {
    return buildingTypes;
  }

  public void add(Building building) {
    this.buildings.add(building);
    fireBuildingAddedEvent(building);
  }

  public void remove(Building building) {
    this.buildings.remove(building);
    fireBuildingRemovedEvent(building);
  }

  public List<Building> getBuildings() {
    return buildings;
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
    fireFilenameChangedEvent(this.file);
  }

  protected void fireBuildingTypeAddedEvent(BuildingType buildingType) {
    for (BuildingtypeAddedListener l : eventListeners
        .getListeners(BuildingtypeAddedListener.class)) {
      l.buildingtypeAdded(buildingType);
    }
  }

  protected void fireBuildingTypeRemovedEvent(BuildingType buildingType) {
    for (BuildingtypeRemovedListener l : eventListeners
        .getListeners(BuildingtypeRemovedListener.class)) {
      l.buildingtypeRemoved(buildingType);
    }
  }

  protected void fireBuildingAddedEvent(Building building) {
    for (BuildingAddedListener l : eventListeners.getListeners(BuildingAddedListener.class)) {
      l.buildingAdded(building);
    }
  }

  protected void fireBuildingRemovedEvent(Building building) {
    for (BuildingRemovedListener l : eventListeners.getListeners(BuildingRemovedListener.class)) {
      l.buildingRemoved(building);
    }
  }

  protected void fireFilenameChangedEvent(File newFilename) {
    for (FilenameChangedListener l : eventListeners.getListeners(FilenameChangedListener.class)) {
      l.filenameChanged(newFilename);
    }
  }


  public void addBuildingtypeAddedListener(BuildingtypeAddedListener l) {
    this.eventListeners.add(BuildingtypeAddedListener.class, l);
  }

  public void removeBuildingtypeAddedListener(BuildingtypeAddedListener l) {
    this.eventListeners.remove(BuildingtypeAddedListener.class, l);
  }

  public void addBuildingtypeRemovedListener(BuildingtypeRemovedListener l) {
    this.eventListeners.add(BuildingtypeRemovedListener.class, l);
  }

  public void removeBuildingtypeRemovedListener(BuildingtypeRemovedListener l) {
    this.eventListeners.remove(BuildingtypeRemovedListener.class, l);
  }


  public void addBuildingAddedListener(BuildingAddedListener l) {
    this.eventListeners.add(BuildingAddedListener.class, l);
  }

  public void removeBuildingAddedListener(BuildingAddedListener l) {
    this.eventListeners.remove(BuildingAddedListener.class, l);
  }

  public void addBuildingRemoveListener(BuildingRemovedListener l) {
    this.eventListeners.add(BuildingRemovedListener.class, l);
  }

  public void removeBuildingRemovedListenerListener(BuildingRemovedListener l) {
    this.eventListeners.remove(BuildingRemovedListener.class, l);
  }

  public void addFilenameChangedListener(FilenameChangedListener l) {
    this.eventListeners.add(FilenameChangedListener.class, l);
  }

  public void removeFilenameChangedListener(FilenameChangedListener l) {
    this.eventListeners.remove(FilenameChangedListener.class, l);
  }


  public interface BuildingtypeAddedListener extends EventListener {

    void buildingtypeAdded(BuildingType buildingType);
  }

  public interface BuildingtypeRemovedListener extends EventListener {

    void buildingtypeRemoved(BuildingType buildingType);
  }

  public interface BuildingAddedListener extends EventListener {

    void buildingAdded(Building building);
  }

  public interface BuildingRemovedListener extends EventListener {

    void buildingRemoved(Building building);
  }

  public interface FilenameChangedListener extends EventListener {

    void filenameChanged(File newFilename);
  }

}
