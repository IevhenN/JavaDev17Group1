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
        model.addAttribute("notes",notes);
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

    @PostMapping("/delete")
    public void noteDelete(@RequestParam Long id, HttpServletResponse response) throws IOException {
        noteService.deleteById(id);
        response.sendRedirect("/note/list");
    }

    @GetMapping("/edit")
    public String noteEdit(@RequestParam(required = false, defaultValue = "0") Long id
            , Model model) {

        Note note = new Note();
        if (id != null) {
            Optional<Note> optionalNote = noteService.getById(id);
            if (optionalNote.isPresent()) {
                note = optionalNote.get();
            }
        }

        model.addAttribute("note",note);
        return "note/edit";
    }

    @PostMapping("/edit")
    public String noteSave(@ModelAttribute Note note) {

        noteService.save(note);

        return "redirect:/note/list";
    }
}
