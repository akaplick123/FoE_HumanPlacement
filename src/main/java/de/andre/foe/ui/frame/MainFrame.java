package de.andre.foe.ui.frame;

import de.andre.foe.ui.action.CreateInternalFrameAction;
import de.andre.foe.ui.action.LoadFileAction;
import de.andre.foe.ui.action.QuitAction;
import de.andre.foe.ui.action.SaveAsFileAction;
import de.andre.foe.ui.action.SaveFileAction;
import de.andre.foe.ui.component.StatusBar;
import de.andre.foe.ui.data.Datacenter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.annotation.PostConstruct;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainFrame extends JFrame {

  private static final long serialVersionUID = 1L;
  private JDesktopPane desktop;
  private StatusBar statusBar;

  @Autowired
  private Datacenter datacenter;

  @PostConstruct
  public void init() {
    setTitle("FoE Building Placement UI");
    datacenter.addFilenameChangedListener(filename -> {
      setTitle("FoE Building Placement UI - " + filename.getName());
    });

    // Make the big window be indented 50 pixels from each edge
    // of the screen.
    final int insetPercent = 5;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setBounds(insetPercent * screenSize.width / 100, //
        insetPercent * screenSize.height / 100, //
        (100 - 2 * insetPercent) * screenSize.width / 100, //
        (100 - 3 * insetPercent) * screenSize.height / 100);

    // Set up the GUI.
    statusBar = new StatusBar();
    desktop = new JDesktopPane(); // a specialized layered pane
    JPanel pMain = new JPanel(new BorderLayout());
    pMain.add(desktop, BorderLayout.CENTER);
    pMain.add(statusBar.getStatusBarComponent(), BorderLayout.SOUTH);
    setContentPane(pMain);
    setJMenuBar(createMenuBar());

    // Make dragging a little faster but perhaps uglier.
    desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

    addBasicFrames();
  }

  private void addBasicFrames() {
    new CreateInternalFrameAction(desktop, () -> new BuildingsPlacementFrame(datacenter, statusBar))
        .actionPerformed(null);
  }

  private JMenuBar createMenuBar() {
    JMenuBar menuBar = new JMenuBar();

    createFileMenu(menuBar);

    createWindowMenu(menuBar);

    return menuBar;
  }

  private void createFileMenu(JMenuBar menuBar) {
    JMenu menu = new JMenu("File");
    menu.setMnemonic(KeyEvent.VK_F);
    menuBar.add(menu);

    JMenuItem menuItem = new JMenuItem("Open File ...");
    menuItem.setMnemonic(KeyEvent.VK_O);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
    menuItem.addActionListener(new LoadFileAction(desktop, statusBar, datacenter));
    menu.add(menuItem);

    menuItem = new JMenuItem("Save File ...");
    menuItem.setMnemonic(KeyEvent.VK_S);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
    menuItem.addActionListener(new SaveFileAction(desktop, statusBar, datacenter));
    menu.add(menuItem);

    menuItem = new JMenuItem("Save as File ...");
    menuItem.addActionListener(new SaveAsFileAction(desktop, statusBar, datacenter));
    menu.add(menuItem);

    menu.add(new JSeparator());

    menuItem = new JMenuItem("Quit Application");
    menuItem.setMnemonic(KeyEvent.VK_Q);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
    menuItem.addActionListener(new QuitAction());
    menu.add(menuItem);

    menuBar.add(menu);
  }


  private void createWindowMenu(JMenuBar menuBar) {
    JMenu menu = new JMenu("Windows");
    menu.setMnemonic(KeyEvent.VK_W);
    menuBar.add(menu);

    JMenuItem menuItem = new JMenuItem("New building type ...");
    menuItem.setMnemonic(KeyEvent.VK_N);
    menuItem.addActionListener(new CreateInternalFrameAction(desktop,
        () -> new BuildingTypeEditFrame(datacenter, BuildingTypeEditFrame.EditType.ADD, null)));
    menu.add(menuItem);

    menuItem = new JMenuItem("Show building type ...");
    menuItem.setMnemonic(KeyEvent.VK_B);
    menuItem.addActionListener(
        new CreateInternalFrameAction(desktop, () -> new BuildingTypesFrame(desktop, datacenter)));
    menu.add(menuItem);

    menuItem = new JMenuItem("Show stats ...");
    menuItem.setMnemonic(KeyEvent.VK_S);
    menuItem.addActionListener(
        new CreateInternalFrameAction(desktop, () -> new BuildingsStatsFrame(datacenter)));
    menu.add(menuItem);

    menuBar.add(menu);
  }


  /**
   * Create the GUI and show it. For thread safety, this method should be invoked from the
   * event-dispatching thread.
   */
  public void createAndShowGUI() {
    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    SwingUtilities.invokeLater(() -> {
      // Make sure we have nice window decorations.
      JFrame.setDefaultLookAndFeelDecorated(true);

      // Create and set up the window.
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // Display the window.
      setAutoRequestFocus(true);
      setVisible(true);
    });
  }
}
