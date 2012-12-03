package com.shloader.test

import org.scalatest.FunSuite
import com.shloader.Episode

class EpisodeTest extends FunSuite {

  test("small episode number") {
    val episode = new Episode("dexter", 1, 2)
    assert(episode.searchString() == "dexter s01e02")
  }

  test("big episode number") {
    val episode = new Episode("dexter", 10, 22)
    assert(episode.searchString() == "dexter s10e22")
  }
}
