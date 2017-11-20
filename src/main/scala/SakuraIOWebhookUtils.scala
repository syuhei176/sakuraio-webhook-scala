package jpn.org.syuhei.sakuraio

import javax.crypto.spec.SecretKeySpec
import javax.crypto.Mac
import play.api.libs.json._

sealed trait ChannelResponse
final case class ChannelIResponse(channel: Int,
                   `type`: String,
                   value: Int) extends ChannelResponse
final case class ChannelBResponse(channel: Int,
                   `type`: String,
                   value: String) extends ChannelResponse


object ChannelIResponse {
  implicit val jsonFormat = Json.format[ChannelIResponse]
}

object ChannelBResponse {
  implicit val jsonFormat = Json.format[ChannelBResponse]
}

sealed trait PayloadResponse
final case class ChannelsResponse(channels: Seq[ChannelResponse]) extends PayloadResponse
final case class ConnectionResponse(is_online: Boolean) extends PayloadResponse

object ConnectionResponse {
  implicit val jsonFormat = Json.format[ConnectionResponse]
}

case class ResponseBody(payload: PayloadResponse,
              module: String,
              datetime: String,
              `type`: String)


case class ChannelRequest(channel: Int,
                   `type`: String,
                   value: String)

object ChannelRequest {
  implicit val jsonFormat = Json.format[ChannelRequest]
}

case class PayloadRequest(channels: Seq[ChannelRequest])

object PayloadRequest {
  implicit val jsonFormat = Json.format[PayloadRequest]
}

case class RequestBody(`type`: String,
                       module: String,
                       payload: PayloadRequest)

object RequestBody {
  implicit val jsonFormat = Json.format[RequestBody]
}


object SakuraIOWebhookUtil {


  def parseOutgoingWebhook(jsValue: Option[JsValue]): Option[ResponseBody] =
    jsValue match {
      case Some(jsonObj) => {
        val messageType = (jsonObj \ "type").as[String]
        val datetime = (jsonObj \ "datetime").as[String]
        val module = (jsonObj \ "module").as[String]
        val payload = (jsonObj \ "payload")
        if(messageType == "channels") {
          val channelsJsArray = (payload \ "channels").as[JsArray]
          val channels = ChannelsResponse(channelsJsArray.value.map(item => {
            val jsObj = item.as[JsObject]
            val channel = (jsObj \ "channel").as[Int]
            val `type` = (jsObj \ "type").as[String]
            if(`type` == "b") {
              ChannelBResponse(channel, "b", (jsObj \ "value").as[String])
            }else if(`type` == "i") {
              ChannelIResponse(channel, "i", (jsObj \ "value").as[Int])
            }else{
              ChannelIResponse(channel, "i", (jsObj \ "value").as[Int])
            }
          }))
          Option(ResponseBody(channels, module, datetime, messageType))
        }else if(messageType == "connection") {
          val connectionJsResult = payload.validate[ConnectionResponse]
          connectionJsResult match {
            case s: JsSuccess[ConnectionResponse] => {
              val connection: ConnectionResponse = s.get
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

  def getXSakuraSignature(secretString: String, payload: String): String = {
    val secret = new SecretKeySpec(secretString.getBytes, "HmacSHA1")
    val mac = Mac.getInstance("HmacSHA1")
    mac.init(secret)
    mac.doFinal(payload.getBytes).map( "%02x".format(_) ).mkString("")
  }

  def makeIncomingWebhook = {

  }

}