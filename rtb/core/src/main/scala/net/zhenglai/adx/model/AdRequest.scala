package net.zhenglai.adx.model

import cats.data.NonEmptyList

final case class AdRequest()
final case class AdResponse()

final case class BidRequest(
    site: Site,
    content: Content,
    user: Option[User],
    device: Option[Device],
    regs: Seq[Regs],
    source: Seq[Source],
    impressions: NonEmptyList[Impression] // only this is technically required
)
final case class BidResponse(
    bid: Bid,
    creative: Creative,
    markup: Option[Markup]
)

final case class Bidder()

final case class WinNotice()
final case class Markup()

final case class LossNotice()

case class Content(
    segments: Seq[Segment]
)

case class Device(geo: Option[Geo])
case class Geo()

case class Site()

case class User(geo: Option[Geo], data: Option[Data])
case class Data(segments: Seq[Segment])
case class Segment()

case class Location()

case class Bid()

case class Creative()

case class Impression(
    native: Option[Native],
    banner: Option[Banner],
    video: Option[Video],
    audio: Option[Audio],
    metric: Option[Metric],
    pmp: Option[Pmp]
)

case class Metric()
case class Native()
case class Banner(formats: Seq[Format])
case class Format()

/**
  * details for video impression
  */
case class Video(
    banners: Seq[Banner]
)

/**
  *  Container for audio impression
  */
case class Audio(
    banners: Seq[Banner]
)
case class Pmp(deal: Seq[Deal])
case class Deal()

case class Regs()
case class Source()

case class DistributionChannel(
)
