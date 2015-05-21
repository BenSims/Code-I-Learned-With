/* Author: Ben Sims
 * Date: 10 May 15
 * A small project to demonstrate my understanding of computer science.
 * This program takes an input .txt file that has to be formatted in a special
 * way, and builds objects based on that.  There is one abstract class, and
 * several classes that extend it. Concurrent programming is later used with
 * the jobs, and a JTree is used to display data.  More fetures are to come.
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

public class ComputerScienceCave{
	
	public static void main(String[] args){
		new ComputerScienceCave();
	}//end main()

	//Start by finding a file path, then making a cave based on that file, then
	//use that cave to make the display 
	public ComputerScienceCave(){
		String filePath = "";
		Cave mainCave = null;

		try {
			filePath = this.findFilePath();
			mainCave = loadData(filePath);
		} 
		catch (IOException e) {
			System.out.println("File not found.  Please try again.");
		}
		catch (NullPointerException e){
			System.exit(0);
		}
		JTree tree = makeJTree(mainCave);
		
		displayData(mainCave, tree);
	}//end SorerersCave constructor

	//Method used to find the path for the file needed to load.  It returns this
	//path as a string
	public String findFilePath(){
		//Create needed elements
		JFrame window = new JFrame();
		String fileName = null;
		JFileChooser chooser = new JFileChooser();
		File workingDirectory = new File(System.getProperty("user.dir"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES",
				"txt", "text");
		
		//Apply needed elements 
		chooser.setCurrentDirectory(workingDirectory);
		chooser.setFileFilter(filter);
		
		//Find file and set "fileName" to equal its path.
		int returnVal = chooser.showOpenDialog(window);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
		   fileName = chooser.getSelectedFile().getPath();
		}
		
		//Remove "chooser" and return "fileName"
		window.dispose();
		return fileName;
	}//end findFilePath()

	//Method parses a database into a populated Cave object.
	public Cave loadData(String fileName) throws FileNotFoundException, 
            UnsupportedEncodingException, IOException{
        String tempInput = "";
        Scanner tempScanner = null;
		Cave tempCave = new Cave();
        
        java.io.File lists = new java.io.File(fileName);
        Scanner input = new Scanner(lists);
        
        while(input.hasNext()){
        	tempInput = input.nextLine().trim();
        	if (tempInput.length() == 0) continue;
        	tempScanner = new Scanner(tempInput).useDelimiter("\\s*:\\s*");
        	switch (tempInput.charAt (0)) {
        	case 'p': makeParty(tempScanner, tempCave); break;
            case 'c': makeCreature(tempScanner, tempCave); break;
            case 't': makeTreasure(tempScanner, tempCave); break;
            case 'a': makeArtifact(tempScanner, tempCave); break;
            case 'j': makeJob(tempScanner, tempCave); break;
            }
        }
        input.close();
        tempScanner.close();
		return tempCave;
	}//end loadData()
	
	private void makeParty(Scanner input, Cave tempCave){
		Party p = new Party(input);
		tempCave.addGameElement(p.getIndex(), p);
	}//end makeParty()
	
	private void makeCreature(Scanner input, Cave tempCave){
		Creature c = new Creature(input);
		tempCave.addGameElement(c.getIndex(), c);
		tempCave.getParty(c.getOwner()).addMember(c.getIndex(), c);
	}//end makeCreature()
	
	private void makeTreasure(Scanner input, Cave tempCave){
		Treasure t = new Treasure(input);
		if (t.getOwner() != 0){
			tempCave.addGameElement(t.getIndex(), t);
			tempCave.getCreature(t.getOwner()).addTreasure(t.getIndex(), t);
		}
		else{
			tempCave.addGameElement(t.getIndex(), t);
			tempCave.addUnpartiedElement(t.getIndex(), t);
		}
	}//end makeTreasure()
	
	private void makeArtifact(Scanner input, Cave tempCave){
		Artifact a = new Artifact(input);
		if (a.getOwner() != 0){
			tempCave.addGameElement(a.getIndex(), a);
			tempCave.getCreature(a.getOwner()).addArtifact(a.getIndex(), a);
		}
		else{
			tempCave.addGameElement(a.getIndex(), a);
			tempCave.addUnpartiedElement(a.getIndex(), a);
		}
	}//end makeArtifact()
	
	private void makeJob(Scanner input, Cave tempCave){
		Job j = new Job(input);
		tempCave.getCreature(j.getOwner()).addJob(j.getIndex(), j);
		tempCave.addGameElement(j.getIndex(), j);
		tempCave.addJob(j);
	}//end makeJob()
	
	//Method used to make a JTree to display all data. As each party is added
	//all of the creatures belonging to it are added. And as each creature is
	//added all of their belongings are added to it. 
	private JTree makeJTree(Cave cave){
		DefaultMutableTreeNode caveRoot = new DefaultMutableTreeNode("Main Cave");
		DefaultTreeModel treeModel = new DefaultTreeModel(caveRoot);
		JTree tree = new JTree(treeModel);
		boolean newItem = true;//used to ensure creatures get the right items
		
		tree.setEditable(true);
		tree.getSelectionModel().setSelectionMode
		        (TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		
		for (Party p : cave.getAllParties()){
			treeModel.insertNodeInto(new DefaultMutableTreeNode(p.toString()), caveRoot, caveRoot.getChildCount());
			for (Creature c : p.members.values()){
				newItem = true;
				treeModel.insertNodeInto(new DefaultMutableTreeNode(c), 
						(MutableTreeNode) caveRoot.getLastChild(), caveRoot.getLastChild().getChildCount());
				for (Treasure t : c.treasures.values()){
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
				for (Artifact a : c.artifacts.values()){
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
				
				for (Job j : c.jobs.values()){
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
		
		for (GameElement e : cave.unpartiedElements.values()){
			treeModel.insertNodeInto(new DefaultMutableTreeNode("Unpartied " + e), caveRoot, caveRoot.getChildCount());
		}
		
		return tree;
	}//end makeJTree()
	
	
	private void displayData(Cave cave, JTree tree){
		JFrame mainFrame = new JFrame();
		JPanel searchPanel = new JPanel();
		JTextArea dataTArea = new JTextArea(10, 20);
		JTextField searchField = new JTextField(17);
		JButton searchButton = new JButton("Search");
		JScrollPane treeScroller = new JScrollPane(tree);
		JScrollPane dataScroller = new JScrollPane(dataTArea);
		
		searchPanel.add(searchField);
		searchPanel.add(searchButton);
		
		searchField.setText("Enter index, or name here");
		searchField.addActionListener(e -> search(searchField.getText(), dataTArea, cave));
		searchField.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if (e.getSource().equals(searchField)){
					searchField.setText("");
				}
			}
		});
		
		searchButton.addActionListener(e -> search(searchField.getText(), dataTArea, cave));
		
		mainFrame.add(searchPanel, BorderLayout.NORTH);
		mainFrame.add(treeScroller, BorderLayout.CENTER);
		mainFrame.add(dataScroller, BorderLayout.SOUTH);
		
		mainFrame.setLocation(800, 0);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setTitle("Computer Science Cave");
		mainFrame.setSize(700, 800);
		mainFrame.setVisible(true);
	}//end displayData()
	
	//Method uses parsing concepts for user-friendly interface. First input
	//is checked if it is an index number, after it is check by name an type.
	private void search(String target, JTextArea dataTArea, Cave cave){
		String data = "";
		if (isNumber(target)){
			int index = Integer.parseInt(target);
			if (isIndex(index)){
				try{
					switch (index / 10000){
					case 1: data = partyInfo(cave.getParty(index)); break;
					case 2: data = creatureInfo(cave.getCreature(index)); break;
					case 3: data = treasureInfo(cave.getTreasure(index)); break;
					case 4: data = artifactInfo(cave.getArtifact(index)); break;
					case 5: data = jobInfo(cave.getJob(index)); break;
					}
				}
				catch (NullPointerException e){
				}
			}
		}
		if (data.isEmpty()){
			for (GameElement e: cave.allGameElements.values()){
				if (e.getName().compareToIgnoreCase(target) == 0 || 
						e.getType().compareToIgnoreCase(target) == 0){
					switch (e.getIndex() / 10000){
					case 1: data = partyInfo(cave.getParty(e.getIndex())); break;
					case 2: data = creatureInfo(cave.getCreature(e.getIndex())); break;
					case 3: data = treasureInfo(cave.getTreasure(e.getIndex())); break;
					case 4: data = artifactInfo(cave.getArtifact(e.getIndex())); break;
					case 5: data = jobInfo(cave.getJob(e.getIndex())); break;
					}
				}
			}
		}
		//if nothing could be found this is stated.
		if (data.isEmpty()){
			data = target.trim() + " could not be found.";
		}
		data += "\n\n";
		dataTArea.append(data);
	}//end search()
	
	private boolean isNumber(String index){
		try{
			Integer.parseInt(index);
		}
		catch(NumberFormatException e){
			return false;
		}
		return true;
	}//end isNumber()
	
	private boolean isIndex(int index){
		int value = index / 10000;
		if (0 < value && value < 6){
			return true;
		}
		return false;
	}//end isIndex()
	
	private String partyInfo(Party party){
		String data = "Party: " + party.getName() + "\n" + "Members: ";
		for (Creature c: party.members.values()){
			data += c.getName() + ", ";
		}
		
		return data;
	}//end partyInfo()
	
	private String creatureInfo(Creature c){
		String data = "Creature: " + c.getName() + "\nStats:\n" + c.stats();
		data += "\nItems\n";
		for (Treasure t: c.treasures.values()){
			data += t.toString() + ", ";
		}
		for (Artifact a: c.artifacts.values()){
			data += a.toString() + ", ";
		}
		return data;
	}//end creatureInfo()
	
	private String treasureInfo(Treasure t){
		String data = "Treasure: " + t.getType() + "\nStats\n";
		data += "Value: " + t.getValue() + " Weight: " + t.getWeight();
		return data;
	}//end treasureInfo()
	
	private String artifactInfo(Artifact a){
		String data = "Artifact: " + a.getType() + " \"" + a.getName() + "\" ";
		return data;
	}//end artifactInfo()
	
	private String jobInfo(Job j){
		String data = "Job: " + j.getName() + " Time Needed: " + j.getTime();
		data += "\n" + j.resourcesNeeded();
		return data;
	}//end jobInfo()
}//end class SorerersCave

abstract class GameElement{
	private int index, owner;
	private String name, type;
	
	public void setIndex(int index){
		this.index = index;
	}//end setIndex()
	
	public void setOwner(int index){
		this.owner = index;
	}//end setOwner()
	
	public void setName(String name){
		this.name = name;
	}//end setName()
	
	public void setType(String type){
		this.type = type;
	}//end setType()
	
	public int getIndex(){
		return index;
	}//end getIndex
	
	public int getOwner(){
		return owner;
	}//end getOwner()
	
	public String getName(){
		return name;
	}//end getName()
	
	public String getType(){
		return type;
	}//end getType()
	
	//Method returns object type as a string
	public String getObjectType(){
		String tempString = this.getClass() + "";
		tempString = tempString.substring(6);
		
		return tempString;
	}//end getObjectType
}//end class GameElement

class Cave extends GameElement{
	protected HashMap<Integer, GameElement> allGameElements = new HashMap<Integer, GameElement>();
	protected HashMap<Integer, GameElement> unpartiedElements = new HashMap<Integer, GameElement>();
	private HashMap<Integer, Job> jobs = new HashMap<Integer, Job>();
	
	public Cave(){}
	
	public void addGameElement(int index, GameElement e){
		allGameElements.put(index, e);
	}//end addGameEmlement()
	
	public void addUnpartiedElement(int index, GameElement e){
		unpartiedElements.put(index, e);
	}//end addUnpartiedGameElement()
	
	public void addJob(Job job){
		jobs.put(job.getIndex(), job);
	}//end addJob()
	
	public Party getParty(int index){
		//if statement ensures index is correct
		if (index / 10000 != 1){
			return null;
		}
		return (Party)allGameElements.get(index);
	}//end getParty()
	
	public Creature getCreature(int index){
		//if statement ensures index is correct
		if (index / 20000 != 1){
			return null;
		}
		return (Creature)allGameElements.get(index);
	}//end getCreature
	
	public Treasure getTreasure(int index){
		//if statement ensures index is correct
		if (index / 30000 != 1){
			return null;
		}
		return (Treasure)allGameElements.get(index);
	}//end getTreasure()
	
	public Artifact getArtifact(int index){
		//if statement ensures index is correct
		if (index / 30000 != 1){
			return null;
		}
		return (Artifact)allGameElements.get(index);
	}//end getTreasure()
	
	public GameElement getUnpartiedGameElement(int index){
		return unpartiedElements.get(index);
	}//end getUnpartiedGameElement()
	
	public ArrayList<Party> getAllParties(){
		ArrayList<Party> list = new ArrayList<Party>();
		allGameElements.values().stream().filter(p -> p.getIndex() / 10000 == 1)
			.forEach(p -> list.add((Party)p));
		return list;
	}//end getAllParties()
	
	public Job getJob(int index){
		return jobs.get(index);
	}//end getJobs
	
	public String toString(){
		String tempString = "";
		for (GameElement e: allGameElements.values()){
			if (e.getObjectType().contentEquals("Treasure")){
				tempString += e.getType() + "\n";
			}
			else{
				tempString += e.getName() + "\n";
			}
		}
		return tempString;
	}//end toString()
}//end class Cave

class Party extends GameElement{
	protected HashMap<Integer, Creature> members = new HashMap<Integer, Creature>();
	private ArrayList<Artifact> resourcePool = new ArrayList<Artifact>();
	
	public Party(Scanner input){
		input.next();
		super.setIndex(input.nextInt());
		super.setName(input.next());
		super.setType(""); // needed to balance search()
	}
	
	public void addMember(int index, Creature c){
		members.put(index, c);
	}//end addCreature()
	
	public Creature getMember(int index){
		return members.get(index);
	}// end getCreature()
	
	public void addResource(Artifact a){
		resourcePool.add(a);
	}//end addReasource()
	
	public Artifact getResource(String type){
		for (Artifact a : resourcePool){
			if (a.getType() == type){
				return a;
			}
		}
		return null;
	}//end getResource()
	
	public String toString(){
		String tempString = "P " + this.getName() + " P";
		return tempString;
	}//end toString()
}//end class Party

class Creature extends GameElement{
	
	private int empathy, fear, carryingCapacity;
	private double age, height, weight;
	protected HashMap<Integer, Artifact> artifacts = new HashMap<Integer, Artifact>();
	protected HashMap<Integer, Treasure> treasures = new HashMap<Integer, Treasure>();
	protected HashMap<Integer, Job> jobs = new HashMap<Integer, Job>();
	
	public Creature(Scanner input){
		input.next();
		super.setIndex(input.nextInt());
		super.setType(input.next());
		super.setName(input.next());
		super.setOwner(input.nextInt()); 
		this.empathy = input.nextInt();
		this.fear = input.nextInt();
		this.carryingCapacity = input.nextInt();
		this.age = input.nextDouble();
		this.height = input.nextDouble();
		this.weight = input.nextDouble();
	}//end Creature constructor
	
	public void addArtifact(int index, Artifact a){
		artifacts.put(index, a);
	}//end addArtifact()
	
	public void addTreasure(int index, Treasure t){
		treasures.put(index, t);
	}//end addTreasure()
	
	public void addJob(int index, Job j){
		jobs.put(index, j);
	}//end addJob()
	
	public Artifact getArtifact(int index){
		return artifacts.get(index);
	}//end getArtifacts()
	
	public Treasure getTreasure(int index){
		return treasures.get(index);
	}// getTreasure()
	
	public Job getJob(int index){
		return jobs.get(index);
	}//end getJob
	
	public String stats(){ 
		String tempString = "Type: " + super.getType() + ", Empathy: "
				+ empathy + ", Fear: " + fear + ", Carrying Capacity: " 
				+ carryingCapacity + ", Age: " + age + ", Height: " 
				+ height + ", Weight: " + weight;
		return tempString;
	}
	
	public String toString(){
		String tempString = "C " + this.getName() + " C";
		return tempString;
	}//end toSting()
}//end class Creature

class Treasure extends GameElement{
	private int weight, value;
	
	public Treasure(Scanner input){
		input.next();
		super.setIndex(input.nextInt());
		super.setType(input.next());
		super.setOwner(input.nextInt());
		this.weight = input.nextInt();
		this.value = input.nextInt();
		super.setName(""); // needed to balance search()
	}//end constructor Treasure()
	
	public void setValue(int value){
		this.value = value;
	}//end setValue()
	
	public double getWeight(){
		return weight;
	}//end getWeight()
	
	public int getValue(){
		return value;
	}//end getValue()
	
	public String toString(){
		String tempString = "T " + super.getType() + " T";
		
		return tempString;
	}//end toString()
}//end class Treasure

class Artifact extends GameElement{
	
	public Artifact(Scanner input){
		input.next();
		super.setIndex(input.nextInt());
		super.setType(input.next());
		super.setOwner(input.nextInt());
		super.setName(input.next());
	}//end Artifact constructor
	
	public String toString(){
		String tempString = "A " + this.getName() + " A";
		
		return tempString;
	}//end toSting()
}//end class Artifact

class Job extends GameElement implements Runnable{
	
	private int wand, potion, stone, weapon, locationX, locationY;
	private long time, startTime, stopTime, jobTime;
	private double duration;
	private volatile boolean running = true;
	private volatile boolean halt = false;
	private volatile JProgressBar pm = new JProgressBar ();
	
	public Job(Scanner input){
		input.next();
		super.setIndex(input.nextInt());
		super.setName(input.next());
		super.setOwner(input.nextInt());
		this.jobTime = input.nextLong();
		input.next();
		this.stone = input.nextInt();
		input.next();
		this.potion = input.nextInt();
		input.next();
		this.wand = input.nextInt();
		input.next();
		this.weapon = input.nextInt();
		super.setType("Job"); // needed to balance search()
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
	}//end getTime()
	
	public int getWand() {
		return wand;
	}//end getWand()
	
	public int getPotion() {
		return potion;
	}//end getPotion()
	
	public int getStone() {
		return stone;
	}//end getStone()
	
	public int getWeapon() {
		return weapon;
	}//end getWeapon()
	
	public boolean getHalt(){
		return halt;
	}//end getHalt()
	
	public int getPos(){
		return (locationX / 30) + (locationY / 100);
	}//end getPos()
	
	//ToString Method
	public String toString(){
		String tempString = "J " + this.getName() + " J";
		return tempString;
	}//end toString
	
	//ResourcesNeeded display
	public String resourcesNeeded(){
		String tempString = "Resources Required: \n";
		tempString += "Wand(s): " + this.getWand() + ", Potion(s): " + 
				this.getPotion() + ", Stone(s): " + this.getStone() + 
				", Weapon(s): " + this.getWeapon();
		
		return tempString;
	}
	
	//This run method was adapted from code written by Dr.Nicholas Duchon
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
		} // end running
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