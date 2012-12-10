package com.shloader

import java.util.Properties
import java.io.FileInputStream
import collection.JavaConversions
import org.apache.commons.lang3.text.StrSubstitutor

/**
 * Load config
 */
object Config {
  private val configLocation = "conf.properties"

  val systemPropertySubstitutor = new StrSubstitutor(System.getenv())

  val properties = new Properties()
  properties.load(new FileInputStream(configLocation))
  val propertiesMap = JavaConversions.propertiesAsScalaMap(properties)

  def getOption(name:String) = propertiesMap.get(name)

  def get(name:String) = propertiesMap.get(name).get

  def getAndReplaceSystemProperties(name:String) = systemPropertySubstitutor.replace(get(name))
}
