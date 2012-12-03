package com.shloader

/**
 *
 */
object TorrentTrackerFactory {
  def create(trackerName:String) = trackerName match {
    case "thepiratebay" => new PirateBayTracker()
    case other:String => throw new Exception("Unsupported torrent tracker: " + other);null
  }
}
