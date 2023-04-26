package by.innowise.covidstatistics.route

import by.innowise.covidstatistics.service.CovidService
import cats.effect.Sync
import cats.implicits.*
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.QueryParamDecoderMatcher

import scala.language.postfixOps

object CovidstatisticsRoutes:
  private object FromDateParam extends QueryParamDecoderMatcher[String]("from")

  private object ToDateParam extends QueryParamDecoderMatcher[String]("to")

  def covidServiceRoutes[F[_]: Sync](CovidService: CovidService[F]): HttpRoutes[F] =
    val dsl = new Http4sDsl[F]{}
    import dsl.*

    HttpRoutes.of[F] {
      case GET -> Root / "countries" =>
        for {
          countries <- CovidService.getCountryList
          response <- Ok(countries)
        } yield response
      case GET -> Root / "country" / country / "minmax" :?
        FromDateParam(from) +& ToDateParam(to) =>
        for {
          minMaxStatistic <- CovidService.getMinMaxStatistic(country, from, to)
          response <- Ok(minMaxStatistic)
        } yield response
    }
