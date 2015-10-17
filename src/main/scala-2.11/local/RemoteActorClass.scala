package local

import akka.actor.Actor

class RemoteActorClass extends Actor {
  def receive = {
    case msg: String =>
      println("=====================================================\n"+
        "\n Bitcoins from client \n\n"+msg+
        "\n=====================================================")
    case _ => println("Invalid message received")

  }
}
