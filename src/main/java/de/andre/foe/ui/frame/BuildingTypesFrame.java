package de.andre.foe.ui.frame;

import de.andre.foe.ui.action.CreateInternalFrameAction;
import de.andre.foe.ui.component.SpringUtilities;
import de.andre.foe.ui.data.Building;
import de.andre.foe.ui.data.BuildingType;
import de.andre.foe.ui.data.Datacenter;
import de.andre.foe.ui.data.Datacenter.BuildingtypeAddedListener;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

public class BuildingTypesFrame extends JInternalFrameBase {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private final JDesktopPane desktop;
  private final Datacenter datacenter;
  private final JPanel pBuildingtypes;
  private int numberOfBuildingTypes = 0;


  public BuildingTypesFrame(JDesktopPane desktop, Datacenter datacenter) {
    super();
    setTitle("building types");
    this.datacenter = datacenter;
    this.desktop = desktop;

    setLayout(new BorderLayout());
    pBuildingtypes = new JPanel(new SpringLayout());
    JScrollPane sc = new JScrollPane(pBuildingtypes);
    add(sc, BorderLayout.CENTER);

    // initialize this component with all existing types
    for (BuildingType buildingType : datacenter.getBuildingTypes()) {
      addBuildingTypeComponent(buildingType);
    }

    // add types dynamically
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
    JButton bEdit = new JButton("edit");
    pBuildingtypes.add(bEdit);
    JButton bClone = new JButton("clone");
    pBuildingtypes.add(bClone);
    numberOfBuildingTypes++;

    // Lay out the panel.
    SpringUtilities.makeCompactGrid(pBuildingtypes, // parent
        numberOfBuildingTypes, 3, // rows, columns
        3, 3, // initX, initY
        3, 3); // xPad, yPad

    bAdd.addActionListener(e -> {
      // create an building
      Building building = new Building();
      building.setType(buildingType);
      datacenter.add(building);
    });

    bEdit.addActionListener(e -> {
      new CreateInternalFrameAction(desktop, () -> {
        return new BuildingTypeEditFrame(datacenter, BuildingTypeEditFrame.EditType.EDIT,
            buildingType);
      }).actionPerformed(null);
    });

    bClone.addActionListener(e -> {
      // create a new building type
      BuildingType newBuildingType = buildingType.doClone();
      newBuildingType.setName("Cloned " + buildingType.getName());
      datacenter.add(newBuildingType);
      BuildingTypesFrame.this.updateUI();
    });
  }

}
