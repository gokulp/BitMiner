package remote

import akka.actor.{Props, Actor}
import akka.routing.RoundRobinRouter
import local.{NumberOfTries, HelpMining}

class RemoteActor extends Actor {
  var tries = 0

  def receive = {
    case "Hi" =>
      println("Hello")
    case msg: String => {
      println("RemoteActor received message ")
      val processes = Runtime.getRuntime().availableProcessors();
      val actorcount=(3*processes)/2
      val actor = context.actorOf(Props[GetBitcoins].withRouter(RoundRobinRouter(actorcount)))

      val k = msg.charAt(0)

      val gatorid="gspalwe"
      val tobesent=msg substring(1,msg.length-1)
      sender ! (actor ! HelpMining(k,gatorid,tobesent))
    }

    case NumberOfTries() => {
      tries=tries+1
      if(tries==1000)
      {
        context.system.shutdown()
      }
    }
  }
}