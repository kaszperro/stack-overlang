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
        "key" -> Passwords.StackOverflowApiKey,
        "client_secret" -> Passwords.StackOverflowApiSecret,
        "site" -> "stackoverflow.com",
        "filter" -> "withbody"
      ))
      .asString

    val parsed = parse(responseString.body).getOrElse(Json.Null)
    val itemsJson = parsed.findAllByKey("items")
    val answerBody = itemsJson.head.findAllByKey("body")

    if (answerBody.isEmpty)
      throw new StackOverflowWrongIdException(
        "There is no answer with id: %d on Stack Overflow"
          .format(id)
      )

    answerBody.head.toString()
  }
}
