package spinyhi.hello

import play.api.libs.json._

case class Channel(channel: Int,
                   `type`: String,
                   value: String)

object Channel {
  implicit val jsonFormat = Json.format[Channel]
}

sealed trait Payload
final case class Channels(channels: Seq[Channel]) extends Payload
final case class Connection(is_online: Boolean) extends Payload

object Channels {
  implicit val jsonFormat = Json.format[Channels]
}
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
          val channelsJsResult = payload.validate[Channels]
          channelsJsResult match {
            case s: JsSuccess[Channels] => {
              val channels: Channels = s.get
              Option(ResponseBody(channels, module, datetime, messageType))
            }
            case e: JsError => {
              None
            }
          }
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