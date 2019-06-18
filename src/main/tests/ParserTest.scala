import org.scalatest.FunSuite
import overlang.stackOverflowBackend.StackOverflowParser._

class ParserTest extends FunSuite {
  test("test parse simple to list of code") {
    val myBody = "<pre><code>test</code></pre>"
    assert(parseAnswerBodyToListOfCode(myBody) == List("test"))
  }

  test("test parse with tag class to list of code") {
    val myBody = "<pre class = 'sth'><code>test</code></pre>"
    assert(parseAnswerBodyToListOfCode(myBody) == List("test"))
  }

  test("test parse with HTML lt and gt to list of code") {
    val myBody = "<pre class = 'sth'><code>&lt;&lt;test&gt;&gt;</code></pre>"
    assert(parseAnswerBodyToListOfCode(myBody) == List("<<test>>"))
  }

  test("test parse many codes") {
    val myBody = "not importat something " +
      "<pre class = 'whatever'  ><code>&lt;&lt;test1&gt;&gt;</code></pre>" +
      "<pre class = 'sth'><code>&lt;&lt;test2&gt;&gt;</code></pre>" +
      "<pre class = 'sth'><code>&lt;&lt;test3&gt;&gt;</code></pre>" +
      "blah blah blah"
    assert(parseAnswerBodyToListOfCode(myBody) == List("<<test1>>", "<<test2>>", "<<test3>>"))
  }
}