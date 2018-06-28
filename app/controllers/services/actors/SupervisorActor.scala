package controllers.services.actors

import akka.actor.{Actor, Props}
import controllers.data.SongHeader

class SupervisorActor extends Actor {
  override def receive: Receive = {
    case s: SongHeader => context.actorOf(Props(new ScraperUGActor)).forward(s)
    case _ => println("Unknown format of message")
  }
}
