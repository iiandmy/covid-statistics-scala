package by.innowise.covidstatistics.service

import by.innowise.covidstatistics.model.Country
import cats.effect.kernel.Concurrent
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.implicits.uri

trait CovidService[F[_]]:
  def getCountryList: F[List[Country]]

object CovidService:
  def apply[F[_]](implicit ev: CovidService[F]): CovidService[F] = ev

  private val baseUrl = uri"https://api.covid19api.com"

  private final case class CovidServiceException(e: Throwable) extends RuntimeException

  def impl[F[_]: Concurrent](client: Client[F]): CovidService[F] = new CovidService[F]:
    val dsl: Http4sClientDsl[F] = new Http4sClientDsl[F] {}
    import dsl.*

    override def getCountryList: F[List[Country]] = {
      client.expect[List[Country]](baseUrl / "countries")
    }