package local

import java.io.File

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._
import akka.dispatch.ExecutionContexts._

object SHA256 extends App {

  implicit val ec = global

  override def main(args: Array[String]) {
    val configFile = getClass.getClassLoader.getResource("local_application.conf").getFile
    val config = ConfigFactory.parseFile(new File(configFile))
    implicit val system = ActorSystem("LocalSystem",config)

    var cmdargs:String = args(0)
    if(args.length != 1){
      println(args.length)
      System.exit(1)
    } else {
      println(cmdargs)
    }

    println("\n\nEnter 'z' - the required number of leading zeroes : ")
    val k= readInt
    println ("\nConstructing the search space.......\n\n")
    val actor = system.actorOf(Props(classOf[MineBitcoins], "gspalwe",k), name = "MineBitcoinsActor")
    val act2 = system.actorOf(Props[RemoteActorClass], name = "RemoteActorLocal") // Responsible for collecting output from Remote Actors
    implicit val timeout = Timeout(25 seconds)
    val future = actor ! StartMining()
  }
}
