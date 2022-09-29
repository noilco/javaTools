import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * Complete the 'onesAndTwos' function below.
     *
     * The function is expected to return an INTEGER.
     * The function accepts following parameters:
     *  1. INTEGER a
     *  2. INTEGER b
     */

    public static int onesAndTwos(int a, int b) {
    // Write your code here
        if (a == 0 && b == 0) return 0;
        List<Integer> resList = new ArrayList<Integer>();
        List<Integer> calcList = new ArrayList<Integer>();
        List<Integer> conbineList = new ArrayList<Integer>();
        if (a != 0) {
            resList.add(1);
            for (int i = 0; i < a; i++) {
                conbineList.add(1);
            }
        }
        if (b != 0) {
            resList.add(2);
            for (int i = 0; i < b; i++) {
                conbineList.add(2);
            }
        }

        int calcIndex = 0;
        int currentIndex = 0;
        for (int i = 0; i < conbineList.size() - 1; i++) {
            if (i == 0) {
                calcList.add(conbineList.get(i));
            }
            currentIndex = calcList.size();
            List<Integer> tmpList = new ArrayList<Integer>();
            for (int j = calcIndex; j < calcList.size(); j++) {
                tmpList.add(calcList.get(j) + conbineList.get(i + 1));
                tmpList.add(calcList.get(j) * conbineList.get(i + 1));
            }
            calcList.addAll(tmpList);
            calcIndex = currentIndex;
        }
        for (int i = 0; i < calcList.size(); i++) {
            Result.addNoDuplicate(calcList.get(i), resList);
        }
        return resList.size();
    }
    
    public static void addNoDuplicate(int charenge, List<Integer> resList) {
        if (!resList.contains(charenge)) resList.add(charenge);
    }

}

public class Solution01 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int t = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, t).forEach(tItr -> {
            try {
                String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

                int a = Integer.parseInt(firstMultipleInput[0]);

                int b = Integer.parseInt(firstMultipleInput[1]);

                int result = Result.onesAndTwos(a, b);

                bufferedWriter.write(String.valueOf(result));
                bufferedWriter.newLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        bufferedReader.close();
        bufferedWriter.close();
    }
}
