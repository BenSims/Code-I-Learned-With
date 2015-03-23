
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*Made by Ben Sims
 * for Anais
 * 10Mar15
 */

public class GuessGame extends JFrame{
	private static final long serialVersionUID = 1L;
	
	ArrayList<JButton> deck = new ArrayList<JButton>();
	Random ranNum = new Random();
	boolean turn = true;
	int count = 20;
	
	public static void main(String[] args){
		new GuessGame();
	}//end main()

	public GuessGame(){
		makeGame();
	}//end GuessGame()
	
	//Method used to create a new game
	private void makeGame(){
		setLayout(new GridLayout(5,5));
		char symbol = 'A';
		int card1, card2;
		//for loop makes all the cards
		for (int i = 0; i < 20; i++){
			deck.add(new JButton(" "));//the blank string is used later
			this.add(deck.get(i));
		}

		//this loop initializes all the cards at random
		for (int i = 0; i < deck.size(); i += 2){
			card1 = randomCard();
			card2 = randomCard();
			//Action listeners force the cards to be paired with another as well as a symbol
			deck.get(card1).addActionListener(setCard(deck.get(card1), deck.get(card2), symbol + ""));
			deck.get(card2).addActionListener(setCard(deck.get(card2), deck.get(card1), symbol + ""));
			deck.get(card1).setText(".");
			deck.get(card2).setText(".");
			deck.get(card1).setFont(new Font("Arial", Font.PLAIN, 40));
			deck.get(card2).setFont(new Font("Arial", Font.PLAIN, 40));
			symbol += 1;
		}
		
		setTitle("ANAIS");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		setVisible(true);
	}//end makeGame()
	
	//Method used to find and reserve a blank card
	private int randomCard(){
		int card = 0;
		do{
			card = ranNum.nextInt(deck.size());
		} while(!deck.get(card).getText().contentEquals(" "));
		deck.get(card).setText("Taken");//this lets you use the method twice in a row
		return card;
	}//end randomCard()
	
	//method used to make ActionListener for JButton
	private ActionListener setCard(final JButton card1, final JButton card2, final String label){
		return new ActionListener(){
			@Override 
            public void actionPerformed(ActionEvent e){
				if (card1.getText().contentEquals(".")){
					card1.setText(label);
				}
				
				checkTurn(card1);//Before checking for a match the other cards might need to be reset
				
				//This part checks for a match
				if (!card1.getText().contentEquals(".") && !card2.getText().contentEquals(".")){
					match(card1, card2, label);
				}
            }
		};
	}//end setCard()
	
	//method used to keep track and reset cards after two turns
	private void checkTurn(JButton card1){
		if (turn){
			for (JButton jb: deck){
				if (!jb.getText().contentEquals(".") && !jb.equals(card1)){
					jb.setText(".");
				}
			}
			turn = false;
		}
		else{
			turn = true;
		}
	}//end checkTurn()
	
	//method used to handle matched cards
	private void match(JButton card1, JButton card2, String label){
		// Cards are replaced by JLabels with the same label
		add(new JLabel(label), deck.indexOf(card1) + 1);
		add(new JLabel(label), deck.indexOf(card2) + 1);
		remove(card1);
		remove(card2);
		count -= 2;
		if (count == 0){
			winner();
		}
		
	}//end match()
	
	//Method used when game is won
	private void winner(){
		setLayout(new FlowLayout());
		getContentPane().removeAll(); //Removes all labels from the JFrame
		add(new JLabel("You won!!  Would you like to paly again?"));

		//Now make a new button, that will let the player start a new game.
		deck.add(new JButton("Yes"));
		deck.get(deck.size() - 1).addActionListener(new ActionListener(){
			@Override 
            public void actionPerformed(ActionEvent e){
				newGame();
			}
		});
		add(deck.get(deck.size() - 1));
		repaint();
		pack();
		
		
	}//end winner()
	
	//Method used to make a new game
	private void newGame(){
		this.dispose();
		new GuessGame();
	}//end newGame()
}
