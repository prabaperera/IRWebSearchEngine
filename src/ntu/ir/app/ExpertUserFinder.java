package ntu.ir.app;

import java.nio.file.Paths;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.GroupingSearch;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.search.join.JoinUtil;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.QueryBuilder;
import ntu.ir.app.model.User;

public class ExpertUserFinder {

	public List<User> searchTopUsers(String queryStr) {
		List<User> orderdList = null;
		IndexSearcher fromSearcher;
		IndexReader iReader;

		IndexSearcher toSearcher;
		IndexReader toReader;

		try {

			iReader = DirectoryReader.open(FSDirectory.open(Paths.get("J:\\MSC\\IR\\data\\TagIndex.txt")));
			fromSearcher = new IndexSearcher(iReader);

			toReader = DirectoryReader.open(FSDirectory.open(Paths.get("J:\\MSC\\IR\\data\\AnswerIndex.txt")));
			toSearcher = new IndexSearcher(toReader);

			QueryBuilder builder = new QueryBuilder(new StandardAnalyzer());
			Query fromQuery = builder.createPhraseQuery("tags", queryStr);

			String fromField = "answerID"; // Name of the from field
			boolean multipleValuesPerDocument = true; // Set only  true in the
														// case when your
														// fromField has
														// multiple values per
														// document in your
														// index
			String toField = "answerID"; // Name of the to field
			ScoreMode scoreMode = ScoreMode.Max;// Defines how the scores are
												// translated into the other
												// side of the join.

		
			Query joinQuery = JoinUtil.createJoinQuery(fromField, multipleValuesPerDocument, toField, fromQuery,
					fromSearcher, scoreMode);

			
			GroupingSearch groupingSearch = new GroupingSearch("ownerId");

			Sort sort = new Sort(new SortField("count", SortField.Type.DOC.INT));
			groupingSearch.setGroupSort(sort);
			TopGroups<BytesRef> results = groupingSearch.search(toSearcher, joinQuery, 0, 1000);

		
			GroupDocs<BytesRef>[] groups = results.groups;
			orderdList = new ArrayList<User>();

			for (GroupDocs<BytesRef> groupDocs : groups) {
				User user = new User();
				user.setAcceptedAnswersCount(groupDocs.totalHits);
				user.setUserId(groupDocs.groupValue.utf8ToString());
				orderdList.add(user);
			}

			Collections.sort(orderdList,
					(left, right) -> left.getAcceptedAnswersCount() - right.getAcceptedAnswersCount());
			Collections.reverse(orderdList);

			orderdList = orderdList.subList(0, 10);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return orderdList;

	}

	/***
	 * public static void main(String args[]){ try { IndexReader iReader =
	 * DirectoryReader.open(FSDirectory.open(Paths.get(
	 * "J:\\MSC\\IR\\data\\AnswerIndex.txt")));
	 * 
	 * //search index ExpertUserFinder searcher=new ExpertUserFinder();
	 * 
	 * 
	 * ScoreDoc[] hits=searcher.searchTopUsers("java");
	 * 
	 * int i = 1; for (ScoreDoc hit : hits) { //System.out.println("\nResult " +
	 * i + "\tDocID: " + hit.doc + "\t Score: " + hit.score); try {
	 * 
	 * 
	 * //System.out.println("Q: " + iReader.document(hit.doc).get("ansid"));
	 * //System.out.println("Q: " + iReader.document(hit.doc).get("ownerId"));
	 * 
	 * //System.out.println("A: " + iqReader.document(hit.doc).get("aansid")); }
	 * catch (Exception e) { e.printStackTrace(); }
	 * 
	 * i++;
	 * 
	 * }
	 * 
	 * 
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace(); // TODO: handle exception }
	 * 
	 * 
	 * 
	 * }
	 ***/
}
