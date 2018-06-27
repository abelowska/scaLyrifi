import java.io.FileOutputStream

import com.lowagie.text._
import com.lowagie.text.pdf.PdfWriter
import com.machinepublishers.jbrowserdriver.{JBrowserDriver, Settings, Timezone}
import org.openqa.selenium.By

import scala.collection.JavaConverters._

object Test {
  val url = "https://www.ultimate-guitar.com/search.php?search_type=title&value=hey%20ya%20outkast"

  def main(args: Array[String]): Unit = {
    val driver = new JBrowserDriver(Settings.builder().timezone(Timezone.EUROPE_WARSAW).build())
    driver.get(url)

    val songPage = driver.findElements(By.className("_1iQi2"))
      .asScala
      .filter(_.getText.contains("Chords"))
      .map(_.findElement(By.tagName("a")))
      .map(_.getAttribute("href"))
      .toArray
      .head

    driver.get(songPage)

    val song = driver.findElements(By.className("_1YgOS"))
      .asScala
      .map(_.getText())
      .mkString("\n")


    val pdfDoc = new Document(PageSize.A4)
    PdfWriter.getInstance(pdfDoc, new FileOutputStream("./song.pdf")).setPdfVersion(PdfWriter.PDF_VERSION_1_7)
    pdfDoc.open()

    val myfont = new Font
    myfont.setStyle(Font.NORMAL)
    myfont.setSize(5)

    val para = new Paragraph(song + "\n", myfont)
    para.setAlignment(Element.ALIGN_JUSTIFIED)
    pdfDoc.add(para)

    pdfDoc.close()

    driver.quit()
  }
}