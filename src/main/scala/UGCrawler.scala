import Data.SongHeader
import com.machinepublishers.jbrowserdriver.{JBrowserDriver, Settings, Timezone}
import org.openqa.selenium.By

import scala.collection.JavaConverters._

class UGCrawler(songHeader: SongHeader) {

  def createUrl(songHeader: SongHeader): String = {
    val params = songHeader.author.replaceAll(" ", "%20") + "%20" + songHeader.title.replaceAll(" ", "%20")
    "https://www.ultimate-guitar.com/search.php?search_type=title&value=" + params
  }

  def executeUrl {
    val driver = new JBrowserDriver(Settings.builder().timezone(Timezone.EUROPE_WARSAW).build())
    driver.get(createUrl(songHeader))
    driver.findElements(By.className("_1iQi2"))
      .asScala
      .filter(_.getText.contains("Chords"))
      .map(_.findElement(By.tagName("a")))
      .map(_.getAttribute("href"))
      .foreach(println)

    driver.quit()
  }


}
