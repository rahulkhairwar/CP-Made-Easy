package cpmadeeasy.utils;

import com.cpmadeeasy.dto.Contest;
import com.cpmadeeasy.dto.Problem;
import com.cpmadeeasy.dto.TestCase;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for using the Codeforces API and fetch necessary data.
 */
public class CodeforcesAPIUtils
{
	public static List<Contest> getContestsList()
	{
		List<Contest> contests = new ArrayList<>();

		try
		{
			BufferedReader in = HTTPUtils.getURLInputStream(HTTPUtils.CODEFORCES_CONTEST_LIST_URL);

			if (in == null)
			{
				error();

				return null;
			}

			String content = in.readLine();
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(content);
			JSONArray jsonArray = (JSONArray) jsonObject.get("result");

			in.close();

			for (Object x : jsonArray)
				contests.add(parseContestFromJSONObject((JSONObject) x));
		}
		catch (IOException | ParseException e)
		{
			e.printStackTrace();
		}

		return contests;
	}

	public static List<Problem> getContestProblemsList(int contestId)
	{
		List<Problem> problems = new ArrayList<>();

		try
		{
			BufferedReader in = HTTPUtils.getURLInputStream(HTTPUtils.createCodeforcesStandingsUrl(contestId));

			if (in == null)
			{
				error();

				return null;
			}

			String content = in.readLine();
			JSONParser parser = new JSONParser();
			String result = ((JSONObject) parser.parse(content)).get("result").toString();
			JSONObject temp = (JSONObject) parser.parse(result);
			JSONArray jsonArray = (JSONArray) (temp).get("problems");

			in.close();

			for (Object x : jsonArray)
				problems.add(parseProblemFromJSONObject((JSONObject) x, contestId));
		}
		catch (IOException | ParseException e)
		{
			e.printStackTrace();
		}

		return problems;
	}

	private static Contest parseContestFromJSONObject(JSONObject object)
	{
		Contest contest = new Contest();
		Object temp;

		contest.setId(((Long) object.get("id")).intValue());
		contest.setName((String) object.get("name"));
		contest.setContestType((String) object.get("type"));
		contest.setPhase((String) object.get("phase"));
		contest.setFrozen(object.get("frozen").equals("true"));
		contest.setDurationSeconds(((Long) object.get("durationSeconds")).intValue());

		// the following attributes can be absent.
		temp = object.get("startTimeSeconds");

		if (temp != null)
			contest.setStartTimeSeconds(((Long) temp).intValue());

		temp = object.get("relativeTimeSeconds");

		if (temp != null)
			contest.setRelativeTimeSeconds(((Long) temp).intValue());

		temp = object.get("difficulty");

		if (temp != null)
			contest.setDifficulty(((Long) temp).intValue());

		temp = object.get("preparedBy");

		if (temp != null)
			contest.setPreparedBy((String) temp);

		temp = object.get("websiteUrl");

		if (temp != null)
			contest.setWebsiteUrl((String) temp);

		temp = object.get("description");

		if (temp != null)
			contest.setDescription((String) temp);

		temp = object.get("kind");

		if (temp != null)
			contest.setKind((String) temp);

		temp = object.get("icpcRegion");

		if (temp != null)
			contest.setIcpcRegion((String) temp);

		temp = object.get("country");

		if (temp != null)
			contest.setCountry((String) temp);

		temp = object.get("city");

		if (temp != null)
			contest.setCity((String) temp);

		temp = object.get("season");

		if (temp != null)
			contest.setSeason((String) temp);

		return contest;
	}

	private static Problem parseProblemFromJSONObject(JSONObject object, int contestId)
	{
		Problem problem = new Problem();

		problem.setContestId(contestId);
		problem.setIndex((String) object.get("index"));
		problem.setName((String) object.get("name"));
		problem.setProblemType((String) object.get("type"));

		// the following attribute can be absent.
		Object temp = object.get("points");

		if (temp != null)
			problem.setPoints((Double) temp);

		JSONArray tags = (JSONArray) object.get("tags");
		List<String> list = new ArrayList<>();

		for (Object tag : tags)
			list.add(tag.toString());

		problem.setTags(list);

		return problem;
	}

	public static List<TestCase> fetchTestCases(int contestId, char problem)
	{
		try
		{
			String url = HTTPUtils.CODEFORCES_CONTEST_URL + contestId + "/problem/" + problem;
			BufferedReader in = HTTPUtils.getURLInputStream(url);

			if (in == null)
			{
				error();

				return null;
			}

			String line;
			StringBuilder html = new StringBuilder("");

			while ((line = in.readLine()) != null)
				html.append(line).append("\n");

			in.close();

			Document doc = Jsoup.parse(html.toString());
			Elements examples = doc.getElementsByClass("sample-test");
			Elements inputs = examples.first().getElementsByClass("input");
			Elements outputs = examples.first().getElementsByClass("output");
			List<TestCase> testCases = new ArrayList<>();

			for (Element e : inputs)
			{
				Elements ii = e.getElementsByTag("pre");
				String first = ii.toString();

				first = first.replace("<pre>", "");
				first = first.replace("</pre>", "");
				first = first.replace("<br>", "\n");
				testCases.add(new TestCase(first));
			}

			for (int i = 0; i < outputs.size(); i++)
			{
				Element e = outputs.get(i);
				Elements ii = e.getElementsByTag("pre");
				String first = ii.toString();

				first = first.replace("<pre>", "");
				first = first.replace("</pre>", "");
				first = first.replace("<br>", "\n");
				testCases.get(i).setOutput(first);
			}

			return testCases;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static void crawlContestPage(int contestId)
	{
		try
		{
			String url = HTTPUtils.CODEFORCES_CONTEST_URL + contestId;
			BufferedReader in = HTTPUtils.getURLInputStream(url);

			if (in == null)
			{
				error();

				return;
			}

			String line;

			while ((line = in.readLine()) != null)
			{
				System.out.println(line);
			}

			in.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static boolean crawlContestProblem(int contestId, char problem)
	{
		try
		{
			System.out.println("parsing problem : " + problem);
			String url = HTTPUtils.CODEFORCES_CONTEST_URL + contestId + "/problem/" + problem;
			BufferedReader in = HTTPUtils.getURLInputStream(url);

			if (in == null)
			{
				error();

				return false;
			}

			String line;

			while ((line = in.readLine()) != null)
				;
//				System.out.println(line);

			in.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return true;
	}

	public static void error()
	{
		System.out.println("Some error occurred. Please try again.");
	}

	public static void getAPIKey()
	{
	}

}
