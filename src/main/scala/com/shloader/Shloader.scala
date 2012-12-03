package com.shloader

import java.util.Calendar
import java.text.SimpleDateFormat

/**
 * Main class
 * Inits major components,
 * shares completed episodes via sharing service,
 * and starts new downloads
 */
object Shloader {
  private val _shloaderHome = System.getenv("SHLOADER_HOME")
  if (_shloaderHome == null || _shloaderHome.isEmpty) {
    throw new Exception("SHLOADER_HOME is not set! Please set SHLOADER_HOME environment variable")
  }

  // TODO support using multiple torrent trackers
  private val torrentTracker:TorrentTracker = TorrentTrackerFactory.create(Config.getOption("plugin.torrenttracker").getOrElse(Defaults.TORRENT_TRACKER))

  private val sharingService:SharingService = SharingServiceFactory.create(Config.getOption("plugin.sharingservice").getOrElse(Defaults.SHARING_SERVICE))

  private val torrentClient:TorrentClient = TorrentClientFactory.create(Config.getOption("plugin.torrentclient").getOrElse(Defaults.TORRENT_CLIENT))

  private val episodeSource:EpisodeSource = EpisodeSourceFactory.create(Config.getOption("plugin.episodesource").getOrElse(Defaults.EPISODE_SOURCE))

  def main(args:Array[String]) {
    var now = Calendar.getInstance().getTime
    val timeFormat = new SimpleDateFormat("hh:mm:ss")

    println("Shloader started at " + timeFormat.format(now))

    shareCompleted()
    downloadNew()

    now = Calendar.getInstance().getTime
    println("Shloader stopped at " + timeFormat.format(now))
  }

  def shloaderHome = _shloaderHome

  private def shareCompleted() {
    sharingService.share()
  }

  private def downloadNew() {
    val availableTorrents = getAvailableTorrents
    availableTorrents.foreach(torrent => {
      println("trying to download torrent: " + torrent.url)
      torrentClient.startTorrent(torrent)
    })
  }

  private def getAvailableTorrents:List[Torrent] = {
    val episodesToDownload = episodeSource.getEpisodesToDownload()
    var result = List[Torrent]()

    episodesToDownload.foreach(episode => {
      val torrentOption = torrentTracker.find(episode)
      if (!torrentOption.isEmpty) {
        episodeSource.setProcessed(episode)
        result ::= torrentOption.get
      }
    })

    result
  }
}
