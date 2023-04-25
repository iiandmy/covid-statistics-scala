package by.innowise.covidstatistics.model

import io.circe.{Decoder, Encoder, DecodingFailure}

case class Country(country: String, slug: String, iso2: String)

object Country:
  given Decoder[Country] = Decoder.instance { cursor =>
    for {
      name <- cursor.get[String]("Country")
      slug <- cursor.get[String]("Slug")
      iso2 <- cursor.get[String]("ISO2")
    } yield Country(name, slug, iso2)
  }
  
  given Encoder[Country] = Encoder.AsObject.derived[Country]

  given Decoder[List[Country]] = Decoder.decodeList[Country]
  
  given Encoder[List[Country]] = Encoder.encodeList[Country]
