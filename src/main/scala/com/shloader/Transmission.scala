package com.shloader

/**
 *
 */
class Transmission extends TorrentClient {
  private val completedDir = Config.getAndReplaceSystemProperties("torrent.client.dir.completed")
  private val uncompletedDir = Config.getAndReplaceSystemProperties("torrent.client.dir.uncompleted")

  override def startTorrent(torrent:Torrent) {
    val command = Seq(
      "transmission-remote",
      "--incomplete-dir", uncompletedDir,
      "-w", completedDir,
      "--add", torrent.url
    )
    Executor.run(command)
  }
}
