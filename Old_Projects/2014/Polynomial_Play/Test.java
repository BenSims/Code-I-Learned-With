
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

/* Class: CMSC 350
Student: Benjamin Sims 
Assignment: Week 2
Due Date: 31 Aug 2014
This File: Test.java
Required Files: StudentInput.txt
Description: project 1 
Input: integer text file                         
Output: Text
*/


public class Test {
    
    static int refStart = 0;
    static int refEnd = 0;
    static int refStart2 = 0;
    static int refEnd2 = 0;
    static int displayStart = 0;
    static String [] tempS;
    static Integer StringNum;
    static Polynomials polynomials = new Polynomials();
    static ArrayList MasterList = new ArrayList();
    static ArrayList nameList = new ArrayList();
    static Scanner input = new Scanner(System.in);
    static String Op1 = "";
    static String Op2 = "";
    static String Opr = "";
    static String test = "null";
    
    public static void main(String[] args) throws UnsupportedEncodingException, IOException{
        
        System.out.println("Ben Sims, CMSC 350 6380 Data Structures and "
                + "Analysis (2148), Project1");
        
        readFile();
        
        display(MasterList, displayStart);
        
        questions();
        
        compute(Op1, Op2, Opr);
        
        System.out.println("Good Bye!");
        
    }
    
    //Method used to upload .txt file
    public static void readFile() throws FileNotFoundException, 
            UnsupportedEncodingException, IOException{
        
        java.io.File lists = new java.io.File("StudentInput.txt");
        Scanner input = new Scanner(lists);
        
        
        //This takes the .txt file as an array of string tokens
        //the name is then saved on the master list
        //and a start point is recorded
        while (input.hasNext()){
            tempS = input.nextLine().split(";");
            
            MasterList.add(tempS[0]);
            nameList.add(MasterList.size() - 1);
            
            polynomials.setPolyStartPoint(0 + polynomials.getCoefficientList().size());
            
            
            //this part truns the rest of the string tokens into numbers,
            //loads them into polynomials, and masterlist records the posision
            for (int i = 1; i < tempS.length; i = i + 2){
                StringNum = Integer.parseInt(tempS[i]);
                polynomials.addCoefficient(StringNum);
                
                StringNum = Integer.parseInt(tempS[i + 1]);
                polynomials.addExponent(StringNum);
                
                refEnd = polynomials.getCoefficientList().size() - 1;
                MasterList.add(refEnd);
            }
            
            polynomials.setPolyEndPoint(polynomials.getCoefficientList().size() - 1);
        }
        
        input.close();
    }
    
    //Method used to display the Masterlist
    public static void display(ArrayList list, int start){
        
        //For loop decides weather the next index of list is a number by checking the unicode
        //if the Unicode is greater than Unicode "9" it writes the name
        //and the first part of the polynomial then jumps i by 1.
        for (int i = start; i < list.size(); i++){
            if (String.valueOf(list.get(i)).codePointAt(0) > 58){
                System.out.print(list.get(i) + "(x) = " +  polynomials.getCoefficient((int)list.get(i + 1)) + 
                        "x^" + polynomials.getExponent((int)list.get(i + 1)) + " ");
                i++;
            }
            
            else if (polynomials.getExponent((int)list.get(i)) == 0){
                if (polynomials.getCoefficient((int)list.get(i)) < 0){
                    System.out.print(polynomials.getCoefficient((int)list.get(i)) + "\n");
                }
                else if (polynomials.getCoefficient((int)list.get(i)) > 0){
                    System.out.print("+" + polynomials.getCoefficient((int)list.get(i)) + "\n");
                }
            }
            
            else if (polynomials.getExponent((int)list.get(i)) == 1){
                if (polynomials.getCoefficient((int)list.get(i)) < 0){
                    System.out.print(polynomials.getCoefficient((int)list.get(i)) + 
                        "x ");
                }
                    
                else if (polynomials.getCoefficient((int)list.get(i)) > 0) {
                    System.out.print("+" + polynomials.getCoefficient((int)list.get(i)) + 
                        "x ");
                }
            }
            
            else if (polynomials.getCoefficient((int)list.get(i)) > 0){
                System.out.print("+" + polynomials.getCoefficient((int)list.get(i)) + 
                        "x^" + polynomials.getExponent((int)list.get(i)) + " ");
            }
            
            else if (polynomials.getCoefficient((int)list.get(i)) < 0){
                System.out.print( polynomials.getCoefficient((int)list.get(i)) + 
                        "x^" + polynomials.getExponent((int)list.get(i)) + " ");
            }
                
        }
    }
    
    //method used to ask questions
    //and to save what operation needs to be done and the new polynomial name
    public static void questions(){
        Op1 = "";
        Op2 = "";
        test = "null";
        
        while (test.equalsIgnoreCase(Op1) == false){
            System.out.println("Please enter the first polynomial operand: ");
            Op1 = input.next();
        
            for (int i = 0; i < nameList.size(); i++){
                test = String.valueOf(MasterList.get((int)nameList.get(i))).toLowerCase().trim();
                if (test.equalsIgnoreCase(Op1)){
                    break;
                }
            }
        }
        
        while (test.equalsIgnoreCase(Op2) == false){
            System.out.println("Please enter the second polynomial operand: ");
            Op2 = input.next();
        
            for (int i = 0; i < nameList.size(); i++){
                test = String.valueOf(MasterList.get((int)nameList.get(i))).toLowerCase().trim();
                if (test.equalsIgnoreCase(Op2)){
                    break;
                }
            }
        }
        
        while (((Opr.equalsIgnoreCase("-")) || (Opr.equalsIgnoreCase("+"))) == false){
            System.out.println("Please enter the operation: ");
            Opr = input.next().trim();
        }
        
        System.out.println("Please enter the name of the new polynomial: ");
        MasterList.add(input.next());
        displayStart = MasterList.size() - 1;
        nameList.add(MasterList.size() - 1);
        polynomials.setPolyStartPoint(polynomials.getCoefficientList().size());
        
    }
    
    //Method used to create new polynomial
    public static void compute(String Operand1, String Operand2, String Operation){
        System.out.println("Executing the operation ...");
        
        
        //this part finds the starting point and ending point of the first operand
        //then saves them as refStart and refEnd
        for (int i = 0; i < MasterList.size() - 1; i++){
            if (String.valueOf(MasterList.get(i)).equalsIgnoreCase(Operand1)){
                for (int j = 0; j < polynomials.getEndPointList().size(); j++){
                    if ((int)MasterList.get(i + 1)   == polynomials.getStartPoint(j)){
                        refStart = polynomials.getStartPoint(j);
                        refEnd = polynomials.getEndPoint(j);
                    }
                }
                
            }
        }
        
        //this part finds the starting point and ending point of the second operand
        //then saves them as refStart2 and refEnd2
        for (int i = 0; i < MasterList.size() - 1; i++){
            if (String.valueOf(MasterList.get(i)).equalsIgnoreCase(Operand2)){
                for (int j = 0; j < polynomials.getEndPointList().size(); j++){
                    if ((int)MasterList.get(i + 1) == polynomials.getStartPoint(j)){
                        refStart2 = polynomials.getStartPoint(j);
                        refEnd2 = polynomials.getEndPoint(j);
                    }
                }
            }
        }
        
        if (Operation.equalsIgnoreCase("+")){
            addPolys();
        }
        
        if (Operation.equalsIgnoreCase("-")){
            subtractPolys();
        }
        
        display(MasterList, displayStart);
        
        System.out.println("Do you want to continue (Y or N)?");
        Opr = input.next().toLowerCase().trim();
        
        if (Opr.equalsIgnoreCase("y")){
            questions();
            compute(Op1, Op2, Opr);
        }
    }
    
    //Methods used to add two polynomials together and add them to Coefficient, and
    //exponent lists, and records their reference in the MasterList
    public static void addPolys(){
        while (refStart <= refEnd || refStart2 <= refEnd2) {
            
            if (polynomials.getExponent(refStart) > polynomials.getExponent(refStart2)){
                polynomials.addCoefficient(polynomials.getCoefficient(refStart));
                polynomials.addExponent(polynomials.getExponent(refStart));
                
                MasterList.add(polynomials.getCoefficientList().size() - 1);
                
                if (refStart < refEnd){
                        refStart++;
                }
            }
            
            if (polynomials.getExponent(refStart) == polynomials.getExponent(refStart2)){
                polynomials.addCoefficient(polynomials.getCoefficient(refStart) + polynomials.getCoefficient(refStart2));
                polynomials.addExponent(polynomials.getExponent(refStart));
                
                MasterList.add(polynomials.getCoefficientList().size() - 1);
                
                if (refStart < refEnd){
                    refStart++;
                }
                
                if (refStart2 < refEnd2){
                    refStart2++;
                }
            }
            
            if (polynomials.getExponent(refStart) < polynomials.getExponent(refStart2)){
                polynomials.addCoefficient(polynomials.getCoefficient(refStart2));
                polynomials.addExponent(polynomials.getExponent(refStart2));
                
                MasterList.add(polynomials.getCoefficientList().size() - 1);
                
                
                if (refStart2 < refEnd2){
                    refStart2++;
                }
            }
            
            //this part insures that the while loop does not loop forever
            //and ensures that there are not two 0 exponents at the end
            if (polynomials.getExponent(refStart) == 0 && 
                    polynomials.getExponent(refStart2) == 0){
                if (polynomials.getExponent(polynomials.getExponentList().size() - 1) == 0){
                    polynomials.setPolyEndPoint(polynomials.getCoefficientList().size() - 1);
                    
                    break;
                }
                polynomials.addCoefficient(polynomials.getCoefficient(refStart) + polynomials.getCoefficient(refStart2));
                polynomials.addExponent(polynomials.getExponent(refStart));
            
                MasterList.add(polynomials.getCoefficientList().size() - 1);
                polynomials.setPolyEndPoint(polynomials.getCoefficientList().size() - 1);
                
                break;    
            }
        }
    }
    
    //method used to subtract polynomials
    //This uses the same code as addPoly() only marked parts below are changed
    public static void subtractPolys(){
        while (refStart <= refEnd || refStart2 <= refEnd2) {
            
            if (polynomials.getExponent(refStart) > polynomials.getExponent(refStart2)){
                polynomials.addCoefficient(polynomials.getCoefficient(refStart));
                polynomials.addExponent(polynomials.getExponent(refStart));
                
                MasterList.add(polynomials.getCoefficientList().size() - 1);
                
                if (refStart < refEnd){
                        refStart++;
                }
            }
            
            //this part subtracts rather than add
            if (polynomials.getExponent(refStart) == polynomials.getExponent(refStart2)){
                polynomials.addCoefficient(polynomials.getCoefficient(refStart) - polynomials.getCoefficient(refStart2));
                polynomials.addExponent(polynomials.getExponent(refStart));
                
                MasterList.add(polynomials.getCoefficientList().size() - 1);
                
                if (refStart < refEnd){
                    refStart++;
                }
                
                if (refStart2 < refEnd2){
                    refStart2++;
                }
            }
            
            //this part multiplies the second coefficient by -1
            if (polynomials.getExponent(refStart) < polynomials.getExponent(refStart2)){
                polynomials.addCoefficient(-(polynomials.getCoefficient(refStart2)));
                polynomials.addExponent(polynomials.getExponent(refStart2));
                
                MasterList.add(polynomials.getCoefficientList().size() - 1);
                
                if (refStart2 < refEnd2){
                    refStart2++;
                }
            }
            
            //This part subtracts rather than adds
            if (polynomials.getExponent(refStart) == 0 && 
                    polynomials.getExponent(refStart2) == 0){
                if (polynomials.getExponent(polynomials.getExponentList().size() - 1) == 0){
                    polynomials.setPolyEndPoint(polynomials.getCoefficientList().size() - 1);
                    
                    break;
                }
                polynomials.addCoefficient(polynomials.getCoefficient(refStart) - polynomials.getCoefficient(refStart2));
                polynomials.addExponent(polynomials.getExponent(refStart));
                
                MasterList.add(polynomials.getCoefficientList().size() - 1);
                polynomials.setPolyEndPoint(polynomials.getCoefficientList().size() - 1);
                
                break;
                    
            }
        }
    }
    
    //end
}
