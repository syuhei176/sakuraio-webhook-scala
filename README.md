README.md



# How To Install


modify build.sbt

add repository

```
resolvers += "sakuraio-webhook-scala Maven Repository on Github" at "https://syuhei176.github.io/sakuraio-webhook-scala"
```

add dependency


```
libraryDependencies += "sakuraiowebhook" % "sakuraiowebhook_2.11" % "0.0.1"
```


# How To Use

in play framework

```
  def receive = Action { implicit request =>
    SakuraIOWebhookReciever.parseOutgoingWebhook(request.body.asJson) match {
      case Some(body: ResponseBody) =>
	      body.payload match {
	        case Channels(channels) =>
	        	// channel nessage
	        	//channels.map(_.value)
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