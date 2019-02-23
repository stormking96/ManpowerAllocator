/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manpowerallocator;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author storm
 */
public class QuickSorter 
{
    private ArrayList<Date> array = new ArrayList<>();
    private ArrayList<String> allDataArray = new ArrayList<>();
    private int length;
    
    public void sort(ArrayList<Date> inputArr, ArrayList<String> allDataArr) {
         
        if (inputArr == null || inputArr.isEmpty()) {
            return;
        }
        this.array = inputArr;
        this.allDataArray = allDataArr;
        length = inputArr.size();
        quickSort(0, length - 1);
    }
 
    private void quickSort(int lowerIndex, int higherIndex) {
         
        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot number, I am taking pivot as middle index number
        Date pivot = array.get(lowerIndex+(higherIndex-lowerIndex)/2);
        // Divide into two arrays
        while (i <= j) {
            /**
             * In each iteration, we will identify a number from left side which 
             * is greater then the pivot value, and also we will identify a number 
             * from right side which is less then the pivot value. Once the search 
             * is done, then we exchange both numbers.
             */
            while (array.get(i).before(pivot)) {
                i++;
            }
            while (array.get(j).after(pivot)) {
                j--;
            }
            if (i <= j) {
                exchangeNumbers(i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j)
            quickSort(lowerIndex, j);
        if (i < higherIndex)
            quickSort(i, higherIndex);
    }
 
    private void exchangeNumbers(int i, int j) {
        Date temp1 = array.get(i);
        array.set(i, array.get(j));
        array.set(j, temp1);
        
        String temp2 = allDataArray.get(i);
        allDataArray.set(i, allDataArray.get(j));
        allDataArray.set(j, temp2);
    }
    
    
}
