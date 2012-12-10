package com.shloader

import org.apache.commons.io.FileUtils
import java.io.File

/**
 *
 */
class DropboxSharingService extends SharingService {
  override def share() {
    val source = new File(Config.getOption("torrent.client.dir.completed").getOrElse(Defaults.COMPLETE_DIR))
    val dest = new File(Config.getOption("dropbox.dir").getOrElse(Defaults.DROPBOX_DIR))
    FileUtils.copyDirectory(source, dest)
    FileUtils.cleanDirectory(source)
  }
}
