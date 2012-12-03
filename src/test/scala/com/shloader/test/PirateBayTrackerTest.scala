package com.shloader.test

import org.scalatest.FunSuite
import com.shloader.{PirateBayTracker, Episode}
import com.shloader.stub.PirateBayContentLoaderStub

/**
 *
 */
class PirateBayTrackerTest extends FunSuite {

  test("test found") {
    val episode = new Episode("dexter", 2, 22)
    val tracker = new PirateBayTracker(new PirateBayContentLoaderStub(true))
    val torrent = tracker.find(episode)
    assert(!torrent.isEmpty)
  }

  test("test not found") {
    val episode = new Episode("dexter", 2, 22)
    val tracker = new PirateBayTracker(new PirateBayContentLoaderStub(false))
    val torrent = tracker.find(episode)
    assert(torrent.isEmpty)
  }
}
