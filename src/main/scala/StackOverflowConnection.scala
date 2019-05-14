import scalaj.http.Http
import io.circe._, io.circe.parser._

final class StackOverflowWrongIdException(private val message: String = "",
                                          private val cause: Throwable = None.orNull)
  extends Exception(message, cause)


object StackOverflowConnection {
  private val apiUrl = "https://api.stackexchange.com/2.2/posts/"


  def getAnswerBody(id: Int): String = {
    val responseString = Http(apiUrl + id.toString)
      .timeout(connTimeoutMs = 10000, readTimeoutMs = 10000)
      .params(Seq(
        "key" -> Passwords.stackOverflowApiKey,
        "client_secret" -> Passwords.stackOverflowApiSecret,
        "site" -> "stackoverflow.com",
        "filter" -> "withbody"
      ))
      .asString

    if (responseString.isError)
      throw new StackOverflowWrongIdException("Cant query Stack Overflow for id: " + id.toString)


    val parsed = parse(responseString.body).getOrElse(Json.Null)
    val itemsJson = parsed.findAllByKey("items")
    itemsJson.head.findAllByKey("body").head.toString()
  }
}
