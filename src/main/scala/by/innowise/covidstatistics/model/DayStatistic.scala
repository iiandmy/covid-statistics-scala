package by.innowise.covidstatistics.model

import cats.effect.Concurrent
import io.circe.{Decoder, Encoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

case class DayStatistic(
  country: String,
  countryCode: String,
  cases: Int,
  status: String,
  date: String
)

object DayStatistic:
  given Decoder[DayStatistic] = Decoder.instance { cursor =>
    for {
      country <- cursor.get[String]("Country")
      countryCode <- cursor.get[String]("CountryCode")
      cases <- cursor.get[Int]("Cases")
      status <- cursor.get[String]("Status")
      date <- cursor.get[String]("Date")
    } yield DayStatistic(country, countryCode, cases, status, date)
  }
  
  given[F[_] : Concurrent]: EntityDecoder[F, DayStatistic] = jsonOf

  given[F[_]]: EntityEncoder[F, DayStatistic] = jsonEncoderOf

  given[F[_] : Concurrent]: EntityDecoder[F, List[DayStatistic]] = jsonOf

  given[F[_]]: EntityEncoder[F, List[DayStatistic]] = jsonEncoderOf

  given Encoder[DayStatistic] = Encoder.AsObject.derived[DayStatistic]

  given Decoder[List[DayStatistic]] = Decoder.decodeList[DayStatistic]

  given Encoder[List[DayStatistic]] = Encoder.encodeList[DayStatistic]
