package by.innowise.covidstatistics

import by.innowise.covidstatistics.server.CovidstatisticsServer
import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp.Simple:
  val run = CovidstatisticsServer.run[IO]
