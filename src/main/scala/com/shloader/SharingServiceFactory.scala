package com.shloader

/**
 *
 */
object SharingServiceFactory {
  def create(serviceName:String) = serviceName match {
    case "dropbox" => new DropboxSharingService
    case other:String => throw new Exception("Unsupported sharing service: " + other);null
  }
}
