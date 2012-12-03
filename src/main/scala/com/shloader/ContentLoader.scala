package com.shloader

import java.net.URL

/**
 *
 */
trait ContentLoader {
  def load(url:URL, episode:Episode):Iterator[String]
}
