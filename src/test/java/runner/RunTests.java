package runner;

import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;

import loader.TestSuiteLoader;
import testManager.RunTestSuite;
import testManager.TestCase;
import testManager.TestStatus;
import testManager.TestStep;
import testManager.TestSuite;
import utilities.ExecuteStep;
public class RunTests extends TestSuiteLoader implements RunTestSuite  {
	
	List<TestCase> beforeAllTests, beforeEachTest, afterAllTests, afterEachTest;
	
	RunTests() {
		super();
		// TODO Auto-generated constructor stub
	}


	public static void main(String args[]) {
		Instant start = Instant.now();
		RunTests test = new RunTests();
		
		test.setupTest();
		test.run();
		Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);

        long seconds = timeElapsed.toSeconds();
        double minutes = timeElapsed.toMinutes();
        System.out.println("Time taken: " + seconds + " seconds (" + minutes + " minutes)");
      }

	
	public void run() {
		
		Iterator<TestSuite> it = this.listOfTestSuites.iterator();
		
		while(it.hasNext()) {
			 TestSuite suite = it.next();
			 filterSuite(suite); 
			 runTestSuite(suite);	
		}
		
	
	}
  
	@Override
	public void runTestSuite(TestSuite testSuite) {
		
		this.beforeAllTests.forEach(this::runTestCase);
		testSuite.getTestCases().forEach(testCase -> {	
		
			this.beforeEachTest.forEach(this::runTestCase);
			runTestCase(testCase);
			this.afterEachTest.forEach(this::runTestCase);
			
		});
		
		this.afterAllTests.forEach(this::runTestCase);
	
	}

	@Override
	public void runTestCase(TestCase testCase) {
		
		Iterator<TestStep> it = testCase.getSteps().iterator();
		while(it.hasNext()) {
			TestStep ts = it.next();
			runTestStep(ts);
			 if(ts.getResult().shouldStop()) {
				testCase.setTestCaseResult(ts.getResult().setStatusTo());
				break;
			}else if(ts.getResult().isFailed()) {
				testCase.setTestCaseResult(ts.getResult());
			}
		}
		if(testCase.getTestCaseResult() == TestStatus.PENDING) {
			testCase.setTestCaseResult(TestStatus.PASSED);
		}
		
	}

	@Override
	public void runTestStep(TestStep testStep) {
		
		ExecuteStep ex = new ExecuteStep();
		String action  = testStep.getAction();
		String locator = testStep.getLocator();
		String testData = testStep.getTestData();
		
		if(locator == null && testData == null) {
			ex.executeStep(action);
		}
		else if(locator == null && testData!=null) {
			ex.executeStep(action, testData);
		}
		else if(locator != null && testData==null) {
			ex.executeStep(action, locator);
		}
		else if(locator!= null && testData!= null) {
			ex.executeStep(action, locator, testData);
		}
		else {
			//Log error in logs here with step details like action, locator and testData
			testStep.setResult(TestStatus.INVALID, "Something missed by compiler\n<<-Didn't find a proper match->>\n");
		}
		System.out.println("Executing : " + action + "\t" + locator + "\t" + testData + "\n" + ex.result + "\t"+ ex.reason);
		if(ex.result == TestStatus.PASSED) {
			
			testStep.setResult(TestStatus.PASSED);
		}
		else {
			testStep.setResult(ex.result, ex.reason);
		}
	}

	public void filterSuite(TestSuite testSuite) {
	  	this.beforeAllTests = testSuite.getBeforeAllMethodFromTestSuite();
		this.beforeEachTest = testSuite.getBeforeEachMethodFromTestSuite();
		this.afterEachTest = testSuite.getAfterEachMethodFromTestSuite();
		this.afterAllTests = testSuite.getAfterAllMethodFromTestSuite();
		
		System.out.println("Test Cases in suite : "+testSuite.getTestCases().size());
  }
	
	
	
}
