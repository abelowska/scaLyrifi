package actors

import java.io.FileOutputStream

import Data.Song
import com.lowagie.text._
import com.lowagie.text.pdf.PdfWriter

class PdfCreator(fileName: String) {

  def saveOne(song: Song): Unit = {
    val pdfDoc = new Document(PageSize.A4)
    PdfWriter.getInstance(pdfDoc, new FileOutputStream(s"$fileName.pdf")).setPdfVersion(PdfWriter.PDF_VERSION_1_7)
    pdfDoc.open()

    val titleFont = new Font
    titleFont.setStyle(Font.BOLD)
    titleFont.setSize(7)

    val authorFont = new Font
    authorFont.setStyle(Font.BOLD)
    authorFont.setSize(6)

    val myfont = new Font
    myfont.setStyle(Font.NORMAL)
    myfont.setSize(5)

    val title = new Paragraph(song.header.title + "\n", titleFont)
    val author = new Paragraph(song.header.author + "\n", authorFont)

    pdfDoc.add(title)
    pdfDoc.add(author)

    val para = new Paragraph(song + "\n", myfont)
    para.setAlignment(Element.ALIGN_JUSTIFIED)
    pdfDoc.add(para)

    pdfDoc.close()
  }

  def saveMany(songs: Seq[Song]): Unit = {

  }


}
