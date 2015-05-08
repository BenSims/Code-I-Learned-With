import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* Author: Ben Sims
 * Date:   12Apr15
 * Info:   This is a small program I made
 * that is used to sort mp3 files.  It takes
 * the information held at the end of an mp3
 * and uses it to sort the files.  Files are 
 * sorted by alphabetically by artist, then 
 * by album name.  
 * Thank you so much Tika/Apache developers.  
 * You guys are the best!
 */

public class Mp3Sort {
	
	public static void main(String[] args){
		new Mp3Sort();
    }//end main()
	
	public Mp3Sort(){
		Display display = new Display();
		display.setButton(makeActionListener(display));
		
		display.setTitle("MP3 Sorter");
		display.setLocationRelativeTo(null);
		display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		display.pack();
		display.setVisible(true);
	}//end Mp3Sort() constructor
	
	//method used to make an ActionListener
	private ActionListener makeActionListener(Display display){
		return new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				File targetDir = new File(display.getTargetDir());
				File finishDir = new File(display.getFinishDir());
				
				if (targetDir.isDirectory() && finishDir.isDirectory()){
					makeDir(display.getFinishDir());
					scanAndSort(targetDir, finishDir.getPath());
					popUpMessage("Finished!");
				}
				else{
					popUpMessage("One of the folders does not exist. Please check folder paths.");
				}
			}
		};
	}//end makeActionListener()
	
	//method used to make sorted directory
	private void makeDir(String targetDir){
		char letter = 'A';
		new File(targetDir + "\\Sorted Music").mkdir();
		
		for (int i = 0; i < 26; i++){
			new File(targetDir + "\\Sorted Music\\" + letter).mkdir();
			letter++;
		}
	}//end makeDir()
	
	//method used to scan folder for other directories and mp3s
	//when either are found it will handle them accordingly.
	private void scanAndSort(File targetDir, String finishDir){
		if (targetDir.getPath().endsWith("AppData") || targetDir.getPath().endsWith("Sorted Music")){
			//Application Data; Cookies
			return;
		}
		File[] dirContent = targetDir.listFiles();
		try{
			for (File f: dirContent){
				if (f.isDirectory()){//recursion is used to handle dirs
					scanAndSort(f, finishDir);
				}
				else if (isMp3(f)){
					moveFile(f, finishDir, getArtist(f), getAlbum(f));
				}
			}
		}catch(NullPointerException e){
			System.out.println(targetDir.getPath());
			e.printStackTrace();
		}
		
		
	}//end scanAndSort()
	
	//method used to check if file is an mp3
	private boolean isMp3(File file){
		if (file.getName().endsWith(".mp3")){
			return true;
		}
		return false;
	}//end isMp3()
	
	//method used to pull Artist data from mp3
	private String getArtist(File mp3){
		String tempString = "";
	    InputStream input;
	    ContentHandler handler = new DefaultHandler();
	    Metadata metadata = new Metadata();
	    Parser parser = new Mp3Parser();
	    ParseContext parseCtx = new ParseContext();
	    
		try {
			input = new FileInputStream(mp3);
			parser.parse(input, handler, metadata, parseCtx);
		    input.close();
		} 
		catch (IOException | SAXException | TikaException e) {
			System.out.println("Error with: " + mp3.getPath());
			e.printStackTrace();
		}
	    
	    tempString = metadata.get("xmpDM:artist");
		
		if (tempString.isEmpty() || tempString.contentEquals(" ")){
			return "Unknown";
		}
		return tempString;
		
	}//end getArtist()
	
	//method used to pull Album data from mp3.  This is kept
	//separate from getArtist() because I plan to expand this
	//program and let the user choose how to sort files.
	private String getAlbum(File mp3){
		String tempString = "";
	    InputStream input;
	    ContentHandler handler = new DefaultHandler();
	    Metadata metadata = new Metadata();
	    Parser parser = new Mp3Parser();
	    ParseContext parseCtx = new ParseContext();
	    
		try {
			input = new FileInputStream(mp3);
			parser.parse(input, handler, metadata, parseCtx);
		    input.close();
		} 
		catch (IOException | SAXException | TikaException e) {
			System.out.println("Error with: " + mp3.getPath());
			e.printStackTrace();
		}
	    
	    tempString = metadata.get("xmpDM:album");
		
		if (tempString.isEmpty() || tempString.contentEquals(" ")){
			return "Unknown";
		}
		return tempString;
	}//end getArtist()
	
	//method used to move an mp3 from target to finish folder
	private void moveFile(File mp3, String finishDir, String artist, String album){
		String destinationFolder = finishDir + "\\" + "Sorted Music\\" + 
				artist.substring(0, 1).toUpperCase() + "\\" + artist;
		new File(destinationFolder).mkdir();
		destinationFolder += "\\" + album;
		new File(destinationFolder).mkdir();
		
		mp3.renameTo(new File(destinationFolder + "\\" + mp3.getName()));
	}//end moveFile()
	
	//Method used to make a popup message
	private void popUpMessage(String message){
		JFrame frame = new JFrame();
		JLabel mText = new JLabel(message);
		
		frame.add(mText);
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		
	}//end popUpMessage()

}//end

class Display extends JFrame{
	private static final long serialVersionUID = 1L;
	private JTextField targetDir = new JTextField(17);
	private JTextField finishDir = new JTextField(17);
	private JButton startJButton = new JButton("Start");
	
	public Display(){
		JPanel mainPanel = new JPanel();
		mainPanel.add(targetDir);
		mainPanel.add(finishDir);
		targetDir.setText("Enter folder to be sorted here.");
		finishDir.setText("Enter folder to sort items to here.");
		add(mainPanel, BorderLayout.WEST);
		add(startJButton, BorderLayout.EAST);
	}//end Display() constructor
	
	public String getTargetDir(){
		return targetDir.getText();
	}//end getTargetDir()
	
	public String getFinishDir(){
		return finishDir.getText();
	}//end getFinishDir()
	
	public void setButton(ActionListener actionListener){
		startJButton.addActionListener(actionListener);
	}//end setButton()
}//end Display class
