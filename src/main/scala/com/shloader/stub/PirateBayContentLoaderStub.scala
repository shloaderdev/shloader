package com.shloader.stub

import com.shloader.{PirateBayTracker, Episode, ContentLoader}
import java.net.URL

/**
 *
 */
class PirateBayContentLoaderStub(mustBeFound:Boolean = true) extends ContentLoader {
  override def load(url:URL, episode:Episode):Iterator[String] = {
    val res = new scala.collection.mutable.ListBuffer[String]
    res += "hello"
    res += "how low"
    if (mustBeFound) {
      var stringWithEpisode = "<a href=\"magnet:?xt=urn:btih:516ad904a62fc60e2728eb054d569677151ba0b5&dn=<episode>.DSR.XviD-NoTV.avi&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Ftracker.publicbt.com%3A80&tr=udp%3A%2F%2Ftracker.istole.it%3A6969&tr=udp%3A%2F%2Ftracker.ccc.de%3A80\" title=\"Download this torrent using magnet\"><img src=\"//static.thepiratebay.se/img/icon-magnet.gif\" alt=\"Magnet link\" /></a><img src=\"//static.thepiratebay.se/img/icon_comment.gif\" alt=\"This torrent has 2 comments.\" title=\"This torrent has 2 comments.\" /><img src=\"//static.thepiratebay.se/img/icon_image.gif\" alt=\"This torrent has a cover image\" title=\"This torrent has a cover image\" /><a href=\"/user/<uploader>\"><img src=\"//static.thepiratebay.se/img/vip.gif\" alt=\"VIP\" title=\"VIP\" style=\"width:11px;\" border='0' /></a>"
      stringWithEpisode = stringWithEpisode.replace("<episode>", episode.searchString())
      stringWithEpisode = stringWithEpisode.replace("<uploader>", PirateBayTracker.ALLOWED_UPLOADERS(0))
      res += stringWithEpisode
    }
    res.iterator
  }
}
