package com.code.tools.analyzer;

import com.code.tools.analyzer.v1.dto.SourceAnalysisReport;
import com.code.tools.analyzer.v1.dto.SourceDetails;
import com.code.tools.analyzer.v1.service.JavaSyntaxAnalyzer;
import com.code.tools.analyzer.v1.service.SourceProcessor;
import com.code.tools.analyzer.v1.utils.SourceFileUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CodeAnalyzerTest {

  @Test
  public void test_SpringBootMainAnalysis(){
    SourceProcessor processor = SourceProcessor.buildWithAnalyzers(new JavaSyntaxAnalyzer());
    List<SourceDetails> sourceDetailsList =  SourceFileUtil.fetchSourceList("/Users/sahas.nanabala/AlterEgo/enthu/CodeAnalyzer/src/test/resources/Java_SpringBootMain.java");
    SourceAnalysisReport report = processor.processAndAggregate(sourceDetailsList);

    Assert.assertEquals(3, report.numBlankLines());
    Assert.assertEquals(10, report.numCodeLines());
    Assert.assertEquals(1, report.numCommentLines());
    Assert.assertEquals(1, report.numMultiCommentLines());
    Assert.assertEquals(18, report.numTotalLines());
    Assert.assertEquals(1, report.codeReport().numClassDeclarations());
    Assert.assertEquals(0, report.codeReport().numConditionals());
    Assert.assertEquals(2, report.codeReport().numImports());
    Assert.assertEquals(0, report.codeReport().numLoops());
    Assert.assertEquals(0, report.codeReport().numReturnStatements());
    Assert.assertEquals(1, report.codeReport().numVarDeclarations());
  }
}
