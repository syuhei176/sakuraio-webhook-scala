README.md



# How To Install


modify build.sbt

add repository

```
resolvers += "sakuraio-webhook-scala Maven Repository on Github" at "https://syuhei176.github.io/sakuraio-webhook-scala"
```

add dependency


```
libraryDependencies += "sakuraiowebhook" % "sakuraiowebhook_2.11" % "0.1.0"
```


# How To Use

in play framework

```scala
import jpn.org.syuhei.sakuraio._


  def receive = Action { implicit request =>
    SakuraIOWebhookReciever.parseOutgoingWebhook(request.body.asJson) match {
      case Some(body: ResponseBody) =>
	      body.payload match {
	        case Channels(channels) =>
	          // channel message
	          channels(0).asInstanceOf[ChannelI]
	          Ok
	        case Connection(is_online) =>
	        	// connection nessage
	          Ok
	        case _ =>
	          Ok
	      }
    }
  }
 ```