package de.andre.foe.ui.frame;

import de.andre.foe.ui.component.IntFormatter;
import de.andre.foe.ui.component.SpringUtilities;
import de.andre.foe.ui.data.Building;
import de.andre.foe.ui.data.BuildingType;
import de.andre.foe.ui.data.Datacenter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
public class BuildingTypeEditFrame extends JInternalFrameBase {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private final Datacenter datacenter;
  private BuildingType buildingType;
  private final EditType editMode;

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
    tfName.setHorizontalAlignment(JTextField.TRAILING);

    JLabel lWidth = new JLabel("Width:");
    JSpinner tfWidth = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

    JLabel lHeight = new JLabel("Height:");
    JSpinner tfHeight = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

    JLabel lPeople = new JLabel("People:");
    JTextField tfPeople = new JTextField(15);
    tfPeople.setHorizontalAlignment(JTextField.TRAILING);

    JLabel lMoney = new JLabel("Money per Day:");
    JTextField tfMoney = new JTextField(15);
    tfMoney.setHorizontalAlignment(JTextField.TRAILING);

    JLabel lCulture = new JLabel("Culture per Day:");
    JTextField tfCulture = new JTextField(15);
    tfCulture.setHorizontalAlignment(JTextField.TRAILING);

    JLabel lResources = new JLabel("Resources per Day:");
    JTextField tfResources = new JTextField(15);
    tfResources.setHorizontalAlignment(JTextField.TRAILING);

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
    pFields.add(lPeople);
    pFields.add(tfPeople);
    pFields.add(lMoney);
    pFields.add(tfMoney);
    pFields.add(lCulture);
    pFields.add(tfCulture);
    pFields.add(lResources);
    pFields.add(tfResources);
    pFields.add(lColor);
    pFields.add(tfColor);

    // Lay out the panel.
    SpringUtilities.makeCompactGrid(pFields, // parent
        8, 2, // rows, columns
        3, 3, // initX, initY
        3, 3); // xPad, yPad

    add(pFields, BorderLayout.CENTER);

    JPanel pButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JButton bOk = new JButton("apply");
    JButton bDelete = new JButton("delete");
    switch (editMode) {
      case EDIT:
        setTitle("Edit building type");
        bOk.setText("apply");
        break;
      case ADD:
        setTitle("Create building type");
        bOk.setText("add");
        bDelete.setVisible(false);
        break;
    }
    JButton bCancel = new JButton("cancel");
    pButtons.add(bCancel);
    pButtons.add(bDelete);
    pButtons.add(bOk);

    add(pButtons, BorderLayout.SOUTH);

    if (editMode == EditType.EDIT) {
      // apply values to fields
      tfName.setText(buildingType.getName());
      tfWidth.setValue(buildingType.getWidth());
      tfHeight.setValue(buildingType.getHeight());
      tfColor.setBackground(buildingType.getFillColor());
      tfPeople.setText(intToText(buildingType.getPeople()));
      tfMoney.setText(intToText(buildingType.getMoney()));
      tfCulture.setText(intToText(buildingType.getCulture()));
      tfResources.setText(intToText(buildingType.getResources()));
    }

    bOk.addActionListener(e -> {
      // apply all values to building type
      if (buildingType == null) {
        buildingType = BuildingType.builder().build();
      }
      buildingType.setName(tfName.getText().trim());
      buildingType.setWidth((Integer) tfWidth.getValue());
      buildingType.setHeight((Integer) tfHeight.getValue());
      buildingType.setFillColor(tfColor.getBackground());
      buildingType.setPeople(textToInt(tfPeople.getText()));
      buildingType.setMoney(textToInt(tfMoney.getText()));
      buildingType.setCulture(textToInt(tfCulture.getText()));
      buildingType.setResources(textToInt(tfResources.getText()));
      if (editMode == EditType.ADD) {
        datacenter.add(buildingType);
      }

      try {
        BuildingTypeEditFrame.this.setClosed(true);
      } catch (PropertyVetoException e1) {
        log.warn("Cannot close frame.", e1);
      }
    });

    bDelete.addActionListener(e -> {
      int result =
          JOptionPane.showConfirmDialog(this, "Do you really want to delete that building type?",
              "Delete building type", JOptionPane.YES_NO_OPTION);
      if (result == JOptionPane.YES_OPTION) {
        // delete building type and all buildings of that type
        ArrayList<Building> toBeDeleted = new ArrayList<>();
        for (Building building : datacenter.getBuildings()) {
          if (building.getType().equals(buildingType)) {
            toBeDeleted.add(building);
          }
        }
        toBeDeleted.stream().forEach(datacenter::remove);
        datacenter.remove(buildingType);

        try {
          BuildingTypeEditFrame.this.setClosed(true);
        } catch (PropertyVetoException e1) {
          log.warn("Cannot close frame.", e1);
        }
      }
    });
  }

  private static String intToText(int value) {
    return IntFormatter.intToText(value);
  }

  private static int textToInt(String text) {
    return IntFormatter.textToInt(text);
  }

  public enum EditType {
    EDIT, ADD
  }
}
