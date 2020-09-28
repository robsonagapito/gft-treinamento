package support;

import driver.DriverManager;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;


@Log4j2
public class Verifications extends DriverManager {


    private Verifications() {
        throw new IllegalStateException();
    }

    /**
     * Highlights the requested part of a component on screen
     *
     * @author Rubens Lobo
     */
    public static void highlightElement(WebElement element) {
        try {
            if (!getDriver().toString().contains("Windows") && !getDriver().toString().contains("appPackage")
                    && !getDriver().toString().contains("WiniumDriver") && getDriver() instanceof JavascriptExecutor) {
                ((JavascriptExecutor) getDriver()).executeScript("arguments[0].style.border='2px dashed red'", element);
                ((JavascriptExecutor) getDriver()).executeScript("arguments[0].style.border='1,5'", element);
                wait(1);
                ((JavascriptExecutor) getDriver()).executeScript("arguments[0].style.border=''", element);
                ((JavascriptExecutor) getDriver()).executeScript("arguments[0].style.border=''", element);
            }
        } catch (Exception e) {
            log.error(String.format("O elemento não esta visivel para o Highlight: %s", e));
        }
    }

    /**
     * Waits for a defined period
     *
     * @param seconds the number of seconds the function will wait for
     * @author Rubens Lobo
     */
    public static void wait(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Verifies an existing element on the screen
     *
     * @param by      Type of "By"
     * @author Rubens Lobo
     */
    public static boolean verifyElementExists(By by) {
        log.info(String.format("Verificando se o elemento via locator %s existe na tela", by.toString()));
        WebElement element = Action.getExistingElement(by);
        highlightElement(element);
        return true;
    }

    /**
     * Verifies if the text requested is part of an attribute of an element on screen
     *
     * @param by           Type of "By"
     * @param attribute    set attribute of the web element
     * @param expectedText is the text expected to be matched on screen
     * @author Rubens Lobo
     */
    public static boolean verifyElementAttributeContains(By by, String attribute, String expectedText) {
        log.info(String.format("Verificando se o texto esperado faz parte do valor do atributo do elemento via locator %s ", by.toString()));
        WebElement element = Action.getClickableElement(by);
        highlightElement(element);
        return element.getAttribute(attribute).contains(expectedText);

    }

    /**
     * Verifies if a clickable element contains the expected text on screen
     *
     * @param by           Type of "By"
     * @param expectedText is the text expected to be matched on screen
     * @author Rubens Lobo
     */
    public static boolean verifyTextsElementClickable(By by, String expectedText) {
        log.info(String.format("Verificando se o elemento via locator %s e clicavel e contem o texto esperado", by.toString()));
        WebElement element = Action.getClickableElement(by);
        int timeout = 0;
        while (!(element.getText().trim().equals(expectedText)) && (timeout <= configuration.timeout())) {
            Verifications.wait(1);
            if (timeout == configuration.timeout()) {
                log.error("Elemento via locator " + by.toString() + " nao encontrado na pagina atual!");
                return false;
            }
            timeout++;
        }
        highlightElement(element);
        return true;
    }


    /**
     * Verifies if an existing element contains the expected text on screen. It can be a disabled element
     *
     * @param by           Type of "By"
     * @param expectedText is the text to be entered expected to be matched on screen
     * @author Rubens Lobo
     */
    public static boolean verifyTextsExistingElement(By by, String expectedText) {
        log.info(String.format("Verificando se o elemento via locator %s existe e se contem o texto esperado", by.toString()));
        WebElement element = Action.getExistingElement(by);
        int timeout = 0;
        while (!(element.getText().trim().equals(expectedText)) && (timeout <= configuration.timeout())) {
            Verifications.wait(1);
            if (timeout == configuration.timeout()) {
                log.error("Elemento via locator " + by.toString() + " nao encontrado na pagina atual!");
                return false;
            }
            timeout++;
        }
        highlightElement(element);
        return true;
    }

    /**
     * Verifies if an element is visible and Clickable
     *
     * @param by      Type of "By"
     * @return checking an element is visible and can be clicked
     * @author Rubens Lobo
     */
    public static boolean verifyElementIsClickable(By by) {
        log.info(String.format("Verificando se o elemento via locator %s e visivel e clicavel", by.toString()));
        WebElement element = Action.getClickableElement(by);
        highlightElement(element);
        return true;
    }

    /**
     * Verifies if the given element is visible.
     *
     * @param by      Type of "By"
     * @author Rubens Lobo
     */
    public static boolean verifyElementIsVisible(By by) {
        log.info(String.format("Verificando o elemento via locator %s e visivel", by.toString()));
        WebElement element = Action.getVisibleElement(by);
        highlightElement(element);
        return true;
    }

    /**
     * Verifies if an element is not on screen
     *
     * @param by      Type of "By"
     * @author Rubens Lobo
     */
    public static void verifyElementDoesNotExist(By by) {
        log.info(String.format("Verificando se o elemento via locator %s nao esta visivel na tela", by.toString()));
        WebDriverWait wait = new WebDriverWait(getDriver(), configuration.timeout());
        ExpectedCondition elementIsDisplayed = (ExpectedCondition<Boolean>) webDriver -> {
            try {
                getDriver().findElement(by).isDisplayed();
                return false;
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return true;
            }
        };
        wait.until(elementIsDisplayed);
    }
}
