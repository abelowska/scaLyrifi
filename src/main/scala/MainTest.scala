import java.io.File

import Data.{Song, SongHeader}
import actors.SupervisorActor
import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import net.sourceforge.tess4j.Tesseract
import services.PdfCreator

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Try

object MainTest {

  val filename = "test.png"

  def main(args: Array[String]): Unit = {

    val file = new File(s"./$filename")
    val tesseract = new Tesseract()
    tesseract.setLanguage("pol")

    val system = ActorSystem()
    val supervisor = system.actorOf(Props(new SupervisorActor()))

    val songs = Try(tesseract.doOCR(file)).toOption
      .map(s => {
        val Array(title, author) = s.split(",")
        SongHeader(title, author)
      })
      .map(supervisor ?)
      .map(f => {
        Await.result(f, Timeout(5 seconds).duration).asInstanceOf[Song]
      })
      .toList

    new PdfCreator("songBook").saveMany(songs)

    system.terminate()
  }

}
