package com.shloader

import java.util.Properties
import java.io.FileInputStream
import collection.JavaConversions

/**
 * Load config
 */
object Config {
  // TODO use environment var, or something
  private val configLocation = Shloader.shloaderHome + "/conf.properties"

  val properties = new Properties()
  properties.load(new FileInputStream(configLocation))
  val propertiesMap = JavaConversions.propertiesAsScalaMap(properties)

  def getOption(name:String) = propertiesMap.get(name)
}
