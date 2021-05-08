package com.code.tools.analyzer.v1.service;

import com.code.tools.analyzer.v1.dto.CodeStatementReport;
import com.code.tools.analyzer.v1.dto.SourceAnalysisReport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class JavaSyntaxAnalyzer implements SyntaxAnalyzer {

  private static final String JAVA_SINGLE_LINE_COMMENT_SYNTAX = "//";
  private static final String JAVA_MULTI_LINE_COMMENT_START_SYNTAX = "/*";
  private static final String JAVA_MULTI_LINE_COMMENT_END_SYNTAX = "*/";
  private static final String JAVA_IMPORT_STATEMENT_START_IDENTIFIER = "import";
  private static final String JAVA_PACKAGE_STATEMENT_START_IDENTIFIER = "package";
  private static final String JAVA_CLASS_DECL_IDENTIFIER = "class";
  private static final String JAVA_INTERFACE_DECL_IDENTIFIER = "interface";
  private static final String JAVA_RETURN_STATEMENT_IDENTIFIER = "return";
  private static final Pattern JAVA_VAR_DECL_PATTERN = getVarDeclarationPattern();

  private static final List<String> JAVA_LOOP_IDENTIFIERS = Arrays.asList("while", "for", "do");
  private static final List<String> JAVA_CONDITIONAL_IDENTIFIERS = Arrays.asList("if", "else if", "else", "switch");
  private static final List<String> JAVA_ACCESS_MODIFIERS = Arrays.asList("public", "private", "protected");
  private static final List<String> JAVA_VAR_MODIFIERS = Arrays.asList("static", "final", "volatile", "abstract");

  @Override
  public List<String> supportedExtensions() {
    return Arrays.asList("java");
  }

  @Override
  public SourceAnalysisReport analyze(List<String> lines) {
    int blankLines = 0;
    int commentLines = 0;
    int multiCommentLines = 0;
    int numCodeLines = 0;
    int totalLines = 0;
    CodeStatementReport codeReport = CodeStatementReport.builder()
      .build();

    for (int i = 0; i < lines.size(); i++) {
      String cleanLine = lines.get(i).trim();

      totalLines++;
      if (isBlankLine(cleanLine)) {
        blankLines++;
      } else if (isCommentLine(cleanLine)) {
        commentLines++;
      } else if (isMultiLineComment(cleanLine)) {
        int commentLength = multiLineCommentLength(lines, i);
        multiCommentLines++;
        i += (commentLength - 1);
        totalLines += (commentLength - 1);
      } else {
        numCodeLines++;
        if (isPackageStatement(cleanLine)) {
          codeReport.numPackageStatements(codeReport.numPackageStatements() + 1);
        } else if (isImportStatement(cleanLine)) {
          codeReport.numImports(codeReport.numImports() + 1);
        } else if (isConditional(cleanLine)) {
          codeReport.numConditionals(codeReport.numConditionals() + 1);
        } else if (isLoop(cleanLine)) {
          codeReport.numLoops(codeReport.numLoops() + 1);
        } else if (isClassDeclaration(cleanLine)) {
          codeReport
            .numClassDeclarations(codeReport.numClassDeclarations() + 1);
        } else if (isReturnStatement(cleanLine)) {
          codeReport
            .numReturnStatements(codeReport.numReturnStatements() + 1);
        } else if (isVariableDeclaration(cleanLine)) {
          codeReport
            .numVarDeclarations(codeReport.numVarDeclarations() + 1);
        }
      }
    }
    return SourceAnalysisReport.builder()
      .numTotalLines(totalLines)
      .numCommentLines(commentLines)
      .numMultiCommentLines(multiCommentLines)
      .numCodeLines(numCodeLines)
      .numBlankLines(blankLines)
      .codeReport(codeReport)
      .build();
  }

  public static boolean isBlankLine(String line) {
    return StringUtils.isBlank(line);
  }

  public static boolean isCommentLine(String line) {
    return line.startsWith(JAVA_SINGLE_LINE_COMMENT_SYNTAX);
  }

  public static boolean isMultiLineComment(String line) {
    return line.startsWith(JAVA_MULTI_LINE_COMMENT_START_SYNTAX);
  }

  public static int multiLineCommentLength(List<String> lines, int currIndex) {
    int count = 0;
    for (int i = currIndex; i < lines.size(); i++) {
      if (lines.get(i).trim().endsWith(JAVA_MULTI_LINE_COMMENT_END_SYNTAX)) {
        return ++count;
      }
      count++;
    }
    return count;
  }

  public static boolean isPackageStatement(String line) {
    return line.startsWith(JAVA_PACKAGE_STATEMENT_START_IDENTIFIER);
  }

  public static boolean isImportStatement(String line) {
    return line.startsWith(JAVA_IMPORT_STATEMENT_START_IDENTIFIER + " ");
  }

  public static boolean isVariableDeclaration(String line) {
    line = cleanupAccessModifiers(line);
    line = cleanupVarModifiers(line);
    Matcher matcher = JAVA_VAR_DECL_PATTERN.matcher(line);
    boolean isVarDecl = matcher.find();
    return isVarDecl;
  }

  public static boolean isClassDeclaration(String line) {
    line = cleanupAccessModifiers(line);
    line = cleanupVarModifiers(line);
    if (line.startsWith(JAVA_CLASS_DECL_IDENTIFIER)) {
      return true;
    }
    return false;
  }

  public static boolean isConditional(String line) {
    for (String conditionalKeyword : JAVA_CONDITIONAL_IDENTIFIERS) {
      if (line.startsWith(conditionalKeyword)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isLoop(String line) {
    for (String loopKeyword : JAVA_LOOP_IDENTIFIERS) {
      if (line.startsWith(loopKeyword)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isReturnStatement(String line) {
    String[] words = line.split(" ");
    if (JAVA_RETURN_STATEMENT_IDENTIFIER.equalsIgnoreCase(words[0])) {
      return true;
    }
    return false;
  }

  public static String cleanupAccessModifiers(String line) {
    for (String accessModifier : JAVA_ACCESS_MODIFIERS) {
      accessModifier = accessModifier + " ";
      if (line.startsWith(accessModifier)) {
        line = line.substring(accessModifier.length()).trim();
      }
    }
    return line;
  }

  public static String cleanupVarModifiers(String line) {
    for (String varModifier : JAVA_VAR_MODIFIERS) {
      varModifier = varModifier + " ";
      if (line.startsWith(varModifier)) {
        line = line.substring(varModifier.length()).trim();
      }
    }
    return line;
  }

  public static Pattern getVarDeclarationPattern() {
    // Check Test class for corresponding tests

    String stringBeginning = "^";
    // <  and > for supporting template literals
    //Supports variable names beginning with/containing underscores.
    // Doesn't match variables starting with numbers
    String varNameOrTypePattern = "_*?[a-zA-Z][a-zA-Z0-9_<]*[a-zA-Z0-9>]*";

    String preAssignmentPossibleOptionalOperators = "[\\+\\-\\/\\%\\*]?";
    String semiColonOrBracketStart = "[;{]?";
    String atleastOneSpace = "\\s+";
    String zeroOrMoreSpaces = "\\s*";
    String atleastOneCharacter = ".+";

    //  %s replaced by the parameter strings
    Map<String, String> templateMap = new HashMap<String, String>() {{
      put("preAssignmentPossibleOptionalOperators", preAssignmentPossibleOptionalOperators);
      put("semiColonOrBracketStart", semiColonOrBracketStart);
      put("atleastOneSpace", atleastOneSpace);
      put("zeroOrMoreSpaces", zeroOrMoreSpaces);
      put("atleastOneCharacter", atleastOneCharacter);
    }};

    StringSubstitutor substitutor = new StringSubstitutor(templateMap);

    // assignmentAndOrSemiColonInRHS = \s*[[\+\-\/\%\*]?=.+[;{]]?|;]
    String assignmentAndOrSemiColonInRHSTemplate = "${zeroOrMoreSpaces}[${preAssignmentPossibleOptionalOperators}=${atleastOneCharacter}${semiColonOrBracketStart}]|;]";
    String assignmentAndOrSemiColonInRHSPattern = substitutor.replace(assignmentAndOrSemiColonInRHSTemplate);
    String varDeclPattern = stringBeginning + varNameOrTypePattern + atleastOneSpace + varNameOrTypePattern + assignmentAndOrSemiColonInRHSPattern;

    return Pattern.compile(varDeclPattern);
  }

}
