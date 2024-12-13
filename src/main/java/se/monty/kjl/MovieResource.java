package se.monty.kjl;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Path("/movies") // Defines the base URI path for all resource methods in this class
@Produces(MediaType.APPLICATION_JSON) //methods in this class produce JSON responses
@Consumes(MediaType.APPLICATION_JSON) //methods in this class consume JSON requests
public class MovieResource {

    @Inject
    private MovieRepository repository; // Injects the MovieRepository for database operations

    @GET
    public List<Movie> getAllMovies() {
        return repository.findAll(); // Retrieves and returns all movies from the repository
    }

    @GET
    @Path("/{id}") // Defines a sub-path to handle requests for a specific movie by ID
    public Response getMovieById(@PathParam("id") Long id) {
        Optional<Movie> movie = repository.findById(id);
        return movie.map(Response::ok) // If movie is found, return 200 OK with the movie data
                .orElse(Response.status(Response.Status.NOT_FOUND)) // if not found, return 404 Not Found
                .build();
    }

    @POST
    public Response createMovie(Movie movie) {
        repository.save(movie); // Saves the new movie to the repository
        // Builds a 201 Created response with the URI of the newly created movie
        return Response.created(URI.create("/movies/" + movie.getId()))
                .entity(movie) // Includes the created movie in the response body
                .build();
    }

    @PUT
    @Path("/{id}") // Defines a sub-path to handle update requests for a specific movie by ID
    public Response updateMovie(@PathParam("id") Long id, Movie movie) {
        Optional<Movie> existingMovie = repository.findById(id);
        if (existingMovie.isPresent()) {
            movie.setId(id); // Ensures the movie ID is set to the path parameter
            repository.update(movie); // Updates the existing movie in the repository
            return Response.ok(movie).build(); // Returns 200 OK with the updated movie
        } else {
            return Response.status(Response.Status.NOT_FOUND).build(); // Returns 404 Not Found if movie doesn't exist
        }
    }

    @DELETE
    @Path("/{id}") // Defines a sub-path to handle delete requests for a specific movie by ID
    public Response deleteMovie(@PathParam("id") Long id) {
        Optional<Movie> existingMovie = repository.findById(id);
        if (existingMovie.isPresent()) {
            repository.delete(id); // Deletes the movie from the repository
            return Response.noContent().build(); // Returns 204 No Content to indicate successful deletion
        } else {
            return Response.status(Response.Status.NOT_FOUND).build(); // Returns 404 Not Found if movie doesn't exist
        }
    }
}
