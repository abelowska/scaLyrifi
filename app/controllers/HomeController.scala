package controllers

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def index = Action {
    Ok(views.html.index("Hello there")())
  }

  def notFound(request: String) = Action {
    Ok(views.html.index(s"Page `$request` not found")())
  }

  def forPath(route: String) = Action { implicit request =>
    route match {
      case "next" =>
        Ok(views.html.next("General Kenobi!")())
    }
  }
}