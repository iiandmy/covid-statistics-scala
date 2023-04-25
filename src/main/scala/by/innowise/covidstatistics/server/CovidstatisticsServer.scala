package by.innowise.covidstatistics.server

import by.innowise.covidstatistics.route.CovidstatisticsRoutes
import by.innowise.covidstatistics.service.CovidService
import cats.effect.Async
import cats.syntax.all.*
import com.comcast.ip4s.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*
import org.http4s.headers.Origin
import org.http4s.Uri
import org.http4s.server.middleware.{CORS, Logger}

object CovidstatisticsServer:

  def run[F[_]: Async]: F[Nothing] = {
    for {
      client <- EmberClientBuilder.default[F].build
      covidService = CovidService.impl[F](client)

      httpApp = (
        CovidstatisticsRoutes.covidServiceRoutes[F](covidService)
      ).orNotFound

      logger = Logger.httpApp(true, true)(httpApp)

      corsHttpApp = CORS.policy
        .withAllowOriginHost(Set(
          Origin.Host(Uri.Scheme.http, Uri.RegName("localhost"), Some(4200))
        ))
        .withAllowCredentials(false)
        .httpApp(logger)

      _ <- 
        EmberServerBuilder.default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(corsHttpApp)
          .build
    } yield ()
  }.useForever
