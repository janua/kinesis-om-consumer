package kinesisconsumer

import java.net.InetAddress
import java.util.UUID
import com.amazonaws.auth.{AWSCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.internal.StaticCredentialsProvider
import com.amazonaws.services.kinesis.clientlibrary.interfaces.{IRecordProcessor, IRecordProcessorCheckpointer, IRecordProcessorFactory}
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.{InitialPositionInStream, KinesisClientLibConfiguration, Worker}
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownReason
import com.amazonaws.services.kinesis.metrics.impl.NullMetricsFactory
import com.amazonaws.services.kinesis.model.Record
import eventsource.Clients
import play.api.libs.json.Json
import scala.collection.JavaConversions._


object KinesisConsumer {

  val awsCredentials: AWSCredentialsProvider =
    new StaticCredentialsProvider(
      new BasicAWSCredentials(ConsumerSettings.accessKey, ConsumerSettings.secretKey)
    )

  val kinesisClientLibConfiguration = new KinesisClientLibConfiguration(
    ConsumerSettings.applicationName,
    ConsumerSettings.streamName,
    awsCredentials,
    InetAddress.getLocalHost.getCanonicalHostName + ":" + UUID.randomUUID())
    .withRegionName(ConsumerSettings.region)
    .withInitialPositionInStream(InitialPositionInStream.LATEST)

  val worker = new Worker(
    RecordProcessorFactory,
    kinesisClientLibConfiguration,
    new NullMetricsFactory()
  )
}

object RecordProcessorFactory extends IRecordProcessorFactory {
  val recordProcessor = new RecordProcessor

  override def createProcessor: IRecordProcessor = recordProcessor
}


class RecordProcessor extends IRecordProcessor {

  private var kinesisShardId: String = _
  private var nextCheckpointTimeInMillis: Long = _
  private val CHECKPOINT_INTERVAL_MILLIS = 1000L

  override def initialize(shardId: String) = {
    println("Initializing record processor for shard: " + shardId)
    this.kinesisShardId = shardId
  }

  override def processRecords(records: java.util.List[Record],
                     checkpointer: IRecordProcessorCheckpointer) = {
    println(s"Processing ${records.size} records from $kinesisShardId")

    if (records.nonEmpty)
      Clients.pushToClients(Json.toJson(records.map(record => Json.parse(record.getData.array))))

    if (System.currentTimeMillis() > nextCheckpointTimeInMillis) {
      checkpointer.checkpoint()
      nextCheckpointTimeInMillis =
        System.currentTimeMillis + CHECKPOINT_INTERVAL_MILLIS
    }
  }

  override def shutdown(checkpointer: IRecordProcessorCheckpointer,
               reason: ShutdownReason) = {
    println(s"Shutting down record processor for shard: $kinesisShardId")
    if (reason == ShutdownReason.TERMINATE) {
      checkpointer.checkpoint()
    }
  }
}
