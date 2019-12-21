package com.testing.hw

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.WebDriverWait

class CreateIssueForm(private val driver: WebDriver) {
    fun createIssue(newSummary: String, newDescription: String) {
        val summary = WebDriverWait(driver, 10).until {
            driver.findElement(By.name("l.D.ni.ei.eit.summary"))
        }
        val description = WebDriverWait(driver, 10).until {
            driver.findElement(By.name("l.D.ni.ei.eit.description"))
        }
        val createButton = WebDriverWait(driver, 10).until {
            driver.findElement(By.id("id_l.D.ni.ei.submitButton_74_0"))
        }
        summary.sendKeys(newSummary)
        description.sendKeys(newDescription)
        createButton.click()
    }

    companion object {
        private const val url = "http://localhost:8080/dashboard#newissue=yes"

        private fun verifyUrl(driver: WebDriver) {
            WebDriverWait(driver, 10).until { it.currentUrl == url }
        }

        fun new(driver: WebDriver): CreateIssueForm {
            driver.navigate().to(url)
            verifyUrl(driver)
            driver.navigate().refresh()
            return CreateIssueForm(driver)
        }
    }
}