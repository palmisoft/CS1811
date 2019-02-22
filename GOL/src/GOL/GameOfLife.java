package GOL;

//All these imports are needed for the different parts of the interface
//Read up in the Javadocs on each of these classes...
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Hashtable;
import java.util.concurrent.ThreadLocalRandom;

//Our class needs to inherit functionality from 'JPanel' and 'JActionListener'
public class GameOfLife extends JPanel implements ActionListener, ChangeListener 
{
	private static final long serialVersionUID = 1L;

	//Buttons: Start/Stop, Next.
	private JButton startbutton, nextbutton;

	//Slider for the size of the grid
	private JSlider sizeSlider;

	//Label
	private JLabel figuresLabel, speedlabel;

	private JLabel iteration;
	//To repaint the figures certain intervals
	private Timer timer;

	private int count = 0;
	
	//Delay of those intervals 
	private int miliseconds = 500;
	//INITIAL Size of the array, How many squares 
	private int size = 100;
	//INITIAL size of the square
	private int title = 3; 
	//INTIAL Size of the square plus separation, int his case 1 
	private int separation = 4;

	private int n =0;

	//Dropdown menu
	final JComboBox<String> Menu;
	//Speed spinner
	final JSpinner spinner;
	//Boundaries of the spinner
	final SpinnerModel speeds;

	//Principal array. Everything is calculated in this array and then pass onto temparray
	private int[][] array = new int[size][size];
	//Temporary array
	private int[][] temparray = new int[size][size]; ;

	//Size of the square plus separation
	final int SizeMIN = 4;
	final int SizeMAX = 32;
	final int SizeMED = 16; 

	public GameOfLife() 
	{
		//Layout of the window
		setLayout(null);
		
		
		/////////////////////////////Figures MENU//////////////////////////////////////////
		String[] choices = { "None", "random","Glider", "Block", "Oscillators",
				"Pulsar", "All alive" };//Array with all the options of the dropdown menu

		Menu = new JComboBox<String>(choices);
		Menu.setBounds(330, 15, 100, 25); //(x, y, width, height ) Position and Size
		Menu.setSelectedIndex(0);//Initial value
		Menu.setActionCommand("Menu"); // Create the event Menu when clicked

		Menu.addActionListener(this);
		add(Menu);// Add the component to the window


		figuresLabel = new JLabel("Fixed Figures");
		figuresLabel.setBounds(250, 15, 100, 25);//(x, y, width, height ) Position and Size
		add(figuresLabel);// Add the component to the window


		/////////////////////////////SIZE SLIDER//////////////////////////////////////////
		sizeSlider = new JSlider(JSlider.HORIZONTAL,// Horizontal Slider
				SizeMIN, SizeMAX, SizeMED); //minimum, medium and max values
		sizeSlider.setBounds(15, 500, 200, 50);//(x, y, width, height ) Position and Size
		sizeSlider.setPaintTicks(true);//In this case does not print the ticks but it gives some space between labels and the thumb

		Hashtable<Integer, JLabel> sizeLabels = new Hashtable<Integer, JLabel>();//Creates the labels 
		sizeLabels.put( new Integer( SizeMIN ), new JLabel("Small") );//add the label to the right value 
		sizeLabels.put( new Integer( SizeMAX ), new JLabel("Big") );

		sizeSlider.setLabelTable( sizeLabels );//add the labels to the right slider
		sizeSlider.setPaintLabels(true);//shows the labels
		sizeSlider.setValue(4);//Initial value 

		sizeSlider.addChangeListener(this);
		add(sizeSlider);// Add the component to the window

		/////////////////////////////SPEED SPINNER//////////////////////////////////////////
		speeds = new SpinnerNumberModel(500, 100, 1000, 100);//(initial, minimum, maximum, step)
		spinner = new JSpinner(speeds);//Creates the object spinner 
		spinner.setBounds(330,500, 100, 25);//(x, y, width, height ) Position and Size

		spinner.addChangeListener(this);
		add(spinner);// Add the component to the window

		speedlabel = new JLabel("Speed");
		speedlabel.setBounds(290,500, 100, 25);//(x, y, width, height ) Position and Size
		add(speedlabel);// Add the component to the window
		
		/////////////////////////////START BUTTON//////////////////////////////////////////
		startbutton = new JButton("Play / Pause");//Play and pause Button
		startbutton.setBounds(15, 15, 120, 25);//(x, y, width, height ) Position and Size
		startbutton.setVerticalTextPosition(AbstractButton.CENTER);	//Centre the text vertically
		startbutton.setHorizontalTextPosition(AbstractButton.CENTER);//Centre the text horizontally
		startbutton.setActionCommand("Start");
		startbutton.setEnabled(false);//If false the button will appear disabled 

		startbutton.addActionListener(this);
		add(startbutton);// Add the component to the window

		/////////////////////////////NEXT BUTTON//////////////////////////////////////////
		nextbutton = new JButton("Next");
		nextbutton.setBounds(140, 15, 75, 25);//(x, y, width, height ) Position and Size
		nextbutton.setVerticalTextPosition(AbstractButton.CENTER);//Centre the text vertically
		nextbutton.setHorizontalTextPosition(AbstractButton.CENTER);//Centre the text horizontally
		nextbutton.setActionCommand("Next");
		nextbutton.setEnabled(false);//If false the button will appear disabled 

		nextbutton.addActionListener(this);
		add(nextbutton);// Add the component to the window

		/////////////////////////////TIMER//////////////////////////////////////////
		timer = new Timer(miliseconds, this);//(delay, actionListener) 
		timer.setActionCommand("Timer"); 
	
/////////////////////////////ITERATION//////////////////////////////////////////
		iteration = new JLabel("Iterations");
		iteration.setBounds(15,470,200,25);//(x, y, width, height ) Position and Size
		add(iteration);
	}
	//Everytime a slider or spinner is clicked this is run
	public void stateChanged(ChangeEvent e){

		//if the spinner is clicked
		if (e.getSource().equals(spinner)){

			//Print spinner
			System.out.println("spinner");

			//Converts the input into an object 
			JSpinner source = (JSpinner)e.getSource();

			//Extracts the value of that input 
			miliseconds = ((int)source.getValue());

		}
		//If the slider is moved
		if(e.getSource().equals(sizeSlider)){

			System.out.println("slider");

			//Converts the input into an object 
			JSlider source = (JSlider)e.getSource();

			//Position of the thumb 
			int position = ((int)source.getValue());

			//the absolute value of 400 pixels divided by the position 
			int s = Math.abs(400/position); 
			//Print the position
			System.out.println(position);

			//change the values of the original painting properties
			size = s;
			title = position - 1;
			separation = position;

			//Call the method paint 
			repaint();
		}
	}
	//Every time a button is clicked or a timer event occurs this method is run
	public void actionPerformed(ActionEvent e) 
	{
		//if start is pressed
		if (e.getActionCommand().equals("Start")) 
		{
			//print start pressed
			System.out.println("Start pressed");

			//if the timers is executing 
			if (timer.isRunning() == true){
				//timer stop
				timer.stop();
			}
			//if not 
			else 
			{
				//time start
				timer.start();	
			}

		}
		//If timer is executed
		if (e.getActionCommand().equals("Timer"))
		{
			//print timer 
			System.out.println("Timer");
			//set the time between iterations 
			timer.setDelay(miliseconds);
			//calculates the next generation 
			countNextGen();
			//executes the paint method
			
			iteration.setText(String.valueOf(count));
			repaint();
		}
		//If next is pressed
		if (e.getActionCommand().equals("Next")) 
		{
			//print next
			System.out.println("Next");
			//calculates the next generation
			countNextGen();
			//executes the paint method
			repaint();
		}
		//If menu is pressed
		if (e.getActionCommand().equals("Menu")) 
		{
			//print menu
			System.out.println("Menu Pressed");
			//enables start button
			startbutton.setEnabled(true);
			//Converts the input into an object 
			JComboBox<?> Menu = (JComboBox<?>)e.getSource();
			//gets the position of the string
			int selection = (int)Menu.getSelectedIndex();
			//prints the position of the string 
			System.out.println(selection);
			//calls this method using the position of the string
			menuForms(selection);
		}
	}
	@Override
	public void paint(Graphics g) 
	{
		super.paint(g);

		//sets the position of the first square
		int x = 15;
		int y = 60;


		for (int i = 0; i < size; i ++){
			for (int j = 0; j < size; j ++){

				//If the title is dead
				if (temparray[i][j] == 0) {

					g.setColor(Color.lightGray);
				}
				//if the title is alive paint red
				if (temparray[i][j] == 1) {
					g.setColor(Color.RED);
				}

				//Create the rectangle (x, y, width, height)
				g.fillRect(x, y, title, title);

				//y = y + separation 
				y+=separation;
			}
			y = 60;
			x+=separation;//x = x + separation 
		}
	}
	public void copyArray(){
		//copy the principal array into the temporary array
		for(int x = 0; x < size; x++)
		{
			for(int y = 0; y< size; y++){

				temparray[x][y] = array[x][y];
			}
		}
	}
	public void FillArrayRandoms(){
		//fills the temporary array with randoms
		for (int x=0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				//numbers between 0 and 1 
				array[x][y] = ThreadLocalRandom.current().nextInt(0, 2);
			}
		}
		copyArray();
	}
	public int Ypoint(int y){
		//bigger or equal to the size of the array
		if(y >= size){

			y = 0;
		}
		if(y < 0){

			y = size -1;
		}

		return y;
	}
	public int Xpoint(int x){
		//bigger or equal to the size of the array
		if(x >= size){

			x = 0;
		}
		if(x < 0){

			x = size -1;
		}


		return x;

	}
	//South 
	public int south(int x, int y)
	{
		y = y+1;
		y = Ypoint(y); 

		if (temparray[x][y]>0){
			return 1;
		}
		else {
			return 0;
		}	

	}
	//South East
	public int southEast(int x, int y)
	{
		x=x+1;
		y=y+1;
		x = Xpoint(x);
		y = Ypoint(y);
		if (temparray[x][y]>0){
			return 1;
		}
		else {
			return 0;
		}	
	}
	//South West
	public int southWest(int x, int y)
	{
		x = x-1;
		y = y+1;
		x = Xpoint(x);
		y = Ypoint(y);
		if(temparray[x][y]>0){
			return 1;
		}
		else 
		{
			return 0;
		}

	}
	//West
	public int west(int x, int y){
		x=x-1;
		x = Xpoint(x);
		if(temparray[x][y]>0){
			return 1;
		}
		else {
			return 0;
		}	
	}
	//East
	public int east(int x, int y){
		x=x+1;
		x = Xpoint(x);
		if(temparray[x][y]>0){
			return 1;
		}
		else {
			return 0;
		}	
	}
	//North east
	public int northEast(int x, int y){
		x=x+1;
		y=y-1;
		x = Xpoint(x);
		y = Ypoint(y);
		if(temparray[x][y]>0){
			return 1;
		}
		else {
			return 0;
		}
	}
	//North
	public int North(int x, int y){
		y=y-1;
		y = Ypoint(y);
		if(temparray[x][y]>0){
			return 1;
		}
		else {
			return 0;
		}	
	}
	//North West
	public int northWest(int x, int y){
		y=y-1;
		x=x-1;
		x = Xpoint(x);
		y = Ypoint(y);
		if(temparray[x][y]>0){
			return 1;
		}
		else {
			return 0;
		}	
	}
	public int total(int x, int y)
	{
		//Count the number of neighbours
		int total = south(x,y) + southEast(x,y) + southWest(x,y) + west(x,y) + east(x,y)+ northEast(x,y) + North(x,y) + northWest(x,y);


		return total;

	}
	//Generates the next generation
	public void countNextGen() {

		int y = 0;

		// decides what will happen to cell
		for (int x = 0; x < size; x++)
		{
			y = 0;
			for (y = 0; y < size; y++)
			{

				int n = total(x, y);

				if (temparray[x][y] == 1 )
				{
					// under or over population, cell dies
					if ((n < 2) || (n > 3)) {
						array[x][y] = 0;
					}
					// cell lives on to next generation
					if ((n == 2) || (n == 3) ) {
						array[x][y] = 1;

					}

				}
				else 
				{
					if ((n == 3) && (temparray[x][y] == 0)) {

						array[x][y] = 1;

					}	
				}

			}
		}
		count++;
		
		System.out.println(count);
		copyArray();

	}
	//Menu containing all the fixed forms
	public void menuForms(int selection){

		//Position 0
		if (selection == 0){

			startbutton.setEnabled(false);
			nextbutton.setEnabled(false);

			FillArrayZeros();

			repaint();

		}
		//Position 1
		else if (selection == 1){


			System.out.println("Random ");

			startbutton.setEnabled(true);
			nextbutton.setEnabled(true);
			FillArrayRandoms();

			repaint();

		}
		//Position 2
		else if (selection == 2){

			System.out.println("Glider");

			FillArrayZeros(); 

			startbutton.setEnabled(true);
			nextbutton.setEnabled(true);

			temparray[14][15] = 1;
			temparray[15][15] = 1;
			temparray[16][15] = 1;
			temparray[16][16] = 1;
			temparray[15][17] = 1;

			repaint();
		}
		//Position 3
		else if(selection == 3){

			FillArrayZeros(); 

			startbutton.setEnabled(true);
			nextbutton.setEnabled(true);

			temparray[1][1]=1;
			temparray[2][1]=1;
			temparray[1][2]=1;
			temparray[2][2]=1;

			repaint();
		}
		//Position 4
		else if(selection == 4){

			FillArrayZeros(); 

			startbutton.setEnabled(true);
			nextbutton.setEnabled(true);

			temparray[1][3]=1;
			temparray[2][3]=1;
			temparray[3][3]=1;
			
			repaint();

		}
		//Position 5
		else if(selection == 5){
			FillArrayZeros(); 

			startbutton.setEnabled(true);
			nextbutton.setEnabled(true);

			temparray[4][8] = 1;
			temparray[8][8] = 1;
			temparray[4][6]= 1;
			temparray[5][6]= 1;
			temparray[6][6]= 1;
			temparray[7][6]= 1;
			temparray[8][6]= 1;
			temparray[4][10]= 1;
			temparray[5][10]= 1;
			temparray[6][10]= 1;
			temparray[7][10]= 1;
			temparray[8][10]= 1;
			
			repaint();
			
		}
		//Position 6
		else if(selection == 6){

			FillArrayOnes();
			repaint();
		}
		


	}
	//Fill array with zeros
	public void FillArrayZeros(){

		for (int x=0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				array[x][y] = 0;
			}
		}
		//copy from the principal array to the temporary array
		copyArray();
	}
	public void FillArrayOnes(){

		for (int x=0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				array[x][y] = 1;
			}
		}
		//copy from the principal array to the temporary array
		copyArray();
	}

}