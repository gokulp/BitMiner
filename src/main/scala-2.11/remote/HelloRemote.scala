package remote

import java.io.File

import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory

object HelloRemote extends App  {
  //get the configuration file from classpath
  val configFile = getClass.getClassLoader.getResource("remote_application.conf").getFile
  //parse the config
  val config = ConfigFactory.parseFile(new File(configFile))
  //create an actor system with that config
  val system = ActorSystem("HelloRemoteSystem" , config)

  val remoteActor = system.actorOf(Props[RemoteActor], name = "RemoteActor")
}