package etf.nim.mm140593d.test;

import java.util.ArrayList;
import java.util.List;

public class GenerateCombinations {

    private static final int[] allObjectValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    private static int[] generateSubset(int[] subset) {
        int[] result = new int[subset.length];

        for (int i = 0; i < subset.length; i++) {
            result[i] = allObjectValues[subset[i]];
        }

        return result;
    }

    public static List<int[]> generate(int i) {
        List<int[]> subsets = new ArrayList<>();
        int[] s = new int[i];

        for (int j = 0; (s[j] = j) < i - 1; j++)
            ;
        subsets.add(generateSubset(s));

        while (true) {
            int j;

            for (j = i - 1; j >= 0 && s[j] == allObjectValues.length - i + j; j--)
                ;
            if (j < 0) {
                break;
            }

            s[j]++;
            for (++j; j < i; j++) {
                s[j] = s[j - 1] + 1;
            }

            subsets.add(generateSubset(s));
        }

        return subsets;
    }
}
