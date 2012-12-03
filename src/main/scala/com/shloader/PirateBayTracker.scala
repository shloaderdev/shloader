package com.shloader

import java.net.{URLEncoder, URL}
import collection.Iterator

/**
 *
 */
class PirateBayTracker(contentLoader:ContentLoader = new RealContentLoader) extends TorrentTracker {
  override def find(episode:Episode):Option[Torrent] = {
    val urlString = getUrlString(episode)
    val url = new URL(urlString)
    val content = contentLoader.load(url, episode)
    getLink(content, episode)
  }

  private def getUrlString(episode:Episode) =
    "http://thepiratebay.se/search/" + URLEncoder.encode(episode.searchString, "UTF-8") + "/0/7/0"

  private def getLink(content:Iterator[String], episode:Episode):Option[Torrent] = {
    val toMatch = regexStringToMatch(episode)
    val toExtractMagnet = "magnet:[^\"]*"
    val matchingStrings = content.map(_.toLowerCase).filter(_.matches(toMatch)).toList

    if (!matchingStrings.isEmpty) {
      val matchingString = matchingStrings(0)
      val magnet = toExtractMagnet.r.findFirstIn(matchingString).get
      new Some(new Torrent(magnet))
    } else {
      None
    }
  }

  def regexStringToMatch(episode:Episode):String = {
    ".*magnet:[^\"]*" +
    episode.searchString().replaceAll(" ", "[^\"]*") + "[^\"]*\" .*" +
    "(" + PirateBayTracker.ALLOWED_UPLOADERS.mkString("|") + ")" +
    ".*"
  }
}

object PirateBayTracker {
  final val ALLOWED_UPLOADERS = List("ettv", "eztv", "tvteam")
}