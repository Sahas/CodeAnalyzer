package com.code.tools.analyzer;

import com.code.tools.analyzer.v1.dto.SourceAnalysisReport;
import com.code.tools.analyzer.v1.dto.SourceDetails;
import com.code.tools.analyzer.v1.service.JavaSyntaxAnalyzer;
import com.code.tools.analyzer.v1.service.SourceProcessor;
import com.code.tools.analyzer.v1.utils.SourceFileUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CodeAnalyzer {

  public static void main(String[] args) {
    String path = args[1];
    SourceProcessor processor = SourceProcessor.buildWithAnalyzers(new JavaSyntaxAnalyzer());
    List<SourceDetails> sourceDetailsList =  SourceFileUtil.fetchSourceList(path);
    SourceAnalysisReport report = processor.processAndAggregate(sourceDetailsList);

    log.info("\n" + report.toString());
  }

}
