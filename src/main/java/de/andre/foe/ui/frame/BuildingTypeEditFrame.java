package de.andre.foe.ui.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.beans.PropertyVetoException;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import de.andre.foe.ui.component.SpringUtilities;
import de.andre.foe.ui.data.BuildingType;
import de.andre.foe.ui.data.Datacenter;
import lombok.extern.log4j.Log4j;

@Log4j
public class BuildingTypeEditFrame extends JInternalFrameBase {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private Datacenter datacenter;
  private BuildingType buildingType;
  private EditType editMode;

  public BuildingTypeEditFrame(Datacenter datacenter, EditType editMode, BuildingType entity) {
    super();
    this.datacenter = datacenter;
    this.buildingType = entity;
    this.editMode = editMode;
    init();
    pack();
  }

  private void init() {
    setLayout(new BorderLayout());
    JPanel pFields = new JPanel(new SpringLayout());
    JLabel lName = new JLabel("Name:");
    JTextField tfName = new JTextField(15);

    JLabel lWidth = new JLabel("Width:");
    JSpinner tfWidth = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

    JLabel lHeight = new JLabel("Height:");
    JSpinner tfHeight = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

    JLabel lColor = new JLabel("Color:");
    JButton tfColor = new JButton("change");
    tfColor.setBackground(Color.blue);
    tfColor.setOpaque(true);
    tfColor.addActionListener(e -> {
      Color newColor =
          JColorChooser.showDialog(this, "Choose building color", tfColor.getBackground());
      if (newColor != null) {
        tfColor.setBackground(newColor);
      }
    });

    pFields.add(lName);
    pFields.add(tfName);
    pFields.add(lWidth);
    pFields.add(tfWidth);
    pFields.add(lHeight);
    pFields.add(tfHeight);
    pFields.add(lColor);
    pFields.add(tfColor);

    // Lay out the panel.
    SpringUtilities.makeCompactGrid(pFields, // parent
        4, 2, // rows, columns
        3, 3, // initX, initY
        3, 3); // xPad, yPad

    add(pFields, BorderLayout.CENTER);

    JPanel pButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JButton bOk = new JButton("apply");
    switch (editMode) {
      case EDIT:
        setTitle("Edit building type");
        bOk.setText("apply");
        break;
      case ADD:
        setTitle("Create building type");
        bOk.setText("add");
        break;
    }
    JButton bCancel = new JButton("cancel");
    pButtons.add(bCancel);
    pButtons.add(bOk);

    add(pButtons, BorderLayout.SOUTH);

    if (editMode == EditType.EDIT) {
      // apply values to fields
      tfName.setText(buildingType.getName());
      tfWidth.setValue(buildingType.getWidth());
      tfHeight.setValue(buildingType.getHeight());
      tfColor.setBackground(buildingType.getFillColor());
    }

    bOk.addActionListener(e -> {
      // apply all values to building type
      if (buildingType == null) {
        buildingType = new BuildingType();
      }
      buildingType.setName(tfName.getText().trim());
      buildingType.setWidth((Integer) tfWidth.getValue());
      buildingType.setHeight((Integer) tfHeight.getValue());
      buildingType.setFillColor(tfColor.getBackground()); 
      if (editMode == EditType.ADD) {
        datacenter.add(buildingType);
      }

      try {
        BuildingTypeEditFrame.this.setClosed(true);
      } catch (PropertyVetoException e1) {
        log.warn("Cannot close frame.", e1);
      }
    });
  }

  public enum EditType {
    EDIT, ADD
  }
}
