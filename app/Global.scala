import kinesisconsumer.KinesisConsumer
import play.api.{Application, GlobalSettings}

object Global extends GlobalSettings {
  import scala.concurrent.ExecutionContext.Implicits.global

  override def onStart(app: Application) {
    super.onStart(app)
    global.execute(KinesisConsumer.worker)
  }

  override def onStop(app: Application) {
    KinesisConsumer.worker.shutdown()
    super.onStop(app)
  }
}