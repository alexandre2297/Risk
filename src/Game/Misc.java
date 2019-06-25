package Game;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import static java.util.concurrent.ThreadLocalRandom.current;

public class Misc {

    public static double threeVTwo = 0.7453703703703703;

    /* Function to remove duplicates from an ArrayList
     */
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {

        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }


    /**
     * generates a random int between min and max including these
     * @param min int
     * @param max int
     * @return int
     */
    public static int RandomInt(int min, int max) {
        return current().nextInt(min, max + 1);
    }
}
