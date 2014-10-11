package eventsource

import play.api.libs.iteratee.{Enumerator, Concurrent}
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.json.JsValue

object Clients {

  val (enumerator, channel): (Enumerator[JsValue], Channel[JsValue]) =
    Concurrent.broadcast[JsValue]

  def pushToClients(message: JsValue): Unit = channel.push(message)
}
