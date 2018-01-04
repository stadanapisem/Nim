package etf.nim.mm140593d.test;

import etf.nim.mm140593d.game.Game;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class QAgentTrainingTest {

    public static final int[] allObjectValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    int[] generateSubset(int[] subset) {
        int[] result = new int[subset.length];

        for (int i = 0; i < subset.length; i++) {
            result[i] = allObjectValues[subset[i]];
        }

        return result;
    }

    @Test public void traning() {

        for (int iterations = 0; iterations < 1; iterations++) {
            System.err.println("iter " + iterations);
            for (int i = 1; i < 10; i++) {
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

                for (int j = 0; j < subsets.size(); j++) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(i).append("\r\n");

                    for (int k = 0; k < i; k++) {
                        sb.append(subsets.get(j)[k]).append("\r\n");
                    }

                    sb.append("4\r\n4\r\n");

                    ByteArrayInputStream inputStream =
                        new ByteArrayInputStream(sb.toString().getBytes());
                    System.setIn(inputStream);
                    System.setOut(new PrintStream(new OutputStream() {
                        @Override public void write(int i) throws IOException {

                        }
                    }));

                    new Game().gameInitialization();
                }
            }
        }
    }
}
