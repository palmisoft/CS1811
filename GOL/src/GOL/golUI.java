package GOL;
import javax.swing.JFrame;


public class golUI implements Runnable
{

	public void run() 
	{
		GameOfLife GOL = new GameOfLife();
		
		//Create a JFrame
		JFrame JFrame = new JFrame("Game OF Life");

		//If false the window cannot be resizable. Hence is fixed
		JFrame.setResizable(false);
		//Sets the behaviour of the button X.		
		JFrame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		
		//Sets all the content opaque 
		GOL.setOpaque(true);
		//Puts the panel into the JFrame
		JFrame.setContentPane(GOL);
		//Sets the size of the window
		JFrame.setBounds(250,250,500,600);
		//Display the frame 
		JFrame.setVisible(true);
	}
}