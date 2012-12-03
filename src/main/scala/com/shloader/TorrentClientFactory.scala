package com.shloader

/**
 *
 */
object TorrentClientFactory {
  def create(clientName:String) = clientName match {
    case "transmission" => new Transmission
    case other:String => throw new Exception("Unsupported torrent client: " + other);null
  }
}
