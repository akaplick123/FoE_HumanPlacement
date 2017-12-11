package de.andre.foe.ui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import de.andre.foe.ui.component.StatusBar;
import de.andre.foe.ui.data.Datacenter;
import de.andre.foe.ui.data.PersistenceHelper;

public class LoadFileAction implements ActionListener {

  private final JDesktopPane desktop;
  private final StatusBar statusBar;
  private final Datacenter datacenter;

  public LoadFileAction(JDesktopPane desktop, StatusBar statusBar, Datacenter datacenter) {
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

    int rc = chooser.showOpenDialog(desktop);
    if (rc == JFileChooser.APPROVE_OPTION) {
      File file = chooser.getSelectedFile();
      new PersistenceHelper().loadFromFile(file, datacenter);
      datacenter.setFile(file);
      statusBar.postStatus("gamefield loaded from " + file.getAbsolutePath());
    }
  }
}
