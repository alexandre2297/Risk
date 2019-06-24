package Game;
import IA.Triple;

import java.util.ArrayList;
import java.util.Random;

public class QuickSortForTriple {
    private static ArrayList<Triple<Country, Country, Integer>> inputArray = new ArrayList<>();


    public QuickSortForTriple(ArrayList<Triple<Country, Country, Integer>> inputArray){
        QuickSortForTriple.inputArray = inputArray;
    }

    public void startQuickStart(int start,int end){
        int q;
        if(start<end){
            q = partition(start, end);
            startQuickStart(start, q);
            startQuickStart(q+1, end);
        }
    }

    public ArrayList<Triple<Country, Country, Integer>> getSortedArray(){
        this.startQuickStart(0, QuickSortForTriple.inputArray.size()-1);
        return QuickSortForTriple.inputArray;
    }

    int partition(int start,int end){

        int init = start;
        int length = end;

        Random r = new Random();
        int pivotIndex = nextIntInRange(start,end,r);
        Triple<Country, Country, Integer> pivot = inputArray.get(pivotIndex);


        while(true){
            while(inputArray.get(length).third > pivot.third && length>start){
                length--;
            }

            while(inputArray.get(init).third < pivot.third && init<end){
                init++;
            }

            if(init<length){
                Triple<Country, Country, Integer> temp = inputArray.get(init);
                inputArray.set(init,inputArray.get(length));
                inputArray.set(length,temp);
                length--;
                init++;

            } else {
                return length;
            }
        }

    }

    // Below method is to just find random integer from given range
    static private int nextIntInRange(int min, int max, Random rng) {
        if (min > max) {
            throw new IllegalArgumentException("Cannot draw random int from invalid range [" + min + ", " + max + "].");
        }
        int diff = max - min;
        if (diff >= 0 && diff != Integer.MAX_VALUE) {
            return (min + rng.nextInt(diff + 1));
        }
        int i;
        do {
            i = rng.nextInt();
        } while (i < min || i > max);
        return i;
    }
}

