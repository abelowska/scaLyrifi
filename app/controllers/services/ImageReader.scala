package controllers.services

import java.awt.image.BufferedImage

import controllers.data.SongHeader
import net.sourceforge.tess4j.Tesseract

import scala.util.Try

class ImageReader {
  private val ocrService = {
    val tesseract = new Tesseract()
    tesseract.setLanguage("pol")
    tesseract
  }

  def extractSongHeaders(image: BufferedImage): Seq[SongHeader] = {
    Try(ocrService.doOCR(image)).toOption
      .toList
      .flatMap(s => s.split("\n"))
      .map(s => {
        val Array(title, author) = s.split(",|\\.")
        SongHeader(title.trim, author.trim)
      })
  }
}
