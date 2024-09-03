package grp1.note;

import grp1.note.Note;
import grp1.note.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    public void deleteById(String id) {
        noteRepository.deleteById(id);
    }

    public void save(Note note) {
        noteRepository.save(note);
    }

    public Optional<Note> getById(String id) {
        return noteRepository.findById(id);
    }

}
