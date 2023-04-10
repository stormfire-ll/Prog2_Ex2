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

}
