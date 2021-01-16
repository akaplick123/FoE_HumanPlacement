package de.andre.foe.ui.component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class StatusBar {

  private final JLabel statusBar = new JLabel("started", JLabel.LEADING);

  public JComponent getStatusBarComponent() {
    return statusBar;
  }

  public void postStatus(String msg) {
    statusBar.setText(DateTimeFormatter.ISO_LOCAL_TIME.format(LocalTime.now()) + ": " + msg);
  }
}
