import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/*File Name: SorcerersCave.java
 * Date: 15 Dec 14
 * Author: Ben Sims
 * Required Files: none
 * Description: This program simulates a flash card game, that can be used to study vocabulary.
 * Input: any .txt                     
 * Output: GUI
 */

public class FlashCardGame extends JFrame{
	private static final long serialVersionUID = 1L;

	private int current, count, wrong;
	private boolean answer;
	private ArrayList<String> flashDeck = new ArrayList<String>();
	private JFrame frame = new JFrame();
	private JTextArea listDisplay = new JTextArea(20, 20);
	private JLabel jlMessage = new JLabel();
	private JTextField gword = new JTextField(15);
	private JTextField eword = new JTextField(15);
	private Random ranNum = new Random();
	
	public static void main(String[] args){
		new FlashCardGame();
	}//end main()
	
	public FlashCardGame(){
		String file = null;
		
		startScreen();
		
		//Loop spins until a decision is made
		while (true){
			frame.requestFocus();
			if (count == 1){
				makeNewDeck();
				break;
			}
			else if (count == 2){
				file = loadFile();
				try {
					flashDeck = loadFlashCards(file);
					startGame();
					break;
				} 
				catch (IOException e) {
					System.exit(0);
				}
				catch (NullPointerException e){
					System.exit(0);
				}
			}
		}
		
	}//end FlashCardGame constructor

	//Method makes a start screen with two options
	private boolean startScreen(){
		JButton newList = new JButton("New Deck");
		JButton loadList = new JButton("Load Deck");
		String message = "<html>Welcome to Flash Card Game<br/>Please select an option.</html>";
		jlMessage = new JLabel(message);
		
		newList.addActionListener(new ActionListener(){
			@Override 
            public void actionPerformed(ActionEvent e){
				count = 1;
				frame.dispose();
            }
		});
		
		loadList.addActionListener(new ActionListener(){
			@Override 
            public void actionPerformed(final ActionEvent e){
            	count = 2;
            	frame.dispose();
            }
		});
		
		frame.add(jlMessage, BorderLayout.NORTH);
		frame.add(newList, BorderLayout.WEST);
		frame.add(loadList, BorderLayout.EAST);
		
		frame.setSize(200, 100);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		return true;
	}//end startScreen()
	
	//Method uses JFileChooser to find a file, and return its path as a String
	private String loadFile(){
		//Create needed elements
		String fileName = null;
		JFileChooser chooser = new JFileChooser();
		File workingDirectory = new File(System.getProperty("user.dir"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
		
		//Apply needed elements 
		chooser.setCurrentDirectory(workingDirectory);
		chooser.setFileFilter(filter);
		
		//Find file and set "fileName" to equal its path.
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
		   fileName = chooser.getSelectedFile().getPath();
		}
		
		//Remove "chooser" and return "fileName"
		this.remove(chooser);
		return fileName;
	}//end loadFile()
	
	//Method used to process .txt file
	private ArrayList<String> loadFlashCards(String filePath)throws FileNotFoundException, 
	UnsupportedEncodingException, IOException{
		String[] inputArray;
		String tempInput;
		ArrayList<String> cardDeck = new ArrayList<String>();
		java.io.File lists = new java.io.File(filePath);
		Scanner input = new Scanner(lists);
		
		//each line is loaded and split into an array.
		while (input.hasNextLine()){
			tempInput = input.nextLine();
			if (!tempInput.startsWith("//") && !tempInput.isEmpty()){
				inputArray = tempInput.split(" : ");
				
				for (String s : inputArray){
					cardDeck.add(s);
				}
			}
		}
		input.close();
		return cardDeck;
	}//end loadFlashCards()

	//Method used to make a new word list
	private void makeNewDeck(){
		JButton addCard = new JButton("Add");
		JButton finish = new JButton("Finish");
		JButton save = new JButton("Save");
		JPanel buttonPanel = new JPanel();
		JPanel textPanel = new JPanel();
		String newDeckMessage = "<html>Enter a German word in the left box. Then its"
				+ " translation in the right.<br/>After click add (or hit enter). Click finish"
				+ " when you are done.<br/>Click save to save the list.<br/>"
				+ "ä: alt + 0228, ö: alt + 0246, ü: alt + 0252, ß: alt + 0223</html>";
		JLabel message = new JLabel(newDeckMessage);
		JScrollPane scroller = new JScrollPane(listDisplay);
		
		gword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e){
            	if (e.getKeyCode() == KeyEvent.VK_ENTER){
            		eword.requestFocus();
            	}
            }
        });
		
		eword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e){
            	if (e.getKeyCode() == KeyEvent.VK_ENTER){
            		updateListDisplay();
            		gword.setText("");
            		eword.setText("");
            		gword.requestFocus();
            	}
            }
        });
		
		addCard.addActionListener(new ActionListener(){
			@Override 
            public void actionPerformed(ActionEvent e){
        		updateListDisplay();
        		gword.setText("");
        		eword.setText("");
        		gword.requestFocus();
            }
		});
		
		save.addActionListener(new ActionListener(){
			@Override 
            public void actionPerformed(ActionEvent e){
				saveList();
            }
		});
		
		finish.addActionListener(new ActionListener(){
			@Override 
            public void actionPerformed(final ActionEvent e){
				if (!listDisplay.getText().isEmpty()){
					String[] tempArray = listDisplay.getText().split(" : ");
					for (String s : tempArray){
						flashDeck.add(s.trim());
					}
					frame.dispose();
					startGame();
				}
            }
		});
            
		textPanel.add(gword);
		textPanel.add(eword);
		
		buttonPanel.add(addCard);
		buttonPanel.add(save);
		buttonPanel.add(finish);
		
		//frame will be used so that the user can see the list they make.
		//this list is what's actually used later on.
		frame = new JFrame();
		frame.setTitle("Current List");
		frame.add(new JLabel("<html>You can edit this list to fix mistakes.<br/>"
				+ "Please keep the ':'s in place."), BorderLayout.NORTH);
		frame.add(scroller, BorderLayout.CENTER);
		frame.setSize(400, 600);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		add(message, BorderLayout.NORTH);
		add(textPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		pack();
		setTitle("Build new deck");
		setLocation(frame.getX(), frame.getY() - 180);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}//end makeNewDeck()
	
	//Method used to show the user the list they make
	private void updateListDisplay(){
		if (!gword.getText().trim().isEmpty() && !eword.getText().trim().isEmpty()){
			listDisplay.append(gword.getText().trim() + " : " + eword.getText().trim() + " : \n");
		}
	}//end updateListDisplay()

	//Method used to save a list.  Text is saved from listDisplay
	private void saveList(){
		String[] tempArray = listDisplay.getText().split(" : ");
		int size = tempArray.length - 1;
		final JFileChooser chooser = new JFileChooser();
		File workingDirectory = new File(System.getProperty("user.dir"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
		
		//Apply needed elements 
		chooser.setCurrentDirectory(workingDirectory);
		chooser.setFileFilter(filter);
		
		int returnVal = chooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			try {
				BufferedWriter output = new BufferedWriter(new FileWriter(file + ".txt"));
				for (int i = 0; i < size; i += 2){
					output.write(tempArray[i] + " : " + tempArray[i + 1]);
					output.newLine();
				}
				output.close();
			} 
	        catch (IOException e) {
			} 
		}
	}//end saveList()

	//Method used to process the game
	private void startGame(){
		JButton check = new JButton("Check");
		JPanel panel = new JPanel();
		String reminderMessage = "<html>Remember, nouns in German need to be capitalized. "
				+ "<br/>So der hund is incorrect.<br?>der Hund is correct.<br/>Also, use a '-'"
				+ " for separable verbs.<br/>Example: ein-schlafen.<html>";
		JLabel reminder = new JLabel(reminderMessage);
		
		getContentPane().removeAll();
		eword = new JTextField(15);
		count = 0;
		
		current = getRandomCard(flashDeck.size());
		String message = "<html>" + count + " out of " + (count + wrong) + 
				" Correct.<br/>ä: alt + 0228, ö: alt + 0246, ü: alt + 0252, ß: alt + 0223<br/>" +
				flashDeck.get(current) + " is: <html>";
		jlMessage.setText(message);
		answer = false;
		
		check.addActionListener(new ActionListener(){
			@Override 
            public void actionPerformed(final ActionEvent e){
				checkList(current);
				nextCard();
				eword.setText("");
            }
		});
		
		eword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e){
            	if (e.getKeyCode() == KeyEvent.VK_ENTER){
            		checkList(current);
    				nextCard();
    				eword.setText("");
            	}
            }
        });
		
		panel.add(eword);
		panel.add(check);
		
		add(jlMessage, BorderLayout.CENTER);
		add(panel, BorderLayout.SOUTH);
		
		setTitle("Flash Card Game");
		setLocationRelativeTo(null);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		frame = new JFrame();
		frame.add(reminder);
		frame.setTitle("Reminder");
		frame.pack();
		frame.setLocation(this.getX(), this.getY() - 125);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		
		requestFocus();
	}//end startGame()

	//Method used to check the users work
	private void checkList(int card){
		if (flashDeck.get(card - 1).equals(eword.getText().trim())){
			count++;
			answer = true;
		}
		else{
			wrong++;
			answer = false;
		}
	}//end checkList()

	//Method used to display the next word
	private void nextCard(){
		int previous = current;
		current = getRandomCard(flashDeck.size());
		if (answer){
			String message = "<html>Correct! " + flashDeck.get(previous) + " is " + flashDeck.get(previous - 1) + "<br/>" +
				count + " out of " + (count + wrong) + " Correct.<br/>ä: alt + 0228, ö: alt + 0246, ü: alt + 0252, ß: alt + 0223<br/>" +
				flashDeck.get(current) + " is: <html>";
			jlMessage.setText(message);
			answer = false;
		}
		else{
			String message = "<html>Incorrect! " + flashDeck.get(previous) + " is " + flashDeck.get(previous - 1) + "<br/>" +
				count + " out of " + (count + wrong) + " Correct.<br/>ä: alt + 0228, ö: alt + 0246, ü: alt + 0252, ß: alt + 0223<br/>" +
				flashDeck.get(current) + " is: <html>";
			jlMessage.setText(message);
		}
		pack();
	}//end nextCard()
	
	//Method used to generate a randome number between 0 and flashDeck.size() - 1
	private int getRandomCard(int size){
		int randCard;
		while (true){
			randCard = ranNum.nextInt(size);
			if (randCard % 2 != 0){
				break;
			}
		}
		return randCard;
	}//end getRandomCard()
}//end Class FlashCardGame