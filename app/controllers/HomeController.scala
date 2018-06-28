package controllers

import java.io.BufferedInputStream
import java.nio.file.{Files, Paths}

import controllers.services.ImageReader
import javax.imageio.ImageIO
import javax.inject._
import play.api.mvc._

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  private val imageReader = new ImageReader

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
      .map(songHeaders => Ok(views.html.next(songHeaders)))
      .getOrElse(Redirect(routes.HomeController.index).flashing("error" -> "Missing file"))
  }

  def forPath(route: String) = Action { implicit request =>
    route match {
      case "home" =>
        Ok(views.html.index("Hello there"))
    }
  }
}