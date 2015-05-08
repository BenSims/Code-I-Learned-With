package Dir;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/* Class: CMSC 412
Student: Benjamin Sims 
Assignment: Week 5
Due Date: 21 Sep 2014
This File: DirectoryP.java
Required Files: none
Description: Homework 5 
Input: file system                         
Output: text
*/


public class DirectoryP {
    
    private static Scanner userInput = new Scanner(System.in);
    private static String directory = "";
    private static String safetyCheck = "";
    private static int userChoice;
    private static ArrayList directoryList = new ArrayList();
    private static ArrayList fileList = new ArrayList();
    
    public static void main(String[] args){
        
        System.out.println("Ben Sims, CMSC 412 Operating Systems"
                + ", HomeWork 5");
        
        displayMenu();
    }
    
    //This displays the options
    public static void displayMenu(){
        System.out.println("Select an option.");
        System.out.println("0 – Exit" + "\n1 - Select directory" + "\n2 - List "
                + "directory content (first level)" + "\n3 – List directory content"
                + " (all levels)" + "\n4 – Delete file" + "\n5 – Display file" +
                "\n6 – Encrypt file (XOR with password)" + "\n7 – Decrypt file "
                + "(XOR with password)");
        
        selectOption();
    }
    
    //Here the menu is available.  The program always goes back to this method.
    public static void selectOption() {
        userInput.reset();
        safetyCheck = userInput.next();
        
        //This part ensures that a number between 0 and 8 was entered, and that
        //only one character was entered
        if (safetyCheck.codePointAt(0) > 56 || safetyCheck.codePointAt(0) < 48
                || safetyCheck.length() != 1){
            System.out.println("Please try again.  Enter 8 to see options.");
            selectOption();
        }
        userChoice = Integer.valueOf(safetyCheck);
        
        switch (userChoice){
            case 0: exit();
                    break;
            case 1: selectDir();
                    break;
            case 2: listDirFirstLevel(directory);
                    break;
            case 3: { // this part was added to organize and display the directories
                      //from the files
                     directoryList.clear();
                     fileList.clear();
                     listDirAllLevel(directory);
                     for (int i = 0; i < directoryList.size(); i++){
                         System.out.println(directoryList.get(i));
                     }
                     for (int i = 0; i < fileList.size(); i++){
                        System.out.println(fileList.get(i));
                     }
                    }
                    selectOption();
                    break;
            
            case 4: deleteFile(directory);
                    break;
            case 5: displayFile(directory);
                    break;
            case 6: try {
                        encryptFile(directory);
                        break;
                }
            catch (Throwable e) {
			e.printStackTrace();
		}
            case 7: try {
                        decryptFile(directory);
                        break;
                }
            catch (Throwable e) {
			e.printStackTrace();
		}
            
            case 8: displayMenu();
                    break;
        }
    }
    
    //This method closes the program
    public static void exit(){
        System.out.println("Good Bye.");
        System.exit(0);
    }
    
    //Method used to select directory, and store it as a String
    public static void selectDir(){
        System.out.println("Enter the directory path you want to work with.  "
                + "Enter clear to reset the directory.");
        System.out.println(directory + ">");
        
        //I used double .nextLine() statments because that was the only way
        //I could get a spaces " " added into the string.  This proved useful
        //for files like "CMSC 412"
        userInput.nextLine();
        directory += userInput.nextLine().trim() + "\\";
        
        //This part will clear the directory String if clear is entered
        if (directory.contains("clear")){
            directory = "";
        }
        
        System.out.println("Done. Select next option for: " + directory + 
                "\nOr enter 8 to see options.");
        userInput.reset();
        selectOption();
    }
    
    //Method used to display current directory
    public static void listDirFirstLevel(String dir){
        try{
            File folder = new File(dir);
            File[] listOfFiles = folder.listFiles();
            
            //Directories are displayed with this part
            for (int i = 0; i < listOfFiles.length; i++){
                
                if (listOfFiles[i].isDirectory()) {
                    System.out.println("Directory: " + dir + listOfFiles[i].getName() + "\\");
                }
            }

            //Files are displayed with this part
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    System.out.println("File " + dir + listOfFiles[i].getName());
                } 
            }
        }
        catch (java.lang.RuntimeException e){
            System.out.println("No directory selected. Please select directory "
                    + "in option 1.");
            
            selectOption();
        }
        
        System.out.println("Done. Select next option for: " + directory + 
                "\nOr enter 8 to see options.");
        selectOption();
    }
    
    //This method works just like listDirFirstLevel, but it uses recursion to 
    //gather all levels of directories and files.
    public static void listDirAllLevel(String dir){
        int count;
        try{
            File folder = new File(dir);
            File[] listOfFiles = folder.listFiles();
            ArrayList<String> listOfDirectories = new ArrayList();
            
            //Directories are gathered with this part
            for (int i = 0; i < listOfFiles.length; i++){
                if (listOfFiles[i].isDirectory()) {
                    listOfDirectories.add(listOfFiles[i].getName());
                    directoryList.add("Directory: " + dir + listOfFiles[i].getName() + "\\");
                }
            }

            //Files are gathered with this part
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    fileList.add("File: " + dir + listOfFiles[i].getName());
                } 
            }
            
            //This portion is the recursive part
            count = listOfDirectories.size();
            while (count >= 0){
                count--;
                if (count < 0){
                    return;
                }
                listDirAllLevel(dir + listOfDirectories.get(count) + "\\");
            }
            
        }
        
        catch (java.lang.RuntimeException e){
            System.out.println("No directory selected. Please select directory "
                    + "in option 1.");
            
            selectOption();
        }
        
        System.out.println("Done. Select next option for: " + directory + 
                "\nOr enter 8 to see options.");
        selectOption();
    }
    
    //Mehtod used to delete files.
    public static void deleteFile(String dir){
        if (dir.compareTo("") == 0){
            System.out.println("No directory was selected.  Please enter a directory"
                    + " with option 1.");
            selectOption();
        }
        
        System.out.println("Please enter the file you wish to delete.");
        
        String fileName = dir + userInput.next();
        File xFile = new File(fileName);
        
        //This part checks to see if file exists
        if (!xFile.exists()){
            System.out.println("File does not exist, please check directory with"
                    + " option 2.");
            selectOption();
        }
        xFile.delete();
        
        
        System.out.println("Done. Select next option for: " + directory + 
                "\nOr enter 8 to see options.");
        selectOption();
    }
    
    //Method used to display files.
    public static void displayFile(String dir) {
        if (dir.compareTo("") == 0){
            System.out.println("No directory was selected.  Please enter a directory"
                    + " with option 1.");
            selectOption();
        }
        
        System.out.println("Please enter the file you wish to display.");
        String fileName = dir + userInput.next();
        File file = new File(fileName);
        
        //This part displays the file, and chatches an errer if it does not exist
        try{
            FileInputStream fis = new FileInputStream(file);
            int content;
            while ((content = fis.read()) != -1) {
                System.out.print((char) content);
            }
            fis.close();
        }
        catch (IOException e){
            System.out.println("File does not exist, please check directory with"
                    + " option 2.");
            selectOption();
        }
        
        System.out.println("\nDone. Select next option for: " + directory + 
                "\nOr enter 8 to see options.");
        selectOption();
        
    }
    
    //Method used to encrypt files
    public static void encryptFile(String dir)throws Throwable{
        if (dir.compareTo("") == 0){
            System.out.println("No directory was selected.  Please enter a directory"
                    + " with option 1.");
            selectOption();
        }
            //This part will chatch an error if no file was found
        try{
            System.out.println("Enter file to be encrypted.");
            String selectedFile = dir + userInput.next();
            FileInputStream fis = new FileInputStream(selectedFile);
        
            System.out.println("Please enter the name of the new file.");
            String eFile = dir + userInput.next();
            FileOutputStream fos = new FileOutputStream(eFile);
            String key = "";
        
            while (key.length() < 8){
                System.out.println("Please enter a password, that is at least 8 characters.");
                key = userInput.next();
            }
            encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, fis, fos);
            fis.close();
            fos.close();
        }
        catch (java.io.FileNotFoundException e){
            System.out.println("The file did not exist.  Please check the directory"
                    + " with option 2.");
            
            selectOption();
        }
        
        System.out.println("Done. Select next option for: " + directory + 
                "\nOr enter 8 to see options.");
        selectOption();
    }
    
    //Method used to decrypt files
    public static void decryptFile(String dir)throws Throwable{
        if (dir.compareTo("") == 0){
            System.out.println("No directory was selected.  Please enter a directory"
                    + " with option 1.");
            selectOption();
        }
        
        //This part decrypts files and catches an error if no file existed
        try{
            System.out.println("Enter file to be decrypted.");
            String eFile = dir + userInput.next();
            FileInputStream fis = new FileInputStream(eFile);
        
            System.out.println("Please enter the name of the new file.");
            String newFile = dir + userInput.next();
            FileOutputStream fos = new FileOutputStream(newFile);
            String key = "";
        
            while (key.length() < 8){
                System.out.println("Please enter a password, that is at least 8 characters.");
                key = userInput.next();
            }
            encryptOrDecrypt(key, Cipher.DECRYPT_MODE, fis, fos);
            fis.close();
            fos.close();
        }
        catch (java.io.FileNotFoundException e){
            System.out.println("The file did not exist.  Please check the directory"
                    + " with option 2.");
            
            selectOption();
        }
        
        System.out.println("Done. Select next option for: " + directory + 
                "\nOr enter 8 to see options.");
        selectOption();
    }
    
    //Method used to encrypt and decrypt files 
    public static void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os) throws Throwable {

		DESKeySpec dks = new DESKeySpec(key.getBytes());
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		SecretKey desKey = skf.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE

		if (mode == Cipher.ENCRYPT_MODE) {
			cipher.init(Cipher.ENCRYPT_MODE, desKey);
			CipherInputStream cis = new CipherInputStream(is, cipher);
			doCopy(cis, os);
		} else if (mode == Cipher.DECRYPT_MODE) {
			cipher.init(Cipher.DECRYPT_MODE, desKey);
			CipherOutputStream cos = new CipherOutputStream(os, cipher);
			doCopy(is, cos);
		}
	}
    
    //Method used to make process encryption and decryption
    public static void doCopy(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[64];
	int numBytes;
	while ((numBytes = is.read(bytes)) != -1) {
            os.write(bytes, 0, numBytes);
	}
	os.flush();
	os.close();
	is.close();
    }
}
