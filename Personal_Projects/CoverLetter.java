import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/*
 * Author: Ben Sims
 * Date: 29 Apr 15
 */
public class CoverLetter extends JFrame{
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args){
		new CoverLetter();
		
	}//end main()
	
	public CoverLetter(){
		JLabel message = new JLabel(getText("English"));
		JButton gitHubButton = new JButton("View Some Source Code!");
		JButton linkedInButton = new JButton("LinkedIn Profile!");
		JRadioButton englishRB = new JRadioButton("In English.");
		JRadioButton deutschRB = new JRadioButton("auf Deutsch.");
		ButtonGroup languageBG = new ButtonGroup();
		JPanel buttonPanel = new JPanel();
		
		gitHubButton.addActionListener(e -> openWebPage("https://github.com/BenSims/Code-I-Learned-With.git"));
		linkedInButton.addActionListener(e -> openWebPage("https://de.linkedin.com/pub/benjamin-sims/45/b65/747"));
		
		gitHubButton.setToolTipText("https://github.com/BenSims/Code-I-Learned-With.git");
		linkedInButton.setToolTipText("https://de.linkedin.com/pub/benjamin-sims/45/b65/747");
		
		englishRB.addActionListener(e -> message.setText(getText("English")));
		deutschRB.addActionListener(e -> message.setText(getText("Deutsch")));
		
		languageBG.add(englishRB);
		languageBG.add(deutschRB);
		
		englishRB.setSelected(true);
		
		buttonPanel.add(englishRB);
		buttonPanel.add(deutschRB);
		buttonPanel.add(gitHubButton);
		buttonPanel.add(linkedInButton);
		
		add(message, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		setLocation(100, 100);;
		setSize(700, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("YOUR NAME Cover Letter");
		setVisible(true);
		
	}//end CoverLetter() constructor
	
	//Method used to set the text of the message
	private String getText(String language){
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<html>");
		
		if (language.contentEquals("English")){
			htmlBuilder.append("<body><p>COVER LETTER HERE</p></body>");
		}
		else if (language.contentEquals("Deutsch")){
			htmlBuilder.append("<body><p>ANSCHREIBEN HIER</p></body>");
		}

		htmlBuilder.append("</html>");
		String html = htmlBuilder.toString();
		return html;
	}//end getText()
	
	//this method found at http://stackoverflow.com/questions/19771836/adding-and-opening-links-on-a-jbutton
	private void openWebPage(String url){
		try {
			java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
		}
		catch (java.io.IOException e) {
			System.out.println(e.getMessage());
		}
	}//end openWebPage()

}//end CoverLetter class
