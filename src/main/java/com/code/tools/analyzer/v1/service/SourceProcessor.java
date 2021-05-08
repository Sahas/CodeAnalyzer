package com.code.tools.analyzer.v1.service;

import com.code.tools.analyzer.v1.dto.CodeStatementReport;
import com.code.tools.analyzer.v1.dto.SourceAnalysisReport;
import com.code.tools.analyzer.v1.dto.SourceDetails;
import com.code.tools.analyzer.v1.utils.SourceFileUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@Slf4j
public class SourceProcessor {
  private final Map<String, SyntaxAnalyzer> extAnalyzerMap;

  public static SourceProcessor buildWithAnalyzers(SyntaxAnalyzer... analyzers) {
    Map<String, SyntaxAnalyzer> extAnalyzerMap = new HashMap<>();
    for (SyntaxAnalyzer analyzer : analyzers) {
      for (String supportedExtension : analyzer.supportedExtensions()) {
        extAnalyzerMap.put(supportedExtension.toLowerCase(), analyzer);
      }
    }
    return SourceProcessor.builder().extAnalyzerMap(extAnalyzerMap).build();
  }

  public List<SourceAnalysisReport> process(final List<SourceDetails> sourceDetailsList) {

    if (sourceDetailsList.size() > 1) {
      log.info("Detected multiple files in the given path. Will use appropriate syntax analyzers");
    }
    final List<SourceAnalysisReport> reports = new ArrayList<>();
    for (SourceDetails source : sourceDetailsList) {
      SyntaxAnalyzer analyzer = extAnalyzerMap.get(source.extension().toLowerCase());
      if (Objects.isNull(analyzer)) {
        log.error("Sorry.The " + source.extension() + "files are not supported for the analysis. File = " + source
          .fileLocation());
        continue;
      }
      log.info("Using {} to analyse the file {}", analyzer.getClass().getSimpleName(), source.fileName());
      List<String> lines = SourceFileUtil.getLinesInFile(source.fileLocation());
      reports.add(analyzer.analyze(lines));
    }
    return reports;
  }

  public SourceAnalysisReport processAndAggregate(final List<SourceDetails> sourceDetailsList) {
    List<SourceAnalysisReport> reports = process(sourceDetailsList);
    SourceAnalysisReport aggregatedReport = SourceAnalysisReport.builder().codeReport(CodeStatementReport.builder().build()).sourceFiles(new ArrayList<>()).build();
    for (SourceAnalysisReport report : reports) {

      aggregatedReport.numBlankLines(aggregatedReport.numBlankLines() + report.numBlankLines());
      aggregatedReport.numCommentLines(aggregatedReport.numCommentLines() + report.numCommentLines());
      aggregatedReport.numMultiCommentLines(aggregatedReport.numMultiCommentLines() + report.numMultiCommentLines());
      aggregatedReport.numCodeLines(aggregatedReport.numCodeLines() + report.numCodeLines());
      aggregatedReport.numTotalLines(aggregatedReport.numTotalLines() + report.numTotalLines());

      CodeStatementReport aggregatedCodeReport = aggregatedReport.codeReport();
      aggregatedCodeReport.numImports(
        aggregatedCodeReport.numImports() + report.codeReport().numImports());
      aggregatedCodeReport.numClassDeclarations(
        aggregatedCodeReport.numClassDeclarations() + report.codeReport()
          .numClassDeclarations());
      aggregatedCodeReport.numReturnStatements(
        aggregatedCodeReport.numReturnStatements() + report.codeReport().numReturnStatements());
      aggregatedCodeReport.numVarDeclarations(
        aggregatedCodeReport.numVarDeclarations() + report.codeReport().numVarDeclarations());
      aggregatedCodeReport.numConditionals(
        aggregatedCodeReport.numConditionals() + report.codeReport().numConditionals());
      aggregatedCodeReport
        .numLoops(aggregatedCodeReport.numLoops() + report.codeReport().numLoops());
    }
    return aggregatedReport;
  }
}
