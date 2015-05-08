import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;

/*File Name: ButtonGame.java
 * Date: 11 Nov 14
 * Author: Ben Sims
 * Required Files: none
 * Description: Game that mimics a mini-game from Super Mario RPG
 * Input: none                    
 * Output: GUI
 */
public class ButtonGame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JButton jb1 = new JButton("++++");
	private JButton jb2 = new JButton("++++");
	private JButton jb3 = new JButton("++++");
	private JButton jb4 = new JButton("++++");
	private JButton jb5 = new JButton("++++");
	private JButton jb6 = new JButton("++++");
	private JButton jb7 = new JButton("++++");
	private JButton jb8 = new JButton("++++");
	private JButton jb9 = new JButton("++++");
	private JButton jb10 = new JButton(":P");
	JTable table = new JTable();
	
	public ButtonGame(){
		setLayout(new GridLayout(3, 3));
		
		jb1.addActionListener(this.setListener(jb1, jb2, jb4, jb10, jb10));
		
		jb2.addActionListener(this.setListener(jb2, jb1, jb3, jb5, jb10));
		
		jb3.addActionListener(this.setListener(jb3, jb2, jb6, jb10, jb10));

		jb4.addActionListener(this.setListener(jb4, jb1, jb5, jb7, jb10));
		
		jb5.addActionListener(this.setListener(jb5, jb2, jb4, jb6, jb8));

		jb6.addActionListener(this.setListener(jb6, jb3, jb5, jb9, jb10));

		jb7.addActionListener(this.setListener(jb7, jb4, jb8, jb10, jb10));

		jb8.addActionListener(this.setListener(jb8, jb5, jb7, jb9, jb10));

		jb9.addActionListener(this.setListener(jb9, jb6, jb8, jb10, jb10));
		
		
		add(jb1);
		add(jb2);
		add(jb3);
		add(jb4);
		add(jb5);
		add(jb6);
		add(jb7);
		add(jb8);
		add(jb9);
		
		
		add(table);
		setTitle("Ben's Button Game");
		pack();
		setSize(300, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	
	//Method used to show the player won
	public void gameOver(){
		JFrame aWinnerIsYou = new JFrame();
		aWinnerIsYou.add(new JLabel("You Won!"));
		aWinnerIsYou.setSize(300, 300);
		aWinnerIsYou.setLocationRelativeTo(null);
		aWinnerIsYou.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		aWinnerIsYou.setVisible(true);
	}
	
	//Method used to make ActionListeners for all the buttons
	public ActionListener setListener(final JButton num1, final JButton num2, final JButton num3, final JButton num4, final JButton num5){
		return new ActionListener(){
            @Override 
            public void actionPerformed(ActionEvent e){
            	if (num1.getText().equals("++++")){
            		num1.setText("----");
            	}
            	else if (num1.getText().equals("----")){
            		num1.setText("++++");
            	}
            	
            	if (num2.getText().equals("++++")){
            		num2.setText("----");
            	}
            	else if (num2.getText().equals("----")){
            		num2.setText("++++");
            	}
            	
            	if (num3.getText().equals("++++")){
            		num3.setText("----");
            	}
            	else if(num3.getText().equals("----")){
            		num3.setText("++++");
            	}
            	
            	if (num4.getText().equals("++++")){
            		num4.setText("----");
            	}
            	else if (num4.getText().equals("----")){
            		num4.setText("++++");
            	}
            	
            	if (num5.getText().equals("++++")){
            		num5.setText("----");
            	}
            	else if(num5.getText().equals("----")){
            		num5.setText("++++");
            	}
            	
            	if (jb1.getText().equals("----") && jb2.getText().equals("----") &&
            			jb3.getText().equals("----") && jb4.getText().equals("----") &&
            			jb5.getText().equals("----") && jb6.getText().equals("----") && 
            			jb7.getText().equals("----")&& jb8.getText().equals("----") &&
            			jb9.getText().equals("----")){
            		gameOver();
            	}
            }
        };
	}
	
	public static void main(String[] args){
		new ButtonGame();
	}

}
