package de.andre.foe.ui.frame;

import de.andre.foe.ui.component.BuildingComponent;
import de.andre.foe.ui.component.StatusBar;
import de.andre.foe.ui.data.Building;
import de.andre.foe.ui.data.Datacenter;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.AbstractAction;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

public class BuildingsPlacementFrame extends JInternalFrameBase {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private final BuildingComponent buildingComponent;
  /**
   * map to associate the model of buildings with there representation on the screen
   */
  private final Map<Building, BuildingComponent.Building> buildingMap = new HashMap<>();

  public BuildingsPlacementFrame(Datacenter datacenter, StatusBar statusBar) {
    super();
    setClosable(false);
    setTitle("Gamefield");

    setLayout(new BorderLayout());

    buildingComponent = new BuildingComponent();
    JScrollPane sc = new JScrollPane(buildingComponent);
    add(sc, BorderLayout.CENTER);
    StatusBar localStatusBar = new StatusBar();
    add(localStatusBar.getStatusBarComponent(), BorderLayout.SOUTH);

    datacenter.addBuildingAddedListener(b -> {
      BuildingComponent.Building obj =
          buildingComponent.placeBuilding(b.getX(), b.getY(), b.getType().getWidth(),
              b.getType().getHeight(), b.getType().getLayer(), b.getType().getFillColor());
      if (b.getType().isShowGrid()) {
        obj.setShowInnerGrid(true);
      }
      if (b.getType().getGridColor() != null) {
        obj.setInnerGridColor(b.getType().getGridColor());
      }
      if (b.getType().getBorderColor() != null) {
        obj.setNormalBorderColor(b.getType().getBorderColor());
      }
      buildingMap.put(b, obj);
    });

    datacenter.addBuildingRemoveListener(building -> {
      BuildingComponent.Building obj = buildingMap.get(building);
      if (obj == null) {
        System.err.println("BuildingsPlacementFrame: Object not found.");
      } else {
        buildingComponent.removeBuilding(obj);
        buildingMap.remove(building);
      }
    });

    buildingComponent.addObjectMovedListener((obj, oldPl, newPl) -> {
      Building b = findKeyByValue(buildingMap, obj);
      if (b == null) {
        System.err.println("BuildingsPlacementFrame: Object not found.");
      } else {
        b.setX(newPl.x);
        b.setY(newPl.y);
      }
    });

    buildingComponent.addObjectSelectedListener(obj -> {
      if (obj.isSelected()) {
        Building b = findKeyByValue(buildingMap, obj);
        if (b == null) {
          System.err.println("BuildingsPlacementFrame: Object not found.");
        } else {
          int cntByType = countPlacedBuildings(b);
          localStatusBar.postStatus("Selected: " + b.getType().getName() + " (1 of " + cntByType + ")");
        }
      }
    });

    this.getInputMap().put(KeyStroke.getKeyStroke('d'), "del");
    this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "del");
    this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "del");
    this.getActionMap().put("del", new AbstractAction() {
      private static final long serialVersionUID = 1L;

      @Override
      public void actionPerformed(ActionEvent e) {
        BuildingComponent.Building obj = buildingComponent.getSelectedBuilding();
        if (obj == null) {
          // no object is selected
          return;
        }
        Building b = findKeyByValue(buildingMap, obj);
        if (b == null) {
          // no building is associated with that object
          return;
        }
        datacenter.remove(b);
      }
    });
  }

  private int countPlacedBuildings(Building building) {
    int count = 0;
    for (Building mapedBuilding : buildingMap.keySet()) {
      if (mapedBuilding.getType().equals(building.getType())) {
        count++;
      }
    }
    return count;
  }

  /**
   * searches thru the given map and try to find an entry where the value equals given value.
   *
   * @param map   the map to search thru
   * @param value the searched value
   * @return the key of the found entry or <code>null</code> if no entry has the given value
   */
  private static <K, V> K findKeyByValue(Map<K, V> map, V value) {
    for (Entry<K, V> entry : map.entrySet()) {
      if (entry.getValue().equals(value)) {
        return entry.getKey();
      }
    }

    return null;
  }

}
