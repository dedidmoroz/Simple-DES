package com.project.application

import java.nio.file.Files

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{BorderPane, GridPane, Priority}
import scalafx.stage.FileChooser
import scalafx.stage.FileChooser.ExtensionFilter

/**
  * Created by speedfire on 04.12.15.
  */

object Main extends JFXApp{
  /**
    * @author speedfire
    * @define textAreaPlain component which contents plain text for enciphering
    * */
  lazy val textAreaPlain:TextArea = new TextArea() { self =>
    self.wrapText = true
  }
  /**
    * @author speedfire
    * @define textAreaEncrypted component which contents encrypt text
    * */
  lazy val textAreaEncryped:TextArea = new TextArea() { self =>
    self.wrapText = true
  }
  /**
    * @author speedfire
    * @define keyText component which contents 10 bits key
    * */
  lazy val keyText:TextField  = new TextField(){self =>
    promptText = "10 bit key"
  }

  /**
    * @define consoleText component which log all
    * */
  lazy val consoleText:TextArea = new TextArea() {
    wrapText = true
  }
  /**
    * @author speedfire
    * @define isNormalKey closure which validate private key*/
  lazy val isNormalKey$:(String => Boolean) = {
    (key:String) =>
      val reg = "^1[01]{9}$".r
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

  /**
    * @author speedfire
    * @define makeEncrypt closure which encrypt text*/

  lazy val makeEncrypt:(ActionEvent => Unit) = {
    (ev:ActionEvent) =>
      val sdes = new com.project.cipher.SDES
      if(isNormalKey$(keyText.text.value)) textAreaEncryped.text.value =  sdes.runEncrypt(textAreaPlain.text.value.trim,keyText.text.value)

  }
  /**
    * @author speedfire
    * @define makeEncrypt closure which decrypt text*/
  lazy val makeDecrypt:(ActionEvent => Unit) = {
    (ev:ActionEvent) =>
      val sdes = new com.project.cipher.SDES
      if(isNormalKey$(keyText.text.value)) textAreaEncryped.text.value =  sdes.runDecrypt(textAreaPlain.text.value,keyText.text.value)

  }

  /**
    * @author speedfire
    * @define encryptButton componet which make operation of encrypt
    * */
  lazy val encryptButton:Button = new Button("Make encipher"){
    onAction = makeEncrypt
  }
  /**
    * @author speedfire
    * @define encryptButton componet which make operation of decrypt
    * */
  lazy val decryptButton:Button = new Button("Make decipher"){
    onAction = makeDecrypt
  }

  lazy val mainPane:GridPane = new GridPane(){
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
                new MenuItem("Load") {
                  onAction = {ev:ActionEvent =>
                    val chooser = new FileChooser()
                    chooser.extensionFilters.add(new ExtensionFilter("Text file","*.txt"))
                    textAreaPlain.text.value =  scala.io.Source.fromFile(chooser.showOpenDialog(stage)).mkString
                  }
                },
                new MenuItem("Save") {
                  onAction = {ev:ActionEvent =>
                    val chooser = new FileChooser()
                    chooser.extensionFilters.add(new ExtensionFilter("Text file","*.txt"))
                    Files.write(chooser.showSaveDialog(stage).toPath,textAreaEncryped.text.value.getBytes())
                  }
                },
                new SeparatorMenuItem,
                new MenuItem("Exit"){
                  onAction = {ev:ActionEvent => System.exit(0)}
                }
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



