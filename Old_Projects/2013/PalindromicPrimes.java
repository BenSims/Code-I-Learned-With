/* 
Ben Sims
17 Febuary 2013
This File: PalindromicPrimes.java
Required Files: none
Description: Programming exercise 5.26. Find first 100 palindromic primes.
Input: none
Output: Text
*/

public class PalindromicPrimes {
	public static void main(String[] args){
		System.out.println("The first 100 palindromic prime numbers are: \n");
		palindromicPrime(100);
		}
	
        //Method used to count and test the number of palindromic prime numbers
	public static void palindromicPrime(int numberOfPrimes){
            final int NUMBER_OF_PRIMES_PER_LINE = 10;
            int count = 0;
            int number = 2;
            
            while (count < numberOfPrimes){
                if (palindromic(number)){
                    if (isPrime(number)){
                        count++;
                        if (count % NUMBER_OF_PRIMES_PER_LINE == 0){
                            System.out.printf("%-6s\n", number);
                        }
                        else 
                            System.out.printf("%-6s", number);
                    }
                }
                number++;
            }
        }
	
        //Method used to determin if number is prime
        public static boolean isPrime(int number){
            for (int divisor = 2; divisor <= number / 2; divisor++){
                if (number % divisor == 0){
                    return false;
                }
            }
            
            return true; 
        }
        
	//Method used to determin if number is palindromic
	public static boolean palindromic(int number){
            int first = (number / 10000);
            int second = ((number - first * 10000) / 1000);
            int third = ((number - (first * 10000 + second * 1000)) / 100);
            int fourth = ((number - (first * 10000 + second * 1000 + third * 100)) / 10);
            int last = (number % 10);
            
            if ((first == last && second == fourth) || (first == 0 && second == last && third == fourth) || 
                    (first == 0 && second == 0 && third == last) || (first == 0 && second == 0 && third == 0 && fourth == last) || 
                    (first == 0 && second == 0 && third == 0 && fourth == 0 && last < 10)){
                
                return true;
            }
            else
                return false;
        }
}