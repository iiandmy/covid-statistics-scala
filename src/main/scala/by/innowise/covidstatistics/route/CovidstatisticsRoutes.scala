package by.innowise.covidstatistics.route

import by.innowise.covidstatistics.service.{CovidService, HelloWorld}
import cats.effect.Sync
import cats.implicits.*
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object CovidstatisticsRoutes:
  def covidServiceRoutes[F[_]: Sync](CovidService: CovidService[F]): HttpRoutes[F] =
    val dsl = new Http4sDsl[F]{}
    import dsl.*

    HttpRoutes.of[F] {
      case GET -> Root / "countries" =>
        for {
          countries <- CovidService.getCountryList
          resp <- Ok(countries)
        } yield resp

    }
