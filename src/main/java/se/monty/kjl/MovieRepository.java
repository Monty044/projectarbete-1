package se.monty.kjl;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import se.monty.kjl.Movie;

import java.util.List;
import java.util.Optional;

@Stateless
public class MovieRepository {


    @PersistenceContext(unitName = "Kju")
    private EntityManager em;

    public Movie save(Movie movie) {
        em.persist(movie);
        return movie;
    }

    public Optional<Movie> findById(Long id) {
        Movie movie = em.find(Movie.class, id);
        return movie != null ? Optional.of(movie) : Optional.empty();
    }

    public List<Movie> findAll() {
        TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m", Movie.class);
        return query.getResultList();
    }

    public Movie update(Movie movie) {
        return em.merge(movie);
    }

    public void delete(Long id) {
        findById(id).ifPresent(em::remove);
    }
}
