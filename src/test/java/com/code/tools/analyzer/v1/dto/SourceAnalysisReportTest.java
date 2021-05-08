package com.code.tools.analyzer.v1.dto;

import org.junit.Test;

import java.util.Arrays;

public class SourceAnalysisReportTest {

  @Test
  public void printOutput() {
    CodeStatementReport codeReport = CodeStatementReport.builder().numClassDeclarations(10).numConditionals(20)
      .numImports(10).numLoops(4).numReturnStatements(5).numVarDeclarations(10).build();
    SourceAnalysisReport report = SourceAnalysisReport.builder().sourceFiles(Arrays.asList("FileA.java"))
      .numBlankLines(10).numCodeLines(10).numCommentLines(10).numTotalLines(30)
      .codeReport(codeReport)
      .build();
    System.out.println(report);
  }

  @Test
  public void printOutputWithFiles() {
    CodeStatementReport codeReport = CodeStatementReport.builder().numClassDeclarations(10).numConditionals(20)
      .numImports(10).numLoops(4).numReturnStatements(5).numVarDeclarations(10).build();
    SourceAnalysisReport report = SourceAnalysisReport.builder().sourceFiles(Arrays.asList("FileA.java", "FileB.java"))
      .numBlankLines(10).numCodeLines(10).numCommentLines(10).numTotalLines(30)
      .codeReport(codeReport).build();
    System.out.println(report.toStringWithSources());
  }
}
