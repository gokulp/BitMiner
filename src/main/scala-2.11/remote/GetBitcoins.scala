package remote

import java.security.MessageDigest

import akka.actor.Actor
import local.HelpMining

class GetBitcoins extends Actor {
  var tries=0
  def receive = {
    case HelpMining(k, gatorid, string) => {

      val remote = context.actorSelection("akka.tcp://LocalSystem@127.0.0.1:5151/user/RemoteActorLocal")
      //LOGIC TO MINE BITCOINS FROM STRING RECEIVED. - IMPLEMENTED
      val stringarr= string.split(" ")
      val sha = MessageDigest.getInstance("SHA-256")
      var qwe = ""
      for(i<-0 to stringarr.length-1)
      {
        val stringwithseed=gatorid+stringarr(i)
        sha.update(stringwithseed.getBytes("UTF-8"))
        val digest = sha.digest()

        val hexString = new StringBuffer()

        for ( j <- 0 to digest.length-1)
        {
          val hex = Integer.toHexString(0xff & digest(j))
          if(hex.length() == 1) hexString.append('0')
          hexString.append(hex)
        }

        // hexString.toString() is the hashed value.
        var a=0
        for (c <- 0 to k-48-1) {
          if (hexString.toString().charAt(c) == '0') {
            a = a + 1
          }
        }

        if (a==k-48)
        {
          qwe += "Bitcoin : " + stringwithseed + "     "+ hexString.toString()+"\n"
        }
      }

      remote ! qwe
    }

  }
}
