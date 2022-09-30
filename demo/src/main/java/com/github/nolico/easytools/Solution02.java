package com.github.nolico.easytools;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

public class Solution02 {
  public static void main(String[] args) throws Exception {
    String coverageFilePath = args[0];
    String writeFilePath = args[1];

    Solution02 sol2 = new Solution02();
    sol2.readCoverageCsv(coverageFilePath);
    sol2.writeCoverageCsv(writeFilePath);
  }

  public void readCoverageCsv(String fileAbsolutePath) throws IOException {
    List<String[]> lines = null;
    try (CSVReader reader = new CSVReader(new FileReader(fileAbsolutePath))) {
        lines = reader.readAll();
    }

    lines.forEach(line -> {
      String className = line[0];
      List<String> covered = Arrays.asList(line[1].split(","));
      List<String> uncovered = Arrays.asList(line[2].split(","));
      getListDifference(className, covered, uncovered);
    });

  }

  public void writeCoverageCsv(String outputFilePath) throws IOException {
    FileWriter writer = new FileWriter(outputFilePath);
    for (String className : classCoverageMap.keySet()) {
      String line = className + "," + new BigDecimal(classCoverageMap.get(className)).setScale(3, RoundingMode.HALF_UP).toString();
      writer.write(line);
      writer.write(System.lineSeparator());
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

    addNoDuplicate(uncoverList, uncovered);
    removeInList(uncoverList, covered);

    coverageRateMap.put("coverList", coverList);
    coverageRateMap.put("uncoverList", uncoverList);

    coverageMap.put(className, coverageRateMap);

    Double dblRate = 0d;
    if (uncoverList.size() == 0){
      dblRate = 1.00d;
    } else {
      dblRate = Double.valueOf(coverList.size()) / Double.valueOf(coverList.size() + uncoverList.size());
    }
    classCoverageMap.put(className, dblRate);
  }

  private void addNoDuplicate(List<String> targetList, List<String> sourceArr) {
    for (int i = 0; i < sourceArr.size(); i++) {
      String challenge = sourceArr.get(i).replace("[", "").replace("]", "");
      if (!targetList.contains(challenge)) {
        targetList.add(challenge);
      }
    }
  }

  private void removeInList(List<String> targetList, List<String> sourceArr) {
    if (targetList == null || targetList.isEmpty()) {
      return;
    }
    for (int i = 0; i < sourceArr.size(); i++) {
      String challenge = sourceArr.get(i).replace("[", "").replace("]", "");
      if (targetList.contains(challenge)) {
        targetList.remove(challenge);
      }
    }

  }

}
