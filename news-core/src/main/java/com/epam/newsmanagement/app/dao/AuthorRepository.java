package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Author repository interface.
 */
public interface AuthorRepository extends JpaRepository<Author, Long>, AuthorRepositoryCustom {
    /**
     * Returns all not expired authors.
     * @return all not expired authors.
     */
    @Query("from Author where expired is null")
    List<Author> getNotExpired();

    /**
     * Checks if author exists.
     * @param authorName authorName
     * of the author to be checked.
     * @return check result.
     */
    @Query("select case when count(*) > 0 then True else False end from Author A where A.authorName = :authorName")
    boolean exists(@Param("authorName") String authorName);
}
