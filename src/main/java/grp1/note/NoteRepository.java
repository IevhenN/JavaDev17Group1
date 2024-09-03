package grp1.note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, String> {

    @Query("SELECT n FROM Note n WHERE n.id = :id AND n.user.username = :username")
    Optional<Note> findByIdAndUsername(@Param("id") String id, @Param("username") String username);
}

