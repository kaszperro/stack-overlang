package overlang.terminal


import net.team2xh.scurses.Scurses
import overlang.stackOverflowBackend.{StackOverflowConnection, StackOverflowParser}

class SearchResultsFrame(array: Array[SearchResult])(implicit screen: Scurses) extends Frame(Some("Search Results")) {

  ClickableLabels(panel, array,
    () => panel.innerWidth, () => panel.innerHeight,
    () => 0, () => 0)
}


object SearchResultsFrame {
  def apply(searchQuery: String)(implicit screen: Scurses): SearchResultsFrame = {
    val res = StackOverflowConnection.getSearchResult(searchQuery)
    val ans = StackOverflowParser.parseSearchResult(res)
    new SearchResultsFrame(ans.toArray)
  }
}