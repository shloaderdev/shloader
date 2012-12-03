package com.shloader

/**
 *
 */
trait TorrentTracker {
  def find(episode:Episode):Option[Torrent]
}
