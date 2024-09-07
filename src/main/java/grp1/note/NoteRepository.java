package grp1.note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, String> {

    @Query("SELECT n FROM Note n WHERE n.id = :id AND n.user.username = :username")
    Optional<Note> findByIdAndUsername(@Param("id") String id, @Param("username") String username);

    @Query("from Note  n where n.title like :query or n.content like :query")
    Optional<Note> findbyContent(@Param("query") String query);


    @Query("SELECT n FROM Note n WHERE n.user.id = :userId")
    List<Note> findByUserId(@Param("userId") Long userId);

}

