package com.code.tools.analyzer.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
public class SourceAnalysisReport {

  private List<String> sourceFiles;
  private int numBlankLines;
  private int numCommentLines;
  private int numCodeLines;
  private int numTotalLines;
  private int numMultiCommentLines;
  private CodeStatementReport codeReport;

  public String toString() {
    String codeStatementClassification = String.format(
      " \t Imports: %d \n \t Class Declarations: %d \n \t Variable  Declarations: %d \n \t Conditionals: %d \n \t Loops:  %d \n \t ReturnStatements:  %d \n",
      codeReport.numImports(), codeReport.numClassDeclarations(), codeReport.numVarDeclarations(),
      codeReport.numConditionals(), codeReport.numLoops(), codeReport.numReturnStatements());
    return String
      .format(" Blank: %d \n Comments: %d \n Multi-Line Comments: %d \n Code: %d \n Breakup: \n %s Total: %d \n",
        numBlankLines,
        numCommentLines, numMultiCommentLines, numCodeLines, codeStatementClassification, numTotalLines);
  }

  public String toStringWithSources() {
    return String
      .format(
        " Analysis for the following sources: %s ", sourceFiles) + toString();
  }
}
