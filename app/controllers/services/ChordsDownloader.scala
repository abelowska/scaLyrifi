package controllers.services

import java.io.{ByteArrayOutputStream, OutputStream}

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import controllers.data.{Song, SongHeader}
import controllers.services.actors.SupervisorActor

import scala.concurrent.Await
import scala.concurrent.duration._
import akka.pattern.ask
import controllers.services.chords.PdfCreator

class ChordsDownloader {

  private val system = ActorSystem()
  private val supervisor = system.actorOf(Props(new SupervisorActor()))

  def download(songsHeaders: Seq[SongHeader]): ByteArrayOutputStream = {

    implicit val timeout: Timeout = Timeout(25 seconds)
    val songs = songsHeaders
      .map(supervisor ?)
      .map(f => {
        Await.result(f, timeout.duration).asInstanceOf[Song]
      })

    new PdfCreator().save(songs)
  }
}
