package overlang.stackOverflowBackend

import scalaj.http.Http
import Passwords._

final class StackOverflowWrongIdException(private val message: String = "Wrong answer id",
                                          private val cause: Throwable = None.orNull)
  extends Exception(message, cause)


object StackOverflowConnection {

  private val apiUrl = "https://api.stackexchange.com/2.2/answers/"
  private val apiFilter = "!2wCsZBV3Ia9osL2Bpq09z4TCwpqxqSaQ3(hlElcXO*"

  private val searchApiUrl = "https://api.stackexchange.com/2.2/search/excerpts"
  private val getAnswersApiUrl = "https://api.stackexchange.com/2.2/questions/%d/answers"

  def getAnswerById(id: Int): String = {
    Http(apiUrl + id.toString)
      .timeout(connTimeoutMs = 5000, readTimeoutMs = 5000)
      .params(Seq(
        "key" -> StackOverflowApiKey,
        "client_secret" -> StackOverflowApiSecret,
        "site" -> "stackoverflow.com",
        "filter" -> apiFilter
      ))
      .asString
      .body
  }

  def getAnswersByQuestionId(id: Int): String = {
    Http(getAnswersApiUrl.format(id))
      .timeout(connTimeoutMs = 5000, readTimeoutMs = 5000)
      .params(Seq(
        "key" -> StackOverflowApiKey,
        "client_secret" -> StackOverflowApiSecret,
        "site" -> "stackoverflow.com",
        "filter" -> apiFilter
      ))
      .asString
      .body
  }

  def getSearchResult(searchQuery: String): String = {
    Http(searchApiUrl)
      .timeout(connTimeoutMs = 5000, readTimeoutMs = 5000)
      .params(Seq(
        "key" -> StackOverflowApiKey,
        "client_secret" -> StackOverflowApiSecret,
        "site" -> "stackoverflow.com",
        "order" -> "desc",
        "q" -> searchQuery,
        "sort" -> "relevance"
      ))
      .asString
      .body
  }


}
