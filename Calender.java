/* Class: 1302CMIS2427982
Student: Benjamin Sims 
Assignment: Week 8
Due Date: 12 May 2013
This File: .java
Required Files: none
Description: Final Project
Usage:  
Input: none
Output: Calendar
*/

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Calender extends JFrame{
	
	private static final long serialVersionUID = 1L;
	/*Make counters for the number of spaces needed to be blank, and number of 
    days in the month, and for the number of rows the calendar will have*/
    
    public static void main(String[] args){
        Calender frame = new Calender();
        frame.pack();
        frame.setMinimumSize(null);
        frame.setTitle("Ben's Calender");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    public Calender(){
        int startday;
        int daysOfTheMonth;
        int rowCounter;
        Calendar mainCalendar = new GregorianCalendar();
        
        //Make panel for dates
        JPanel body = new JPanel();
        body.setLayout(new GridLayout(0, 7, 0, -5));
        
        //Fill body with date and blank labels based on the current date.
        startday = setUp.getStartDay(mainCalendar.YEAR, mainCalendar.MONTH);
        daysOfTheMonth = setUp.getNumberOfDaysInMonth(mainCalendar.YEAR, mainCalendar.MONTH);
        
        for (int i = 0; i < startday; i++){
            body.add(new date());
        }
        for (int i = 1; i <= daysOfTheMonth; i++)
            body.add(new date("" + i));
        
        //if statement that decides how many pixels are needed for the height
        if (startday + daysOfTheMonth > 35)
            rowCounter = 550;
        else 
            rowCounter = 455;
        
        //Set size based on rowCounter
        body.setPreferredSize(new Dimension(530, rowCounter));
        add(body, BorderLayout.CENTER);
        
        //Make and add week object
        week weekText = new week();
        add(weekText, BorderLayout.NORTH);
        
    }
    

}

class setUp {
    
    public setUp(){  
    }
    
    //Get start day of the week for first day of the month
    public static int getStartDay(int year, int month){
        final int START_DAY_FOR_JAN_1_1800 = 3;
        int totalNumberOfDays = getTotalNumberOfDays(year, month);
        
        return (totalNumberOfDays + START_DAY_FOR_JAN_1_1800) % 7;
    }
    
    //Get the total number of days since Jan 1 1970
    public static int getTotalNumberOfDays(int year, int month){
        int total = 0;
        
        for (int i = 1800; i < year; i++)
            if (isLeapYear(i))
                total += 366;
            else 
                total += 365;
        
        for (int i = 0; i <= month; i++)
            total += getNumberOfDaysInMonth(year, i);
        return total;
    }
    
    //Get the number of days in a month
    public static int getNumberOfDaysInMonth(int year, int month){
        if (month == 0 || month == 2 || month == 4 || month == 6 ||
                month == 7 || month == 9 || month == 11)
            return 31;
        if (month == 3 || month == 5 || month == 8 || month == 10)
            return 30;
        if (month == 1)
            return isLeapYear(year) ? 29 : 28;
        return 12;
    }
    
    //Check if a year is a leap year
    public static boolean isLeapYear(int year){
        return year % 400 == 0 || (year % 4 == 0 && year % 100 != 0);
    }
}

class date extends JPanel {
    private JLabel jlblDate = new JLabel();
    LineBorder lineBorder = new LineBorder(Color.BLACK, 1);
    
    //Blank JLabel
    public date(){
    }
    
    public date(String day){
        
        //Set up for each date 
        jlblDate.setText(day);
        jlblDate.setPreferredSize(new Dimension(75, 90));
        jlblDate.setHorizontalAlignment(JLabel.RIGHT);
        jlblDate.setVerticalAlignment(JLabel.TOP);
        jlblDate.setHorizontalTextPosition(SwingConstants.RIGHT);
        jlblDate.setVerticalTextPosition(JLabel.TOP);
        jlblDate.setBorder(lineBorder);
        
        //If statment make the current day highlighted red
        if (jlblDate.getText().compareTo("" + Calendar.DAY_OF_MONTH) == 0){
            jlblDate.setBorder(new LineBorder(Color.RED, 3));
            jlblDate.setToolTipText("Today's Date");
        }
        add(jlblDate);
        
        
    }
    
    

}class week extends JPanel{
    //Make JLabels for the date and days of the week
    private JLabel weekDays = new JLabel();
    private JLabel monthYear = new JLabel();
    
    public week(){
        //Make main panel and set weekDay's properties
        JPanel body = new JPanel();
        body.setLayout(new BorderLayout());
        weekDays.setText("Sunday         Monday          Tuesday        Wednesday       Thursday        Friday          Saturday");
        weekDays.setVerticalAlignment(JLabel.BOTTOM);
        weekDays.setVerticalTextPosition(JLabel.BOTTOM);
        
        //set monthYear's properties
        monthYear.setText(getMonth(Calendar.MONTH) + " " + Calendar.YEAR);
        monthYear.setHorizontalTextPosition(JLabel.CENTER);
        monthYear.setHorizontalAlignment(JLabel.CENTER);
        
        body.add(monthYear, BorderLayout.NORTH);
        body.add(weekDays, BorderLayout.SOUTH);
        
        add(body);
    }
    
    //Method used to convert Calender.MONTH into a word
    public static String getMonth(int month){
        String monthName = "";
        switch (month){
            case 0: monthName = "January"; break;
            case 1: monthName = "February"; break;
            case 2: monthName = "March"; break;
            case 3: monthName = "April"; break;
            case 4: monthName = "May"; break;
            case 5: monthName = "June"; break;
            case 6: monthName = "July"; break;
            case 7: monthName = "August"; break;
            case 8: monthName = "September"; break;
            case 9: monthName = "October"; break;
            case 10: monthName = "November"; break;
            case 11: monthName = "December"; break;
        }
        
        return monthName;
    }

}

