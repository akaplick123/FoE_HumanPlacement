package de.andre.foe.ui.action;

import de.andre.foe.ui.component.StatusBar;
import de.andre.foe.ui.data.Datacenter;
import de.andre.foe.ui.data.PersistenceHelper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;

public class SaveAsFileAction implements ActionListener {

  private final JDesktopPane desktop;
  private final StatusBar statusBar;
  private final Datacenter datacenter;

  public SaveAsFileAction(JDesktopPane desktop, StatusBar statusBar, Datacenter datacenter) {
    this.desktop = desktop;
    this.statusBar = statusBar;
    this.datacenter = datacenter;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    File currentDirectory = new File("gamefield.dat");
    if (datacenter.getFile() != null) {
      currentDirectory = datacenter.getFile();
    }
    JFileChooser chooser = new JFileChooser(currentDirectory);

    int rc = chooser.showSaveDialog(desktop);
    if (rc == JFileChooser.APPROVE_OPTION) {
      File file = chooser.getSelectedFile();
      new PersistenceHelper().storeToFile(file, datacenter);
      if (datacenter.getFile() == null) {
        datacenter.setFile(file);
      }
      statusBar.postStatus("gamefield saved as " + file.getAbsolutePath());
    }
  }
}
