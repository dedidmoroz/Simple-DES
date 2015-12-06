package com.project.application

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{Priority, BorderPane, GridPane}
/**
  * Created by speedfire on 04.12.15.
  */

object Main extends JFXApp{

  val textAreaPlain:TextArea = new TextArea() { self =>
    self.wrapText = true
  }
  val textAreaEncryped:TextArea = new TextArea() { self =>
    self.wrapText = true
  }

  val isNormalKey$:(String => Boolean) = {
    (key:String) =>
    val reg = "[01]{10}".r
    if(!reg.findFirstIn(keyText.text.value).isEmpty) {
      true
    } else {
      val alert = new Alert(Alert.AlertType.Warning)
      alert.setTitle("Problem with key")
      alert.setHeaderText("Check the key please")
      alert.setResult(ButtonType.Close)
      alert.showAndWait()
      false
    }

  }
  val makeEncrypt:(ActionEvent => Unit) = {
    (ev:ActionEvent) =>
      val sdes = new com.project.cipher.SDES
      if(isNormalKey$(keyText.text.value)) textAreaEncryped.text.value =  sdes.runEncrypt(textAreaPlain.text.value.trim,keyText.text.value)

  }
  val makeDecrypt:(ActionEvent => Unit) = {
    (ev:ActionEvent) =>
      val sdes = new com.project.cipher.SDES
      if(isNormalKey$(keyText.text.value)) textAreaEncryped.text.value =  sdes.runDecrypt(textAreaPlain.text.value,keyText.text.value)

  }

  val encryptButton:Button = new Button("Make encipher"){
    onAction = makeEncrypt
  }
  val decryptButton:Button = new Button("Make decipher"){
    onAction = makeDecrypt
  }

  val keyText:TextField  = new TextField(){self =>
    promptText = "10 bit key"
  }

  val mainPane:GridPane = new GridPane(){
    hgrow = Priority.Always;
    hgap = 10
    vgap = 20
    addColumn(0, TextArea.sfxTextArea2jfx(textAreaPlain),Button.sfxButton2jfx(encryptButton),TextField.sfxTextField2jfx(keyText))
    addColumn(1,TextArea.sfxTextArea2jfx(textAreaEncryped),Button.sfxButton2jfx(decryptButton))

  }
  /**
    * @author speedfire
    * <p>Main body of project</p>
    * @version 1.0*/
  stage = new PrimaryStage {
    title = "SIMPLE DES"
    resizable = false
    scene = new Scene() {

      content = new BorderPane(){
        top = new MenuBar() {
          menus = Seq(
            new Menu("File"){self =>
              self.items = Seq(
                new MenuItem("Load") {},
                new MenuItem("Save") {},
                new SeparatorMenuItem
              )
            },
            new Menu("Cipher"){}
          )
        }

        center = mainPane
      }
    }
  }
  stage.show()


}



