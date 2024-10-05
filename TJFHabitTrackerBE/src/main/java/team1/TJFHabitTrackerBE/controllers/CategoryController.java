package team1.TJFHabitTrackerBE.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team1.TJFHabitTrackerBE.entities.Category;
import team1.TJFHabitTrackerBE.servicies.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * Recupera tutte le categorie predefinite.
     */
    @GetMapping
    public List<Category> getCategories() {
        return categoryService.getAllCategories();
    }
}