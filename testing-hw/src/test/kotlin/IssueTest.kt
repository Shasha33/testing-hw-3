package com.testing.hw

import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import java.util.concurrent.TimeUnit


internal class IssueTest {
    private val driver: WebDriver

    init {
        WebDriverManager.chromedriver().setup()
        driver = ChromeDriver()
    }

    @Before
    fun loginAndOpenIssues() {
        driver.get("http://localhost:8080/login")
        driver.manage()?.timeouts()?.implicitlyWait(10, TimeUnit.SECONDS)
        val loginField = driver.findElement(By.name("l.L.login"))
        val passwordField = driver.findElement(By.name("l.L.password"))
        val sendButton = driver.findElement(By.id("id_l.L.loginButton"))
        loginField.sendKeys("root")
        passwordField.sendKeys("admin")
        sendButton.click()
        driver.manage()?.timeouts()?.implicitlyWait(10, TimeUnit.SECONDS)
    }

    @After
    fun closeDriver() {
        driver.close()
    }

    @Test
    fun simpleLoginTest() {
    }


    @Test
    fun pageIssuePageOpenTest() {
        IssuePage.new(driver)
    }

    @Test
    fun pageNewIssueFormOpenTest() {
        CreateIssueForm.new(driver)
    }


    @Test
    fun createIssueWithOneLetterSummaryTest() {
        val createIssueForm = CreateIssueForm.new(driver)
        createIssueForm.createIssue("a", "b")
        val issuePage = IssuePage.new(driver)
        assertTrue(issuePage.containsIssueBySummary("a"))
    }

    @Test
    fun createIssueWithSpacesInSummaryTest() {
        val createIssueForm = CreateIssueForm.new(driver)
        createIssueForm.createIssue("a a a", "b")
        val issuePage = IssuePage.new(driver)
        assertTrue(issuePage.containsIssueBySummary("a a a"))
    }

    @Test
    fun createIssueWithLongSummaryTest() {
        val createIssueForm = CreateIssueForm.new(driver)
        val summary = "1".repeat(10000)
        createIssueForm.createIssue(summary, "b")
        val issuePage = IssuePage.new(driver)
        assertTrue(issuePage.containsIssueBySummary(summary))
    }

    @Test
    fun createIssueWithCapitalLettersInSummaryTest() {
        val createIssueForm = CreateIssueForm.new(driver)
        val summary = "UnIqUe NaMe11 011"
        createIssueForm.createIssue(summary, "b")
        val issuePage = IssuePage.new(driver)
        assertTrue(issuePage.containsIssueBySummary(summary))
    }

    @Test
    fun createIssueWithMagicInSummaryTest() {
        val createIssueForm = CreateIssueForm.new(driver)
        val summary = "( ･ω･｡)つ━☆・*"
        createIssueForm.createIssue(summary, "aaaaaa")
        val issuePage = IssuePage.new(driver)
        assertTrue(issuePage.containsIssueBySummary(summary))
    }


    @Test
    fun createSeveralIssuesWithSameSummaryTest() {
        val summary = "1"
        for (i in (1..5)) {
            val createIssueForm = CreateIssueForm.new(driver)
            createIssueForm.createIssue(summary, "b$i")
        }
        val issuePage = IssuePage.new(driver)
        assertEquals(issuePage.countSameIssues(summary), 5)
    }

    @Test
    fun createIssueWithEmptySummaryTest() {
        val createIssueForm = CreateIssueForm.new(driver)
        createIssueForm.createIssue("", "1")
        val error = driver.findElement(By.className("errorSeverity"))
        assertTrue(error.text == "Summary is required")
    }

    @Test
    fun createIssueWithSeveralLinesDescriptionTest() {
        val createIssueForm = CreateIssueForm.new(driver)
        val summary = "summary"
        createIssueForm.createIssue(summary, "b\nb\nb\nb")
        val issuePage = IssuePage.new(driver)
        assertTrue(issuePage.containsIssueBySummary(summary))
    }

    @Test
    fun createIssueWithSpacesInDescriptionTest() {
        val createIssueForm = CreateIssueForm.new(driver)
        val summary = "summary"
        createIssueForm.createIssue(summary, "b b b b")
        val issuePage = IssuePage.new(driver)
        assertTrue(issuePage.containsIssueBySummary(summary))
    }

    @Test
    fun createIssueWithEmptyDescriptionTest() {
        val createIssueForm = CreateIssueForm.new(driver)
        val summary = "summary"
        createIssueForm.createIssue(summary, "")
        val issuePage = IssuePage.new(driver)
        assertTrue(issuePage.containsIssueBySummary(summary))
    }

    @Test
    fun createIssueWitComplexDescriptionTest() {
        val desc = """
            *     ,MMM8&&&.            *
                  MMMM88&&&&&    .
                 MMMM88&&&&&&&
     *           MMM88&&&&&&&&
                 MMM88&&&&&&&&
                 'MMM88&&&&&&'
                   'MMM8&&&'      *
          |\___/|     /\___/\
          )     (     )    ~( .              '
         =\     /=   =\~    /=
           )===(       ) ~ (
          /     \     /     \
          |     |     ) ~   (
         /       \   /     ~ \
         \       /   \~     ~/
  jgs_/\_/\__  _/_/\_/\__~__/_/\_/\_/\_/\_/\_
  |  |  |  |( (  |  |  | ))  |  |  |  |  |  |
  |  |  |  | ) ) |  |  |//|  |  |  |  |  |  |
  |  |  |  |(_(  |  |  (( |  |  |  |  |  |  |
  |  |  |  |  |  |  |  |\)|  |  |  |  |  |  |
  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
  """
        val createIssueForm = CreateIssueForm.new(driver)
        val summary = "summary"
        createIssueForm.createIssue(summary, desc)
        val issuePage = IssuePage.new(driver)
        assertTrue(issuePage.containsIssueBySummary(summary))

    }
}