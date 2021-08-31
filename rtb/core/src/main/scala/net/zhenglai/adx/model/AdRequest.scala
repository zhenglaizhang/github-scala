package net.zhenglai.adx.model

import cats.data.NonEmptyList

final case class AdRequest()
final case class AdResponse()

/**
  * Top level object for RTB bidding request
  */
final case class BidRequest(
    site: Site,
    content: Content,
    user: Option[User],
    device: Option[Device],
    test: Boolean =
      false, // 0 = live mode, 1 = test mode (auctions are not billable), default 0,
    at: Int = AuctionType.SecondPricePlus,
    regs: Seq[Regs],
    source: Seq[Source],
    imp: NonEmptyList[Imp] // only this is technically required
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

/**
  * Container for the description of a specific impression; at least 1 per request
  * */
case class Imp(
    native: Option[Native],
    banner: Option[Banner],
    video: Option[Video],
    audio: Option[Audio],
    metric: Option[Metric],
    pmp: Option[Pmp]
)

/**
  * A quantifiable often historical data point about an impression
  */
case class Metric()
case class Native()
case class Banner(formats: Seq[Format])

/**
  * An allowed size of a banner
  */
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

/**
  * Regulatory conditions in effect for all impressions in this bid request
  */
case class Regs()

/**
  * Request source details on post-auction decisioning (e.g. header bidding)
  */
case class Source()

case class DistributionChannel(
)

/**
  * Details of the application calling for the impression
  */
case class App()

/**
  * Entity that controls the content of and distributes the site or app
  */
case class Publisher()

// todo use enum
object AuctionType {
  val FirstPrice = 1
  val SecondPricePlus = 2
}
