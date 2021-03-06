/*
Benjamin Sims 
24 Febuary 2013
This File: QuincunxGame.java
Required Files: none
Description: Programming exercise 6.23. Quincunx game.
Input: keybord
Output: Text
*/

import java.util.Scanner;

public class QuincunxGame{
    
    //The two variables the prject demanded
    static int balls = 0;
    static int rows = 0;
    
    //A veriable I used for dispay purposes
    static int ballCounter = 1;
    
    public static void main(String[] args){
        System.out.println("__________WELCOME__________");
        System.out.println("_________QUINCUNX__________");
        game();
    
    }
    
    //The main part of the game.
    public static void game(){ 
        Scanner input = new Scanner(System.in);
        System.out.println("Please input the number of balls used.");
        balls = input.nextInt();
        System.out.println("Please input the number of slots the Quincunx bord has.  For this number you must enter a number greater than 1.");
        rows = input.nextInt();
        
        //Test loop to ensure user inputs correct data.
        while (rows <= 1){  
            System.out.println("Please enter a number greater than 1.");
            rows = input.nextInt();
        }
        
        //int slots is used later for keeping the total of all the balls.
        int[] slots = new int[rows];
        
        //this "for" is placed here simulate each balls performance
        for (int i = 0; i < balls; i++){ 
            //first simulate the path the ball dropped.
            int[] path = rout(rows); 
            //then make each fall a 50/50 chance, and place them into the correct slot.
            int[] round = endPlace(path); 
            //then add the totals of all the falls.
            slots = total(round, slots);
            //display the falls for the user.
            displayBalls(path);  
            ballCounter++;
        }
        displaySlots(balls, slots);
    }
    
    //This method takes the number of rows and makes an array.  Then places a random number between 1 and 10 in each element.
    public static int[] rout(int numberOfPins){  
        int[] temp = new int[numberOfPins-1];
        for (int i = 0; i < numberOfPins-1; i++){ 
            temp[i] = (int)(Math.random() * 10) + 1;
        }	
        return temp;
    }
    
    //This method takes the random array and counts each number above 5.  This is done to simulate the ball falling right with a 50/50 chance.
    public static int[] endPlace(int[] list){   
        int[] temp = new int[list.length + 1];
        int count = 0;
        for (int i = 0; i < list.length; i++){
            if (list[i] > 5)
                count++;
        }
        temp[count] = 1;
        return temp;
    }
    
    //This method finds the letter that matches the fall direction
    public static void displayBalls(int[] permu){
        System.out.println("The path that ball " + ballCounter + " fell was:");
        for (int i = 0; i < permu.length; i++){
            if (permu[i] > 5)
                System.out.print("R");
            else 
                System.out.print("L");
        }
        System.out.println("");
    }
    
    //Method used to combined slots and round totals
    public static int[] total(int[] endRound, int[] total){ 
        for (int i = 0; i < endRound.length; i++){
            total[i] += endRound[i];
        }
        return total;
    }
    
    //Method used to dispay where the balls fell.
    public static void displaySlots(int ball, int[] totals){  
        //this let me use ball as two refrences. "temp" here is used to subtract from the y axis, and then check if a "0" should be added to the dispay
        int temp = ball;
        for (int j = 0; j < ball; j++){
            for (int i = 0; i < totals.length; i++){
                if (totals[i] >= temp){
                    System.out.print("|O|");
                }
                else
                    System.out.print("| |");
            }
            System.out.println("");
            temp--;
        }
    }
}
	