package services

import Data.{Song, SongHeader}
import com.machinepublishers.jbrowserdriver.{JBrowserDriver, Settings, Timezone}
import org.openqa.selenium.By

import scala.collection.JavaConverters._

object UGCrawler {

  private val LinkClassName = "_1iQi2"
  private val SongContentClassName = "_1YgOS"
  private val TypeOfSong = "Chords"
  private val LinkTag = "a"
  private val Link = "href"
  private val BaseUrl = "https://www.ultimate-guitar.com/search.php?search_type=title&value="
  private val SpaceCode = "%20"

  private def createUrl(songHeader: SongHeader): String = {
    val params = (songHeader.author + " " + songHeader.title).replaceAll(" ", SpaceCode)
    BaseUrl + params
  }
}

class UGCrawler(songHeader: SongHeader) {

  import UGCrawler._

  def executeUrl: Song = {

    val url = createUrl(songHeader)

    val driver = new JBrowserDriver(Settings.builder().timezone(Timezone.EUROPE_WARSAW).build())
    driver.get(url)

    val songPage = driver.findElements(By.className(LinkClassName))
      .asScala
      .filter(_.getText.contains(TypeOfSong))
      .map(_.findElement(By.tagName(LinkTag)))
      .map(_.getAttribute(Link))
      .toArray
      .head

    driver.get(songPage)

    val song = driver.findElements(By.className(SongContentClassName))
      .asScala
      .map(_.getText())
      .mkString("\n")

    driver.quit()

    Song(songHeader, song)
  }
}
