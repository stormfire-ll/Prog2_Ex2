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
    private static final String DELIMITER = "&";

    // generate URL for request & set parameter of URL
    private static String buildUrl(String query, String genre, String releaseYear, String ratingFrom){
        StringBuilder url = new StringBuilder(URL);
        if((query != null && !query.isEmpty()) || genre != null || releaseYear != null || ratingFrom != null){
            url.append("?");
            if(query != null && !query.isEmpty()){
                url.append("query=").append(query).append(DELIMITER);
            }
            if (genre != null && !genre.equals("No filter")){
                url.append("genre=").append(genre).append(DELIMITER);
            }
            if (releaseYear != null && !releaseYear.equals("No filter")) {
                url.append("releaseYear=").append(releaseYear).append(DELIMITER);
            }
            if (ratingFrom != null && !ratingFrom.equals("No filter")) {
                url.append("ratingFrom=").append(ratingFrom).append(DELIMITER);
            }
        }
        return url.toString();
    }
    public static List<Movie> getAllMovies() {
        return getAllMovies(null, null, null, null);
    }
    // getAllMovies: Request & Response
    public static List<Movie> getAllMovies(String query, String genre, String releaseYear, String ratingFrom) {
        String url = buildUrl(query, genre, releaseYear, ratingFrom);
        Request request = new Request.Builder()                         // Request
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

    public static List<Movie> getRequestedMovies() {
        return getAllMovies(null, null, null, null);
    }
    public static List<Movie> getRequestedMovies(String queryR, String genreR, String releaseYearR, String ratingFromR) {
        String url = buildUrl(queryR, genreR, releaseYearR, ratingFromR);
        Request request = new Request.Builder()
                .url(url)
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "http.agent")
                .build();
        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()){
            String responseBody = response.body().string();
            Gson gson = new Gson();
            Movie[] filteredMovies = gson.fromJson(responseBody, Movie[].class);
            return Arrays.asList(filteredMovies);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }
}
