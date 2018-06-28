package services

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, InputStream}

import Data.Song
import com.lowagie.text._
import com.lowagie.text.pdf._
import org.apache.pdfbox.io.MemoryUsageSetting
import org.apache.pdfbox.multipdf.PDFMergerUtility

import scala.collection.JavaConverters._

object PdfCreator {

  private val TitleFont = {
    val tf = new Font
    tf.setStyle(Font.BOLD)
    tf.setSize(7)
    tf
  }

  private val AuthorFont = {
    val af = new Font
    af.setStyle(Font.BOLD)
    af.setSize(6)
    af
  }

  private val BasicFont = {
    val bf = new Font
    bf.setStyle(Font.NORMAL)
    bf.setSize(5)
    bf
  }

}

class PdfCreator(fileName: String) {

  import PdfCreator._

  def save(songs: Seq[Song]): Unit = {

    val inputStreams = songs.map(s => generateOne(s))

    val util = new PDFMergerUtility
    util.addSources(inputStreams.asJava)
    util.setDestinationFileName(s"./$fileName.pdf")

    util.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly())

  }

  def generateOne(song: Song): InputStream = {
    val out = new ByteArrayOutputStream
    val pdfDoc = new Document(PageSize.A4)
    PdfWriter.getInstance(pdfDoc, out).setPdfVersion(PdfWriter.PDF_VERSION_1_7)
    pdfDoc.open()

    val title = new Paragraph(song.header.title + "\n", TitleFont)
    val author = new Paragraph(song.header.author + "\n", AuthorFont)

    pdfDoc.add(title)
    pdfDoc.add(author)

    val para = new Paragraph(song + "\n", BasicFont)
    para.setAlignment(Element.ALIGN_JUSTIFIED)
    pdfDoc.add(para)

    pdfDoc.close()

    new ByteArrayInputStream(out.toByteArray)
  }


}
