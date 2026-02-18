
import java.util.ArrayList;

public class BruteForceSearch {

    public static boolean bruteForceSearch(ArrayList<Integer> list, int target) {
        for (int value : list) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {

    }
}
