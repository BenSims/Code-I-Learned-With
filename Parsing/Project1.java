import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/*File Name: Project1.java
 * Date: 5 Feb 15
 * Author: Ben Sims
 * Required Files: none
 * Description: Parsing program 
 * Input: .txt files                     
 * Output: GUI 
 */

public class Project1 {
	
	public static void main(String[] args){
		new Project1();
	}//end main() method
	
	public Project1(){
		ArrayList<String> code = null;
		String file = null;
		
		try{
			file = load_file();
			try {
				code = lexer(file);
			} catch (IOException e) {
				System.out.println("File not valid");
				System.exit(0);
			}
		}catch (NullPointerException e){
			System.exit(0);
		}
		System.out.println(file.getClass());
		for (String s: code){
			if (!token_check(s)){
				System.out.println("ERROR '" + s + "' is not valid syntax.");
			}
		}
		
		parse_it_up(code);
	}//end Project1() constructor
	
	//Method used to find the path for the file needed to load.  It returns this path
	//as a string
	private String load_file(){
		//Create needed elements
		String fileName = null;
		JFrame jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JFileChooser chooser = new JFileChooser();
		File workingDirectory = new File(System.getProperty("user.dir"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", 
				"txt", "text");
		
		//Apply needed elements 
		chooser.setCurrentDirectory(workingDirectory);
		chooser.setFileFilter(filter);
		
		//Find file and set "fileName" to equal its path.
		int returnVal = chooser.showOpenDialog(jframe);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
		   fileName = chooser.getSelectedFile().getPath();
		}
		
		//Remove "chooser" and return "fileName"
		jframe.remove(chooser);
		return fileName;
	}//end load_file()
	
	//method used to load file, and process all the data into an arraylist
	private ArrayList<String> lexer(String fileName) throws FileNotFoundException, 
    UnsupportedEncodingException, IOException{
		ArrayList<String> sourceCode = new ArrayList<String>();
		char[] tempLex ={'"', '”', '“', ';', ':', '(', ')', ',', 'E', 'B', 'G', 'L', 'P',
				'T', 'R', 'G', 'F', 'W', '.'};
		char[] tempInput;
		String tempString = "";
		boolean newLex = false;
		
        java.io.File code = new java.io.File(fileName);
        Scanner input = new Scanner(code);
        
        //Each character is examined here
        while (input.hasNextLine()){
        	tempInput = input.nextLine().toCharArray();
        	
        	for (char c: tempInput){
				if (tempString.startsWith("\"") || tempString.startsWith("“")){
					if (c != '"' && c != '”'){
						tempString += c;
					}
					else{
						tempString += c;
						sourceCode.add(tempString);
						tempString = "";
					}
				}
				else if (c != ' '){
        			if (!tempString.isEmpty()){
        				if (is_number("" +  c)){
        					newLex = true;
        				}
        				for (char l: tempLex){
        					if (l == c){
        						newLex = true;
        					}
        				}
        				//if newLex becomes true then the char is a character that will
        				//create a new lex, and will be handled as needed below.  It could also
        				// be a number and will also be handled here.
        				if (newLex){
    						if (c == '(' || c == ')' || c == ';' || c == ',' ||
    								c == ':' || c == '.'){
        						sourceCode.add(tempString);
        						sourceCode.add("" +c);
        						tempString = "";
        					}
        					else if (is_number("" + c)){
        						if (is_number(tempString)){
        							tempString += c;
        						}
        						else{
        							sourceCode.add(tempString);
        							tempString = "" + c;
        						}
        					}
        					else{
            					sourceCode.add(tempString);
            					tempString = "" + c;	
        					}
    					
    					newLex = false;
        				}
        				else{
        					tempString += c;
        				}
        			}
        			else{
        				tempString += c;
        			}
        		}
        	}
        }
        input.close();
		return sourceCode;
	}//end load_data()
	
	//Method used to check if a token was valid
	private boolean token_check(String token){
		String[] tokenList = {",", "End", "(", ")", ";", ".", ":", "Button",
				"Window", "Layout", "Flow", "Grid", "Group", "Panel", "Textfield",
				"Radio", "Label"};
		for (String s: tokenList){
			if (s.compareTo(token) == 0){
				return true;
			}
		}
		if ((token.startsWith("\"") && token.endsWith("\"")) || (token.startsWith("“") && token.endsWith("”"))){
			return true;
		}
		if (is_number(token)){
			return true;
		}
		return false;
	}//end token_check()
	
	//Method used to check if a token is a number
	private boolean is_number(String number){
		
		try{
			Integer.parseInt(number);
		}
		catch(NumberFormatException e){
			return false;
		}
		return true;
	}//end is_number
	
	//Method used to parse the code
	private void parse_it_up(ArrayList<String> code){
		int tokenCount = 0;
		JFrame frame = null;
		
		frame = make_frame(code, tokenCount);
		
		frame.validate();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}//end parse_it_up()
	
	//Method used to build window.  From this point an every element
	//that was part of the source code is check and applied if there
	//is no error.
	private JFrame make_frame(ArrayList<String> code, int tc){
		JFrame tempFrame = new JFrame();
		Widget widget = null;
		String token = null;
		int tempInt1 = 0, tempInt2 = 0;
		if (code.get(tc).contentEquals("Window")){
			tc++;
			token = code.get(tc++);
			if ((token.startsWith("\"") && token.endsWith("\"")) || (token.startsWith("“") && token.endsWith("”"))){
				tempFrame = new JFrame(token.substring(1, token.length() - 1));
				token = code.get(tc++);
			}
			else{
				System.out.println("Error found at '" + token + "'. Should be "
						+ "surrounded by \" \"");
				System.exit(0);
			}
			
			if (token.contentEquals("(")){
				token = code.get(tc++);
				if (is_number(token)){
					tempInt1 = Integer.parseInt(token);
					token = code.get(tc++);
				}
				else{
					System.out.println("Error at " + token + ". Integer required.");
					System.exit(0);
				}
				
				if (token.contentEquals(",")){
					token = code.get(tc++);
					if (is_number(token)){
						tempInt2 = Integer.parseInt(token);
						token = code.get(tc++);
					}
					else{
						System.out.println("Error at " + token + ". Integer required.");
						System.exit(0);
					}
				}
				else{
					System.out.println("Error at " + token + ". ',' is required.");
					System.exit(0);
				}
				if (token.contentEquals(")")){
					token = code.get(tc++);
				}
				else{
					System.out.println("Error at " + token + ". ')' is expected.");
					System.exit(0);
				}
			}
			else{
				System.out.println("Error at " + token + ". '(' is expected.");
				System.exit(0);
			}
		}
		else{
			System.out.println("Error at " + token + ". 'Window' is expected.");
			System.exit(0);
		}

		tempFrame.setSize(tempInt1, tempInt2);
		
		//this section makes the layout
		if (token.contentEquals("Layout")){
			tempFrame.setLayout(make_layout(code, tc));
			while (!code.get(tc).contentEquals(":")){
				tc++;
			}
			tc++;
			token = code.get(tc++);
		}
		else{
			System.out.println("Layout required for Window");
			System.exit(0);
		}
		
		//this section loops until all widgets attached to the Window are made.
		while (token.contentEquals("Panel") || token.contentEquals("Textfield") ||
				token.contentEquals("Button") || token.contentEquals("Group") ||
				token.contentEquals("Label")){
			widget = make_widget(token, code, tc);
			
			if (token.contentEquals("Panel")){
				widget.increase_endCount();
				tempFrame.add(widget.get_jp());
				try{
					tempInt1 = widget.get_endCount();
					while (tempInt1 != 0){
						if (code.get(tc).contentEquals("End")){
							tempInt1--;
						}
						tc++;
					}	
				}catch (IndexOutOfBoundsException e){
					System.out.println("Missing 'End' syntax.");
					System.exit(0);
				}
				if (!code.get(tc).contentEquals(";")){
					System.out.println("Error at " + token + ". ';' is required");
					System.exit(0);
				}
			}
			
			if (token.contentEquals("Textfield")){
				tempFrame.add(widget.get_jtf());
			}
			
			if (token.contentEquals("Button")){
				tempFrame.add(widget.get_jb());
			}
			
			if (token.contentEquals("Group")){
				for (JRadioButton b: widget.get_jrbs()){
					tempFrame.add(b);
				}
				try{
					while (!code.get(tc).contentEquals("End")){
						tc++;
					}	
				}catch (IndexOutOfBoundsException e){
					System.out.println("Missing 'End' syntax.");
					System.exit(0);
				}
			}
			
			if (token.contentEquals("Label")){
				tempFrame.add(widget.get_jl());
			}

			try{
				while (!code.get(tc).contentEquals(";")){
					tc++;
				}	
			}catch (IndexOutOfBoundsException e){
				System.out.println("Missing ';' syntax.");
				System.exit(0);
			}
			tc++;
			token = code.get(tc++);
		}
		if (!token.contentEquals("End")){
			System.out.println("Missing 'End' syntax.");
			System.exit(0);
		}
		
		token = code.get( tc);
		
		if (!token.contentEquals(".")){
			System.out.println("Missing '.' syntax.");
			System.exit(0);
		}
		return tempFrame;
	}//end make_frame()
	
	//Method used to make a layout
	private LayoutManager make_layout(ArrayList<String> code, int tc){
		String token = code.get(tc++);
		int tempInt1 = 0, tempInt2 = 0, tempInt3 = 0, tempInt4 = 0;
		LayoutManager tempLayout = null;
		
		if (token.contentEquals("Flow")){
			tempLayout = new FlowLayout();
			token = code.get(tc);
		}
		else if (token.contentEquals("Grid")){
			token = code.get(tc++);
			if (token.contentEquals("(")){
				token = code.get(tc++);
			}
			else{
				System.out.println("Error at " + token + ". '(' is expected.");
				System.exit(0);
			}
			if (is_number(token)){
				tempInt1 = Integer.parseInt(token);
				token = code.get(tc++);
			}
			else{
				System.out.println("Error at " + token + ". Integer value required.");
				System.exit(0);
			}
			if (token.contentEquals(",")){
				token = code.get(tc++);
			}
			else{
				System.out.println("Error at " + token + ". ',' is required.");
				System.exit(0);
			}
			if (is_number(token)){
				tempInt2 = Integer.parseInt(token);
				token = code.get(tc++);
			}
			else{
				System.out.println("Error at " + token + ". Integer value required.");
				System.exit(0);
			}
			if (token.contentEquals(")") || token.contentEquals(",")){
				if (token.contentEquals(")")){
					token = code.get(tc++);
					if (token.contentEquals(":")){
						return tempLayout = new GridLayout(tempInt1, tempInt2);
					}
					else{
						System.out.println("Error at " + token + ". ':' is required.");
						System.exit(0);
					}
				}
				else if (token.contentEquals(",")){
					token = code.get(tc++);

					if (is_number(token)){
						tempInt3 = Integer.parseInt(token);
						token = code.get(tc++);
					}
					else{
						System.out.println("Error at " + token + ". Integer value required.");
						System.exit(0);
					}
					if (token.contentEquals(",")){
						token = code.get(tc++);
					}
					else{
						System.out.println("Error at " + token + ". ',' is required.");
						System.exit(0);
					}
					if (is_number(token)){
						tempInt4 = Integer.parseInt(token);
						token = code.get(tc++);
					}
					else{
						System.out.println("Error at " + token + ". Integer value required.");
						System.exit(0);
					}
				}
				else{
					System.out.println("Error at " + token + ". Grid specs are not correct.");
					System.exit(0);
				}
				if (!token.contentEquals(")")){
					System.out.println("Error at " + token + ". ')' is expected.");
					System.exit(0);
				}
				token = code.get(tc);
			}
			tempLayout = new GridLayout(tempInt1, tempInt2, tempInt3, tempInt4);
		}
		else{
			System.out.println("Error at " + token + ". Invalid Layout type.");
			System.exit(0);
		}
		if (!token.contentEquals(":")){
			System.out.println("Error at " + token + ". ':' is expected.");
			System.exit(0);
		}
		return tempLayout;
	}//end make_layout()
	
	//Method used to make widgets
	private Widget make_widget(String type, ArrayList<String> code, int tc){
		Widget tempWidget = new Widget();
		String tempString = null;
		boolean inPanel = false;
		int endCount;
		
		if (type.contentEquals("Button")){
			tempString = code.get(tc++);
			if ((tempString.startsWith("\"") && tempString.endsWith("\"")) || (tempString.startsWith("“") && tempString.endsWith("”"))){
				tempWidget.set_jb(new JButton(tempString.substring(1, tempString.length() - 1)));
				tempString = code.get(tc++);
			}
			else{
				System.out.println("Error at " + tempString + ". Text surrounded by \" \" required");
				System.exit(0);
			}
			if (!tempString.contentEquals(";")){
				System.out.println("Error at " + tempString + ". ';' is required");
				System.exit(0);
			}
		}
		else if (type.contentEquals("Group")){
			tempString = code.get(tc++);
			tempWidget.set_bg(new ButtonGroup());
			while (tempString.contentEquals("Radio")){
				tempWidget.add_jrb(make_rb(code, tc++));
				if(code.get(tc).contentEquals(";")){
					tc++;
					tempString = code.get(tc++);
				}
				else{
					System.out.println("Error at " + tempString + ". ';' is expected.");
					System.exit(0);
				}
			}
			tempWidget.set_bg(new ButtonGroup());
			for (JRadioButton b: tempWidget.get_jrbs()){
				tempWidget.get_bg().add(b);
			}
			tempString = code.get(tc++);
			if (!tempString.contentEquals(";")){
				System.out.println("Error at " + tempString + ". ';' is required");
				System.exit(0);
			}
		}
		else if (type.contentEquals("Label")){
			tempString = code.get(tc++);
			if ((tempString.startsWith("\"") && tempString.endsWith("\"")) || (tempString.startsWith("“") && tempString.endsWith("”"))){
				tempWidget.set_jl(new JLabel(tempString.substring(1, tempString.length() - 1)));
				tempString = code.get(tc++);
			}
			else{
				System.out.println("Error at " + tempString + ". Text surrounded by \" \" required");
				System.exit(0);
			}
			if (!tempString.contentEquals(";")){
				System.out.println("Error at " + tempString + ". ';' is required");
				System.exit(0);
			}
		}
		else if (type.contentEquals("Panel")){
			inPanel = true;
			tempString = code.get(tc++);
			tempWidget.set_jp(new JPanel());
			tempWidget.get_jp().setLayout(make_layout(code, tc));
			while (!code.get(tc).contentEquals(":")){
				tc++;
			}
			tc++;
			tempString = code.get(tc++);
			while (true){
				inPanel = true;
				Widget tempWidget2 = make_widget(tempString, code, tc);
				
				if (tempString.contentEquals("Panel")){
					inPanel = false;
					tempWidget2.increase_endCount();
					tempWidget.get_jp().add(tempWidget2.get_jp());
					try{
						endCount = tempWidget2.get_endCount();
						while (endCount != 0){
							if (code.get(tc).contentEquals("End")){
								endCount--;
								tempWidget.increase_endCount();
							}
							tc++;
						}	
					}catch (IndexOutOfBoundsException e){
						System.out.println("Missing 'End' syntax.");
						System.exit(0);
					}
					if (!code.get(tc).contentEquals(";")){
						System.out.println("Error at " + tempString + ". ';' is required ");
						System.exit(0);
					}
				}
				
				if (tempString.contentEquals("Textfield")){
					tempWidget.get_jp().add(tempWidget2.get_jtf());
				}
				
				if (tempString.contentEquals("Button")){
					tempWidget.get_jp().add(tempWidget2.get_jb());
				}
				
				if (tempString.contentEquals("Group")){
					inPanel = false;
					tempWidget.increase_endCount();
					for (JRadioButton b: tempWidget2.get_jrbs()){
						tempWidget.get_jp().add(b);
					}
					try{
						while (!code.get(tc).contentEquals("End")){
							tc++;
						}	
					}catch (IndexOutOfBoundsException e){
						System.out.println("Missing 'End' syntax.");
						System.exit(0);
					}
				}
				
				if (tempString.contentEquals("Label")){
					tempWidget.get_jp().add(tempWidget2.get_jl());
				}

				try{
					while (!code.get(tc).contentEquals(";")){
						tc++;
					}	
				}catch (IndexOutOfBoundsException e){
					System.out.println("Missing ';' syntax.");
					System.exit(0);
				}
				
				if (inPanel && code.get(tc - 1).contentEquals("End")){
					break;
				}
				tc++;
				tempString = code.get(tc++);
			}
		}
		else if (type.contentEquals("Textfield")){
			tempString = code.get(tc++);
			if (is_number(tempString)){
				tempWidget.set_jtf(new JTextField(Integer.parseInt(tempString)));
				tempString = code.get(tc++);
			}
			else{
				System.out.println("Error at " + tempString + ". Integer required.");
				System.exit(0);
			}
			if (!tempString.contentEquals(";")){
				System.out.println("Error at " + tempString + ". ';' is required");
				System.exit(0);
			}
		}
		return tempWidget;
	}//end make_widget
	
	//Method used to make radio buttons
	private JRadioButton make_rb(ArrayList<String> code, int tc){
		String tempString = code.get(tc);
		JRadioButton jrb = null;
		
		if ((tempString.startsWith("\"") && tempString.endsWith("\"")) || (tempString.startsWith("“") && tempString.endsWith("”"))){
			jrb = new JRadioButton(tempString.substring(1, tempString.length() - 1));
		}
		else{
			System.out.println("Error at " + tempString + ". Text surrounded by \" \" is required");
			System.exit(0);
		}
		return jrb;
	}
}//end class Project1

class Widget{
	int endCount = 0;
	JButton b = null;
	ButtonGroup g = null;
	ArrayList<JRadioButton> jrbs = null;
	JLabel l = null;
	JPanel p = null;
	JTextField t = null;
	
	public Widget(){
	}//end Widget constructor
	
	//setter methods for select widgets
	public void increase_endCount(){
		endCount++;
	}
	
	public void decrease_endCount(){
		endCount--;
	}
	
	public void set_jb(JButton jb){
		b = jb;
	}
	
	public void set_bg(ButtonGroup bg){
		g = bg;
	}
	
	public void add_jrb(JRadioButton jrb){
		if (jrbs == null){
			jrbs =  new ArrayList<JRadioButton>();
		}
		jrbs.add(jrb);
	}
	
	public void set_jl(JLabel jl){
		l = jl;
	}
	
	public void set_jp(JPanel jp){
		p = jp;
	}
	
	public void set_jtf(JTextField jtf){
		t = jtf;
	}
	
	//getter methods for select widgets
	public int get_endCount(){
		return endCount;
	}
	
	public JButton get_jb(){
		return b;
	}
	
	public ButtonGroup get_bg(){
		return g;
	}
	
	public ArrayList<JRadioButton> get_jrbs(){
		return jrbs;
	}
	
	public JLabel get_jl(){
		return l;
	}
	
	public JPanel get_jp(){
		return p;
	}
	
	public JTextField get_jtf(){
		return t;
	}
}//end widget class
