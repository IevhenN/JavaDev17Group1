package grp1.note;

import grp1.user.User;
import grp1.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/note")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;

    @GetMapping("/list")
    public String getNoteList(Model model) {

        List<Note> notes = noteService.findAll();
        model.addAttribute("notes", notes);
        return "note/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Note note = new Note();
        model.addAttribute("note", note);
        return "note/create";
    }

    @PostMapping("/create")
    public String createNote(@ModelAttribute Note note) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        note.setUser(user);
        noteService.save(note);
        return "redirect:/note/list";
    }

//    @PostMapping("/delete")
//    public void noteDelete(@RequestParam Long id, HttpServletResponse response) throws IOException {
//        noteService.deleteById(id);
//        response.sendRedirect("/note/list");
//    }

    @GetMapping("/edit")
    public String noteEdit(@RequestParam String id, Model model) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }

        Optional<Note> optionalNote = noteService.getById(id);
        if (optionalNote.isPresent()) {
            Note note = optionalNote.get();
            model.addAttribute("note", note);
            return "note/edit";
        } else {
            return "redirect:/note/list";
        }
    }

    @PostMapping("/edit")
    public String noteSave(@ModelAttribute Note note) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }

        Optional<Note> existingNoteOptional = noteService.getById(note.getId());
        if (existingNoteOptional.isPresent()) {
            Note existingNote = existingNoteOptional.get();

            existingNote.setTitle(note.getTitle());
            existingNote.setContent(note.getContent());
            existingNote.setAccessType(note.getAccessType());

            noteService.save(existingNote);
        } else {
            return "redirect:/note/list";
        }

        return "redirect:/note/list";
    }


}
