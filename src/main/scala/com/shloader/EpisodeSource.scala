package com.shloader

/**
 *
 */
trait EpisodeSource {
  def getEpisodesToDownload():List[Episode]
  def setProcessed(episode:Episode)
}
