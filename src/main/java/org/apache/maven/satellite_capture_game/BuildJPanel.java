package org.apache.maven.satellite_capture_game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class BuildJPanel
{
    private final JPanel customJPanel;

    public BuildJPanel(int[] fl, int[] dim, int[] col, boolean bev, boolean op)
    {
        // Build the basic JPanel object.
        customJPanel = new JPanel();
        // Set it to be opaque.
        customJPanel.setOpaque(op);
        // Set the dimensions (in monitor values).
        customJPanel.setMinimumSize(new Dimension(dim[0],dim[1]));
        customJPanel.setPreferredSize(new Dimension(dim[0],dim[1]));
        // Set the background color of the JPanel.
        customJPanel.setBackground(new Color(col[0],col[1],col[2]));

        if(bev)
        {
            // Set a raised beveled appearance using the Border layout.
            customJPanel.setLayout(new BorderLayout());
            customJPanel.setBorder(BorderFactory.
                                        createBevelBorder(BevelBorder.RAISED));
        }
        else
        {
            // Use the Flow (non-beveled) layout.
            customJPanel.setLayout(new FlowLayout(fl[0],fl[1],fl[2]));
        }
    }

    public JPanel getJPanel()
    {
        return customJPanel;
    }
}