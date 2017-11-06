package jpn.org.syuhei.sakuraio

import play.api.libs.json._

sealed trait Channel
final case class ChannelI(channel: Int,
                   `type`: String,
                   value: Int) extends Channel
final case class ChannelB(channel: Int,
                   `type`: String,
                   value: String) extends Channel


object ChannelI {
  implicit val jsonFormat = Json.format[ChannelI]
}

object ChannelB {
  implicit val jsonFormat = Json.format[ChannelB]
}

sealed trait Payload
final case class Channels(channels: Seq[Channel]) extends Payload
final case class Connection(is_online: Boolean) extends Payload

object Connection {
  implicit val jsonFormat = Json.format[Connection]
}

case class ResponseBody(payload: Payload,
              module: String,
              datetime: String,
              `type`: String)

object SakuraIOWebhookReciever {


  def parseOutgoingWebhook(jsValue: Option[JsValue]): Option[ResponseBody] =
    jsValue match {
      case Some(jsonObj) => {
        val messageType = (jsonObj \ "type").as[String]
        val datetime = (jsonObj \ "datetime").as[String]
        val module = (jsonObj \ "module").as[String]
        val payload = (jsonObj \ "payload")
        if(messageType == "channels") {
          val channelsJsArray = (payload \ "channels").as[JsArray]
          val channels: Channels = Channels(channelsJsArray.value.map(item => {
            val jsObj = item.as[JsObject]
            val channel = (jsObj \ "channel").as[Int]
            val `type` = (jsObj \ "type").as[String]
            if(`type` == "b") {
              ChannelB(channel, "b", (jsObj \ "value").as[String])
            }else if(`type` == "i") {
              ChannelI(channel, "i", (jsObj \ "value").as[Int])
            }else{
              ChannelI(channel, "i", (jsObj \ "value").as[Int])
            }
          }))
          Option(ResponseBody(channels, module, datetime, messageType))
        }else if(messageType == "connection") {
          val connectionJsResult = payload.validate[Connection]
          connectionJsResult match {
            case s: JsSuccess[Connection] => {
              val connection: Connection = s.get
              Option(ResponseBody(connection, module, datetime, messageType))
            }
            case e: JsError => {
              None
            }
          }
        }else{
          None
        }
      }
      case None =>
        None
    }

}