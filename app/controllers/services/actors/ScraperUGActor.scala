package controllers.services.actors

import akka.actor.Actor
import controllers.data.SongHeader
import controllers.services.chords.UGCrawler

class ScraperUGActor extends Actor {
  override def receive: Receive = {
    case s: SongHeader => {
      val songObject = new UGCrawler(s).executeUrl
      sender() ! songObject
    }
    case _ =>
  }
}
