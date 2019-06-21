package overlang.stackOverflowBackend

import io.circe.parser.parse
import io.circe.{Decoder, HCursor, Json}
import overlang.terminal.SearchResult

import scala.util.matching.Regex

object StackOverflowParser {
  implicit val AnswerDecoder: Decoder[StackOverflowAnswer] = (c: HCursor) => for {
    id <- c.get[Int]("answer_id")
    score <- c.get[Int]("score")
    body <- c.get[String]("body")
    tags <- c.get[List[String]]("tags")

  } yield
    new StackOverflowAnswer(id, score, parseAnswerBody(body), tags)


  implicit val QuestionDecoder: Decoder[StackOverflowQuestion] = (c: HCursor) =>
    for {
      id <- c.get[Int]("question_id")
      title <- c.get[String]("title")
      score <- c.get[Int]("question_score")
    } yield
      new StackOverflowQuestion(id, replaceHTMLCharacters(title), score)


  implicit val SearchResultDecoder: Decoder[SearchResult] = (c: HCursor) => {
    for {
      res_type <- c.get[String]("item_type")
    } yield res_type match {
      case "answer" => parseAnswer(
        StackOverflowConnection.getAnswerById(c.get[Int]("answer_id").right.get)
      )
      case "question" => c.as[StackOverflowQuestion].right.get
    }
  }


  private def replaceHTMLCharacters(string: String): String = {
    string.replaceAll("&lt;", "<").
      replaceAll("&gt;", ">").
      replaceAll("&amp;", "&").
      replaceAll("&quot;", "\"").
      replaceAll("&#39;", "\'")
  }


  def parseAnswerBody(body: String): List[String] = {
    val codeRegex: Regex = "(?<=<pre.*><code>)(?s).*?(?=</code></pre>)".r
    val preHTML = codeRegex.findAllMatchIn(body).map(b => b.toString).toList

    preHTML.map(s => replaceHTMLCharacters(s))
  }


  private def listParseHelper[T](strToParse: String)(implicit instance: Decoder[T]): T = {
    val decodeSearchResults = Decoder[T].prepare(
      _.downField("items")
    )
    io.circe.parser.decode(strToParse)(decodeSearchResults).right.get
  }

  def parseSearchResult(response: String): List[SearchResult] = {
    listParseHelper[List[SearchResult]](response)
  }

  def parseAnswers(response: String): List[StackOverflowAnswer] = {
    listParseHelper[List[StackOverflowAnswer]](response)
  }

  def parseAnswer(response: String): StackOverflowAnswer = {
    val parsed = parse(response).getOrElse(Json.Null)
    val maybeAnswer: Option[Json] = parsed.hcursor.downField("items").downArray.focus

    maybeAnswer match {
      case None => throw new StackOverflowWrongIdException
      case Some(answer) => answer.as[StackOverflowAnswer].right.get
    }
  }


}
