##### Future requirements ?
1. Requirement to add  more  metrics in the analysis ->  No  of assignment operations, idenitfy function calls  etc
2. Requirement to add support for New  languages

#### How to run
1. For Mac users -> 
    1. Open the project  folder  and run the command ->  ./gradlew analyzeCode --args='path FILE_OR_REPO_LOCATION_HERE'
    2. ./gradlew analyzeCode --args='path ./src/test/resources/Java_EventUtils.java'
2. For window users -> 
    1. Open the project  folder  and run the command  (Unverified) ->  gradlew.bat analyzeCode --args='path FILE_OR_REPO_LOCATION_HERE'

#### Package Structure
1. dto -> Contains Request, Response classes
2. utils  -> Contains file utilities  etc
3. service -> Contains business logic. 
   1. SourceProcessor  is  main class to take in list of source details and find,call appropriate syntax Analyzer and aggregates the data
   2. SyntaxAnalyzer  is core interface that should  be implemented by analyzers
   3. javaSyntaxAnalyzer is implementation of the above anlayzer for  Java  interface that analyzes file, builds the metrics for the file.

###### How to extend?
1. Add support to new language -> Add a new class which implements Syntax Analyzer and Add the analyzer to SourceProcessor while building it.
2. Add new metrics  ->  Add new fields in SourceAnalysisReport and make corresponding  changes in SyntaxAnalyzer.process()


###### Limitations:
1. Doesn't support multi-line variable declarations or assignments 
2. Doesn't identify methods
3. Variable declaration regex is not exhaustively tested.

## Functional
1. Input = Single source File of a language
2. Output =
    1. Number of blank lines
    2. Number of lines with Comments
    3. Number of lines with code
    4. Total number of lines in file
3. Support one programming language syntax  - [Done]
4. Types of lines you should support: Blank, Comments, Code - [Done]
5. A line counts as a comment only if it has no other code. - [Done]
6. Design for extensibility: you should be able to support new language syntaxes by extending your solution.  - [Done]
7. One passing test  - []

Not necessary to have running code for the following requirements, but account for these in your design.
1. Supporting multiple syntaxes - [Design Support  Done  ->  Extend SyntaxAnalyzer]
2. Supporting multiple files and giving totals for an entire source tree - [Done -> Check SourceProcessor.processAndAggregate()] 
3. Supporting multi-line comments - [Done. Irrespective  of length of MultilineComment,  
it  is always treated as 1. Uncomment count updation code to treat each line as part of count ]
4. Ability to add more granular breakup (eg: classify lines as imports, variable declarations, etc) - [Done. 
Check JavaSyntaxAnalyzer.analyze(). Check test files for proper understanding of Variable declaration Pattern syntax]


## Non-Functional
1. Readable  & Intuitive Code ->  [To best of  the abilities :D ]
