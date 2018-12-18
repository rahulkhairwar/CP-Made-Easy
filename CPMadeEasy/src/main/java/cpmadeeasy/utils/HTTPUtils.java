package cpmadeeasy.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * A utility class for some HTTP actions.
 */
class HTTPUtils
{
	static final String CODEFORCES_CONTEST_LIST_URL = "http://codeforces.com/api/contest.list";
	static final String CODEFORCES_CONTEST_URL = "http://codeforces.com/contest/";

	private static HttpURLConnection getHttpUrlConnection(String s) throws IOException
	{
		if (!isValidURL(s))
			return null;

		HttpURLConnection connection = null;

		try
		{
			URL url = new URL(s);

			connection = (HttpURLConnection) url.openConnection();
			connection.connect();
		}
		catch (IOException e)
		{
			//Handle invalid URL
			e.printStackTrace();
		}

		return connection;
	}

	static BufferedReader getURLInputStream(String url)
	{
		try
		{
			HttpURLConnection connection = HTTPUtils.getHttpUrlConnection(url);

			if (connection == null)
				return null;

			connection.connect();

			return new BufferedReader(new InputStreamReader(connection.getInputStream()));
		}
		catch (IOException ignored)
		{
		}

		return null;
	}

	private static boolean isValidURL(String url)
	{
		URL u;

		try
		{
			u = new URL(url);
		}
		catch (MalformedURLException e)
		{
			return false;
		}

		try
		{
			u.toURI();
		}
		catch (URISyntaxException e)
		{
			return false;
		}

		return true;
	}

	static String createCodeforcesStandingsUrl(int contestId)
	{
		return "http://codeforces.com/api/contest.standings?contestId=" + contestId + "&from=1&count=1&showUnofficial="
				+ true;
	}

}
