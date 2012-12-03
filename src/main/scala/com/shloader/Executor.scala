package com.shloader

import scala.sys.process._

/**
 *
 */
object Executor {
  def run(args:Seq[String]) {
    val commandStr = args.mkString(" ")
    println("executing command: " + commandStr)
    args.lines.mkString.foreach(print(_))
    println()
  }
}
