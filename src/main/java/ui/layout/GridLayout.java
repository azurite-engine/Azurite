package ui.layout;

import org.joml.Vector2i;
import ui.Component;
import ui.Container;

/**
 * @author Juyas
 * @version 11.11.2021
 * @since 11.11.2021
 */
public class GridLayout implements ContainerLayout {

    private FillingOrder fillingOrder = FillingOrder.NATURAL_ORDER;
    private int rows, columns;

    public GridLayout(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public void setFillingOrder(FillingOrder fillingOrder) {
        this.fillingOrder = fillingOrder;
    }

    public FillingOrder getFillingOrder() {
        return fillingOrder;
    }

    @Override
    public void updateComponents(Container container) {
        //this method isnt even near efficiency yet - but lets just leave it like this for now
        Component[][] grid = new Component[rows][columns];
        int curr = 0;
        for (int i = 0; i < rows * columns; i++) {
            Component component;
            int row, col;
            do {
                //select component
                component = container.getComponents().get(curr++);
                //if there is info specified in the component, use that
                if (component.getLayoutInfo() != null) {
                    //info has to be vector2i
                    if (component.getLayoutInfo() instanceof Vector2i) {
                        Vector2i vector2i = (Vector2i) component.getLayoutInfo();
                        //if info meets gridlayout borders, it can be used
                        if (vector2i.x < rows && vector2i.y < columns) {
                            //if in the targeted cell is already a component,
                            //exchange that and find a new slot for the other one
                            if (grid[vector2i.x][vector2i.y] == null) {
                                Component tmp = grid[vector2i.x][vector2i.y];
                                grid[vector2i.x][vector2i.y] = component;
                                component = tmp;
                                break;
                            } else grid[vector2i.x][vector2i.y] = component;
                        } else break;
                    }
                }
            }
            while (component.getLayoutInfo() != null || curr >= container.getComponents().size());
            //if there is no layout info, calculate the next cell based on the fillingOrder and assign component to that
            row = fillingOrder.getRow(i, columns, rows);
            col = fillingOrder.getColumn(i, columns, rows);
            if (grid[row][col] != null) continue;
            grid[row][col] = component;
        }

        //calculate the cell size based on rows and columns and the size of the parent container
        float cw = container.getWidth() / columns;
        float ch = container.getHeight() / rows;

        //update all components according to the grid
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Component comp = grid[i][j];
                comp.getFrame().setX(cw * j);
                comp.getFrame().setY(ch * i);
                comp.getFrame().setWidth(cw);
                comp.getFrame().setHeight(ch);
            }
        }

    }

    public enum FillingOrder {
        //left to right, top to bottom
        NATURAL_ORDER {
            @Override
            public int getColumn(int i, int columns, int rows) {
                return i % columns;
            }

            @Override
            public int getRow(int i, int columns, int rows) {
                return i / columns;
            }
        },
        //top to bottom, left to right
        ROWS_FIRST {
            @Override
            public int getColumn(int i, int columns, int rows) {
                return i / rows;
            }

            @Override
            public int getRow(int i, int columns, int rows) {
                return i % rows;
            }
        };

        public int getRow(int i, int columns, int rows) {
            return -1;
        }

        public int getColumn(int i, int columns, int rows) {
            return -1;
        }

    }

}