package at.ac.fhcampuswien.fhmdb.api;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieAPI {
    private static final String URL = "http://prog2.fh-campuswien.ac.at/movies";
    //private static final String URL = "http://localhost:8080/movies";
    private static final String DELIMITER = "&";

    // generates URL, set parameter of URL
    private static String buildUrl(String query, Genre genre, String releaseYear, String ratingFrom){
        StringBuilder url = new StringBuilder(URL);
        if((query != null && !query.isEmpty()) || genre != null || releaseYear != null || ratingFrom != null){
            url.append("?");
            if(query != null && !query.isEmpty()){
                url.append("query=").append(query).append(DELIMITER);
            }
            if (genre != null){
                url.append("genre=").append(genre).append(DELIMITER);
            }
            if (releaseYear != null) {
                url.append("releaseYear=").append(releaseYear).append(DELIMITER);
            }
            if (ratingFrom != null) {
                url.append("ratingFrom=").append(ratingFrom).append(DELIMITER);
            }
        }
        return url.toString();
    }
    // Request: get all Movies from API
    public static List<Movie> getAllMovies() {
        return getAllMovies(null, null, null, null);
    }
    public static List<Movie> getAllMovies(String query, Genre genre, String releaseYear, String ratingFrom) {
        String url = buildUrl(query, genre, releaseYear, ratingFrom);
        Request request = new Request.Builder()             // Request
                .url(url)
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "http.agent")      // gesetzter User-Agent Header
                .build();

        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()){
            String responseBody = response.body().string();
            Gson gson = new Gson();
            Movie[] movies = gson.fromJson(responseBody, Movie[].class); //parse responses in JSON into Java objects
            return Arrays.asList(movies);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }
}
