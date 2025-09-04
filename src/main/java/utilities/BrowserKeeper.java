package utilities;

import java.util.Arrays;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.Cookie;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;

public class BrowserKeeper {
	 static Browser browser;
	 static Page page;
	 static Playwright playwright;	 
	 
	 @SuppressWarnings("unused")
	public void initiateBrowser(String browserName) {
		 playwright = Playwright.create();
		 BrowserContext context = null;
		 if(browserName.equalsIgnoreCase("chrome")) {
			 // ensures browser starts in maximized state
			 browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
					 .setHeadless(false)
					 .setArgs(Arrays.asList("--start-maximized")));
			 
		 }else if(browserName.equalsIgnoreCase("edge")) {
			 browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
					 .setChannel("msedge")
					 .setHeadless(false)
					 .setArgs(Arrays.asList("--start-maximized")));
		 }
		  else if(browserName.equalsIgnoreCase("firefox")) {
			 browser = playwright.firefox().launch(
			          new BrowserType.LaunchOptions()
			          .setHeadless(false)
			          .setArgs(Arrays.asList("--start-maximized")));
			      
			 }
		
		 
		 // it removes the fixed viewport 
		// (seems like rendering issue is there with fixed 1280×720 option as Accept cookies option isn't available)
		 context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null)); 
		 
		// Adds the tab on the browser 
		 page = context.newPage();
		 this.movePageToTackleLazyLoad();
	 }
	
	 public void initiateHeadlessBrowser(String browserName) {
		 playwright = Playwright.create();
		 if(browserName.equalsIgnoreCase("chrome")) {
			 // ensures browser works in headless state
			 browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
					 .setHeadless(true));
		 }else if(browserName.equalsIgnoreCase("edge")) {
			 browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
					 .setChannel("msedge")
					 .setHeadless(true));
		 }
		  else if(browserName.equalsIgnoreCase("firefox")) {
			 browser = playwright.firefox().launch(new BrowserType.LaunchOptions()
			          .setHeadless(true));
			 }
		
		 
		// Adds the tab on the browser 
		 page = browser.newPage();
	 }
	 
	 public void openUrl(String url) {
		 page.navigate(url);
	 }
	 
	 public void movePageToTackleLazyLoad() {
		  page.mouse().wheel(0, 300); 
	 }
	 
	 public void waitForPresenceOfElement(String locator) {
		 page.waitForSelector(locator);
	 }
	 
	 public void scrollIntoView(String locator) {
		 page.locator(locator).scrollIntoViewIfNeeded();
	 }
	 
	 public void clickWebElement(String locator) {
		 Locator ele = page.locator(locator);
		 ele.click();
	 }
	 
	
	public void enterTextInTextBox(String locator,String text) {
		 Locator ele = page.locator(locator);
		 ele.fill(text);
	 }
	 
	 public void selectValueFromDropDown(String locator,String selectByLabel) {
		 Locator ele = page.locator(locator);
		 SelectOption labelValue = new SelectOption();
		 labelValue.setLabel(selectByLabel);
		 ele.selectOption(labelValue);
	 }
	 
	 public void selectIndexFromDropDown(String locator, int itemNumber) {
		 Locator ele = page.locator(locator);
		 SelectOption labelValue = new SelectOption();
		 labelValue.setIndex(itemNumber);
		 ele.selectOption(labelValue);
	 }
	 
	 public boolean checkVisibilityOfElement(String locator) {
		 boolean result = false;
		 result = page.locator(locator).isVisible();
		 return result;
	 }
	 
	 public void waitForPageToRender() {
		
		 page.waitForLoadState(LoadState.NETWORKIDLE); 
		
	 }
	 
	 public String getTextFromTextBox(String locator) {
		 String text = "";
		 Locator ele = page.locator(locator);
		 text = ele.inputValue();
		 return text;
	 }
	 
	 public String getTextFromElement(String locator) {
		 String text = "";
		 Locator ele = page.locator(locator);
		 text = ele.innerText();
		 return text;
	 }
	 
	 public void pressKeyboardKey(String keyName) {
		 page.keyboard().press(keyName);
	 }
	 
	 public void closePage() {
		 page.close();
	 }
	 
	 public void closeBrowserSession() {
		 browser.close();
	 }
	
}
