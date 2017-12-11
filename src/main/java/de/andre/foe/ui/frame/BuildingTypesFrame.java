package de.andre.foe.ui.frame;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import de.andre.foe.ui.component.SpringUtilities;
import de.andre.foe.ui.data.Building;
import de.andre.foe.ui.data.BuildingType;
import de.andre.foe.ui.data.Datacenter;
import de.andre.foe.ui.data.Datacenter.BuildingtypeAddedListener;

public class BuildingTypesFrame extends JInternalFrameBase {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private Datacenter datacenter;
  private JPanel pBuildingtypes;
  private int numberOfBuildingTypes = 0;

  public BuildingTypesFrame(Datacenter datacenter) {
    super();
    setTitle("building types");
    this.datacenter = datacenter;
    
    setLayout(new BorderLayout());
    pBuildingtypes = new JPanel(new SpringLayout());
    add(pBuildingtypes, BorderLayout.CENTER);

    for (BuildingType buildingType : datacenter.getBuildingTypes()) {
      addBuildingTypeComponent(buildingType);
    }

    BuildingtypeAddedListener newBuildingTypeListener = new BuildingtypeAddedListener() {
      @Override
      public void buildingtypeAdded(BuildingType buildingType) {
        addBuildingTypeComponent(buildingType);
        repaint();
      }
    };

    this.datacenter.addBuildingtypeAddedListener(newBuildingTypeListener);

    this.addInternalFrameClosedListener(e -> {
      datacenter.removeBuildingtypeAddedListener(newBuildingTypeListener);
    });
  }

  protected void addBuildingTypeComponent(final BuildingType buildingType) {
    JButton bAdd = new JButton(buildingType.getName());
    pBuildingtypes.add(bAdd);
    numberOfBuildingTypes++;
    
    // Lay out the panel.
    SpringUtilities.makeCompactGrid(pBuildingtypes, // parent
        numberOfBuildingTypes, 1, // rows, columns
        3, 3, // initX, initY
        3, 3); // xPad, yPad
    
    bAdd.addActionListener(e -> {
      // create an building
      Building building = new Building();
      building.setType(buildingType);
      datacenter.add(building);
    });
  }

}
