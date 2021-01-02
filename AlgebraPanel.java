import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Shows information about the process of the algorithm
 */
public class AlgebraPanel extends JPanel{
    public AlgebraPanel()
    {
        setBackground(new Color(250,210,250));
    }

    /**
     * Paints the info about the next clip on the right side of the screen
     * @param g Graphics object used by JPanel
     */
    public void paint(Graphics g)
    {
        super.paintComponent(g);
        if (Main.phase == Main.PhaseType.FINAL)
        {
            Font currentFont = g.getFont();
            Font newFont = currentFont.deriveFont(currentFont.getSize() * 1.4F);
            g.setFont(newFont);
            g.drawString("Total Triangulations: " + Main.gpanel.polygon.triangulations(),20,40);
            Main.gpanel.polygon.paintTable(g);
            g.setFont(currentFont);
        }
    }
}
