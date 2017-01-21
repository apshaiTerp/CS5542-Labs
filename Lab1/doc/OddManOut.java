package com.ac.junk;

import java.util.HashSet;
import java.util.Iterator;

/**
 * @author AC010168
 */
public class OddManOut {

  /**
   * @param args Command line arguments
   */
  public static void main(String[] args) {
    int[] allNumbers;
    //...  Fill out the array, however we get it

    OddManOut omo = new OddManOut();
    omo.findOddManOut(allNumbers);
  }
  
  /**
   * Helper Method to find the Odd Man Out.  My approach will be to add numbers to a 
   * HashSet as we find them, since we'll be using the contains() method a lot.
   * 
   * @param allNumbers Our list of numbers. M ost occur twice, but only one once.
   * @return The one odd number out
   */
  public int findOddManOut(int[] allNumbers) {
    //
    HashSet<Integer> oddManSet = new HashSet<Integer>();
    
    for (int i = 0; i < allNumbers.length; i++) {
      Integer value = new Integer(allNumbers[i]);
      //if we have a match, remove it
      if (oddManSet.contains(value)) oddManSet.remove(value);
      //if we have no match, add it
      else                           oddManSet.add(value);
    }
    
    Iterator<Integer> iter = oddManSet.iterator();
    int answer = iter.next();
    
    System.out.println ("The answer is " + answer);
    return answer;
  }
}
