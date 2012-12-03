package com.shloader

/**
 *
 */
class KTorrent extends TorrentClient {
  override def startTorrent(torrent:Torrent) {
    val command = Seq("ktorrent", "--silent", torrent.url)
    Executor.run(command)
  }
}
