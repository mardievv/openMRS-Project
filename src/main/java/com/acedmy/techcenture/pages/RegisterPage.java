package com.acedmy.techcenture.pages;

import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class RegisterPage {

    private final WebDriver driver;
    private SoftAssert softAssert;
    private final Faker faker = new Faker(new Locale("en-US"));

    public RegisterPage(WebDriver driver,SoftAssert softAssert){
        this.driver = driver;
        this.softAssert = softAssert;
        PageFactory.initElements(driver,this);
    }



    @FindBy(xpath = "//input[@name='givenName']")
    private WebElement firstNameInput;

    @FindBy(xpath = "//input[@name='middleName']")
    private WebElement middleNameInput;

    @FindBy(xpath = "//input[@name='familyName']")
    private WebElement lastNameNameInput;

    @FindBy(xpath = "//label[@for='checkbox-unknown-patient']")
    private WebElement unknownCheckbox;

    @FindBy(id = "next-button")
    private WebElement nextBtn;

    @FindBy(id = "gender-field")
    private WebElement selectGender;

    @FindBy(id = "birthdateDay-field")
    private WebElement birthDay;

    @FindBy(id = "birthdateMonth-field")
    private WebElement birthMonthSelect;

    @FindBy(id = "birthdateYear-field")
    private WebElement birthYear;

    @FindBy(id = "birthdateYears-field")
    private WebElement estimatedYear;

    @FindBy(id = "birthdateMonths-field")
    private WebElement estimatedMoth;

    @FindBy(id = "address1")
    private WebElement address1Input;

    @FindBy(id = "address2")
    private WebElement address2Input;

    @FindBy(id = "cityVillage")
    private WebElement cityInput;

    @FindBy(id = "stateProvince")
    private WebElement stateInput;

    @FindBy(id = "country")
    private WebElement countryInput;

    @FindBy(id = "postalCode")
    private WebElement zipCodeInput;

    @FindBy(xpath = "//input[@name='phoneNumber']")
    private WebElement phoneNumberInput;

    @FindBy(id = "relationship_type")
    private WebElement relationshipTypeSelect;

    @FindBy(xpath = "//input[@placeholder='Person Name']")
    private WebElement relationshipPersonName;

    @FindBy(xpath = "//div[@id='dataCanvas']//p/span[1]")
    private List<WebElement> confirmInfoNames;

    @FindBy(xpath = "//div[@id='dataCanvas']//p")
    private List<WebElement> confirmInfoValues;

    @FindBy(id = "cancelSubmission")
    private WebElement cancelSubmissionBtn;

    @FindBy(id = "submit")
    private WebElement submitBtn;


    public void fillOutPatientInfo(HashMap<String,String> data) {

        verifyRegisterPageTitle();

        fillOutPatientName(data);

        selectGender(data);

        fillOutPatientsBirthDate(data);

        fillOutPatientsAddress(data);

        fillOutPatientPhoneNumber(data);

        fillOutRelatives(data);

        clickOnSubmitButton();
    }



    private void verifyRegisterPageTitle(){
        softAssert.assertEquals(driver.getTitle().trim(), "OpenMRS Electronic Medical Record", "TITLES DO NOT MATCH");
    }

    private void fillOutPatientName(HashMap<String,String> data) {

        firstNameInput.sendKeys(data.get("Given"));

        middleNameInput.sendKeys(data.get("Middle"));

        lastNameNameInput.sendKeys(data.get("Family Name"));


        softAssert.assertTrue(nextBtn.isDisplayed() && nextBtn.isEnabled(),"NEXT BUTTON IS NOT ENABLED");
        nextBtn.click();
    }

    private void selectGender(HashMap<String,String> data){
        selectGender.click();
        Select select = new Select(selectGender);
        select.selectByVisibleText(data.get("Gender"));


        softAssert.assertTrue(nextBtn.isDisplayed() && nextBtn.isEnabled(),"NEXT BUTTON IS NOT ENABLED");
        nextBtn.click();
    }

    private void fillOutPatientsBirthDate(HashMap<String ,String> data){

        if (data.get("BirthType").equalsIgnoreCase("Yes")) {

            data.put("Day",(int)Double.parseDouble(data.get("Day"))+"");
            birthDay.sendKeys(data.get("Day"));

            Select select = new Select(birthMonthSelect);
            select.selectByVisibleText(data.get("Month"));

            data.put("Year",(int)Double.parseDouble(data.get("Year"))+"");
            birthYear.sendKeys(data.get("Year"));

        }else {
            data.put("EstYears",(int)Double.parseDouble(data.get("EstYears"))+"");
            estimatedYear.sendKeys(data.get("EstYears")+"");

            data.put("EstMonth",(int)Double.parseDouble(data.get("EstMonth"))+"");
            estimatedMoth.sendKeys(data.get("EstMonth")+"");

        }
        softAssert.assertTrue(nextBtn.isDisplayed() && nextBtn.isEnabled(),"NEXT BUTTON IS NOT ENABLED");
        nextBtn.click();
    }

    private void fillOutPatientsAddress(HashMap<String ,String > data) {
        address1Input.sendKeys(data.get("Address"));


        data.put("Address 2", (int)Double.parseDouble(data.get("Address 2"))+"");
        address2Input.sendKeys(data.get("Address 2"));

        cityInput.sendKeys(data.get("City/Village"));

        stateInput.sendKeys(data.get("State/Province"));

        countryInput.sendKeys(data.get("Country"));

        data.put("Postal Code", (int)Double.parseDouble(data.get("Postal Code"))+"");
        zipCodeInput.sendKeys(data.get("Postal Code"));


        softAssert.assertTrue(nextBtn.isDisplayed() && nextBtn.isEnabled(),"NEXT BUTTON IS NOT ENABLED");
        nextBtn.click();
    }

    private void fillOutPatientPhoneNumber(HashMap<String,String> data) {
        data.put("Phone Number", (int)Double.parseDouble(data.get("Phone Number"))+"");
        phoneNumberInput.sendKeys(data.get("Phone Number"));

        softAssert.assertTrue(nextBtn.isDisplayed() && nextBtn.isEnabled(),"NEXT BUTTON IS NOT ENABLED");
        nextBtn.click();
    }

    private void fillOutRelatives(HashMap<String, String> data){
        Select select = new Select(relationshipTypeSelect);
        select.selectByVisibleText(data.get("Relationship Type"));

        relationshipPersonName.sendKeys(data.get("Related Name"));


        softAssert.assertTrue(nextBtn.isDisplayed() && nextBtn.isEnabled(),"NEXT BUTTON IS NOT ENABLED");
        nextBtn.click();
        verifyPatientInfo(data);
    }

    public void verifyPatientInfo(HashMap<String,String> data){

        List<WebElement> actualInfo = driver.findElements(By.xpath("//div[@id='dataCanvas']/div/p"));

        String expectedFullName = (data.get("Given") + ", " + (data.get("Middle").isEmpty() ? "" : data.get("Middle") + ", ")) + data.get("Family Name");

        String expectedGender = data.get("Gender");

        String expectedFullDOB = data.get("BirthType").equalsIgnoreCase("Yes") ? data.get("Day") + ", " + data.get("Month") + ", " + data.get("Year") : data.get("EstYears") + " year(s), " + data.get("EstMonth") + " month(s)";

        String expectedFullAddress = data.get("Address") + ", " + data.get("Address 2") + ", " + data.get("City/Village")+ ", " + data.get("State/Province") + ", " + data.get("Country") + ", " + data.get("Postal Code");

        String expectedPhoneNumber = data.get("Phone Number");

        String expectedRelatives = data.get("Related Name") + " - " + data.get("Relationship Type");

        String[] info = {"Name: " + expectedFullName, "Gender: " + expectedGender, "Birthdate: " + expectedFullDOB, "Address: " + expectedFullAddress, "Phone Number: " + expectedPhoneNumber, "Relatives: " + expectedRelatives};
        for (int i = 0; i < actualInfo.size(); i++) {
            String  actualData = actualInfo.get(i).getText().trim();
            String expectedData = info[i];
            softAssert.assertEquals(actualData,expectedData, "PATIENT " + actualData + " DOES NOT MATCH");
        }
    }

    private void clickOnSubmitButton(){
        softAssert.assertTrue(submitBtn.isEnabled() && cancelSubmissionBtn.isEnabled(),"SUBMIT OR CANCEL BUTTON IS NOT ENABLED");
        submitBtn.click();
    }
}
