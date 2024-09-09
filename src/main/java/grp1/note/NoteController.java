package grp1.note;

import grp1.user.User;
import grp1.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/note")
@RequiredArgsConstructor
public class NoteController {
    private static final String REDIRECT_NOTE_ERROR = "redirect:error";
    private static final String NOTE_ERROR_ATTRIBUTE = "error";
    private final NoteService noteService;
    private final UserService userService;

    @GetMapping("/list")
    public String getNoteList(Model model) {
        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            return "redirect:/login";
        }

        List<Note> userNotes = noteService.findByUserId(currentUser.getId());

        model.addAttribute("notes", userNotes);

        return "note/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Note note = new Note();
        model.addAttribute("note", note);
        return "note/create";
    }

    @PostMapping("/create")
    public String createNote(@ModelAttribute Note note, RedirectAttributes redirectAttributes) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        note.setUser(user);
        if (!saveNote(note, redirectAttributes)) {
            return REDIRECT_NOTE_ERROR;
        }

        return "redirect:/note/list";
    }

    @PostMapping("/delete")
    public String noteDelete(@RequestParam String id, HttpServletResponse response) throws IOException {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }

        Optional<Note> optionalNote = noteService.getById(id);
        if (optionalNote.isPresent()) {
            Note note = optionalNote.get();
            if (!note.getUser().equals(currentUser)) {
                return "redirect:/note/denied";
            }
            noteService.deleteById(id);
        }

        return "redirect:/note/list";
    }

    @GetMapping("/edit")
    public String noteEdit(@RequestParam String id, Model model) {
        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            return "redirect:/login";
        }

        Optional<Note> optionalNote = noteService.getById(id);

        if (optionalNote.isPresent()) {
            Note note = optionalNote.get();

            if (!note.getUser().equals(currentUser)) {
                return "redirect:/note/denied";
            }

            model.addAttribute("note", note);
            return "note/edit";
        } else {

            return "redirect:/note/list";
        }
    }

    @PostMapping("/edit")
    public String noteSave(@ModelAttribute Note note, RedirectAttributes redirectAttributes) {
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
            if (!saveNote(existingNote, redirectAttributes)) {
                return REDIRECT_NOTE_ERROR;
            }
        } else {
            return "redirect:/note/list";
        }

        return "redirect:/note/list";
    }


    @GetMapping("/share/{id}")
    public String viewSharedNote(@PathVariable("id") String noteId, Model model) {
        Optional<Note> optionalNote = noteService.getById(noteId);
        Note note = new Note();

        if (optionalNote.isPresent()) {
            note = optionalNote.get();
        }
        if (note.getAccessType().equals(AccessType.PRIVATE)) {
            return "redirect:/note/denied";
        }

        model.addAttribute("note", note);
        return "note/access-permit";
    }

    @GetMapping("/error")
    public String error(Model model, @ModelAttribute(NOTE_ERROR_ATTRIBUTE) String errorMessage) {
        model.addAttribute("error", errorMessage);
        return "note/error";
    }

    private boolean saveNote(Note note, RedirectAttributes redirectAttributes) {
        try {
            noteService.save(note);
            return true;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addAttribute(NOTE_ERROR_ATTRIBUTE, e.getMessage());
            return false;
        }
    }

    @GetMapping("/denied")
    public String getDenied(Model model) {
        return "note/access-denied";
    }
    @GetMapping("/test")
    public String getTest(Model model) {
        return "note/error";
    }

    @GetMapping("/view/{id}")
    public String viewNote(@PathVariable("id") String noteId, Model model) {
        User currentUser = userService.getCurrentUser();
        Optional<Note> optionalNote = noteService.getById(noteId);

        if (optionalNote.isPresent()) {
            Note note = optionalNote.get();
            if (note.getUser().equals(currentUser) || note.getAccessType() == AccessType.PUBLIC) {
                model.addAttribute("note", note);
                return "note/view";
            } else {
                return "redirect:/note/denied";
            }
        }
        return "redirect:/note/list";
    }

    @GetMapping("/notfound")
    public String notFound(Model model) {
        return "note/note-notfound";
    }

    @GetMapping("/found-notes/{content}")
    public String viewNotesByContent(@PathVariable("content") String content, Model model) {
        Note note = new Note();
        User currentUser = userService.getCurrentUser();
        Optional<Note> optionalNote = noteService.getNoteByContent(content);

        if (optionalNote.isPresent()) {
            note = optionalNote.get();
        }
        if (optionalNote.isEmpty()) {
            return "redirect:/note/notfound";
        }
        if (note.getAccessType().equals(AccessType.PRIVATE) && !note.getUser().equals(currentUser)) {
            return "redirect:/note/denied";
        }
        model.addAttribute("note", note);
        return "note/found-notes";
    }

    @GetMapping("/found-noteslist/{content}")
    public String viewNotesByContentList(@PathVariable("content") String content, Model model) {
        User currentUser = userService.getCurrentUser();
        List<Note> resultNote = new ArrayList<>();
        if (currentUser == null) {
            return "redirect:/login";
        }
        List<Note> userNotes = noteService.listNoteByContent(content);
        for (Note note : userNotes) {
            if (note.getUser().equals(currentUser)) {
                resultNote.add(note);
            }
        }

        model.addAttribute("notes", resultNote);

        return "note/list";

    }

}
