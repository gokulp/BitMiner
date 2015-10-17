package local

import akka.actor.{Identify, Actor, ActorRef, Props}
import akka.routing.RoundRobinRouter


class MineBitcoins(gatorid: String,k: Integer) extends Actor {

  private var running = false
  private var fileSender: Option[ActorRef] = None
  private var tries = 0
  val remote = context.actorSelection("akka.tcp://HelloRemoteSystem@127.0.0.1:5150/user/RemoteActor")

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    remote ! "Hi"
    println("That 's remote:" + remote)
  }

  // LOGIC TO GET STRING PREFIXED WITH GATORID, APPEND SOME STRINGS, DIVIDE AND ASSIGN IT TO WORKERS

  def receive = {

    case msg: String =>
      println(s": '$msg'")

    case StartMining() => {
      println("Started Mining")
      val processes = Runtime.getRuntime().availableProcessors()
      val actorcount=(3*processes)/2
      val actor = context.actorOf(Props[GetBitcoins].withRouter(RoundRobinRouter(actorcount)))
      var tobesent=k+""
      for(i <- 1 to 10000)
        tobesent+=scala.util.Random.alphanumeric.take(5).mkString+" "
      println("Search space created.....Now mining Bitcoins...!!\n\n")
      val toremote= tobesent substring(0,tobesent.length/2)
      tobesent =  tobesent substring(tobesent.length/2+1,tobesent.length - 1)
      println(toremote+"")
      remote ! toremote+""
      actor ! HelpMining(k,gatorid,tobesent)
    }

    case NumberOfTries() => {
      tries=tries+1
      if(tries>10000)
      {
        context.system.shutdown()
      }
    }

    case ConsolidateCoins(tries,bitcoin,hexvalue/*BITCOINS RECEIVED FROM WORKERS*/) => {
      println("Bitcoin : " + bitcoin + "     "+ hexvalue)
    }

    case _ => //println("Message not recognized!")
  }
}
