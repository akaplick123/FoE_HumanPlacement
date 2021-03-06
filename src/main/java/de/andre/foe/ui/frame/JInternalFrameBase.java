package de.andre.foe.ui.frame;

import java.awt.Rectangle;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public abstract class JInternalFrameBase extends JInternalFrame {

  private static final long serialVersionUID = 1L;

  @FunctionalInterface
  public interface InternalFrameClosedListener {

    void internalFrameClosed(InternalFrameEvent e);
  }

  private static final int xOffset = 30, yOffset = 30;
  private static final int xMax = 20, yMax = 10;
  private static int openFrameCount = 0;

  public JInternalFrameBase() {
    super("Document #" + (++openFrameCount), //
        true, // resizable
        true, // closable
        true, // maximizable
        true);// iconifiable

    // ...Then set the window size or call pack...
    setSize(500, 300);

    // Set the window's location.
    setLocation(xOffset * (openFrameCount % xMax), yOffset * (openFrameCount % yMax));
  }

  public void center(JInternalFrame parent) {
    Rectangle pBounds = parent.getBounds();
    int x = pBounds.x + pBounds.width / 2 - this.getWidth() / 2;
    int y = pBounds.y + pBounds.height / 2 - this.getHeight() / 2;
    setLocation(x, y);
  }

  public void show(JDesktopPane desktop) {
    this.setVisible(true);
    desktop.add(this);
    try {
      this.setSelected(true);
    } catch (java.beans.PropertyVetoException ex) {
    }
  }

  /**
   * adds another internalFrameClosed listener
   *
   * @param listener the action to be executed on internalFrameClosed events
   */
  public void addInternalFrameClosedListener(InternalFrameClosedListener listener) {
    this.addInternalFrameListener(new InternalFrameAdapter() {
      @Override
      public void internalFrameClosed(InternalFrameEvent e) {
        listener.internalFrameClosed(e);
      }
    });
  }
}
