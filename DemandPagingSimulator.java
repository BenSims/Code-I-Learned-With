
import java.util.ArrayList;
import java.util.Scanner;

/* Class: CMSC 412
Student: Benjamin Sims 
Assignment: Final
Due Date: 12 Oct 2014
This File: DemandPagingSimulator.java
Required Files: none
Description: Demand paging virtual memory simulator
Input: Keyboard                         
Output: text
*/


public class DemandPagingSimulator {
    
    private static int refBit = 0;
    private static int faultCount;
    private static int victimFrame = 10;
    private static int frameNumber = -1;
    private static String fault = null;
    private static Scanner input = new Scanner(System.in);
    private static ArrayList<Integer> refString  = new ArrayList();
    private static ArrayList<Integer> phyMemory = new ArrayList();
    private static ArrayList<ArrayList<String>> table = new ArrayList();
    
    public static void main(String[] args){
        System.out.println("Ben Sims, CMSC 412 Operating Systems"
                + ", Final");
        System.out.println("Please enter the number of physical frames.  The number"
                + " can be between 1 and 8.");
        frameNumber = validateInput();
        
        while (frameNumber < 1 || frameNumber > 8){
            System.out.println("Please try again.  Enter a number between 1 and 8.");
            frameNumber = validateInput();
        }
        
        displayMenu();
        mainMenu();
    }
    
    //Method used to display menu
    public static void displayMenu(){
        System.out.println("Please select an option.");
        System.out.println("0 – Exit" + "\n1 – Read reference string" +
                "\n2 – Generate reference string" + "\n3 – Display current "
                + "reference string" + "\n4 – Simulate FIFO" + "\n5 – Simulate "
                + "OPT" + "\n6 – Simulate LRU" + "\n7 – Simulate LFU");
    }
    
    //Main menu for the simulation
    public static void mainMenu(){
        int inputNumber = 0;
        
        inputNumber = validateInput();
        while (inputNumber < 0 || inputNumber > 7){
            System.out.println("That is not an option.  Please try again.");
            inputNumber = validateInput();
        }
        
        switch (inputNumber){
            case 0:  exit();
                     break;
            case 1:  readRefString();
                     break;
            case 2:  generateRefString();
                     break;
            case 3:  displayList(-1);
                     break;
            case 4:  simFIFO();
                     break;
            case 5:  simOPT();
                     break;
            case 6:  simLRU();
                     break;
            case 7:  simLFU();
                     break;    
        }
        
        mainMenu();
    }
    
    //This method validates the user input
    public static int validateInput(){
        String userInput;
        int testInt = 0;
        
        userInput = input.next();
        try{
            testInt = Integer.parseInt(userInput);
        }
        catch (NumberFormatException e){
            System.out.println("That is not a number.  Please try again.");
            
            testInt = validateInput();
        }
        
        return testInt;
    }
    
    //This method closes the program
    public static void exit(){
        System.out.println("Good Bye.");
        System.exit(0);
    }
    
    //This method lets the user build their own
    public static void readRefString(){
        int refStringLength;
        
        refString.clear();
        
        System.out.println("Please enter the length of the string.  It must be"
                + " between 1 and 20");
        refStringLength = validateInput();
        
        //While loop checks to see if refStringLenght is within range.
        while (refStringLength > 20 || refStringLength < 1){
            System.out.println("That was not a number between 1 and 20.  Please"
                    + " try again.");
            refStringLength = validateInput();
        }
        
        System.out.println("Enter in ten numbers between 0 and 9.");
        
        for (int i = 0; i < refStringLength; i++){
            refString.add(validateInput());
            
            //While loop checks to see if input is within range
            while (refString.get(i) < 0 || refString.get(i) > 9){
                System.out.println("That is not between 0 and 9.  Please try again.");
                
                refString.set(i, validateInput());
            }
            if (i == refStringLength - 1){
                break;
            }
            System.out.println("Please enter next number between 0 and 9");
        }
        System.out.println("Complete.  Please select the next option.");
        displayMenu();
    }
    
    //This method makes a random reference string
    public static void generateRefString(){
        int refStringLength;
        
        refString.clear();
        
        System.out.println("Please enter the length of the string.  It must be"
                + " between 1 and 20.");
        refStringLength = validateInput();
        
        while (refStringLength > 20 || refStringLength < 1){
            System.out.println("That was not a number between 1 and 20.  Please"
                    + " try again.");
            refStringLength = validateInput();
        }
        
        for (int i = 0; i < refStringLength; i++){
            refString.add((int)(Math.random() * 10));
            
        }
        System.out.println("Complete.  Please select the next option.");
        displayMenu();
    }
    
    //This method displays the refString and the physical memory content
    public static void displayList(int mode){
        
        //Here the refString is displayed with the current number hilighted
        if (mode >= 0){
            System.out.print("Reference String |");
            for (int i = 0; i < refString.size(); i++){
                 System.out.print(refString.get(i) + "|");
            }
        }
        
        //Here the table is displayed
        else if (mode == -2){
            for (int i = 0; i < frameNumber; i++){
                System.out.print("Physical frame " + i + " |");
                for (int j = 0; j < table.size(); j++){
                    System.out.print(table.get(j).get(i) + "|");
                }
                System.out.println();
            }
            System.out.print("Page faults      |");
            for (int i = 0; i < table.size(); i++){
                System.out.print(table.get(i).get(frameNumber) + "|");
            }
            System.out.print("\nVictim frames    |");
            for (int i = 0; i < table.size(); i ++){
                System.out.print(table.get(i).get(frameNumber + 1) + "|");
            }
        }
        
        //Here the reference string is displayed for option 3
        else{
            System.out.println("Current reference string:");
            for (int i = 0; i < refString.size(); i++){
                System.out.print(refString.get(i) + " ");
            }
        }
        System.out.println();
    }
    
    //Method used to record physical memory into a table
    public static void updateTable(){
        //if phyMemory is not full it is recorded here
        if (frameNumber > phyMemory.size()){
            table.add(new ArrayList<String>());
            for (int i = 0; i < phyMemory.size(); i++){
                table.get(table.size() - 1).add(String.valueOf(phyMemory.get(i)));
            }
            for (int i = phyMemory.size(); i < frameNumber; i++){
                table.get(table.size() - 1).add(" ");
            }
        }
        //if it is full it is recorded here
        else{
            table.add(new ArrayList<String>());
            for (int i = 0; i < phyMemory.size(); i++){
                table.get(table.size() - 1).add(String.valueOf(phyMemory.get(i)));
            }
        }
        
        //if there was a fault it is recorded here 
        if (fault != null){
            table.get(table.size() - 1).add("F");
        }
        //if there was no fault it is recorded here
        else{
            table.get(table.size() - 1).add(" ");
        }
        //if there was a victim frame it is recorded here
        if (victimFrame != 10){
            table.get(table.size() - 1).add(String.valueOf(victimFrame));
        }
        //if there was no victim frame there is a blank recorded
        else{
            table.get(table.size() - 1).add(" ");
        }
    }
    
    //Method used to simulate FIFO
    public static void simFIFO(){
        //This part checks to see if the reference string has elements, and returns
        //the user is it does not
        if (refString.isEmpty()){
            System.out.println("Reference string is empty.  Please select option"
                    + " 1 or 2.");
            return;
        }
        System.out.println("Starting FIFO simulation");
        //phyMemory and table are cleared cleared to ensure they are empty
        phyMemory.clear();
        table.clear();
        
        //Here the first element is added to phyMemory
        phyMemory.add(refString.get(0));
        //fault is set to F so that it can be recorded in updateTable(), faultCount
        //is increased by one.
        fault = "F";
        faultCount = 1;
        
        //update the Table, then display all results
        updateTable();
        displayList(0);
        displayList(-2);
        
        System.out.println("Press enter to continue.");
        //I used a scanner instead of a listener because it produces the same result
        //with less code.
        input.nextLine();
        input.nextLine();
        
        //for loop exicutes the rest of the simulation
        for (int i = 1; i < refString.size(); i++){
            displayList(i);
            //this segment works with phyMemory before it is full
            if (phyMemory.size() < frameNumber){
                //this loop checks to see if the current element is in phyMemory
                for (int j = 0; j < phyMemory.size(); j++){
                    if (phyMemory.get(j) == refString.get(i)){
                        refBit = 1;
                        fault = null;
                    }
                }
                //if reBit != 1 then the element was not in phyMemory, and will be
                //added here.  The faultCount goes up by one. fault is set to 'F'
                if (refBit != 1){
                    phyMemory.add(refString.get(i));
                    fault = "F";
                    faultCount++;
                }
            }
            //the rest of the simulation is exicuted here
            else{
                //this loop checks to see if the current element is in phyMemory
                for (int j = 0; j < phyMemory.size(); j++){
                    if (phyMemory.get(j) == refString.get(i)){
                        refBit = 1;
                    }
                }
                //if refBit != 1 then the element needs to be added.  In FIFO 
                //the element at the front is removed, and the new element is added
                //at the end.  faultCount goes up by one. fault is set to 'F'
                if (refBit != 1){
                    victimFrame =phyMemory.get(0);
                    phyMemory.remove(0);
                    phyMemory.add(refString.get(i));
                    fault = "F";
                    faultCount++;
                }
            }
            //updateTable and display it
            updateTable();
            displayList(-2);
            //reset variables
            refBit = 0;
            victimFrame = 10;
            fault = null;
            //Prompt user to continue
            System.out.println("Press enter to continue.");
            input.nextLine();
        }
        //Simulation complete.  faultCount, and mainMenu texts are displayed
        System.out.println("Simulation complete.  Total fault count is: " + faultCount);
        displayMenu();
    }
    
    //Method used to simulate OPT.  Works just like FIFO, but with the noted changes
    //below.
    public static void simOPT(){
        if (refString.isEmpty()){
            System.out.println("Reference string is empty.  Please select option"
                    + " 1 or 2.");
            return;
        }
        System.out.println("Starting OPT simulation");
        //These are additonal variables needed to keep track of data
        int tempIndex = 10;
        int tempCount = 21;
        //this is a list of how often each element occurs.
        ArrayList<Integer> optList = prepareOPT();
        
        phyMemory.clear();
        table.clear();
        
        phyMemory.add(refString.get(0));
        //Here the occurance of element refString.get(0) is decreased by one.
        optList.set(refString.get(0), optList.get(refString.get(0)) - 1);
        
        fault = "F";
        faultCount = 1;
        
        updateTable();
        displayList(0);
        displayList(-2);
        
        System.out.println("Press enter to continue.");
        input.nextLine();
        input.nextLine();
        
        for (int i = 1; i < refString.size(); i++){
            displayList(i);
            if (phyMemory.size() < frameNumber){
                for (int j = 0; j < phyMemory.size(); j++){
                    if (phyMemory.get(j) == refString.get(i)){
                        refBit = 1;
                        //Here the occurance of element refString.get(i) is decreased by one.
                        optList.set(refString.get(i), optList.get(refString.get(i)) - 1);
                        fault = null;
                    }
                }
                if (refBit != 1){
                    phyMemory.add(refString.get(i));
                    fault = "F";
                    faultCount++;
                    //Here the occurance of element refString.get(i) is decreased by one.
                    optList.set(refString.get(i), optList.get(refString.get(i)) - 1);
                }
            }
            else{
                for (int j = 0; j < phyMemory.size(); j++){
                    if (phyMemory.get(j) == refString.get(i)){
                        refBit = 1;
                        //Here the occurance of element refString.get(i) is decreased by one.
                        optList.set(refString.get(i), optList.get(refString.get(i)) - 1);
                    }
                }
                if (refBit != 1){
                    //Here it is checked which element of phyMemory has the lowest
                    //occurrance count.  The first one with the lowest count gets
                    //replaced.
                    for (int j = 0; j < phyMemory.size(); j++){
                        if (optList.get(phyMemory.get(j)) < tempCount){
                            tempCount = optList.get(phyMemory.get(j));
                            tempIndex = j;
                        }
                    }
                    victimFrame =phyMemory.get(tempIndex);
                    phyMemory.set(tempIndex, refString.get(i));
                    //Here the occurance of element refString.get(i) is decreased by one.
                    optList.set(refString.get(i), optList.get(refString.get(i)) - 1);
                    fault = "F";
                    faultCount++;
                }
                
            }
            updateTable();
            displayList(-2);
            
            //tempIndex and tempCount are set to limits that are one above the range
            //they could acheive.
            refBit = 0;
            tempIndex = 10;
            tempCount = 21;
            victimFrame = 10;
            fault = null;
            
            System.out.println("Press enter to continue.");
            input.nextLine();
        }
        System.out.println("Simulation complete.  Total fault count is: " + faultCount);
        displayMenu();
    }
    
    //Method used to simulate LRU.  Works in the same fasion as FIFO, but with the 
    //noted changes below
    public static void simLRU(){
        if (refString.isEmpty()){
            System.out.println("Reference string is empty.  Please select option"
                    + " 1 or 2.");
            return;
        }
        System.out.println("Starting LRU simulation");
        
        //This list is used to keep track of the order the elements were used
        ArrayList<Integer> lruList = new ArrayList();
        phyMemory.clear();
        table.clear();
        
        phyMemory.add(refString.get(0));
        //element is added to lruList as well. lruList.get(0) will always be 
        //the last recently used element
        lruList.add(refString.get(0));
        
        fault = "F";
        faultCount = 1;
        
        updateTable();
        displayList(0);
        displayList(-2);
        
        System.out.println("Press enter to continue.");
        input.nextLine();
        input.nextLine();
        
        for (int i = 1; i < refString.size(); i++){
            displayList(i);
            if (phyMemory.size() < frameNumber){
                for (int j = 0; j < phyMemory.size(); j++){
                    if (phyMemory.get(j) == refString.get(i)){
                        refBit = 1;
                        //Since the element was present it is taken off the list, 
                        //and added on the end.  This part searches for it.
                        for (int k = 0; k < lruList.size(); k++){
                            if (lruList.get(k) == refString.get(i)){
                                lruList.remove(k);
                                lruList.add(refString.get(i));
                                break;
                            }
                        }
                        break;
                    }
                }
                if (refBit != 1){
                    phyMemory.add(refString.get(i));
                    fault = "F";
                    faultCount++;
                    //element was just used, so it is added here to lruList
                    lruList.add(refString.get(i));
                }
            }
            else{
                for (int j = 0; j < phyMemory.size(); j++){
                    if (phyMemory.get(j) == refString.get(i)){
                        refBit = 1;
                        //Since the element was present it is taken off the list, 
                        //and added on the end.  This part searches for it.
                        for (int k = 0; k < lruList.size(); k++){
                            if (lruList.get(k) == refString.get(i)){
                                lruList.remove(k);
                                lruList.add(refString.get(i));
                                break;
                            }
                        }
                        break;
                    }
                }
                
                if (refBit != 1){
                    //Since element was not present the contents of phyMemory are
                    //compared to lruList.get(0).  This is because this element is
                    //the Last Recently Used element
                    for (int j = 0; j < phyMemory.size(); j++){
                        if (phyMemory.get(j) == lruList.get(0)){
                            fault = "F";
                            lruList.remove(0);
                            lruList.add(refString.get(i));
                            victimFrame =phyMemory.get(j);
                            phyMemory.set(j, refString.get(i));
                            faultCount++;
                            break;
                        }
                    }
                }
            }
            updateTable();
            displayList(-2);
            
            refBit = 0;
            victimFrame = 10;
            fault = null;
            
            System.out.println("Press enter to continue.");
            input.nextLine();
        }
        System.out.println("Simulation complete.  Total fault count is: " + faultCount);
        displayMenu();
    }
    
    //Method used to simulate LFU.  It works in the same fasion as OPT, but with
    //the noted changes below.
    public static void simLFU(){
        if (refString.isEmpty()){
            System.out.println("Reference string is empty.  Please select option"
                    + " 1 or 2.");
            return;
        }
        System.out.println("Starting LFU simulation");

        int tempIndex = 10;
        int tempCount = 21;
        //lfuCountList is used to track how often an element is used.  Here it is
        //initiallized.  Later it will be built up as each element is used.
        ArrayList<Integer> lfuCountList = new ArrayList();
        for (int i = 0; i < 10; i++){
            lfuCountList.add(0);
        }
        
        phyMemory.clear();
        table.clear();
        
        System.out.println("Fault. " + refString.get(0) + " was added"
                + " to physical memory.");
        
        phyMemory.add(refString.get(0));
        fault = "F";
        faultCount = 1;
        //Here the occurance of element refString.get(0) is increased by one.
        lfuCountList.set(refString.get(0), lfuCountList.get(refString.get(0)) + 1);
        
        updateTable();
        displayList(0);
        displayList(-2);
        
        System.out.println("Press enter to continue.");
        input.nextLine();
        input.nextLine();
        
        for (int i = 1; i < refString.size(); i++){
            displayList(i);
            if (phyMemory.size() < frameNumber){
                for (int j = 0; j < phyMemory.size(); j++){
                    if (phyMemory.get(j) == refString.get(i)){
                        refBit = 1;
                        //Here the occurance of element refString.get(i) is increased by one.
                        lfuCountList.set(refString.get(i), lfuCountList.get(refString.get(i)) + 1);
                    }
                }
                if (refBit != 1){
                    phyMemory.add(refString.get(i));
                    faultCount++;
                    fault = "F";
                    //Here the occurance of element refString.get(i) is increased by one.
                    lfuCountList.set(refString.get(i), lfuCountList.get(refString.get(i)) + 1);
                }
            }
            else{
                for (int j = 0; j < phyMemory.size(); j++){
                    if (phyMemory.get(j) == refString.get(i)){
                        refBit = 1;
                        //Here the occurance of element refString.get(i) is increased by one.
                        lfuCountList.set(refString.get(i), lfuCountList.get(refString.get(i)) + 1);
                    }
                }
                if (refBit != 1){
                    for (int j = 0; j < phyMemory.size(); j++){
                        if (lfuCountList.get(phyMemory.get(j)) < tempCount){
                            tempCount = lfuCountList.get(phyMemory.get(j));
                            tempIndex = j;
                        }
                    }
                    victimFrame =phyMemory.get(tempIndex);
                    phyMemory.set(tempIndex, refString.get(i));
                    //Here the occurance of element refString.get(i) is increased by one.
                    lfuCountList.set(refString.get(i), lfuCountList.get(refString.get(i)) + 1);
                    fault = "F";
                    faultCount++;
                }
            }
            
            updateTable();
            displayList(-2);
            
            tempIndex = 10;
            tempCount = 21;
            refBit = 0;
            victimFrame = 10;
            fault = null;
            
            System.out.println("Press enter to continue.");
            input.nextLine();
        }
        System.out.println("Simulation complete.  Total fault count is: " + faultCount);
        displayMenu();
    }
    
    //Method used to prepare data for OPT
    public static ArrayList<Integer> prepareOPT(){
        //For the first part an Array list of 10 zeros is made.
        ArrayList<Integer> occurrenceCount = new ArrayList();
        
        for (int i = 0; i < 10; i++){
            occurrenceCount.add(0);
        }
        
        //Here the occurance of each number is recorded by its index number
        //ex. if 3 occurs 5 times occurrenceCount.get(3) will equal 5
        for (int i = 0; i < refString.size(); i++){
            occurrenceCount.set(refString.get(i), occurrenceCount.get(refString.get(i)) + 1);
        }
        
        return occurrenceCount;
    }

}
