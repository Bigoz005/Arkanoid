import java.awt.*;
import javax.swing.*;


public class Main {

	
	public static void main(String[] args) {
		
	Gameplay game= new Gameplay();
	JFrame obj = new JFrame();
	
	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	obj.setLocation(dim.width/2-obj.getSize().width/2, dim.height/2-obj.getSize().height/2);
	obj.setBounds(10,10,694,590);
	obj.setResizable(false);
	obj.setLocationRelativeTo(null);
	obj.setVisible(true);
	obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	obj.add(game);
	
	
	game.setSize(obj.getSize());
	
	}
	
}
