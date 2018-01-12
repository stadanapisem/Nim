package etf.nim.mm140593d.test;

import etf.nim.mm140593d.game.Game;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

public class QAgentTrainingTest {

     @Test public void training() {
        int wins[] = new int[2];
        int games = 0;

        for (int iterations = 0; iterations < 1; iterations++) {
            System.err.println("iter " + iterations);
            for (int i = 1; i <= 10; i++) {
                System.err.println("asdasdassadasd: " + i);
                List<int[]> subsets = GenerateCombinations.generate(i);

                for (int j = 0; j < subsets.size(); j++) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(i).append("\r\n");

                    for (int k = 0; k < i; k++) {
                        sb.append(subsets.get(j)[k]).append("\r\n");
                    }

                    //sb.append("4\r\n3\r\n6\r\n");
                    //sb.append("3\r\n6\r\n4\r\n");
                    sb.append("4\r\n4\r\n");

                    ByteArrayInputStream inputStream =
                        new ByteArrayInputStream(sb.toString().getBytes());
                    System.setIn(inputStream);
                    System.setOut(new PrintStream(new OutputStream() {
                        @Override public void write(int i) throws IOException {

                        }
                    }));
                    games++;
                    wins[new Game().gameInitialization()]++;
                }
            }
        }

        System.err.println(games + " " + wins[0] + " " + wins[1]);
    }
}
