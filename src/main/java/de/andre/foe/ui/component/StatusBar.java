package de.andre.foe.ui.component;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class StatusBar {

  private static final DateTimeFormatter LOCAL_TIME = new DateTimeFormatterBuilder()
      .appendValue(HOUR_OF_DAY, 2)
      .appendLiteral(':')
      .appendValue(MINUTE_OF_HOUR, 2)
      .optionalStart()
      .appendLiteral(':')
      .appendValue(SECOND_OF_MINUTE, 2)
      .toFormatter();

  private final JLabel statusBar = new JLabel("started", JLabel.LEADING);

  public JComponent getStatusBarComponent() {
    return statusBar;
  }

  public void postStatus(String msg) {
    statusBar.setText(LOCAL_TIME.format(LocalTime.now()) + ": " + msg);
  }
}
