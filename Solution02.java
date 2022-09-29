import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;;

public class Solution02 {
  public static void main(String[] args) throws Exception {
    String coverageFilePath = args[0];
    String writeFilePath = args[1];

    Solution02 sol2 = new Solution02();
    sol2.readCoverageCsv(coverageFilePath);
    sol2.writeCoverageCsv(writeFilePath);
  }

  public void readCoverageCsv(String fileAbsolutePath) throws FileNotFoundException {
    File csvFile = new File(fileAbsolutePath);
    Scanner reader = new Scanner(csvFile);

    while (reader.hasNextLine()) {
      String line = reader.nextLine();
      String[] cols = line.split(",");
      String className = cols[0];
      List<String> covered = Arrays.asList(cols[1].split(","));
      List<String> uncovered = Arrays.asList(cols[2].split(","));
      getListDifference(className, covered, uncovered);
    }
    reader.close();
  }

  public void writeCoverageCsv(String outputFilePath) throws IOException {
    FileWriter writer = new FileWriter(outputFilePath);
    for (String className : classCoverageMap.keySet()) {
      String line = className + "," + new BigDecimal(classCoverageMap.get(className)).setScale(3, RoundingMode.HALF_UP).toString();
      writer.write(line);
    }
    writer.close();
  }

  public Map<String, Map<String, List<String>>> coverageMap;
  public Map<String, Double> classCoverageMap; 

  private void getListDifference(String className, List<String> covered, List<String> uncovered) {
    if (coverageMap == null) {
      coverageMap = new HashMap<String, Map<String, List<String>>>();
      classCoverageMap = new HashMap<String, Double>();
    }
    Map<String, List<String>> coverageRateMap = coverageMap.get(className);
    if (coverageRateMap == null) {
      coverageRateMap = new HashMap<String, List<String>>();
    }
    List<String> coverList = coverageRateMap.get("coverList");
    List<String> uncoverList = coverageRateMap.get("uncoverList");
    if (coverList == null) {
      coverList = new ArrayList<String>();
    }
    if (uncoverList == null) {
      uncoverList = new ArrayList<String>();
    }

    addNoDuplicate(coverList, covered);
    removeInList(coverList, uncovered);

    addNoDuplicate(uncoverList, uncovered);
    removeInList(uncoverList, covered);

    coverageRateMap.put("coverList", coverList);
    coverageRateMap.put("uncoverList", uncoverList);

    coverageMap.put(className, coverageRateMap);

    Double dblRate = Double.valueOf(coverList.size() / coverList.size() + uncoverList.size());
    classCoverageMap.put(className, dblRate);
  }

  private void addNoDuplicate(List<String> targetList, List<String> sourceArr) {
    for (int i = 0; i < sourceArr.size(); i++) {
      if (!targetList.contains(sourceArr.get(i))) {
        targetList.add(sourceArr.get(i));
      }
    }
  }

  private void removeInList(List<String> targetList, List<String> sourceArr) {
    if (targetList == null || targetList.isEmpty()) {
      return;
    }
    for (int i = 0; i < sourceArr.size(); i++) {
      if (targetList.contains(sourceArr.get(i))) {
        targetList.remove(sourceArr.get(i));
      }
    }

  }

}
