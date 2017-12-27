package de.andre.foe.ui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.TreeMap;
import javax.swing.JComponent;
import javax.swing.event.EventListenerList;
import lombok.Getter;
import lombok.Setter;

public class BuildingComponent extends JComponent {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static interface ObjectSelectedListener extends EventListener {
    public void objectSelectedEvent(Building object);
  }

  public static interface ObjectMovedListener extends EventListener {
    public void objectMovedEvent(Building object, Point oldLocation, Point newLocation);
  }

  /** basic grid configuration */
  private Grid grid = new Grid();

  /** all buildings. key is the layer. higher numbered layers gets painted later */
  private final TreeMap<Integer, List<Building>> objects = new TreeMap<>();

  // drag and drop support
  private Building draggedObject = null;
  private Point startDragLocation = null;
  private Point redSquareStartingLocation = null;

  /** all event listeners */
  private EventListenerList eventListeners = new EventListenerList();

  public BuildingComponent() {
    setPreferredSize(new Dimension(1000, 1500));

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        // deselct all objects
        for (List<Building> layer : objects.values()) {
          for (Building object : layer) {
            if (object.isSelected()) {
              object.setSelected(false);
              fireObjectSelectedEvent(object);
            }
          }
        }

        // select object if mouse is over one
        Building selectedObject = objectAt(e.getPoint());
        if (selectedObject != null) {
          selectedObject.setSelected(true);
          fireObjectSelectedEvent(selectedObject);
        }

        repaint();
      }

      @Override
      public void mousePressed(MouseEvent e) {
        Building selectedObject = objectAt(e.getPoint());
        if (selectedObject != null) {
          draggedObject = selectedObject;
          redSquareStartingLocation = grid.getLogicalPoint(draggedObject);
          startDragLocation = e.getPoint();
          draggedObject.setDragging(true);
        }
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        if (startDragLocation != null) {
          grid.snap(draggedObject);
          draggedObject.setDragging(false);
          moveObjectTo(draggedObject, draggedObject.getXPos(), draggedObject.getYPos());
          fireObjectMovedEvent(draggedObject, redSquareStartingLocation,
              grid.getLogicalPoint(draggedObject));
          startDragLocation = null;
          draggedObject = null;
        }
      }
    });

    addMouseMotionListener(new MouseAdapter() {
      @Override
      public void mouseDragged(MouseEvent e) {
        if (startDragLocation != null && draggedObject != null) {
          int newX = e.getX() - startDragLocation.x + draggedObject.getXPos();
          int newY = e.getY() - startDragLocation.y + draggedObject.getYPos();
          moveObjectTo(draggedObject, newX, newY);
          startDragLocation = e.getPoint();
        }
      }
    });
  }

  /**
   * adds a new logical component to the grid
   * 
   * @param x logical x coordinate
   * @param y logical y coordinate
   * @param width number of horizontal grid cells
   * @param height number of vertical grid cells
   * @param layer an arbitrary number. Buildings at layers with higher numbers get painted later.
   * @param fillColor color for the background
   */
  public Building placeBuilding(int x, int y, int width, int height, int layer, Color fillColor) {
    Building building = new Building(this.grid);
    if (fillColor == null) {
      fillColor = Color.red;
    }
    building.setColorScheme(fillColor);

    building.setXCells(width);
    building.setYCells(height);

    this.grid.setToLogicalPoint(building, x, y);
    List<Building> objectList = this.objects.get(layer);
    if (objectList == null) {
      objectList = new ArrayList<>();
      this.objects.put(layer, objectList);
    }
    objectList.add(building);
    repaint();

    return building;
  }

  public void removeBuilding(Building building) {
    for (List<Building> layer : objects.values()) {
      layer.remove(building);
    }

    repaint();
  }

  /**
   * removes all buildings
   */
  public void clear() {
    this.objects.clear();
    repaint();
  }

  /**
   * @return the selected object or <code>null</code> if no object is selected
   */
  public Building getSelectedBuilding() {
    for (List<Building> layer : objects.values()) {
      for (Building object : layer) {
        if (object.isSelected()) {
          return object;
        }
      }
    }

    return null;
  }

  /**
   * searches for an building at the given location
   * 
   * @param p physical location
   * @return an building or <code>null</code> if no building at given coordinates
   */
  private Building objectAt(Point p) {
    for (List<Building> layer : this.objects.descendingMap().values()) {
      Building result = null;
      for (Building object : layer) {
        if (object.isLocatedAt(p.x, p.y)) {
          result = object;
        }
      }
      if (result != null) {
        return result;
      }
    }

    // no object found
    return null;
  }

  /**
   * @param building the object to move
   * @param x new physical x position
   * @param y new physical y position
   */
  private void moveObjectTo(Building building, int x, int y) {
    // Current square state, stored as final variables
    // to avoid repeat invocations of the same methods.
    final int CURR_X = building.getXPos();
    final int CURR_Y = building.getYPos();

    if ((CURR_X != x) || (CURR_Y != y)) {
      // Update coordinates.
      building.setXPos(x);
      building.setYPos(y);
    }

    // Repaint the square at the new location.
    repaint();
  }

  public void addObjectMovedListener(ObjectMovedListener listener) {
    this.eventListeners.add(ObjectMovedListener.class, listener);
  }

  public void addObjectSelectedListener(ObjectSelectedListener listener) {
    this.eventListeners.add(ObjectSelectedListener.class, listener);
  }

  protected void fireObjectMovedEvent(Building object, Point oldLoaction, Point newLocation) {
    for (ObjectMovedListener listener : eventListeners.getListeners(ObjectMovedListener.class)) {
      listener.objectMovedEvent(object, oldLoaction, newLocation);
    }
  }

  protected void fireObjectSelectedEvent(Building object) {
    for (ObjectSelectedListener listener : eventListeners
        .getListeners(ObjectSelectedListener.class)) {
      listener.objectSelectedEvent(object);
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    for (List<Building> layer : this.objects.values()) {
      for (Building object : layer) {
        object.paintSquare(g2);
      }
    }
  }

  private static class Grid {
    private int xStart = 10;
    private int yStart = 10;
    private int boxWidth = 20;
    private int boxHeight = 20;

    /**
     * @return width of a single cell or box
     */
    public int getCellWidth() {
      return boxWidth;
    }

    /**
     * @return height of a single cell or box
     */
    public int getCellHeight() {
      return boxHeight;
    }

    /**
     * adopts x and y of red square so that it matches the grid constraints
     * 
     * @param object
     */
    public void snap(Building object) {
      Point boxNo = getLogicalPoint(object);

      setToLogicalPoint(object, boxNo.x, boxNo.y);
    }

    /**
     * @param object
     * @return the logical coordinates of the given object
     */
    public Point getLogicalPoint(Building object) {
      double xBoxNo = ((double) (object.getXPos() - xStart)) / boxWidth;
      double yBoxNo = ((double) (object.getYPos() - yStart)) / boxHeight;

      xBoxNo = Math.round(xBoxNo);
      yBoxNo = Math.round(yBoxNo);

      return new Point((int) xBoxNo, (int) yBoxNo);
    }

    /**
     * @param building the building to be placed
     * @param x logical x coordinate
     * @param y logical y coordinate
     */
    public void setToLogicalPoint(Building building, int x, int y) {
      int newX = this.xStart + x * getCellWidth();
      int newY = this.yStart + y * getCellHeight();
      building.setXPos(newX);
      building.setYPos(newY);
    }
  }

  @Getter
  @Setter
  public static class Building {
    private final Grid gridConfig;

    private int xPos = 50;
    private int yPos = 50;

    private int xCells = 1;
    private int yCells = 1;

    private boolean dragging = false;
    private boolean selected = false;
    private boolean showInnerGrid = false;

    private Color innerGridColor = Color.lightGray;

    private Color normalFillColor;
    private Color normalBorderColor;

    private Color selectedFillColor;
    private Color selectedBorderColor;

    private Color draggingFillColor;
    private Color draggingBorderColor;

    public Building(Grid gridConfig) {
      this.gridConfig = gridConfig;
    }

    public void setColorScheme(Color normalFillColor) {
      this.normalFillColor = normalFillColor;
      this.normalBorderColor = Color.black;

      this.selectedFillColor = this.normalFillColor.brighter().brighter();
      this.selectedBorderColor = Color.yellow;

      Color tmp = normalFillColor.darker();
      this.draggingFillColor = new Color(tmp.getRed(), tmp.getGreen(), tmp.getBlue(), 72);
      this.draggingBorderColor = Color.black;
    }

    /**
     * paints that object
     * 
     * @param g
     */
    public void paintSquare(Graphics2D g) {
      Color fillColor;
      Color borderColor;
      if (isDragging()) {
        fillColor = draggingFillColor;
        borderColor = draggingBorderColor;
      } else if (isSelected()) {
        fillColor = selectedFillColor;
        borderColor = selectedBorderColor;
      } else {
        fillColor = normalFillColor;
        borderColor = normalBorderColor;
      }

      int width = gridConfig.getCellWidth() * xCells;
      int height = gridConfig.getCellHeight() * yCells;
      g.setPaint(fillColor);
      g.fillRect(xPos, yPos, width, height);

      if (showInnerGrid) {
        g.setPaint(innerGridColor);
        int w = gridConfig.getCellWidth();
        int h = gridConfig.getCellHeight();
        for (int x = 0; x < xCells; x++) {
          for (int y = 0; y < yCells; y++) {
            g.drawRect(xPos + x * w, yPos + y * h, w, h);
          }
        }
      }

      g.setPaint(borderColor);
      g.drawRect(xPos, yPos, width, height);
    }

    /**
     * @param x
     * @param y
     * @return <code>true</code> if (x,y) is contained within object
     */
    public boolean isLocatedAt(int x, int y) {
      int width = gridConfig.getCellWidth() * xCells;
      int height = gridConfig.getCellHeight() * yCells;
      return (x >= xPos && y >= yPos && x <= xPos + width && y <= yPos + height);
    }
  }
}
