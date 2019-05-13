import scalaj.http.Http
import io.circe._, io.circe.parser._

object StackOverflowConnection {
  private val apiUrl = "https://api.stackexchange.com/2.2/posts/"

  private val apiKey = "KOFpauAI3)whX1PrGEZ0PA(("
  private val apiSecret = "dlFsu*AyJyflclCbISXuKw(("


  def getAnswerBody(id: Int): String = {
    val responseString = Http(apiUrl + id.toString).params(Seq(
      "key" -> apiKey,
      "client_secret" -> apiSecret,
      "site" -> "stackoverflow.com",
      "filter" -> "withbody"
    )).asString.body

    val parsed = parse(responseString).getOrElse(Json.Null)
    val itemsJson = parsed.findAllByKey("items")
    itemsJson.head.findAllByKey("body").head.toString()
  }
}
