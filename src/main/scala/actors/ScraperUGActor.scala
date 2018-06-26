package actors

import Data.SongHeader
import akka.actor.Actor

class ScraperUGActor extends Actor {
  override def receive: Receive = {
    case s: SongHeader => {
      val url =
    }
  }
}
