package controllers

import eventsource.Clients
import play.api.libs.EventSource
import play.api.libs.ws.WS
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {

  def eventSource = Action(
    Ok.feed(
      Clients.enumerator &> EventSource()
    ).as("text/event-stream")
  )

  def index = Action { Ok(views.html.stream() ) }
}

