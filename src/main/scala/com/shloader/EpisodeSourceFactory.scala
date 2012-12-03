package com.shloader

/**
 *
 */
object EpisodeSourceFactory {
  def create(episodeSourceName:String) = episodeSourceName match {
    case "myshows" => new MyShowsSource
    case "text" => new TextFileEpisodeSource
    case other:String => throw new Exception("Unsupported episode source: " + other);null
  }
}
