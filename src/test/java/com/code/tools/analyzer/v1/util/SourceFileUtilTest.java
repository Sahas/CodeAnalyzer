package com.code.tools.analyzer.v1.util;

import com.code.tools.analyzer.v1.dto.SourceDetails;
import com.code.tools.analyzer.v1.utils.SourceFileUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SourceFileUtilTest {

  @Test
  public void test_FetchSourcesForFile_ShouldReturnSourceDetailsForFile(){
    List<SourceDetails> sourceDetailsList = SourceFileUtil.fetchSourceList("/Users/sahas.nanabala/AlterEgo/enthu/CodeAnalyzer/src/test/resources/Java_EventUtils.java");
    Assert.assertEquals(1, sourceDetailsList.size());
    Assert.assertEquals("java", sourceDetailsList.get(0).extension());
    Assert.assertEquals("Java_EventUtils.java", sourceDetailsList.get(0).fileName());
    Assert.assertEquals("/Users/sahas.nanabala/AlterEgo/enthu/CodeAnalyzer/src/test/resources/Java_EventUtils.java", sourceDetailsList.get(0).fileLocation());
  }

  @Test
  public void test_FetchSourcesForFolder_ShouldReturnSourceDetailsOfAllFilesInFolder(){
    List<SourceDetails> sourceDetailsList = SourceFileUtil.fetchSourceList("/Users/sahas.nanabala/AlterEgo/enthu/CodeAnalyzer/src/test/resources/testFolderFilesFetch");
    Assert.assertEquals(3, sourceDetailsList.size());
    Assert.assertEquals("java", sourceDetailsList.get(0).extension());
    Assert.assertEquals("java", sourceDetailsList.get(1).extension());
    Assert.assertEquals("java", sourceDetailsList.get(2).extension());
  }
}


