package by.innowise.covidstatistics.model

import io.circe.Encoder
import org.http4s.EntityEncoder
import org.http4s.circe.{jsonEncoderOf}

case class CasesStatistic(
  country: String, 
  minCases: DayStatistic, 
  maxCases: DayStatistic, 
  fromDate: String, 
  toDate: String
)

object CasesStatistic:
  given[F[_]]: EntityEncoder[F, CasesStatistic] = jsonEncoderOf
  given Encoder[CasesStatistic] = Encoder.AsObject.derived[CasesStatistic]
