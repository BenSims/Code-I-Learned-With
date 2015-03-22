
import java.util.ArrayList;

/* Class: CMSC 350
Student: Benjamin Sims 
Assignment: Week 2
Due Date: 31 Aug 2014
This File: polynomials.java
Required Files: integer text file
Description: project 1 
Input: integer text file                         
Output: Text
*/


public class Polynomials  {
    
    public ArrayList<Integer> coefficient = new ArrayList();
    public ArrayList<Integer> exponent = new ArrayList();
    
    public ArrayList<Integer> polyStart = new ArrayList();
    public ArrayList<Integer> polyEnd = new ArrayList();
    
    
    public Polynomials(){
        
    }
    
    //Coefficient methods
    public void addCoefficient(int num){
        coefficient.add(num);
    }
    
    public ArrayList<Integer> getCoefficientList(){
        return coefficient;
    }
    
    public int getCoefficient(int index){
        return coefficient.get(index);
    }
    
    //Exponent Methods
    public void addExponent(int num){
        exponent.add(num);
    }
    
    public ArrayList<Integer> getExponentList(){
        return exponent;
    }
    
    public int getExponent(int index){
        return exponent.get(index);
    }
    
    //StartPoint Methods
    public void setPolyStartPoint(int num){
        
        polyStart.add(num);
    }
    
    public ArrayList<Integer> getStartPointList(){
        return polyStart;
    }
    
    public int getStartPoint(int index){
        return polyStart.get(index);
    }
    
    //EndPoint Methods
    public void setPolyEndPoint(int num){
        
        polyEnd.add(num);
    }
    
    public ArrayList<Integer> getEndPointList(){
        return polyEnd;
    }
    
    public int getEndPoint(int index){
        return polyEnd.get(index);
    }
}
