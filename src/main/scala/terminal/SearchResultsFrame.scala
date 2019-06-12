package terminal


import net.team2xh.scurses.Scurses
import stackOverflowBackend.{StackOverflowConnection, StackOverflowParser}

class SearchResultsFrame(array: Array[SearchResult])(implicit screen: Scurses) extends Frame(Some("Search Results")) {

  new Labels(panel, array,
    () => panel.innerWidth, () => panel.innerHeight,
    () => 0, () => 0)
}


object SearchResultsFrame {
  def apply(searchQuery: String)(implicit screen: Scurses): SearchResultsFrame = {
    val res = StackOverflowConnection.getSearchResultAsString(searchQuery)
    //FilesManager.writeToFile(res, "./res.txt")
    val ans = StackOverflowParser.parseSearchResponseToListOfResults(res)
    new SearchResultsFrame(ans.toArray)
  }
}