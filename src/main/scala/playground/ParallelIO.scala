package playground

import java.time.LocalTime
import cats.effect.{ContextShift, IO}
import cats.implicits._
import scala.concurrent.ExecutionContext

object ParallelIO extends App {
  implicit val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  val tasks = Vector(1, 2, 3)
  val program: IO[Vector[Unit]] = tasks.traverse(runTask)
  val programInParallel: IO[Vector[Unit]] = tasks.parTraverse(runTask)
  runProgram("program in sequence", program)
  runProgram("program in parallel", programInParallel)

  def runTask(taskNumber: Int): IO[Unit] = IO {
    val thread = Thread.currentThread.getName
    println(s"[$thread] task: $taskNumber, time of execution: ${LocalTime.now.getSecond}th sec")
    Thread.sleep(1000)
  }

  def runProgram(name: String, prog: IO[Vector[Unit]]): Vector[Unit] = {
    println(name)
    prog.unsafeRunSync()
  }
}
