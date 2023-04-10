package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.ReleaseYear;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static at.ac.fhcampuswien.fhmdb.api.MovieAPI.getMovies;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXComboBox ratingComboBox;

    @FXML
    public JFXComboBox releaseYearComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies;

    protected ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    protected SortedState sortedState;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeState();
        initializeLayout();
    }

    public void initializeState() {
        allMovies = getMovies();         // Ex.1 allMovies = Movie.initializeMovies();
        observableMovies.clear();
        observableMovies.addAll(allMovies); // add all movies to the observable list
        sortedState = SortedState.NONE;
    }
    public void initializeLayout() {
        movieListView.setItems(observableMovies);   // set the items of the listview to the observable list
        movieListView.setCellFactory(movieListView -> new MovieCell()); // apply custom cells to the listview

        Object[] genres = Genre.values();   // get all genres
        genreComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        genreComboBox.getItems().addAll(genres);    // add all genres to the combobox
        genreComboBox.setPromptText("Filter by Genre");

        List<Integer> releaseYears = allMovies.stream()
                .map(Movie::getReleaseYear)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        releaseYearComboBox.getItems().add("No filter");
        releaseYearComboBox.getItems().addAll(releaseYears);
        releaseYearComboBox.setPromptText("Filter by Release Year");

        ratingComboBox.getItems().add("No filter");
        IntStream.range(0,10).forEach(ratingComboBox.getItems()::add);
        ratingComboBox.setPromptText("Filter by Rating");
        /*
                List<Double> ratings = allMovies.stream()
                .map(Movie::getRating)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        ratingComboBox.getItems().add("No filter");
        //IntStream.range(0,10).forEach(ratingComboBox.getItems()::add);
        releaseYearComboBox.getItems().addAll(ratings);
        ratingComboBox.setPromptText("Filter by Rating");
         */
    }
    public void sortMovies(){
        if (sortedState == SortedState.NONE || sortedState == SortedState.DESCENDING) {
            sortMovies(SortedState.ASCENDING);
        } else if (sortedState == SortedState.ASCENDING) {
            sortMovies(SortedState.DESCENDING);
        }
    }
    // sort movies based on sortedState
    // by default sorted state is NONE
    // afterwards it switches between ascending and descending
    public void sortMovies(SortedState sortDirection) {
        if (sortDirection == SortedState.ASCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle));
            sortedState = SortedState.ASCENDING;
        } else {
            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
            sortedState = SortedState.DESCENDING;
        }
    }
    /*
    // Ex1: methods for static list
    public List<Movie> filterByQuery(List<Movie> movies, String query) {
        if (query == null || query.isEmpty()) return movies;
        if (movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }
        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movie ->
                        movie.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                                movie.getDescription().toLowerCase().contains(query.toLowerCase())
                )
                .toList();
    }
    public List<Movie> filterByGenre(List<Movie> movies, Genre genre) {
        if (genre == null) return movies;
        if (movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }
        return movies.stream()
                .filter(Objects::nonNull) // nur Objekte die nicht null sind werden berücksichtigt
                .filter(movie -> movie.getGenres().contains(genre))
                .toList();
    }
    public void applyAllFilters(String searchQuery, Object genre) {
        List<Movie> filteredMovies = allMovies;

        if (!searchQuery.isEmpty()) {
            filteredMovies = filterByQuery(filteredMovies, searchQuery);
        }

        if (genre != null && !genre.toString().equals("No filter")) {
            filteredMovies = filterByGenre(filteredMovies, Genre.valueOf(genre.toString()));
        }
        observableMovies.clear();
        observableMovies.addAll(filteredMovies);
    }
     */

    public void searchBtnClicked(ActionEvent actionEvent) {
        String searchQuery = searchField.getText().trim().toLowerCase();
        Object genre = genreComboBox.getSelectionModel().getSelectedItem();
        Object releaseYear = releaseYearComboBox.getSelectionModel().getSelectedItem();     //Ex1: applyAllFilters(searchQuery, genre);
        Object rating = ratingComboBox.getSelectionModel().getSelectedItem();

        String genreR = "";
        String releaseYearR = "";
        String ratingR = "";

        if (!searchQuery.isEmpty()) {
            //List<Movie> searchQueryMovies = getMovies(searchQuery, genreR, releaseYearR, ratingR);
        }
        if (genre != null && !genre.toString().equals("No filter")) {
            genreR = genre.toString();
            //List<Movie> genreMovies = getMovies(searchQuery, genreR, releaseYearR, ratingR);
        }
        if (releaseYear != null && !releaseYear.equals("No filter")) {
            releaseYearR = releaseYear.toString();
            //List<Movie> releaseYearMovies = getMovies(searchQuery, genreR, releaseYearR, ratingR);
        }
        if (rating != null && !rating.equals("No filter")) {
            ratingR = rating.toString();
            //List<Movie> ratingMovies = getMovies(searchQuery, genreR, releaseYearR, ratingR);
        }

        List<Movie> filteredMovies = getMovies(searchQuery, genreR, releaseYearR, ratingR);
        observableMovies.clear();
        observableMovies.addAll(filteredMovies);

        sortMovies(sortedState);
        }

    public void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies();
        /*
        To test stream functions
        System.out.println(getLongestMovieTitle(allMovies));
        System.out.println(getMostPopularActor(allMovies));
        System.out.println(countMoviesFrom(allMovies, "Quentin Tarantino"));
        System.out.println(getMoviesBetweenYears(allMovies, 2001, 2003));
         */
    }

    public long countMoviesFrom(List<Movie> movies, String director) {
        var result = movies.stream()
                .filter(movie -> movie.getDirectors().contains(director))
                .count();
        //System.out.println(result);
        return result;
    }
    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        if (movies == null || movies.isEmpty()) {
            return Collections.emptyList();
        }
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .collect(Collectors.toList());
    }
    public int getLongestMovieTitle(List<Movie> movies) {
        if (movies == null || movies.isEmpty()) {
            return 0;
        }
        return movies.stream()
                .mapToInt(movie -> movie.getTitle().length())
                .max()
                .orElse(0);
    }
    public String getMostPopularActor(List<Movie> movies) {
        if (movies == null || movies.isEmpty()) {
            return null;
        }
        return movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
