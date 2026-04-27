package com.bigdata.second.controller;

import com.bigdata.second.entity.Book;
import com.bigdata.second.service.BookService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
public class BookViewController {

    private final BookService service;

    public BookViewController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("books", service.getAllBooks());
        return "books/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("book", new Book());
        return "books/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("book") Book book,
                         BindingResult result,
                         RedirectAttributes flash) {
        if (result.hasErrors()) {
            return "books/form";
        }
        service.createBook(book);
        flash.addFlashAttribute("successMessage", "Kitap başarıyla eklendi.");
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", service.findById(id));
        return "books/form";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("book") Book book,
                         BindingResult result,
                         RedirectAttributes flash) {
        if (result.hasErrors()) {
            return "books/form";
        }
        service.update(id, book);
        flash.addFlashAttribute("successMessage", "Kitap başarıyla güncellendi.");
        return "redirect:/books";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        try {
            service.delete(id);
            flash.addFlashAttribute("successMessage", "Kitap başarıyla silindi.");
        } catch (Exception e) {
            flash.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/books";
    }
}
