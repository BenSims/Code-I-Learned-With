import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

/*File Name: SorcerersCave.java
 * Date: 20 Oct 14
 * Author: Ben Sims
 * Required Files: none
 * Description: This program simulates the game "The Sorcerer's Cave.
 * Input: inputFile.txt                    
 * Output: Game
 */

public class SorcerersCave extends JFrame{
	private static final long serialVersionUID = 123L;
	
	private static boolean moveForward = false;
	private JTextArea textArea = new JTextArea (20, 10);
	private JTextArea jobsPerforming = new JTextArea(11, 10);
	private JTextArea jobsWaiting = new JTextArea(12, 10);
	private JTextArea activityLog = new JTextArea(10, 10);
    private JComboBox <String> comboBox = new JComboBox<String>();
    private JComboBox <String> sortBox = new JComboBox<String>();
    private JRadioButton partyJRB = new JRadioButton("Party", true);
    private JRadioButton creatureJRB = new JRadioButton("Creature");
    private JTextField textField = new JTextField(17);
    private JTextField sortField = new JTextField(17);
    private ButtonGroup radioButtons = new ButtonGroup();
    private JFrame tempJF = new JFrame();
    private JTree tree;
    private Cave mainCave = new Cave();
    private ArrayList<Job> jobQOn = new ArrayList<Job>();
    private static boolean[][] displayMatrix = new boolean[4][10];
	
	public SorcerersCave(){
		String fileName = "";

		try {
			fileName = this.loadFile();
			mainCave = loadData(fileName);
		} 
		catch (IOException e) {
			System.out.println("File not found.  Please try again.");
		}
		catch (NullPointerException e){
			System.exit(0);
		}
		this.displayGame(mainCave);
	}
	
	public static void main(String[] args){
		System.out.println("Ben Sims, Object-Oriented and Concurrent Programming" +
				" (2148), Final Project");
		//this boolean variable keeps the program in a loop until a file is loaded
		new SorcerersCave();
	}
	
	
	//Method used to load a txt file.
	public static Cave loadData(String fileName) throws FileNotFoundException, 
            UnsupportedEncodingException, IOException{
		//Create needed elements
        String[] inputArray;
        String tempInput = "";
        String tempName = "";
        int tempIndex;
		Cave tempCave = new Cave();
        
        java.io.File lists = new java.io.File(fileName);
        Scanner input = new Scanner(lists);
		
        //This part takes each line of the file, and checks to see if it does not begin with "//"
		while (input.hasNextLine()){
			tempInput = input.nextLine();
			if (!tempInput.startsWith("//")){
				//If the file did not begin with "//" it is broken down into a String Array, splitting every ":"
				inputArray = tempInput.split(":");
				//Here all spaces before and after each element are taken out
				for (int i = 0; i < inputArray.length; i++){
					inputArray[i] = inputArray[i].trim();
				}
				//From this point on the beginning of inputArray is checked to see if it matches a letter.
				//this letter shows which object needs to be made.  The object is made within the ArrayList
				//that will hold it, so if an object made is "c" it will be a creature and this will be stored in
				//the creature's assigned party.  If no parent object is assigned the object will be placed in the
				//unpartiedElement ArrayList.  After each object is placed in a list it is referenced in master list
				//called allElements.
				if (inputArray[0].contentEquals("p")){
					tempCave.addParty(new Party(Integer.parseInt(inputArray[1]), inputArray[2]));
					tempCave.addElement(tempCave.getParty(Integer.parseInt(inputArray[1])));
				}
				
				if (inputArray[0].compareToIgnoreCase("c") == 0){
					tempIndex = Integer.parseInt(inputArray[4]);
					if (tempIndex == 0){
						tempCave.addUnpartiedElement(new Creature(Integer.parseInt(inputArray[1]), inputArray[2], inputArray[3], tempIndex,
									Integer.parseInt(inputArray[5]), Integer.parseInt(inputArray[6]), Integer.parseInt(inputArray[7]), ""));
						tempCave.addElement(tempCave.getUnpartiedElement(Integer.parseInt(inputArray[1])));
					}
					for (Party p : tempCave.getAllParties()){
						if (tempIndex == p.getIndex()){
							p.addCreature(new Creature(Integer.parseInt(inputArray[1]), inputArray[2], inputArray[3], tempIndex,
									Integer.parseInt(inputArray[5]), Integer.parseInt(inputArray[6]), Integer.parseInt(inputArray[7]), p.getName()));
							tempCave.addElement(p.getCreature(Integer.parseInt(inputArray[1])));
						}
					}
				}
				
				if (inputArray[0].compareToIgnoreCase("t") == 0){
					tempIndex = Integer.parseInt(inputArray[3]);
					if (tempIndex == 0){
						tempCave.addUnpartiedElement(new Treasure(Integer.parseInt(inputArray[1]), inputArray[2], tempIndex, 
								Integer.parseInt(inputArray[4]), Integer.parseInt(inputArray[5]), ""));
						tempCave.addElement(tempCave.getUnpartiedElement(Integer.parseInt(inputArray[1])));
					}
					else{
						for (Party p : tempCave.getAllParties()){
							for (Creature c : p.getMembers()){
								if (tempIndex == c.getIndex()){
									tempName = c.getName();
									c.addTreasure(new Treasure(Integer.parseInt(inputArray[1]), inputArray[2], tempIndex, 
											Integer.parseInt(inputArray[4]), Integer.parseInt(inputArray[5]), tempName));
									tempCave.addElement(c.getTreasure(Integer.parseInt(inputArray[1])));
								}
							}
						}
					}
				}
				
				//Some Artifacts may contain a name, while other do not.  The below comments mark the code that handles un-named Artifacts
				if (inputArray[0].compareToIgnoreCase("a") == 0){
					tempIndex = Integer.parseInt(inputArray[3]);
					if (tempIndex == 0){
						if (inputArray.length == 5){
							tempCave.addUnpartiedElement(new Artifact(Integer.parseInt(inputArray[1]), inputArray[2], 
									Integer.parseInt(inputArray[3]), inputArray[4], ""));
						}
						//This Artifact has no name
						else{
							tempCave.addUnpartiedElement(new Artifact(Integer.parseInt(inputArray[1]), inputArray[2], 
									Integer.parseInt(inputArray[3]), "", ""));
						}

						tempCave.addElement(tempCave.getUnpartiedElement(Integer.parseInt(inputArray[1])));
					}
					else{
						for (Party p : tempCave.getAllParties()){
							for (Creature c : p.getMembers()){
								if (tempIndex == c.getIndex()){
									tempName = c.getName();
									if (inputArray.length == 5){
										c.addArtifact(new Artifact(Integer.parseInt(inputArray[1]), inputArray[2], 
												Integer.parseInt(inputArray[3]), inputArray[4], tempName));
									}
									//This Artifact has no name
									else{
										c.addArtifact(new Artifact(Integer.parseInt(inputArray[1]), inputArray[2], 
												Integer.parseInt(inputArray[3]), "", tempName));
									}
									tempCave.addElement(c.getArtifact(Integer.parseInt(inputArray[1])));
								}
							}
						}
					}
				}
				
				//This part adds a job
				if (inputArray[0].compareToIgnoreCase("j") == 0){
					tempIndex = Integer.parseInt(inputArray[3]);
					String cName = tempCave.getElement(tempIndex).getName();
					int size = inputArray.length;
					int tempWand = Integer.parseInt(inputArray[6]);
					int tempPotion = 0;
					int tempStone = 0;
					int tempScroll = 0;
					int tempSword = 0;
					int tempArmor = 0;
					for (int i = 5; i < size; i++){
						switch (inputArray[i]){
						case "Wands": tempWand = Integer.parseInt(inputArray[i + 1]); i++; break;
						case "Potions": tempPotion = Integer.parseInt(inputArray[i +1]); i++; break;
						case "Stone": tempStone = Integer.parseInt(inputArray[i +1]); i++; break;
						case "Scroll": tempScroll = Integer.parseInt(inputArray[i + 1]); i++; break;
						case "Weapons": tempSword = Integer.parseInt(inputArray[i +1]); i++; break;
						case "Armor": tempArmor = Integer.parseInt(inputArray[i + 1]); i++; break;
						}
					}
					for (Party p : tempCave.getAllParties()){
						for (Creature c : p.getMembers()){
							if (tempIndex == c.getIndex()){
								c.addJob(new Job(Integer.parseInt(inputArray[1]), inputArray[2], tempIndex, 
										Long.parseLong(inputArray[4]), tempWand, tempPotion, tempStone, tempScroll, tempSword, tempArmor, cName, c.getParty()));
								tempCave.addElement(c.getJob(Integer.parseInt(inputArray[1])));
								tempCave.addJob(c.getJob(Integer.parseInt(inputArray[1])));
							}
						}
					}
				}
			}
		}
		input.close();
		return tempCave;
	}

	//Method used to find the path for the file needed to load.  It returns this path
	//as a string
	public String loadFile(){
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
	}
	
	public void displayGame(Cave newCave){
		JFrame jobFrame = new JFrame();
		JPanel controlePanel = new JPanel();
		JPanel searchPanel = new JPanel();
		JPanel sortPanel = new JPanel();
		JButton exploreButton = new JButton("Explore");
		JButton loadCave = new JButton("Load Cave");
		JButton sortButton = new JButton("Sort Data");
		JScrollPane scrollerTA = new JScrollPane (textArea);
		JScrollPane scrollerJP = new JScrollPane(jobsPerforming);
		JScrollPane scrollerJW = new JScrollPane(jobsWaiting);
		JScrollPane scrollerAL = new JScrollPane(activityLog);
		
		tree = makeJTree(newCave);
		JScrollPane treeScroller = new JScrollPane(tree);
		
		comboBox.addItem("Index");
		comboBox.addItem("Name");
		comboBox.addItem("Type");

    	sortBox.addItem("Empathy");
    	sortBox.addItem("Fear");
    	sortBox.addItem("Carrying Capacity");
		
		textField.setText("Enter name, number or type here.");
		sortField.setText("Enter party or creature name here.");

        //Action listeners for JButtons
        exploreButton.addActionListener(new ActionListener(){
            @Override 
            public void actionPerformed(ActionEvent e){
            	search((String)comboBox.getSelectedItem(), textField.getText());
            }
        });

        loadCave.addActionListener(new ActionListener(){
            @Override 
            public void actionPerformed(ActionEvent e){
            	String newFile = loadFile();
            	Cave newCave = null;
            	try {
					newCave = loadData(newFile);
				} catch (IOException e1) {
				}
            	
            	mainCave = newCave;
        		newFile = "-----Main Cave-----\n"; 
        		newFile += mainCave.toString();
        		
        		textField.setText("");
        		textArea.setText(newFile);
    			return;
            }
        });
        
        sortButton.addActionListener(new ActionListener(){
            @Override 
            public void actionPerformed(ActionEvent e){
            	showSort();
            }
        });
        
        //Action Listeners for JRadioButtons
        partyJRB.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
            	sortBox.removeAllItems();
            	sortBox.addItem("Empathy");
            	sortBox.addItem("Fear");
            	sortBox.addItem("Carrying Capacity");
            }           
        });
        
        creatureJRB.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
            	sortBox.removeAllItems();
            	sortBox.addItem("Weight");
            	sortBox.addItem("Value");
            }           
        });
        
        jobFrame.add(scrollerJW, BorderLayout.NORTH);
        jobFrame.add(scrollerJP, BorderLayout.SOUTH);
		
        jobFrame.setTitle("Job Status");
        jobFrame.setLocation(1200, 0);
        jobFrame.setSize(600, 425);
        jobFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jobFrame.validate();
        jobFrame.setVisible(true);
        
        
        radioButtons.add(partyJRB);
        radioButtons.add(creatureJRB);
        
		searchPanel.add(loadCave, BorderLayout.PAGE_START);
		searchPanel.add(exploreButton, BorderLayout.EAST);
		searchPanel.add(textField, BorderLayout.CENTER);
		searchPanel.add(comboBox, BorderLayout.PAGE_END);
		
		sortPanel.add(partyJRB, BorderLayout.PAGE_START);
		sortPanel.add(creatureJRB, BorderLayout.EAST);
		sortPanel.add(sortButton, BorderLayout.CENTER);
		sortPanel.add(sortField, BorderLayout.WEST);
		sortPanel.add(sortBox, BorderLayout.PAGE_END);
		
		controlePanel.setLayout(new BorderLayout());
		
		controlePanel.add(searchPanel, BorderLayout.NORTH);
		controlePanel.add(sortPanel, BorderLayout.SOUTH);
		
		setTitle("Sorcerer's Cave Ben Sims");
		add(controlePanel, BorderLayout.SOUTH);
		add(scrollerTA, BorderLayout.WEST);
		add(treeScroller, BorderLayout.CENTER);
		add(scrollerAL, BorderLayout.NORTH);
		setLocation(1200, 425);
		setSize(600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		validate();
		setVisible(true);
		
		ArrayList<Job> jobList = mainCave.getAllJobs();
		
		updateActivityLog("Now that the parties are ready, the quest of the Sorcerer's Cave will begin!");
		
		jobList = doJobs(jobList);
		
		notifyUser("Resources will now be reorganized for jobs.");

		while (!moveForward){
			tempJF.requestFocus();
		}
		finishOtherJobs(jobList, mainCave.getAllParties());
	}
	
	//Method used to search for data
	public void search(String type, String target){
    	String tempString = "";
    	int tempIndex;
    	tempString = textField.getText().trim();
    	
    	if (tempString.equalsIgnoreCase("main")){
    		tempString = "-----Main Cave-----\n"; 
    		tempString += mainCave.toString();
    		
    		textField.setText("");
    		textArea.setText(tempString);
			return;
    	}
    	
    	if (type.equals("Index")){
    		if (isNumber(tempString)){
            	tempIndex = Integer.parseInt(textField.getText());
            	
            	for (GameElement el : mainCave.getAllElements()){
            		if (el.getIndex() == tempIndex){
            			textArea.append(el.toString());
            			textField.setText("");
            			return;
            		}
            	}
        	}
    	}
    	
    	if (type.equals("Name")){
    		try{
        		for (GameElement el : mainCave.getAllElements()){
        			if (el.getName().compareToIgnoreCase(tempString) == 0){
        				textArea.append(el.toString());
        				textField.setText("");
            			return;
        			}
        		}
    		}
    		catch (NullPointerException ex){
    			tempString = "Element did not exist.  Please try again.\n" +
    					"Or enter main to start again.";
        		textArea.append(tempString);
        		textField.setText("");
    		}
    	}
    	
    	if (type.equals("Type")){
    		if (tempString.equalsIgnoreCase("party") ||
    				tempString.equalsIgnoreCase("parties")){
    			for (Party p : mainCave.getAllParties()){
    				textArea.append(p.toString());
    			}
    		}
    		
    		if (tempString.equalsIgnoreCase("creature") ||
    				tempString.equalsIgnoreCase("creatures")){
    			for (GameElement e : mainCave.getAllElements()){
    				tempIndex = e.getIndex();
    				if (tempIndex < 30000 && tempIndex >= 20000){
    					textArea.append(e.toString());
    				}
    			}
    		}
    		
    		if (tempString.equalsIgnoreCase("treasure") ||
    				tempString.equalsIgnoreCase("treasures")){
    			for (GameElement e : mainCave.getAllElements()){
    				tempIndex = e.getIndex();
    				if (tempIndex < 40000 && tempIndex >= 30000){
    					textArea.append(e.toString());
    				}
    			}
    		}
    		
    		if (tempString.equalsIgnoreCase("artifact") ||
    				tempString.equalsIgnoreCase("artifacts")){
    			for (GameElement e : mainCave.getAllElements()){
    				tempIndex = e.getIndex();
    				if (tempIndex < 50000 && tempIndex >= 40000){
    					textArea.append(e.toString());
    				}
    			}
    		}
    	}
    	validate();
	}
	
	//Method used to sort selected material
	public void showSort(){
		String name = sortField.getText();
		Collection<Integer> list = null;
		
		if (partyJRB.isSelected()){
			for (Party p : mainCave.getAllParties()){
				if (p.getName().equalsIgnoreCase(name)){
					if (sortBox.getSelectedItem().equals("Empathy")){
						textArea.append(name + " members sorted by empathy: \n");
						list = p.getEmpathyTree().keySet();
					}
					
					if (sortBox.getSelectedItem().equals("Fear")){
						textArea.append(name + " members sorted by fear: \n");
						list = p.getFearTree().keySet();
					}
					
					if (sortBox.getSelectedItem().equals("Carrying Capacity")){
						textArea.append(name + " members sorted by carrying capacity: \n");
						list = p.getCCTree().keySet();
					}
					
					for (int c : list){
						textArea.append(mainCave.getElement(c).toString());
					}
				}
			}
		}
		
		if (creatureJRB.isSelected()){
			for (Creature c : mainCave.getAllCreatures()){
				if (c.getName().equalsIgnoreCase(name)){
					if (sortBox.getSelectedItem().equals("Weight")){
						textArea.append(name + "'s treasure sorted by weight: \n");
						list = c.getWeightTree().keySet();
						 
					}
					
					if (sortBox.getSelectedItem().equals("Value")){
						textArea.append(name + "'s treasure sorted by value: \n");
						list = c.getValueTree().keySet();
					}
				}
			}

			try{
				for (int c : list){
					textArea.append(mainCave.getElement(c).toString());
				}
			}
			catch (NullPointerException e){
				JFrame tempJFrame = new JFrame();
				tempJFrame.add(new JLabel("That was not a creature's name.  Please try again."));
				tempJFrame.setLocationRelativeTo(null);
				tempJFrame.pack();
				tempJFrame.validate();
				tempJFrame.setVisible(true);
				
			}
		}
		validate();
	}
	
	private JTree makeJTree(Cave cave){
		DefaultMutableTreeNode caveRoot = new DefaultMutableTreeNode("Main Cave");
		DefaultTreeModel treeModel = new DefaultTreeModel(caveRoot);
		JTree tree = new JTree(treeModel);
		boolean newItem = true;
		
		tree.setEditable(true);
		tree.getSelectionModel().setSelectionMode
		        (TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		tree.setEditable(true);
		
		for (Party p : cave.getAllParties()){
			treeModel.insertNodeInto(new DefaultMutableTreeNode(p.getName() + " " + p.getIndex()), caveRoot, caveRoot.getChildCount());
			for (Creature c : p.getMembers()){
				newItem = true;
				treeModel.insertNodeInto(new DefaultMutableTreeNode(c), 
						(MutableTreeNode) caveRoot.getLastChild(), caveRoot.getLastChild().getChildCount());
				for (Treasure t : c.getAllTreasure()){
					if (newItem == true){
						treeModel.insertNodeInto(new DefaultMutableTreeNode(t), caveRoot.getLastLeaf(), 0);
						newItem = false;
					}
					else{
						treeModel.insertNodeInto(new DefaultMutableTreeNode(t), 
								(MutableTreeNode) caveRoot.getLastLeaf().getParent(), 
								caveRoot.getLastLeaf().getParent().getChildCount());
					}
				}
				for (Artifact a : c.getAllArtifacts()){
					if (newItem == true){
						treeModel.insertNodeInto(new DefaultMutableTreeNode(a), caveRoot.getLastLeaf(), 0);
						newItem = false;
					}
					else{
						treeModel.insertNodeInto(new DefaultMutableTreeNode(a), 
								(MutableTreeNode) caveRoot.getLastLeaf().getParent(), 
								caveRoot.getLastLeaf().getParent().getChildCount());
					}
				}
				
				for (Job j : c.getAllJobs()){
					if (newItem == true){
						treeModel.insertNodeInto(new DefaultMutableTreeNode(j), caveRoot.getLastLeaf(), 0);
						newItem = false;
					}
					else{
						treeModel.insertNodeInto(new DefaultMutableTreeNode(j.toString()), 
								(MutableTreeNode) caveRoot.getLastLeaf().getParent(), 
								caveRoot.getLastLeaf().getParent().getChildCount());
					}
				}
			}
		}
		
		for (GameElement e : cave.getAllUnpartiedElements()){
			treeModel.insertNodeInto(new DefaultMutableTreeNode("Unpartied " + e), caveRoot, caveRoot.getChildCount());
		}
		
		return tree;
	}
	
	//Method used to perform and manage jobs
	public ArrayList<Job> doJobs(ArrayList<Job> jobs){
		ArrayList<Job> jobsWaitList = new ArrayList<Job>();
		int size = jobs.size();
		boolean run = false;
		int index;
		int position;
		
		intDisplayMatrix();
		displayMatrix();
		
		while (!jobs.isEmpty()){
			for (int i = 0; i < size; i++){
				if (!checkJob(jobs.get(i))){
					jobsWaitList.add(jobs.get(i));
					jobs.remove(i);
					size = jobs.size();
				}
			}
			for (int i = 0; i < size; i++){
				index = jobs.get(i).getCreature();
				if (jobQOn.isEmpty() && checkJob(jobs.get(i))){
					jobQOn.add(jobs.get(i));
					updateJobsPerforming(jobQOn);
					jobs.get(i).setLocationX(0);
					jobs.get(i).setLocationY(0);
					Thread t = new Thread(jobs.get(i));
					t.start();
					jobs.remove(i);
					size = jobs.size();
					updateJobsWaiting(jobs, jobsWaitList);
				}
				else{
					run = true;
					for (int j = 0; j < jobQOn.size(); j++){
						if (index == jobQOn.get(j).getCreature()){
							run = false;
						}
					}
					if (run && checkJob(jobs.get(i))){
						jobQOn.add(jobs.get(i));
						updateJobsPerforming(jobQOn);
						position = displayMatrix();
						jobs.get(i).setLocationX((position / 10) * 300);
						jobs.get(i).setLocationY((position % 10) * 100);
						Thread t = new Thread(jobs.get(i));
						t.start();
						jobs.remove(i);
						size = jobs.size();
						updateJobsWaiting(jobs, jobsWaitList);
					}
				}
			}
			for (int i = 0; i < jobQOn.size(); i++){
				if (jobQOn.get(i).getHalt()){
					setDisplayMatrix(jobQOn.get(i).getPos());
					jobQOn.remove(i);
					updateJobsPerforming(jobQOn);
				}
			}
		}
		size = jobQOn.size();
		while (size > 0){
			for (int i = 0; i < size; i++){
				if (jobQOn.get(i).getHalt()){
					setDisplayMatrix(jobQOn.get(i).getPos());
					jobQOn.remove(i);
					updateJobsPerforming(jobQOn);
					size = jobQOn.size();
				}
			}
		}
		
		return jobsWaitList;
	}
	
	private boolean checkJob(Job job){
		Creature tempCreature = (Creature) mainCave.getElement(job.getCreature());
		int[] tempCount = tempCreature.getResourceCount();
		if (job.getWand() <= tempCount[0] && job.getPotion() <= tempCount[1] && job.getStone() <= tempCount[2]
				&& job.getWeapon() <= tempCount[3]){
			return true;
		}
		return false;
	}
	
	private void finishOtherJobs(ArrayList<Job> jobs, ArrayList<Party> parties){
		int size = jobs.size();
		boolean run = false;
		int index;
		int position;
		
		intDisplayMatrix();
		displayMatrix();
		
		updateActivityLog("To continue the quest all members \nof each party pooled their resources to share.\n");
		
		for (Party p : parties){
			stripBumbs(p);
		}
		
		while (!jobs.isEmpty()){
			for (int i = 0; i < size; i++){
				if (shiftResources(jobs.get(i), mainCave.getParty(jobs.get(i).getParty()))){
					index = jobs.get(i).getParty();
					for (Party p : parties){
						if (index == p.getIndex()){
							shiftResources(jobs.get(i), p);
						}
					}
					index = jobs.get(i).getCreature();
					if (jobQOn.isEmpty() && checkJob(jobs.get(i))){
						jobQOn.add(jobs.get(i));
						updateJobsPerforming(jobQOn);
						jobs.get(i).setLocationX(0);
						jobs.get(i).setLocationY(0);
						Thread t = new Thread(jobs.get(i));
						t.start();
						jobs.remove(i);
						size = jobs.size();
						updateJobsWaiting(null, jobs);
					}
					else{
						run = true;
						for (int j = 0; j < jobQOn.size(); j++){
							if (index == jobQOn.get(j).getCreature()){
								run = false;
							}
						}
						if (run && checkJob(jobs.get(i))){
							jobQOn.add(jobs.get(i));
							updateJobsPerforming(jobQOn);
							position = displayMatrix();
							jobs.get(i).setLocationX((position / 10) * 300);
							jobs.get(i).setLocationY((position % 10) * 100);
							Thread t = new Thread(jobs.get(i));
							t.start();
							jobs.remove(i);
							size = jobs.size();
							updateJobsWaiting(null, jobs);
						}
					}
				}
			}
			for (int i = 0; i < jobQOn.size(); i++){
				if (jobQOn.get(i).getHalt()){
					setDisplayMatrix(jobQOn.get(i).getPos());
					stripWorker(jobQOn.get(i).getCreature(), mainCave.getParty(jobQOn.get(i).getParty()));
					jobQOn.remove(i);
					updateJobsPerforming(jobQOn);
				}
			}
		}
		size = jobQOn.size();
		while (size > 0){
			for (int i = 0; i < size; i++){
				if (jobQOn.get(i).getHalt()){
					setDisplayMatrix(jobQOn.get(i).getPos());
					stripWorker(jobQOn.get(i).getCreature(), mainCave.getParty(jobQOn.get(i).getParty()));
					jobQOn.remove(i);
					updateJobsPerforming(jobQOn);
					size = jobQOn.size();
				}
			}
		}
		
		updateActivityLog("All the jobs are complete!");
	}
	
	//Method used to see if the party has the resources needed to do a job
	private boolean shiftResources(Job job, Party p){
		Creature tempCreature = p.getCreature(job.getCreature());
		int[] partyResources = p.getPoolCount().clone();
		int number;
		
		if (partyResources[0] < job.getWand()){
			return false;
		}
		if (partyResources[1] < job.getPotion()){
			return false;
		}
		if (partyResources[2] < job.getStone()){
			return false;
		}
		if (partyResources[3] < job.getWeapon()){
			return false;
		}
		
		number = job.getWand();
		for (int i = 0; i <= number; i++){
			tempCreature.addArtifact(p.getResource("Wand"));
		}
		updateActivityLog(tempCreature.getName() + " is given " + number + " wand(s) ");
		number = job.getPotion();
		for (int i = 0; i <= number; i++){
			tempCreature.addArtifact(p.getResource("Potion"));
		}
		updateActivityLog(number + " potion(s) ");
		number = job.getStone();
		for (int i = 0; i <= number; i++){
			tempCreature.addArtifact(p.getResource("Stone"));
		}
		updateActivityLog(number + " stone(s) ");
		number = job.getWeapon();
		for (int i = 0; i <= number; i++){
			tempCreature.addArtifact(p.getResource("Weapon"));
		}
		updateActivityLog("and " + number + " weapon(s)\n");
		return true;
	}
	
	//Method used to take Artifacts from Creatures
	private boolean stripWorker(int index, Party p){
		int size = p.getCreature(index).getAllArtifacts().size();
		Creature tempCreature = p.getCreature(index);
		for (int i = 0; i < size;){
			p.addResource(tempCreature.getAllArtifacts().get(i));
			tempCreature.removeArtifact(tempCreature.getAllArtifacts().get(i));
			size--;
		}
		updateActivityLog(p.getCreature(index).getName() + " gives back all artifacts.\n");
		return true; 
	}
	
	//Method used to pull all artifacts in a praty to one pool
	private boolean stripBumbs(Party p){
		int size;
		for (Creature c : p.getMembers()){
			size = c.getAllArtifacts().size();
			for (int i = 0; i < size;){
				p.addResource(c.getAllArtifacts().get(i));
				c.removeArtifact(c.getAllArtifacts().get(i));
				size--;
			}
		}
		return true;
	}
	
	//Method used to update the performing jobs display
	private void updateJobsPerforming(ArrayList<Job> jobsOnQ){
		String tempString = "Jobs being performed: \n";
		
		for (Job j : jobsOnQ){
			tempString += j.toString() + "\n";
		}
		
		jobsPerforming.setText(tempString);
		
	}
	
	//Method used to update the jobs waiting list.
	private void updateJobsWaiting(ArrayList<Job> waitingList, ArrayList<Job> waitingListNR){
		String tempString = "Jobs Waiting: \n";
		
		if (waitingList != null){
			for (Job j : waitingList){
				tempString += j.getName() + "\n";
			}
		}
		
		tempString += "Jobs needing resources: \n";
		
		for (Job j : waitingListNR){
			tempString += j.getName() + " " + j.resourcesNeeded() + "\n";
		}
		
		jobsWaiting.setText(tempString);
	}
	
	//Method used to update the activity log
	private void updateActivityLog(String message){
		activityLog.append(message);
	}
	
	//Method used to reset displayMatrix
	private static void setDisplayMatrix(int position){
		int x = position / 10;
		int y = position % 10;
		
		displayMatrix[x][y] = false;

		
	}
	
	//Method used to find empty spot for display
	private static int displayMatrix(){
		
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 10; j++){
				if (!displayMatrix[i][j]){
					displayMatrix[i][j] = true;
					return (i * 10) + j;
				}
				
			}
		}
		return 0;
	}
	
	//Method used to initialize displayMatrix
	private static void intDisplayMatrix(){
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 10; j++){
				displayMatrix[i][j] = false;
			}
		}
	}
	
	//Method used to check number
	public boolean isNumber(String text){
		try {
			Integer.parseInt(text);
		}
		catch(NumberFormatException e){
			return false;
		}
		
		return true;
	}
	
	//Method used to notify users
	private void notifyUser(String message){
		JLabel notification = new JLabel(message);
		JButton ok = new JButton("Ok");
		
		ok.addActionListener(new ActionListener(){
            @Override 
            public void actionPerformed(final ActionEvent e){
            	moveForward = true;
            	tempJF.dispose();
            }
        });
		
		tempJF.add(notification, BorderLayout.CENTER);
		tempJF.add(ok, BorderLayout.SOUTH);
		
		tempJF.setSize(400, 100);
		tempJF.setLocationRelativeTo(null);
		tempJF.validate();
		tempJF.setVisible(true);
	}
}

//All classes branch out from this class.  This is to make every class
//the same type of class.
class GameElement {
	private int index;
	private String name;
	
	public GameElement(){
		
	}
	
	//Setter method
	public void setIndex(int newIndex){
		index = newIndex;
	}
	
	public void setName(String newName){
		name = newName;
	}
	
	//Getter method
	public int getIndex(){
		return index;
	}
	
	public String getName(){
		return name;
	}
	
	//Sort Method found here: http://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
	//Modified to fit project needs.
	public LinkedHashMap<Integer, Integer> sortHashMapByValuesD(Map<Integer, Integer> creatureFear) {
	   List<Integer> mapKeys = new ArrayList<Integer>(creatureFear.keySet());
	   List<Integer> mapValues = new ArrayList<Integer>(creatureFear.values());
	   Collections.sort(mapValues);
	   Collections.sort(mapKeys);

	   LinkedHashMap<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();

	   Iterator<Integer> valueIt = mapValues.iterator();
	   while (valueIt.hasNext()) {
	       Object val = valueIt.next();
	       Iterator<Integer> keyIt = mapKeys.iterator();

	       while (keyIt.hasNext()) {
	           Object key = keyIt.next();
	           String comp1 = creatureFear.get(key).toString();
	           String comp2 = val.toString();

	           if (comp1.equals(comp2)){
	               creatureFear.remove(key);
	               mapKeys.remove(key);
	               sortedMap.put((Integer)key, (Integer)val);
	               break;
	           }

	       }

	   }
	   return sortedMap;
	}
}

class Cave extends GameElement{
	
	private ArrayList<Party> parties = new ArrayList<Party>();
	private ArrayList<GameElement> unpartiedElement = new ArrayList<GameElement>();
	private ArrayList<GameElement> allElements = new ArrayList<GameElement>();
	private ArrayList<Job> allJobs = new ArrayList<Job>();
	
	public Cave(){
	}
	
	//Adder methods
	public void addParty(Party newParty){
		parties.add(newParty);
	}
	
	public void addUnpartiedElement(GameElement t){
		this.unpartiedElement.add(t);
	}
	
	public void addElement(GameElement i){
		allElements.add(i);
	}
	
	public void addJob(Job job){
		allJobs.add(job);
	}
	
	//Getter methods
	public Party getParty(int index){
		for (Party p : parties){
			if (p.getIndex() == index){
				return p;
			}
		}
		return null;
	}
	
	public ArrayList<Creature> getAllCreatures(){
		ArrayList<Creature> list = new ArrayList<Creature>();
		
		for (GameElement e : this.getAllElements()){
			if (e.getIndex() / 10000 == 2){
				list.add((Creature)e);
			}
		}
		return list;
	}
	
	public ArrayList<GameElement> getAllUnpartiedElements(){
		return unpartiedElement;
	}
	
	public GameElement getUnpartiedElement(int index){
		for (GameElement e : unpartiedElement){
			if (e.getIndex() == index){
				return e;
			}
		}
		return null;
	}
	
	public ArrayList<Party> getAllParties(){
		return parties;
	}
	
	public GameElement getElement(int index){
		for (GameElement e : allElements){
			if (e.getIndex() == index){
				return e;
			}
		}
		return null;
	}
	
	public ArrayList<GameElement> getAllElements(){
		return allElements;
	}
	
	public ArrayList<Job> getAllJobs(){
		return allJobs;
	}
	
	//ToString method
	public String toString(){
		String tempString = "-----Parties-----\n";
		
		for (Party p : parties){
			tempString += p.getName() + ", Index: " + p.getIndex() + "\n";
			for (Creature c : p.getMembers()){
				tempString += "    Creature: " + c.getName() + "\n";
				for (Treasure t : c.getAllTreasure()){
					tempString += "        Treasure: " + t.getType() + "\n";
				}
				for (Artifact a : c.getAllArtifacts()){
					tempString += "        Artifact: " + a.getType() + "\n";
				}
			}
		}
		tempString += "\n";
		
		tempString += "-----UnpartiedElement(s)-----\n"; 
		
		for (GameElement e : unpartiedElement){
			tempString += e.toString() + "\n";
			tempString += "\n";
		}
		return tempString;
	}
}

class Party extends Cave{
	
	
	private ArrayList<Creature> members = new ArrayList<Creature>();
	private ArrayList<Artifact> resourcePool = new ArrayList<Artifact>();
	private int[] poolCount = new int[4];
	private Map<Integer, Integer> creatureEmpathy = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> creatureFear = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> creatureCC = new HashMap<Integer, Integer>();
	
	public Party(){
		
	}
	
	public Party(int newIndex, String newName){
		super.setName(newName);
		super.setIndex(newIndex);
	}
	//Getter methods
	
	public ArrayList<Creature> getMembers(){
		return members;
	}
	
	public Creature getCreature(int index){
		for (Creature c : members){
			if (c.getIndex() == index){
				return c;
			}
		}
		return null;
	}
	
	public Map<Integer, Integer> getEmpathyTree(){
		creatureEmpathy = this.sortHashMapByValuesD(creatureEmpathy);
		return creatureEmpathy;
	}
	
	public Map<Integer, Integer> getFearTree(){
		creatureFear = this.sortHashMapByValuesD(creatureFear);
		return creatureFear;
	}
	
	public Map<Integer, Integer> getCCTree(){
		creatureCC = this.sortHashMapByValuesD(creatureCC);
		return creatureCC;
	}
	
	public Artifact getResource(String type){
		for (Artifact a : this.resourcePool){
			if (a.getType().compareTo(type) == 0){
				switch (type){
				case "Wand": poolCount[0]--; break;
				case "Potion": poolCount[1]--; break;
				case "Stone": poolCount[2]--; break;
				case "Weapon": poolCount[3]--; break;
				}
				
				return a;
			}
		}
		return null;
	}
	
	public int[] getPoolCount(){
		return poolCount;
	}
	
	//Adder method
	public void addCreature(Creature newCreature){
		members.add(newCreature);
		creatureEmpathy.put(newCreature.getIndex(), newCreature.getEmpathy());
		creatureFear.put(newCreature.getIndex(), newCreature.getFear());
		creatureCC.put(newCreature.getIndex(), newCreature.getCarryingCapacity());
	}
	
	public void addResource(Artifact a){
		String tempType = a.getType();
		resourcePool.add(a);
		
		switch (tempType){
		case "Wand": poolCount[0]++; break;
		case "Potion": poolCount[1]++; break;
		case "Stone": poolCount[2]++; break;
		case "Weapon": poolCount[3]++; break;
		}
	}
	
	//ToString method
	public String toString(){
		String tempString = "-*-*-" + this.getName() + " " + this.getIndex() + "-*-*-\n";
		
		for (Creature c : members){
			tempString += c.getName() + " " + c.getIndex() + "\n";
		}
		
		tempString += "\n";
		return tempString;
	}
}

class Creature extends Party{
	
	private String type, ownerName;
	private int party, empathy, fear, carryingCapacity;
	private int[] resourceCount = new int[4]; 
	private ArrayList<Treasure> treasures = new ArrayList<Treasure>();
	private ArrayList<Artifact> artifacts = new ArrayList<Artifact>();
	private Map<Integer, Job> jobs = new HashMap<Integer, Job>();
	private Map<Integer, Integer> itemWeight = new TreeMap<Integer, Integer>();
	private Map<Integer, Integer> itemValue = new TreeMap<Integer, Integer>();
	
	public Creature(){
		
	}

	public Creature( int newIndex, String newType, String newName, int newParty,
			int newEmpathy, int newFear, int newCarryingCapacity, String newOwnerName){
		super.setIndex(newIndex);
		this.type = newType;
		super.setName(newName);
		this.party = newParty;
		this.empathy = newEmpathy;
		this.fear = newFear;
		this.carryingCapacity = newCarryingCapacity;
		this.ownerName = newOwnerName;
	}
	
	//Setter methods
	public void setType(String newType){
		type = newType;
	}
	
	public void setParty(int newParty){
		party = newParty;
	}
	
	public void setEmpathy(int newEmpathy){
		empathy = newEmpathy;
	}
	
	public void setFear(int newFear){
		fear = newFear;
	}
	
	public void setCarryingCapacity(int newCarryingCapacity){
		carryingCapacity = newCarryingCapacity;
	}
	
	public void setOwnerName(String newOwnerName){
		ownerName = newOwnerName;
	}
	
	//Getter methods 
	public String getType(){
		return type;
	}
	
	public int getParty(){
		return party;
	}
	
	public int getEmpathy(){
		return empathy;
	}
	
	public int getFear(){
		return fear;
	}
	
	public int getCarryingCapacity(){
		return carryingCapacity;
	}
	
	public String getOwnerName(){
		return ownerName;
	}
	
	public Treasure getTreasure(int index){
		for (Treasure t : treasures){
			if (t.getIndex() == index){
				return t;
			}
		}
		return null;
	}
	
	public Artifact getArtifact(int index){
		for (Artifact a : artifacts){
			if (a.getIndex() == index){
				return a;
			}
		}
		return null;
	}
	
	public ArrayList<Treasure> getAllTreasure(){
		return treasures;
	}
	
	public ArrayList<Artifact> getAllArtifacts(){
		return artifacts;
	}
	
	public Map<Integer, Integer> getWeightTree(){
		itemWeight = this.sortHashMapByValuesD(itemWeight);
		return itemWeight;
	}
	
	public Map<Integer, Integer> getValueTree(){
		itemValue = this.sortHashMapByValuesD(itemValue);
		return itemValue;
	}
	
	public Job getJob(int index){
		return jobs.get(index);
	}
	
	public Collection<Job> getJobs(){
		Collection<Job> tempList = new ArrayList<Job>();
		return tempList = jobs.values();
	}
	
	public int[] getResourceCount(){
		return resourceCount;
	}
	
	//this method removes an artifact
	public void removeArtifact(Artifact a){
		this.getAllArtifacts().remove(a);
		switch (a.getType()){
		case "Wand": resourceCount[0]--; break;
		case "Potion": resourceCount[1]--; break;
		case "Stone": resourceCount[2]--; break;
		case "Weapon": resourceCount[3]--; break;
		}
	}
	
	//Adder methods
	public void addTreasure(Treasure newTreasure){
		treasures.add(newTreasure);
		itemWeight.put(newTreasure.getIndex(), newTreasure.getWeight());
		itemValue.put(newTreasure.getIndex(), newTreasure.getValue());
	}
	
	public void addArtifact(Artifact newArtifact){
		String tempType = newArtifact.getType();
		artifacts.add(newArtifact);
		
		switch (tempType){
		case "Wand": resourceCount[0]++; break;
		case "Potion": resourceCount[1]++; break;
		case "Stone": resourceCount[2]++; break;
		case "Weapon": resourceCount[3]++; break;
		}
	}
	
	public void addJob(Job job){
		jobs.put(job.getIndex(), job);
	}
	
	//method used to perform jobs
	public void performJob(){
		for (Job j : this.getJobs()){
			Thread t = new Thread(j);
			t.start();
			while(t.isAlive()){
			}
		}
	}
	//ToString mehtod
	public String toString(){
		String tempString = "*** " + super.getName() + " " + super.getIndex() + " *** \n";
		
		tempString += "Type: " + this.getType() + " \n";
		if (this.getOwnerName().compareTo("") == 0){
			tempString += "Party: None \n";
		}
		else{
			tempString += "Party: " + this.getOwnerName() + " \n";
		}
		tempString += "Empathy: " + this.getEmpathy() + " \n";
		tempString += "Fear: " + this.getFear() + " \n";
		tempString += "Carrying Capacity: " + this.getCarryingCapacity() + " \n";
		tempString += "Items: ";
		if (!artifacts.isEmpty()){
			for (Artifact a : artifacts){
				tempString += "A:" + a.getType() + " " + a.getIndex() + ", \n";
			}
		}
		else if (!treasures.isEmpty()){
			for (Treasure t : treasures){
				tempString += "T:" + t.getType() + " " + t.getIndex() + ", \n";
			}
		}
		else{
			tempString += "none \n";
		}

		tempString += " \n";
		
		return tempString;
	}
}


class Treasure extends Creature{
	
	private int creature;
	private int weight;
	private int value;

	public Treasure(int newIndex, String newType, int newCreature, int newWeight,
			int newValue, String newOwnerName){
		super.setIndex(newIndex);
		super.setType(newType);
		this.creature = newCreature;
		this.weight = newWeight;
		this.value = newValue;
		super.setOwnerName(newOwnerName);
		
	}
	
	//Setter methods
	public void setCreature(int newCreature){
		creature = newCreature;
	}
	
	//Getter methods
	public int getCreature(){
		return creature;
	}
	
	public int getWeight(){
		return weight;
	}
	
	public int getValue(){
		return value;
	}
	
	//ToString method
	public String toString(){
		String tempString = "-T- " + super.getType() + " " + super.getIndex() + " -T-\n";
		
		tempString += "Weight: " + this.getWeight() + "\n";
		tempString += "Value: " + this.getValue() + "\n";
		if (this.getOwnerName().equals("")){
			tempString += "No owner\n";
		}
		else{
			tempString += "Owner: " + super.getOwnerName() + " " +  + this.getCreature() + "\n";
		}

		tempString += "\n";
		return tempString;
	}
}

class Artifact extends Creature{
	
	private int creature;
	
	public Artifact(int newIndex, String newType, int newCreature, String newName, String newOwnerName){
		super.setIndex(newIndex);
		super.setType(newType);
		this.creature = newCreature;
		super.setName(newName);
		super.setOwnerName(newOwnerName);
	}
	
	//Setter method
	public void setCreature(int newCreature){
		creature = newCreature;
	}
	
	//Getter method
	public int getCreature(){
		return creature;
	}
	
	//ToString method
	public String toString(){
		String tempString = "-A- " + super.getType() + " " + super.getIndex() + " -A-\n";
		
		if (super.getOwnerName().compareTo("") == 0){
			tempString += "No Owner\n";
		}
		else{
			tempString += "Owner: " + super.getOwnerName() + " " + this.getCreature() +  "\n";	
		}
		
		if (!super.getName().equals("")){
			tempString += "Name: " + super.getName() + "\n";
		}
		
		tempString += "\n";
		
		return tempString;
	}
}

class Job extends Creature implements Runnable{
	
	private int creature, wand, potion, stone, scroll, weapon, armor, locationX, locationY;
	private String creatureName;
	private long time, startTime, stopTime, jobTime;
	private double duration;
	private volatile boolean running = true;
	private volatile boolean halt = false;
	private volatile JProgressBar pm = new JProgressBar ();
	
	public Job(int index, String name, int creature, long jobTime, int wand, int potion, int stone,
			int scroll, int weapon, int armor, String creatureName, int party){
		this.setIndex(index);
		this.setName(name);
		this.jobTime = jobTime;
		this.creature = creature;
		this.wand = wand;
		this.potion = potion;
		this.stone = stone;
		this.scroll = scroll;
		this.weapon = weapon;
		this.armor = armor;
		this.creatureName = creatureName;
		super.setParty(party);
	}
	//Setter Methods
	public void setLocationY(int y){
		locationY = y;
	}
	
	public void setLocationX(int x){
		locationX = x;
	}
	
	//Getter Methods
	public long getTime(){
		return jobTime;
	}

	public int getCreature() {
		return creature;
	}
	
	public int getWand() {
		return wand;
	}
	
	public int getPotion() {
		return potion;
	}
	
	public int getStone() {
		return stone;
	}
	
	public int getScroll() {
		return scroll;
	}
	
	public int getWeapon() {
		return weapon;
	}
	
	public int getArmor() {

		return armor;
	}
	
	public boolean getHalt(){
		return halt;
	}
	
	public int getPos(){
		return (locationX / 30) + (locationY / 100);
	}
	
	//ToString Method
	public String toString(){
		String tempString = "-J-" + this.getName() + "-J- ";
		tempString += "Perfromed by: " + creatureName;
		return tempString;
	}
	
	//ResourcesNeeded display
	public String resourcesNeeded(){
		String tempString = "Resources Required: ";
		tempString += "Wand(s): " + this.getWand() + ", Potion(s): " + this.getPotion() + 
				", Stone(s): " + this.getStone() + ", Weapon(s): " + this.getWeapon();
		
		return tempString;
	}
	
	//This run method was adapted from code written by Dr.Nicholas Duchon
	//the original code can be found in the project description
	@Override
	public void run () {
		pm.setStringPainted (true);
		JFrame jf = new JFrame (this.getName());
		jf.add (new JLabel (this.toString()),
		BorderLayout.SOUTH);
		JButton suspend = new JButton("Suspend");
		JButton cancel = new JButton("Cancel");
		suspend.addActionListener(new ActionListener(){
            @Override 
            public void actionPerformed(ActionEvent e){
            	suspend();
            }
        });
		cancel.addActionListener(new ActionListener(){
            @Override 
            public void actionPerformed(ActionEvent e){
            	stop();
            }
        });
		jf.add (pm, BorderLayout.NORTH);
		jf.add(suspend, BorderLayout.WEST);
		jf.add(cancel, BorderLayout.EAST);
		jf.setSize(300, 100);
		jf.setVisible (true);
		jf.setLocation(locationX, locationY);
		time = System.currentTimeMillis();
		startTime = time;
		stopTime = time + 1000 * jobTime;
		duration = stopTime - time;
		while (time < stopTime) {
			try {
			Thread.sleep (100);
			} catch (InterruptedException e) {}
			//System.out.printf ("Running, time: %.2f\n", (time -
			//startTime)/1000.0);
		pm.setValue((int)(((time - startTime) / duration) *
			100));
			time = System.currentTimeMillis ();
			while (!running){
				Thread.yield();
				if (halt){
					jf.dispose();
					return;
				}
			}
			if (halt){
				jf.dispose();
				return;
			}
		} // end runninig
		pm.setValue (100);
		halt = true;
		jf.dispose();
		} // end method run - implements runnable
	
	//Method used to pause and start thread
	private void suspend(){
		if (running){
			running = false;
		}
		else{
			startTime += System.currentTimeMillis() - time;
			stopTime += System.currentTimeMillis() - time;
			time = System.currentTimeMillis();
			duration = stopTime - startTime;
			running = true;
		}
	}
	
	//Method used to kill thread
	private void stop(){
		halt = true;
	}
}
