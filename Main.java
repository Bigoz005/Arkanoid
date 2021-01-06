package com.company;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

/**
 *
 * @author Michal Nawrot
 */

/**
* Main class which is controlling window
*/
public class Main {
    public static void main(String[] args) {

        Gameplay game= new Gameplay();
        JFrame obj = new JFrame();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation(dim.width/2-obj.getSize().width/2, dim.height/2-obj.getSize().height/2);
        obj.setBounds(10,10,1099,762);
        obj.setResizable(false);
        obj.setLocationRelativeTo(null);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                game.saveScore();
                e.getWindow().dispose();
            }
        });

        obj.add(game);
        game.setSize(obj.getSize());
    }

}