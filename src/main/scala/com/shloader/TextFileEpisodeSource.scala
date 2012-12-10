package com.shloader

import java.io.{FileInputStream, File}

/**
 *
 */
class TextFileEpisodeSource extends EpisodeSource {

  override def getEpisodesToDownload():List[Episode] = {
    getEpisodesFromFile(Config.getOption("episodesource.to_download").getOrElse(Defaults.TEXT_SOURCE_TO_DOWNLOAD_DIR))
  }

  def getEpisodesFromFile(fileName:String):List[Episode] = {
    val is = new FileInputStream(fileName)
    val episodeStrings = scala.io.Source.fromInputStream(is).getLines.filter(!_.trim.isEmpty)
    episodeStrings.map(parseEpisode(_)).toList
  }

  private def parseEpisode(s:String):Episode = {
    val tokens = s.split(" ")
    val season = tokens(tokens.size - 2).toInt
    val episodeNumber = tokens(tokens.size - 1).toInt
    val showName = tokens.slice(0, tokens.size - 2).mkString(" ")
    new Episode(showName, season, episodeNumber)
  }

  override def setProcessed(episode:Episode) = {}
}
