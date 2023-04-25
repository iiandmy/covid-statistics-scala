package by.innowise.covidstatistics.model

import cats.effect.Concurrent
import io.circe.{Decoder, DecodingFailure, Encoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

case class Country(country: String, slug: String, iso2: String)

object Country:
  given Decoder[Country] = Decoder.instance { cursor =>
    for {
      name <- cursor.get[String]("Country")
      slug <- cursor.get[String]("Slug")
      iso2 <- cursor.get[String]("ISO2")
    } yield Country(name, slug, iso2)
  }

  given[F[_] : Concurrent]: EntityDecoder[F, Country] = jsonOf

  given[F[_]]: EntityEncoder[F, Country] = jsonEncoderOf

  given[F[_] : Concurrent]: EntityDecoder[F, List[Country]] = jsonOf

  given[F[_]]: EntityEncoder[F, List[Country]] = jsonEncoderOf

  given Encoder[Country] = Encoder.AsObject.derived[Country]

  given Decoder[List[Country]] = Decoder.decodeList[Country]

  given Encoder[List[Country]] = Encoder.encodeList[Country]
