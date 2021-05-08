package com.code.tools.analyzer.v1.service;

import com.code.tools.analyzer.v1.dto.SourceAnalysisReport;

import java.util.List;

public interface SyntaxAnalyzer {
  List<String> supportedExtensions();
  SourceAnalysisReport analyze(List<String> lines);

}
