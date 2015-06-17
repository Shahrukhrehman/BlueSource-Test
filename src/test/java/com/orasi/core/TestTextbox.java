package com.orasi.core;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.LabelImpl;
import com.orasi.core.interfaces.impl.TextboxImpl;
import com.orasi.utils.TestEnvironment;

public class TestTextbox extends TestEnvironment{
    
    @BeforeTest(groups ={"regression", "interfaces", "textbox", "dev"})
    @Parameters({ "runLocation", "browserUnderTest", "browserVersion",
	    "operatingSystem", "environment" })
    public void setup(@Optional String runLocation, String browserUnderTest,
	    String browserVersion, String operatingSystem, String environment) {
	setApplicationUnderTest("Test Site");
	setBrowserUnderTest(browserUnderTest);
	setBrowserVersion(browserVersion);
	setOperatingSystem(operatingSystem);
	setRunLocation(runLocation);
	setTestEnvironment(environment);
	setPageURL("file:" +getClass().getClassLoader().getResource("sites/htmlTest/testApp.html").getPath());
	testStart("TestTextbox");
    }
    
    @AfterTest(groups ={"regression", "interfaces", "textbox", "dev"})
    public void close(){
	endTest("TestTextbox");
    }

    @Test(groups ={"regression", "interfaces", "textbox"})
    public void getText(){
	Textbox textbox= new TextboxImpl(getDriver().findElement(By.id("FirstName")));
	Assert.assertTrue(textbox.getText().equals("Enter First Name:"));
    }
    
    @Test(groups ={"regression", "interfaces", "textbox"}, dependsOnMethods="getText")
    public void set(){
	Textbox textbox= new TextboxImpl(getDriver().findElement(By.id("FirstName")));
	textbox.set("set");
	Assert.assertTrue(textbox.getAttribute("value").equals("set"));
    }
    
    @Test(groups ={"regression", "interfaces", "textbox"}, dependsOnMethods="set")
    public void setScrollIntoView(){
	Textbox textbox= new TextboxImpl(getDriver().findElement(By.id("FirstName")));
	textbox.set(getDriver(), "setScrollIntoView");
	Assert.assertTrue(textbox.getAttribute("value").equals("setScrollIntoView"));
    }
    
    @Test(groups ={"regression", "interfaces", "textbox"}, dependsOnMethods="setScrollIntoView")
    public void clear(){
	Textbox textbox= new TextboxImpl(getDriver().findElement(By.id("FirstName")));
	textbox.clear();
	Assert.assertTrue(textbox.getAttribute("value").equals(""));
    }
    
    @Test(groups ={"regression", "interfaces", "textbox"}, dependsOnMethods="clear")
    public void safeSet(){
	Textbox textbox= new TextboxImpl(getDriver().findElement(By.id("FirstName")));
	textbox.safeSet("safeSet");
	if(getBrowserUnderTest().equalsIgnoreCase("html")) Assert.assertTrue(textbox.getAttribute("value").equals("asafeSet"));
	else Assert.assertTrue(textbox.getAttribute("value").equals("safeSet"));
	
    }
    
    @Test(groups ={"regression", "interfaces", "textbox"}, dependsOnMethods="safeSet")
    public void setSecure(){
	Textbox textbox= new TextboxImpl(getDriver().findElement(By.id("LastName")));
	textbox.setSecure("c2V0U2VjdXJl");
	Assert.assertTrue(textbox.getAttribute("value").equals("setSecure"));
	textbox.clear();
    }
    
    @Test(groups ={"regression", "interfaces", "textbox"}, dependsOnMethods="setSecure")
    public void safeSetSecure(){
	Textbox textbox= new TextboxImpl(getDriver().findElement(By.id("LastName")));
	textbox.safeSetSecure("c2V0U2VjdXJl");
	if(getBrowserUnderTest().equalsIgnoreCase("html")) Assert.assertTrue(textbox.getAttribute("value").equals("asetSecure"));
	else Assert.assertTrue(textbox.getAttribute("value").equals("setSecure"));
	textbox.clear();
    }
    
    @Test(groups ={"regression", "interfaces", "textbox"}, dependsOnMethods="safeSetSecure")
    public void setValidate(){
	Textbox textbox= new TextboxImpl(getDriver().findElement(By.id("LastName")));
	textbox.setValidate(getDriver(), "setValidate");
	Assert.assertTrue(textbox.getAttribute("value").equals("setValidate"));
	textbox.clear();
    }
    
    @Test(groups ={"regression", "interfaces", "textbox"}, dependsOnMethods="setValidate")
    public void safeSetValidate(){
	Textbox textbox= new TextboxImpl(getDriver().findElement(By.id("LastName")));
	textbox.safeSetValidate(getDriver(), "safeSetValidate");
	if(getBrowserUnderTest().equalsIgnoreCase("html")) Assert.assertTrue(textbox.getAttribute("value").equals("asafeSetValidate"));
	else Assert.assertTrue(textbox.getAttribute("value").equals("safeSetValidate"));
	textbox.clear();
    }
}
