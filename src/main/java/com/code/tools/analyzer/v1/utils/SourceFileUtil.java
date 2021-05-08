package com.code.tools.analyzer.v1.utils;

import com.code.tools.analyzer.v1.dto.SourceDetails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SourceFileUtil {
  private SourceFileUtil() {
  }

  public static List<SourceDetails> fetchSourceList(final String path) {
    return getSourceDetailsFromPath(path);
  }

  public static List<SourceDetails> getSourceDetailsFromPath(String path) {
    File file = new File(path);

    if (!file.exists()) {
      throw new RuntimeException("Unable to find a file/directory. Please check the path: " + path);
    } else if (file.isFile()) {
      SourceDetails fileDetails = getFileSourceDetails(file);
      return new ArrayList<>(Arrays.asList(fileDetails));
    } else {
      return getDirectorySourceDetails(file);
    }
  }

  public static List<String> getLinesInFile(String path){
    try {
      return FileUtils.readLines(new  File(path), StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException("Unable to read the file at : " + path, e);
    }
  }

  private static SourceDetails getFileSourceDetails(File file) {
    return SourceDetails.builder().fileName(file.getName())
      .extension(FilenameUtils.getExtension(file.getName())).fileLocation(file.getAbsolutePath()).build();
  }

  private static List<SourceDetails> getDirectorySourceDetails(File folder) {
    List<SourceDetails> sourceDetailsList = new ArrayList<>();
    Collection<File> allFiles = FileUtils.listFiles(folder, null, true);
    for (File file : allFiles) {
      sourceDetailsList.add(getFileSourceDetails(file));
    }
    return sourceDetailsList;
  }
}
