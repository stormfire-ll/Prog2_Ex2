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

import static at.ac.fhcampuswien.fhmdb.api.MovieAPI.getAllMovies;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    //Ex2: add ReleaseYear & Rating
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
        allMovies = getAllMovies();        // allMovies = Movie.initializeMovies();
        observableMovies.clear();
        observableMovies.addAll(allMovies); // add all movies to the observable list
        sortedState = SortedState.NONE;
    }
    //ToDo: Releaseyear ENUMS?
    public void initializeLayout() {
        movieListView.setItems(observableMovies);   // set the items of the listview to the observable list
        movieListView.setCellFactory(movieListView -> new MovieCell()); // apply custom cells to the listview

        Object[] genres = Genre.values();   // get all genres
        genreComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        genreComboBox.getItems().addAll(genres);    // add all genres to the combobox
        genreComboBox.setPromptText("Filter by Genre");


/*


        //releaseYear
        Object[] releaseYear = Releaseyear.values();   // get all release years
        releaseYearComboBox.getItems().add("No filter");
        releaseYearComboBox.getItems().addAll(genres);    // add all release years to the combobox
        releaseYearComboBox.setPromptText("Filter by Genre");

        //Rating
        Object[] rating = Rating.values();   // get all ratings
        ratingComboBox.getItems().add("No filter");
        ratingComboBox.getItems().addAll(genres);    // add all ratings to the combobox
        ratingComboBox.setPromptText("Filter by Genre");
         */
    }

    // sort movies based on sortedState
    // by default sorted state is NONE
    // afterwards it switches between ascending and descending
    public void sortMovies() {
        if (sortedState == SortedState.NONE || sortedState == SortedState.DESCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle));
            sortedState = SortedState.ASCENDING;
        } else if (sortedState == SortedState.ASCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
            sortedState = SortedState.DESCENDING;
        }
    }

    public List<Movie> filterByQuery(List<Movie> movies, String query){
        if(query == null || query.isEmpty()) return movies;

        if(movies == null) {
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

    public List<Movie> filterByGenre(List<Movie> movies, Genre genre){
        if(genre == null) return movies;
        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }
        return movies.stream()
                .filter(Objects::nonNull) // nur Objekte die nicht null sind werden berücksichtigt
                .filter(movie -> movie.getGenres().contains(genre))
                .toList();
    }
    // Ex2 add filter releaseyear & rati
    // ng
    /*
    public List<Movie> filterByReleaseyear(List<Movie> movies) {
    }
    public List<Movie> filterByRating() {

    }
     */

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

    public void searchBtnClicked(ActionEvent actionEvent) {

        String searchQuery = searchField.getText().trim().toLowerCase();
        Object genre = genreComboBox.getSelectionModel().getSelectedItem();
        Object releasYear = releaseYearComboBox.getSelectionModel().getSelectedItem();
        Object rating = ratingComboBox.getSelectionModel().getSelectedItem();

        String genreStr = null;
        String releaseYearStr = null;
        String ratingStr = null;

        if (genre != null) {
            genreStr = genre.toString();
        }
        if (releasYear != null) {
            releasYear = releasYear.toString();
        }
        if (rating != null) {
            ratingStr = rating.toString();
        }
        //our filter logic
        applyAllFilters(searchQuery, genre);

        if(sortedState != SortedState.NONE) {
            sortMovies();
        }
        else {
            observableMovies.addAll(allMovies);
            List<Movie> movies = MovieAPI.getMovies(searchQuery,genreStr, releaseYearStr, ratingStr);
            observableMovies.clear();
            observableMovies.addAll(movies);

        }
    }

    public void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies();
        // To test stream functions
        System.out.println(getLongestMovieTitle(allMovies));
        System.out.println(getMostPopularActor(allMovies));
        System.out.println(countMoviesFrom(allMovies, "Quentin Tarantino"));
        System.out.println(getMoviesBetweenYears(allMovies, 2003, 2004));

    }
    public long countMoviesFrom(List<Movie> movies, String director) {
        var result = movies.stream()
                .filter(movie -> movie.getDirectors().contains(director))
                .count();
                System.out.println(result);
        return result;
    }
    public List<Movie> getLongestMovieTitle(List<Movie> movies) {
        if (movies == null || movies.isEmpty()) {
            return Collections.emptyList();
        }
        int maxLength = movies.stream()
                .mapToInt(movie -> movie.getTitle().length())
                .max()
                .orElse(0);
        return movies.stream()
                .filter(movie -> movie.getTitle().length() == maxLength)
                .collect(Collectors.toList());
    }
/*


    public int getLongestMovieTitle(List<Movie> movies, String title) {
    int titlelength = movies.stream().collect().filter(movie -> movie.getTitle()); //filter by names
    return titlelength;


    public int getLongestMovieTitle(List<Movie> movies) {
        if (movies == null || movies.isEmpty()) {
            return 0;
        }

        return movies.stream()
                .mapToInt(movie -> movie.getTitle().length())
                .max()
                .orElse(0);
    }


    /*
    public int getLongestMovieTitle(){
        var movieList = MovieAPI.getAllMovies();
        // movieList.stream().collect().filter(movie -> movie.getTitle()); //filter by names
        return movieList;
    }
    */
    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
            if (movies == null || movies.isEmpty()) {
                return Collections.emptyList();
            }
            return movies.stream()
                 .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                    .collect(Collectors.toList());
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