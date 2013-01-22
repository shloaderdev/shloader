package com.shloader

import org.apache.commons.io.FileUtils
import java.io.File

/**
 *
 */
class DropboxSharingService extends SharingService {
  override def share() {
    val source = new File(Config.getAndReplaceSystemProperties("torrent.client.dir.completed"))
    val dest = new File(Config.getAndReplaceSystemProperties("dropbox.dir").getOrElse(Defaults.DROPBOX_DIR))
    FileUtils.copyDirectory(source, dest)
    FileUtils.cleanDirectory(source)
  }
}
