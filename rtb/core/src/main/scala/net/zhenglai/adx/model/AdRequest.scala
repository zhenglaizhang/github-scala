package net.zhenglai.adx.model

import jdk.vm.ci.code.site.Site
import net.zhenglai.util.login.User

final case class AdRequest()
final case class AdResponse()

final case class BidRequest(
    site: Site,
    content: Content,
    user: User,
    device: Device,
    location: Location,
    impressions: Seq[Impression]
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

case class Content()

case class Device()

case class Location()

case class Bid()

case class Creative()

case class Impression(
    native: Option[Native],
    banner: Option[Banner],
    video: Option[Video],
    audio: Option[Audio],
    pmp: Option[Pmp]
)

case class Native()
case class Banner()
case class Video()
case class Audio()
case class Pmp(deal: Option[Deal])
case class Deal()
