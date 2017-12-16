package de.andre.foe.ui.frame;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import de.andre.foe.ui.component.SpringUtilities;
import de.andre.foe.ui.data.Building;
import de.andre.foe.ui.data.BuildingType;
import de.andre.foe.ui.data.Datacenter;
import de.andre.foe.ui.data.Datacenter.BuildingAddedListener;
import de.andre.foe.ui.data.Datacenter.BuildingRemovedListener;

public class BuildingsStatsFrame extends JInternalFrameBase {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private Datacenter datacenter;

  private JTextField tfPeople;
  private JTextField tfMoney;
  private JTextField tfCulture;

  public BuildingsStatsFrame(Datacenter datacenter) {
    super();
    setTitle("Stats");
    this.datacenter = datacenter;
    init();
    pack();
  }

  private void init() {
    setLayout(new BorderLayout());
    JPanel pFields = new JPanel(new SpringLayout());
    JLabel lPeople = new JLabel("People:");
    tfPeople = createNumberTextfield();
    JLabel lCulture = new JLabel("Culture:");
    tfCulture = createNumberTextfield();

    JLabel lMoney = new JLabel("Money income per Day:");
    tfMoney = createNumberTextfield();

    pFields.add(lPeople);
    pFields.add(tfPeople);
    pFields.add(lCulture);
    pFields.add(tfCulture);
    pFields.add(lMoney);
    pFields.add(tfMoney);

    updateStats();

    // Lay out the panel.
    SpringUtilities.makeCompactGrid(pFields, // parent
        3, 2, // rows, columns
        3, 3, // initX, initY
        3, 3); // xPad, yPad

    add(pFields, BorderLayout.CENTER);

    BuildingAddedListener buildingAddedListener = b -> updateStats();
    BuildingRemovedListener buildingRemovedListener = b -> updateStats();
    datacenter.addBuildingAddedListener(buildingAddedListener);
    datacenter.addBuildingRemoveListener(buildingRemovedListener);
    addInternalFrameClosedListener(e -> {
      datacenter.removeBuildingAddedListener(buildingAddedListener);
      datacenter.removeBuildingRemovedListenerListener(buildingRemovedListener);
    });
  }

  private void updateStats() {
    // gather stats
    int sumPeople = 0;
    int sumCulture = 0;
    int sumMoney = 0;
    for (Building b : datacenter.getBuildings()) {
      BuildingType bt = b.getType();
      if (bt.getMoney() > 0) {
        sumMoney += bt.getMoney();
      }
      if (bt.getPeople() > 0) {
        sumPeople += bt.getPeople();
      }
      if (bt.getCulture() > 0) {
        sumCulture += bt.getCulture();
      }
    }

    // update UI
    tfPeople.setText(intToText(sumPeople));
    tfCulture.setText(intToText(sumCulture));
    tfMoney.setText(intToText(sumMoney));
  }

  private JTextField createNumberTextfield() {
    JTextField tfMoney = new JTextField(15);
    tfMoney.setHorizontalAlignment(JTextField.TRAILING);
    tfMoney.setEditable(false);
    return tfMoney;
  }

  private static String intToText(int value) {
    if (value % 100_000 == 0 && value >= 1_000_000) {
      // e.g. 1500000 -> 1.5M
      return Integer.toString(value / 1_000_000) + "M";
    }
    if (value % 100 == 0 && value >= 1_000) {
      // e.g. 1500 -> 1.5K
      return Integer.toString(value / 1_000) + "K";
    }

    return Integer.toString(value);
  }
}
