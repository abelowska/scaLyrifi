import java.io.File

import net.sourceforge.tess4j.Tesseract

import scala.util.Try

case class Song(title: String, author: String)

object MainTest {

  val filename = "test.png"

  def main(args: Array[String]): Unit = {

    val file = new File(s"./$filename")
    val tesseract = new Tesseract()
    tesseract.setLanguage("pol")

    //val songbook =
    Try(tesseract.doOCR(file)).toOption
      .map(s => s.split(","))
      .foreach(println)

  }

}
