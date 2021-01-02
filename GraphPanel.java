import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Shows the left side of the screen with the polygon
 */
public class GraphPanel extends JPanel implements MouseListener, KeyListener {
    SimplePolygon polygon;
    ArrayList<Vertex> vertices;
    static int pointerX,pointerY;
    public GraphPanel()
    {
        setBackground(Color.white);
        addMouseListener(this);
        setFocusable(true);
        addKeyListener(this);
        polygon = new SimplePolygon(new Vertex[]{});
        vertices = new ArrayList<>();
        repaint();
        pointerX = pointerY = -10;
    }
    /**
     * Paints the left side of the screen
     * @param g Graphics object used by JPanel
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        polygon.paint(g);
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
    /**
     * Determines what to do when mouse is pressed
     * @param e KeyEvent containing info on which mouse button was pressed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (Main.phase == Main.PhaseType.DRAW) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                vertices.add(new Vertex(e.getX(), e.getY()));
                polygon = new SimplePolygon(vertices);
            }
            if (e.getButton() == MouseEvent.BUTTON3) {
                Vertex closest = vertices.get(0);
                double dist = Math.hypot(closest.getX() - e.getX(), closest.getY() - e.getY());
                for (int i = 1; i < vertices.size(); i++) {
                    Vertex vertex = vertices.get(i);
                    if (Math.hypot(vertex.getX() - e.getX(), vertex.getY() - e.getY()) < dist) {
                        dist = Math.hypot(vertex.getX() - e.getX(), vertex.getY() - e.getY());
                        closest = vertex;
                    }
                }
                vertices.remove(closest);
                polygon = new SimplePolygon(vertices);
            }
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}