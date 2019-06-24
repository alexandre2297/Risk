package Game;
import IA.Pair;
import IA.Triple;

import java.util.ArrayList;
import java.util.Random;

public class QuickSortForPair {
    private static ArrayList<Pair<Integer, Country>> inputArray = new ArrayList<>();


    public QuickSortForPair(ArrayList<Pair<Integer, Country>> inputArray){
        QuickSortForPair.inputArray = inputArray;
    }

    public void startQuickStart(int start,int end){
        int q;
        if(start<end){
            q = partition(start, end);
            startQuickStart(start, q);
            startQuickStart(q+1, end);
        }
    }

    public ArrayList<Pair<Integer, Country>> getSortedArray(){
        this.startQuickStart(0, QuickSortForPair.inputArray.size()-1);
        return QuickSortForPair.inputArray;
    }

    int partition(int start,int end){

        int init = start;
        int length = end;

        Random r = new Random();
        int pivotIndex = nextIntInRange(start,end,r);
        Pair<Integer, Country> pivot = inputArray.get(pivotIndex);


        while(true){
            while(inputArray.get(length).first > pivot.first && length>start){
                length--;
            }

            while(inputArray.get(init).first < pivot.first && init<end){
                init++;
            }

            if(init<length){
                Pair<Integer, Country> temp = inputArray.get(init);
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

