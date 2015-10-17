package local

import java.security.MessageDigest

import akka.actor.Actor

class GetBitcoins extends Actor {
  var tries=0
  def receive = {
    case HelpMining(k,gatorid,string) => {

      //LOGIC TO MINE BITCOINS FROM STRING RECEIVED. - IMPLEMENTED
      val stringarr= string.split(" ")
      val sha = MessageDigest.getInstance("SHA-256")
      for(i<-0 to stringarr.length-1)
      {
        val stringwithseed=gatorid+stringarr(i)
        sha.update(stringwithseed.getBytes("UTF-8"))
        val digest = sha.digest();

        val hexString = new StringBuffer();

        for ( j <- 0 to digest.length-1)
        {
          val hex = Integer.toHexString(0xff & digest(j));
          if(hex.length() == 1) hexString.append('0');
          hexString.append(hex);
        }
        sender ! NumberOfTries()

        // hexString.toString() is the hashed value.
        var a=0
        for(c <- 0 to k-1)
          if (hexString.toString().charAt(c)=='0')
            a = a+1
        if (a==k)
        {

          sender ! ConsolidateCoins(tries,stringwithseed,hexString.toString()/*BITCOIN AND MAYBE HASH VALUE AS WELL*/)
        }
      }
    }
    case _ => //println("Error: message not recognized")
  }
}
