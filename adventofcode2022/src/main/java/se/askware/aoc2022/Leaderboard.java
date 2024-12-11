package se.askware.aoc2022;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import com.google.gson.Gson;

public class Leaderboard {

	public static void main(String[] args) throws IOException {

		URL u = new URL("https://adventofcode.com/2022/leaderboard/private/view/439090.json");
		final HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
		urlConnection.setRequestProperty("cookie", "session=53616c7465645f5fef82e668ebe7f1169acd2f0d9159a2ae169d03a7eff93687a6b200c4b6501254ba6cf2099a7d84f96df433a5b6e8601e799be44e3b993c32");
		urlConnection.connect();
		Gson gson = new Gson();
		final InputStream inputStream = urlConnection.getInputStream();



		final Map map = gson.fromJson(new InputStreamReader(inputStream), Map.class);

		System.out.println(map);
	}
}
