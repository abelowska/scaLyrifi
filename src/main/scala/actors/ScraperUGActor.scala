package actors

import Data.SongHeader
import akka.actor.Actor
import services.UGCrawler

class ScraperUGActor extends Actor {
  override def receive: Receive = {
    case s: SongHeader => {
      val songObject = new UGCrawler(s).executeUrl
      sender() ! songObject
    }
    case _ =>
  }

}
