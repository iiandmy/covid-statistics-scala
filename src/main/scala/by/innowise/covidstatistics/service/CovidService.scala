package by.innowise.covidstatistics.service

import by.innowise.covidstatistics.model.*
import cats.effect.kernel.Concurrent
import org.http4s.implicits.uri

trait CovidService[F[_]]:
  def getCountryList: F[List[Country]]

object CovidService:
  def apply[F[_]](implicit ev: CovidService[F]): CovidService[F] = ev

  private val baseUrl = uri"https://api.covid19api.com"

  def impl[F[_]: Concurrent](client: Client[F]): CovidService[F] = new CovidService[F]:
    override def getCountryList: F[List[Country]] = {
      client.expect[List[Country]](baseUrl / "countries")

    }