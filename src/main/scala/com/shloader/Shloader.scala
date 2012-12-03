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

  // TODO create factories for all this stuff

  val sharingService:SharingService = Config.getOption("plugin.sharingservice").getOrElse(Defaults.SHARING_SERVICE) match {
    case "dropbox" => new DropboxSharingService
    case other:String => throw new Exception("Unknown sharing service: " + other);null
  }

  val torrentClient:TorrentClient = Config.getOption("plugin.torrentclient").getOrElse(Defaults.TORRENT_CLIENT) match {
    case "transmission" => new Transmission
    case other:String => throw new Exception("Unknown torrent client: " + other);null
  }

  // TODO support using multiple torrent trackers
  val torrentTracker:TorrentTracker = Config.getOption("plugin.torrenttracker").getOrElse(Defaults.TORRENT_TRACKER) match {
    case "thepiratebay" => new PirateBayTracker
    case other:String => throw new Exception("Unknown torrent tracker: " + other);null
  }

  val episodeSource:EpisodeSource = Config.getOption("plugin.episodesource").getOrElse(Defaults.EPISODE_SOURCE) match {
    case "myshows" => new MyShowsSource
    case other:String => throw new Exception("Unknown episode source: " + other);null
  }

  def main(args:Array[String]) {
    var now = Calendar.getInstance().getTime()
    val timeFormat = new SimpleDateFormat("hh:mm:ss")

    println("Shloader started at " + timeFormat.format(now))

    shareCompleted
    downloadNew

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

  private def getAvailableTorrents():List[Torrent] = {
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
