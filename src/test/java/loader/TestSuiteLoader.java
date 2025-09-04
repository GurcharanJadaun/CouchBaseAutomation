package loader;

import java.util.List;

import dataExtractor.XlsxDataExtractor;
import testManager.TestCase;
import testManager.TestSuite;

public class TestSuiteLoader extends XlsxDataExtractor {
	
	String locatorDir, keywordDir;
	
	protected TestSuiteLoader(){
		this.locatorDir = "CompilerDictionary\\LocatorDictionary";
		this.keywordDir = "CompilerDictionary\\KeywordDictionary";
	}
	
	List <TestCase> beforeAllMethods, beforeEachMethod;
	
	public void setupTest() {
		this.loadLocatorMap(locatorDir);
		this.loadFunctionNames(keywordDir);
		this.generateTestSuite();
		this.generateCompilationReport(listOfTestSuites);
	}
}
