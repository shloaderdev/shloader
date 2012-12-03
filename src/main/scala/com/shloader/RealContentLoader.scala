package com.shloader

import java.net.URL

/**
 *
 */
class RealContentLoader extends ContentLoader {
  override def load(url:URL, episode:Episode):Iterator[String] = {
    val connection = url.openConnection()
    connection.setConnectTimeout(2000)
    connection.setReadTimeout(2000)
    val is = connection.getInputStream
    val result = scala.io.Source.fromInputStream(is).getLines
    result
  }
}
