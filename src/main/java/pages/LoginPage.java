package pages;

import driver.DriverManager;
import lombok.extern.log4j.Log4j2;
import report.Report;
import support.Action;
import support.Verifications;
import org.openqa.selenium.By;


import java.util.HashMap;

@Log4j2
public class LoginPage extends DriverManager implements CommonTestingType {

    private By txtUsuario = By.name("email");
    private By txtSenha = By.name("senha");
    private By btnEntrar = By.xpath("//button");
    private By abaLogin = By.xpath("//a[contains(text(),'Login')]");
    private By lblErrorMsg = By.xpath("//*[contains(text(),'Problemas com o login do usuário')]");

    @Override
    public boolean isPresent() {
        return  Verifications.verifyTextsElementClickable(abaLogin,"Login")
                && Verifications.verifyElementIsClickable(txtUsuario);
    }

    public void acessaAplicacao(){
        getDriver().get(configuration.url());
        Report.takeScreenShot();
        log.info("Acesso a aplicacao efetuado com sucesso");
    }

    public void executaLogin(HashMap data){
        Action.setText(txtUsuario,data.get("usuario"));
        getDriver().findElement(txtSenha).sendKeys((CharSequence) data.get("senha"));
        Report.takeScreenShot();
        Action.clickOnElement(btnEntrar);
        log.info("Login na aplicacao efetuado com sucesso");
    }

    public boolean isErrorMsg(){
        Report.takeScreenShot();
        return Verifications.verifyElementIsClickable(lblErrorMsg);
    }

}
