package de.andre.foe.ui.action;

import de.andre.foe.ui.component.StatusBar;
import de.andre.foe.ui.data.Datacenter;
import de.andre.foe.ui.data.PersistenceHelper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDesktopPane;

public class SaveFileAction implements ActionListener {

  private final JDesktopPane desktop;
  private final StatusBar statusBar;
  private final Datacenter datacenter;

  public SaveFileAction(JDesktopPane desktop, StatusBar statusBar, Datacenter datacenter) {
    this.desktop = desktop;
    this.statusBar = statusBar;
    this.datacenter = datacenter;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (datacenter.getFile() == null) {
      new SaveAsFileAction(desktop, statusBar, datacenter).actionPerformed(e);
    } else {
      new PersistenceHelper().storeToFile(datacenter.getFile(), datacenter);
      statusBar.postStatus("gamefield saved");
    }
  }
}
