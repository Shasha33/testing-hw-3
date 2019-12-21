package com.testing.hw

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.WebDriverWait

class IssuePage(driver: WebDriver) {
    private val issues = driver.findElements(By.className("issue-summary"))

    fun containsIssueBySummary(summary: String): Boolean {
        return countSameIssues(summary) > 0
    }

    fun countSameIssues(summary: String): Int {
        var cnt = 0
        for (issue in issues) {
            if (issue.text == summary) cnt++
        }
        return cnt
    }

    companion object {
        private const val url = "http://localhost:8080/issues"

        private fun verifyUrl(driver: WebDriver) {
            WebDriverWait(driver, 10).until { it.currentUrl == url }
        }

        fun new(driver: WebDriver): IssuePage {
            driver.navigate().to(url)
            verifyUrl(driver)
            driver.navigate().refresh()
            return IssuePage(driver)
        }
    }
}