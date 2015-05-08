package Final;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/* Class: CMSC 350
Student: Benjamin Sims 
Assignment: Final
Due Date: 12 Oct 2014
This File: TestSocialGraph.java
Required Files: socialGraphInput.txt, SocialGraph.java, Graph.java
Description: Final for CMSC 350 
Input: socialGraphInput.txt                        
Output: Social Graph data
*/


public class TestSocialGraph {
    
    private static Scanner input = new Scanner(System.in);
    private static List<String> vertexList = new ArrayList(); 
    
    public static void main(String[] args){
        System.out.println("Ben Sims, CMSC 350 6380 Data Structures and "
                + "Analysis (2148), Final");
        
        //This int is later used to keep the main menu in an infinite loop.
        int loopCondition = -1;
        SocialGraph currentGraph = null;
        
        System.out.println("Welcome to Test Social Graph.  Please enter the name of"
                + "the file you wish to load.  The defult file is socialGraph"
                + "Input.txt, or enter 'New' to make a new blank Social Graph.");
        
        String file = input.next().trim();
        
        //This part loads the file.  All Exceptions are caught and handled here.
        //The loop continues as long as loopCondition is -1.  If the file loads
        //the loop will be broken
        while (loopCondition == -1){
            try{
                
                if (file.equalsIgnoreCase("new")){
                    currentGraph = new SocialGraph();
                    break;
                }
                currentGraph = loadData(file);
                break;
            }
            catch (UnsupportedEncodingException e){
                System.out.println("UnsupportedEncodingException.  Please try again.  \nPlease "
                        + "enter the name of the file you wish to load.  The defult "
                        + "file is socialGraphInput.txt, or enter 'New' to make a new blank Social Graph.");
                file = input.next().trim();
            }
            catch (FileNotFoundException e){
                System.out.println("File not found.  Please try again.  \nPlease "
                        + "enter the name of the file you wish to load.  The defult "
                        + "file is socialGraphInput.txt, or enter 'New' to make a new blank Social Graph.");
                file = input.next().trim();
                
            }
            catch (IOException e){
                System.out.println("IOException.  Please try again.  \nPlease "
                        + "enter the name of the file you wish to load.  The defult "
                        + "file is socialGraphInput.txt, or enter 'New' to make a new blank Social Graph.");
                file = input.next().trim();
                
            }
        }
        
        //Here a global list of names is made to keep track of vertices
        vertexList = currentGraph.getVertices();
        
        displayMenu();
        //Here the main menu is kept in an infinite loop until exit() is exicuted
        while (loopCondition != 0){
            mainMenu(currentGraph);
        }
    }
    
    //Method used to load data from inputFile
    public static SocialGraph loadData(String selectedFile) throws FileNotFoundException, 
            UnsupportedEncodingException, IOException{
        ArrayList<String> tempStringInput = new ArrayList();
        ArrayList<ArrayList<Integer>> tempIntegerInput = new ArrayList();
        String[] tempInputArray;
        String[] vertices;
        int vertexNumber;
        
        java.io.File text = new java.io.File(selectedFile);
        Scanner fileInput = new Scanner(text);
        
        //This part loads the String data of the input file
        while (fileInput.hasNext()){
            tempInputArray = fileInput.next().split(";");
            
            //Here the loop ends if the seperator # is incountered
            if (tempInputArray[0].equalsIgnoreCase("#")){
                break;
            }
            //All String data is collected here
            tempStringInput.addAll(Arrays.asList(tempInputArray));
            
        }
        
        //vetrices is filled here 
        vertices = tempStringInput.toArray(new String[tempStringInput.size()]);
        
        //This part collects the edge data
        while (fileInput.hasNext()){
            tempInputArray = fileInput.nextLine().split(" ");
            
            //This part ensures that the array is populated
            if (tempInputArray.length != 0){
                //The number of the vertex is stored here 
                vertexNumber = Integer.parseInt(tempInputArray[0]);
                //This part pairs the edge connections with its vertex
                for (int i = 1; i < tempInputArray.length; i++){
                    tempIntegerInput.add(new ArrayList<>());
                    tempIntegerInput.get(tempIntegerInput.size() - 1).add(vertexNumber);
                    tempIntegerInput.get(tempIntegerInput.size() - 1).add(Integer.parseInt(tempInputArray[i]));
                }
            }
            
        }
        
        //Here int[][] edges is made and populated based on the input of tempIntegerInput
        int[][] edges = new int[tempIntegerInput.size()][];
        for (int i = 0; i < tempIntegerInput.size(); i++){
            ArrayList<Integer> row = tempIntegerInput.get(i);
            edges[i] = new int[row.size()];
            for (int j = 0; j < row.size(); j++){
                edges[i][j] = row.get(j);
            }
        }
        //A SocialGraph is made and returned
        SocialGraph tempGraph = new SocialGraph(edges, vertices);
        
        return tempGraph;
    }
    
    
    //Method used to display menu options
    public static void displayMenu(){
        System.out.println("Please select an option:" + "\n(0) Exit program" +
                "\n(1) Normalized Degree Of Centrality" + "\n(2) Number Of Triangles"
                + " Incident To Vertex" + "\n(3) List Of Triangles Incident To Vertex"
                + "\n(4) Cluster Individual" + "\n(5) Average Clustering" + 
                "\n(6) Is Indirect Acquaintance" + "\n(7) Add Vertex" + "\n(8) Add"
                + " Edge" + "\n(9) Print Edges");
    }
    
    //Method used to act as main menu for program
    public static void mainMenu(SocialGraph graph){
        String tempName1, tempName2;
        int tempIndex1, tempIndex2;
        //This part checks to ensure the input is a number
        int menuSelection = validateNumber();
        
        //This part check to ensure the number is within the range of the switch
        if (menuSelection > 9 || menuSelection < 0){
            System.out.println("That number is out of bounds.  Please try again.");
            return;
        }
        
        //This switch acts as the main menu.  Each case corisponds with the list 
        //displayed in displayMenu()
        switch(menuSelection){
            case 0:  {
                exit();
            }
                     break;
                
            case 1:  {
                System.out.println("Please enter the name of the person you wish to"
                        + " check the centrality of.");
                //each case follows the same pattern.  It asks for a name.  Finds
                //the index of the name then calls upon the corisponding method using
                //the index, and stores and displays the results.
                tempName1 = validateName();
                tempIndex1 = graph.getIndex(tempName1);
                
                double centrality  = graph.normalizedDegreeOfCentrality(tempIndex1);
                System.out.println("The centrality of " + tempName1 + " is " +
                        centrality);
            }
                     break;
                
            case 2:  {
                System.out.println("Please enter the name of the person you wish to"
                        + " inquire about.");
                tempName1 = validateName();
                tempIndex1 = graph.getIndex(tempName1);
                
                System.out.println(tempName1 + " has " +
                        graph.numberOfTrianglesIncidentToVertex(tempIndex1) +
                        " associated triangle(s).");
            }
                     break;
                
            case 3:  {
                System.out.println("Please enter the name of the person you wish to"
                        + "inquire about.");
                tempName1 = validateName();
                tempIndex1 = graph.getIndex(tempName1);
                int size;
                
                ArrayList<ArrayList<String>> tempList = graph.listOfTrianglesIncidentToVertex(tempIndex1);
                if (!tempList.isEmpty()){
                    System.out.println(tempName1 + " has the folloing triangle(s)");
                    for (ArrayList a : tempList){
                        size = a.size();
                        for (int i = 0; i < size; i++){
                            System.out.print(a.get(i) + ", ");
                        }
                        System.out.print(" ");
                    }
                    System.out.println();
                }
                else{
                    System.out.println(tempName1 + " has no associated triangle");
                }
            }
                     break;
                
            case 4:  {
                System.out.println("Please enter the name of the person you wish to"
                        + " find the cluster percentage of.");
                tempName1 = validateName();
                tempIndex1 = graph.getIndex(tempName1);
                
                double percentage = graph.clusterIndividual(tempIndex1);
                System.out.println(tempName1 + " has a cluster percentage of "
                        + percentage + "%");
            }
                     break;
                
            case 5:  {
                double average = graph.averageClustering();
                System.out.println("The average clustering for the graph is: " +
                        average);
            }
                     break;
                
            case 6:  {
                System.out.println("This option checks to see if two people are "
                        + "connected as contacts, or through other contacts.  Please"
                        + " enter two names you wish to inquire about.");
                tempName1 = validateName();
                System.out.println("Now enter the second name.");
                tempName2 = validateName();
                
                tempIndex1 = graph.getIndex(tempName1);
                tempIndex2 = graph.getIndex(tempName2);
                
                boolean possible = graph.isIndirectAcquaintance(tempIndex1, tempIndex2);
                
                System.out.print(tempName1 + " and " + tempName2);
                
                if (possible){
                    System.out.println(" have connections.  They could be aquaintances.");
                }
                else{
                    System.out.println(" have no connection.");
                }
            }
                     break;
                
            case 7:  {
                System.out.println("Please enter the name of the person you wish"
                        + " to add.");
                tempName1 = input.next();
                
                graph.addVertex(tempName1);
                vertexList.add(tempName1);
                System.out.println(tempName1 + " added.");
            }
                break;
                
            case 8:  {
                System.out.println("Please enter the names of the two people you"
                        + " wish to make a connection between.");
                tempName1 = validateName();
                System.out.println("Now enter the second name.");
                tempName2 = validateName();
                
                tempIndex1 = graph.getIndex(tempName1);
                tempIndex2 = graph.getIndex(tempName2);
                
                graph.addEdge(tempIndex1, tempIndex2);
                
                System.out.println("Connection made.");
            }
                     break;
                
            case 9:  {
                System.out.println("The current connections are: ");
                graph.printEdges();
            }
                     System.out.println("Complete.");
                     break;
        }
        System.out.println("Please select another option.");
    }

    //This method is used for error handling of number input.
    public static int validateNumber(){
        String safetyCheck = input.next();
        int tempInt;
        
        try{
            tempInt = Integer.valueOf(safetyCheck);
        }
        catch (NumberFormatException e){
            System.out.println("Please try again.");
            tempInt = validateNumber();
        }
        
        return tempInt;
    }
    
    //Method used to validate names
    public static String validateName(){
        String safetyCheck = input.next();
        
        for (String s : vertexList){
            if (s.equalsIgnoreCase(safetyCheck)){
                return safetyCheck;
            }
        }
        
        System.out.println("That name is not in the graph.  Please try again.");
        return validateName();
    }
    
    //Method used to close the program
    public static void exit(){
        System.out.println("Good Bye.");
        System.exit(0);
    }

}
