//package sandbox
//
//import java.util.concurrent.{Executors, ScheduledExecutorService}
//
//import scala.concurrent.ExecutionContext
//import scala.concurrent.duration._
//import scala.language.postfixOps
//
//import scalaz.stream._
//import scalaz.Nondeterminism
//import scalaz.concurrent.{Strategy, Task}
//import scalaz.stream.async.mutable.Queue
//
//object Zip extends App {
//  implicit val pool: ScheduledExecutorService = Executors.newScheduledThreadPool(3, Executors.defaultThreadFactory())
//  implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(pool)
//  implicit val S: Strategy = Strategy.Executor(pool)
//
//  def effect(ids: Seq[String]): Task[Unit] = Task.delay(println(ids))
//
//  val q: Queue[String] = async.boundedQueue[String](10)
//  val received: Process[Task, Seq[String]] = q.dequeueBatch(3)
//  val throtler: Process[Task, Duration] = time.awakeEvery(1 second)
//  val sink: Sink[Task, Seq[String]] = scalaz.stream.sink.lift[Task, Seq[String]](effect)
//  val batches: Process[Task, Seq[String]] = (received zip throtler).map(_._1)
//  val consumer: Process[Task, Unit] = batches to sink
//
//  val producer: Process[Task, Unit] = time.awakeEvery(100 milliseconds).evalMap(d => for {
//    _ <- Task.delay(println("about to enqueue"))
//    _ <- q.enqueueOne(d.toMillis.toString)
//    _ <- Task.delay(println("done enqueue"))
//  } yield ())
//
//  val app: Task[Unit] = Nondeterminism[Task].nmap2(producer.run, consumer.run){ case (_,_) => () }
//
//  app.run
//}