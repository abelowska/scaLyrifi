package controllers

import java.io.{BufferedInputStream, ByteArrayInputStream, File}
import java.nio.file.{Files, Paths}

import akka.stream.scaladsl.StreamConverters
import controllers.data.SongHeader
import controllers.services.{ChordsDownloader, ImageReader}
import javax.imageio.ImageIO
import javax.inject._
import play.api.http.{HttpEntity, MimeTypes}
import play.api.mvc._
import scala.collection.mutable.{Map => mMap}

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  private val imageReader = new ImageReader
  private val chordsDownloader = new ChordsDownloader
  private val requestMap = mMap[Int, Seq[SongHeader]]()
  private var requestCounter = 0

  def index = Action { implicit request =>
    Ok(views.html.index("Select file to upload"))
  }

  def notFound(request: String) = Action { implicit request =>
    Ok(views.html.index(s"Page `$request` not found"))
  }

  def uploadFile = Action(parse.multipartFormData) { implicit request =>
    request.body.file("picture")
      .map { picture =>
      val stream = new BufferedInputStream(Files.newInputStream(picture.ref.path))
      ImageIO.read(stream)
    }
      .filterNot(_ == null)
      .map(imageReader.extractSongHeaders)
      .filterNot(_.isEmpty)
      .map(songHeaders => {
        requestCounter += 1
        requestMap += requestCounter -> songHeaders
        Ok(views.html.next(requestCounter, songHeaders))
      })
      .getOrElse(Redirect(routes.HomeController.index).flashing("error" -> "Missing file"))
  }

  def downloadSongs(requestId: Int) = Action { implicit request =>
    val songHeaders = requestMap(requestId)
    val outputStream = chordsDownloader.download(songHeaders)
    val inputStream = new ByteArrayInputStream(outputStream.toByteArray)

    Ok.sendEntity(
      HttpEntity.Streamed(
        data = StreamConverters.fromInputStream(() => inputStream),
        contentLength = Some(outputStream.toByteArray.length),
        contentType = Some(MimeTypes.BINARY)
      )
    ).withHeaders("Content-Disposition" -> "attachment; filename=chords.pdf")
  }
}