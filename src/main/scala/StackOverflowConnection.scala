import scalaj.http.Http


final class StackOverflowWrongIdException(private val message: String = "Wrong answer id",
                                          private val cause: Throwable = None.orNull)
  extends Exception(message, cause)


object StackOverflowConnection {
  private val apiUrl = "https://api.stackexchange.com/2.2/answers/"
  private val apiFilter = "!X31vb_(qKP3w1PVM1I_1EKdkcUc_)3d16bbmeKD"


  def getAnswerAsString(id: Int): String = {
    Http(apiUrl + id.toString)
      .timeout(connTimeoutMs = 10000, readTimeoutMs = 10000)
      .params(Seq(
        "key" -> Passwords.StackOverflowApiKey,
        "client_secret" -> Passwords.StackOverflowApiSecret,
        "site" -> "stackoverflow.com",
        "filter" -> apiFilter
      ))
      .asString
      .body
  }


}
