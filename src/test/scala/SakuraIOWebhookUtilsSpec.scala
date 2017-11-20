import org.scalatest._
import jpn.org.syuhei.sakuraio._
import play.api.libs.json._

class SakuraIOWebhookRecieverSpec extends FlatSpec with Matchers {

  it should "channels b" in {
    val body = SakuraIOWebhookUtil.parseOutgoingWebhook(Option(Json.obj(
      "id" -> "1101231",
      "datetime" -> "2017-09-09T00:51:37.917120079Z",
      "type" -> "channels",
      "module" -> "moduleid",
      "payload" -> Json.obj(
        "channels" -> Json.toJson(Seq(
          Json.obj(
            "channel" -> 0,
            "type" -> "b",
            "value" -> "0011223344556677"
          )
        ))
      )
    )))
    body.get.payload match {
      case ChannelsResponse(channels) => {
        channels(0) match {
          case ChannelBResponse(0, "b", value) => {
            value should be ("0011223344556677")
          }
        }
      }
    }
  }

  it should "channels i" in {
    val body = SakuraIOWebhookUtil.parseOutgoingWebhook(Option(Json.obj(
      "id" -> "1101231",
      "datetime" -> "2017-09-09T00:51:37.917120079Z",
      "type" -> "channels",
      "module" -> "moduleid",
      "payload" -> Json.obj(
        "channels" -> Json.toJson(Seq(
          Json.obj(
            "channel" -> 0,
            "type" -> "i",
            "value" -> 5
          )
        ))
      )
    )))
    body.get.payload match {
      case ChannelsResponse(channels) => {
        channels(0) match {
          case ChannelIResponse(0, "i", value) => {
            value should be (5)
          }
        }
      }
    }
  }

  it should "channels b 0 1 2" in {
    val body = SakuraIOWebhookUtil.parseOutgoingWebhook(Option(Json.obj(
      "id" -> "1101231",
      "datetime" -> "2017-09-09T00:51:37.917120079Z",
      "type" -> "channels",
      "module" -> "moduleid",
      "payload" -> Json.obj(
        "channels" -> Json.toJson(Seq(
          Json.obj(
            "channel" -> 0,
            "type" -> "b",
            "value" -> "0011223344556677"
          ),
          Json.obj(
            "channel" -> 1,
            "type" -> "b",
            "value" -> "1111111111111111"
          ),
          Json.obj(
            "channel" -> 2,
            "type" -> "b",
            "value" -> "2222222222222222"
          )
        ))
      )
    )))
    body.get.payload match {
      case ChannelsResponse(channels) => {
        channels(0) match {
          case ChannelBResponse(0, "b", value) => {
            value should be ("0011223344556677")
          }
        }
        channels(1) match {
          case ChannelBResponse(1, "b", value) => {
            value should be ("1111111111111111")
          }
        }
        channels(2) match {
          case ChannelBResponse(2, "b", value) => {
            value should be ("2222222222222222")
          }
        }
      }
    }
  }

  it should "connection" in {
    val body = SakuraIOWebhookUtil.parseOutgoingWebhook(Option(Json.obj(
      "id" -> "1101233",
      "datetime" -> "2017-09-09T00:51:37.917120079Z",
      "type" -> "connection",
      "module" -> "moduleid",
      "payload" -> Json.obj(
        "is_online" -> true
      )
    )))
    body.get.payload match {
      case ConnectionResponse(is_online) => {
        is_online should be (true)
      }
    }
  }

  it should "unknown" in {
    SakuraIOWebhookUtil.parseOutgoingWebhook(Option(Json.obj(
      "id" -> "1101233",
      "datetime" -> "2017-09-09T00:51:37.917120079Z",
      "type" -> "ping",
      "module" -> "moduleid",
      "payload" -> Json.obj(
      )
    )))
  }

}