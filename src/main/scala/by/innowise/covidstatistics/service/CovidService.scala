package by.innowise.covidstatistics.service

import by.innowise.covidstatistics.model.{CasesStatistic, Country, DayStatistic}
import cats.effect.kernel.Concurrent
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits.uri
import cats.implicits.*

trait CovidService[F[_]]:
  def getCountryList: F[List[Country]]
  def getMinMaxStatistic(countrySlug: String, fromDate: String, toDate: String): F[CasesStatistic]

object CovidService:
  def apply[F[_]](implicit ev: CovidService[F]): CovidService[F] = ev

  private val baseUrl = uri"https://api.covid19api.com"
  private val requiredStatus: String = "confirmed"

  private final case class CovidServiceException(e: Throwable) extends RuntimeException

  def impl[F[_]: Concurrent](client: Client[F]): CovidService[F] = new CovidService[F]:
    val dsl: Http4sClientDsl[F] = new Http4sClientDsl[F] {}
    import dsl.*

    override def getCountryList: F[List[Country]] = {
      client.expect[List[Country]](baseUrl / "countries")
    }

    override def getMinMaxStatistic(countrySlug: String, fromDate: String, toDate: String): F[CasesStatistic] = {
      for {
        casesForCountry <- getCasesForCountry(countrySlug, fromDate, toDate)
      } yield CasesStatistic(countrySlug, getMinCases(casesForCountry), getMaxCases(casesForCountry), fromDate, toDate)
    }

    private def getCasesForCountry(countrySlug: String, fromDate: String, toDate: String): F[List[DayStatistic]] = {
      for {
        casesForCountry <- client.expect[List[DayStatistic]](
          baseUrl / "country" / countrySlug / "status" / requiredStatus +? ("from", fromDate) +? ("to", toDate)
        )
      } yield casesForCountry
    }

    private def getMinCases(cases: List[DayStatistic]): DayStatistic = {
      cases.minByOption(dayStatistic => dayStatistic.cases).getOrElse(throw new RuntimeException())
    }

    private def getMaxCases(cases: List[DayStatistic]): DayStatistic = {
      cases.maxByOption(dayStatistic => dayStatistic.cases).getOrElse(throw new RuntimeException())
    }