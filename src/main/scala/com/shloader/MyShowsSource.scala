package com.shloader

import util.parsing.json.JSON
import java.net.URL
import collection.JavaConversions
import java.io.{FileInputStream, FileWriter}

/**
 *
 */
class MyShowsSource() extends EpisodeSource {
  private val login = Config.getOption("episodesource.myshows.login").getOrElse(Defaults.MYSHOWS_DEFAULT_LOGIN)
  private val passwordMd5 = Config.getOption("episodesource.myshows.password_md5").getOrElse(Defaults.MYSHOWS_DEFAULT_PASSWORD_MD5)
  private var phpSessionId = ""
  private val showNames = collection.mutable.Map[Int, String]()
  private var processedEpisodes = List[Episode]()

  doLogin()
  initProcessed()

  private def doLogin() {
    val url = new URL("http://api.myshows.ru/profile/login?login=" + login + "&password=" + passwordMd5)
    val connection = url.openConnection()
    val cookiesString = JavaConversions.asScalaBuffer(JavaConversions.mapAsScalaMap(connection.getHeaderFields).get("Set-Cookie").get).mkString(" ")
    val regex = """.*PHPSESSID=(.+);.*""".r
    val regex(_phpSessionId) = cookiesString
    phpSessionId = _phpSessionId
    println("Logged in: phpSessionId=" + phpSessionId)
  }

  private def initProcessed() {
    val is = new FileInputStream(Shloader.shloaderHome + "/" + Config.getOption("episodesource.myshows.processed").getOrElse(Defaults.MYSHOWS_PROCESSED_FOLDER))
    val episodeStrings = scala.io.Source.fromInputStream(is).getLines.filter(!_.trim.isEmpty)
    processedEpisodes = episodeStrings.map(parseEpisode(_)).toList
  }

  def getEpisodesToDownload() :List[Episode] = {
    var res = List[Episode]()
    val json = getUnwatchedJson().asInstanceOf[Map[Any, Any]]
    json.foreach(p => {
      val episodeData = p._2.asInstanceOf[Map[Any, Any]]
      val showId = episodeData.get("showId").get.asInstanceOf[Double].toInt
      val showName = getShowName(showId)
      val seasonNumber = episodeData.get("seasonNumber").get.asInstanceOf[Double].toInt
      val episodeNumber = episodeData.get("episodeNumber").get.asInstanceOf[Double].toInt
      if (episodeNumber > 0) {
        val episode = new Episode(showName, seasonNumber, episodeNumber, showId)
        if (!processedEpisodes.contains(episode)) {
          res ::= episode
        }
      }
    })

    res
  }

  private def parseEpisode(s:String):Episode = {
    val tokens = s.split(" ")
    new Episode("", tokens(1).toInt, tokens(2).toInt, tokens(0).toInt)
  }

  private def getUnwatchedJson():Any = {
    doRequest("http://api.myshows.ru/profile/episodes/unwatched/", true)
  }

  // cache
  private def getShowName(id:Int):String = {
    if (!showNames.get(id).isEmpty) {
      return showNames.get(id).get
    }

    val json = getShowDataJson(id)
    val result = json.get("title").get.asInstanceOf[String]
    showNames.put(id, result)
    result
  }

  private def getShowDataJson(id:Int):Map[Any, Any] = {
    doRequest("http://api.myshows.ru/shows/" + id, false).asInstanceOf[Map[Any, Any]]
  }

  private def doRequest(urlStr:String, auth:Boolean = true):Any = {
    val url = new URL(urlStr)
    val connection = url.openConnection()
    if (auth) {
      connection.setRequestProperty("Cookie", "PHPSESSID=" + phpSessionId)
    }
    val response = scala.io.Source.fromInputStream(connection.getInputStream).mkString
    JSON.parseFull(response).get
  }

  override def setProcessed(episode:Episode) {
    val fw = new FileWriter(Shloader.shloaderHome + "/" + Config.getOption("episodesource.myshows.processed").getOrElse(Defaults.MYSHOWS_PROCESSED_FOLDER), true) ;
    fw.write(episode.showId + " " + episode.seasonNumber + " " + episode.episodeNumber + "\n") ;
    fw.close()
  }
}
