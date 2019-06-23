import org.scalatest.FunSuite
import overlang.stackOverflowBackend.{StackOverflowAnswer, StackOverflowConnection, StackOverflowQuestion, StackOverflowWrongIdException}
import overlang.stackOverflowBackend.StackOverflowParser._
import overlang.terminal.SearchResult

class ParserTest extends FunSuite {
  test("parse answer body to list of code") {
    val myBody = "<pre><code>test</code></pre>"
    parseAnswerBody(myBody) === List("test")
  }

  test("parse answer body with tag class to list of code") {
    val myBody = "<pre class = 'sth'><code>test</code></pre>"
    parseAnswerBody(myBody) === List("test")
  }

  test("parse answer body with HTML lt and gt to list of code") {
    val myBody = "<pre class = 'sth'><code>&lt;&lt;test&gt;&gt;</code></pre>"
    parseAnswerBody(myBody) === List("<<test>>")
  }

  test("parse answer body with many codes") {
    val myBody = "not importat something " +
      "<pre class = 'whatever'  ><code>&lt;&lt;test1&gt;&gt;</code></pre>" +
      "<pre class = 'sth'><code>&lt;&lt;test2&gt;&gt;</code></pre>" +
      "<pre class = 'sth'><code>&lt;&lt;test3&gt;&gt;</code></pre>" +
      "blah blah blah"
    parseAnswerBody(myBody) === List("<<test1>>", "<<test2>>", "<<test3>>")
  }


  test("parse whole answer, should throw error, with no answer") {
    val myAns = "{\"items\": [] }"
    assertThrows[StackOverflowWrongIdException](parseAnswer(myAns))
  }

  test("parse whole correct StackOverflowAnswer") {

    val myRequest = "{\"items\": [{\"tags\":[\"tag1\"], \"score\": 33, " +
      "\"answer_id\": 1234, \"body\": \"<pre><code>yo</pre></code>\"}]}"


    val myAnswer = parseAnswer(myRequest)
    myAnswer === new StackOverflowAnswer(1234, 33, List("yo"), List("tag1"))
  }


  test("parse whole correct SearchResult") {

    val myRequest = "{\"items\": [{\"item_type\": \"question\", \"question_id\": 1234, " +
      "\"question_score\": 4321, \"title\": \"best question\"}, " +
      "{\"item_type\": \"answer\",\"tags\":[\"tags1\"], \"score\": 545, \"answer_id\": 2334, \"body\": \"<pre><code>code1</pre></code>\"}]}"


    val mySearchRes = parseSearchResult(myRequest)

    mySearchRes === List[SearchResult](
      new StackOverflowQuestion(1234, "best question", 4321),
      new StackOverflowAnswer(2334, 545, List("code1"), List("tags1"))
    )
  }
}