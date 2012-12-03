package com.shloader

/**
 *
 */
class Transmission extends TorrentClient {
  override def startTorrent(torrent:Torrent) {
    val command = Seq(
      "transmission-remote",
      "--incomplete-dir", "/home/lacungus/.shloader/incomplete",
      "-w", "/home/lacungus/.shloader/complete",
      "--add", torrent.url
    )
    Executor.run(command)
  }
}
