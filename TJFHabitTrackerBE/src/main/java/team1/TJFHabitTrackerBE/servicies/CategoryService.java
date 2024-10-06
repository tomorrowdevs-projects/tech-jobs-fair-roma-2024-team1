package team1.TJFHabitTrackerBE.servicies;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team1.TJFHabitTrackerBE.entities.Category;
import team1.TJFHabitTrackerBE.exceptions.NotFoundException;
import team1.TJFHabitTrackerBE.repositories.CategoryRepository;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Recupera tutte le categorie.
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Trova una categoria per nome.
     */
    public Category findByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Categoria non trovata: " + name));
    }
    public Category findById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria non trovata con ID: " + id));
    }

    /**
     * Inizializza categorie predefinite all'avvio dell'applicazione.
     */
    @PostConstruct
    public void initCategories() {
        List<String> predefinedCategories = List.of("Health", "Work", "Personal Development", "Fitness", "Education");
        for (String categoryName : predefinedCategories) {
            categoryRepository.findByName(categoryName)
                    .orElseGet(() -> categoryRepository.save(new Category(categoryName)));
        }
    }
}
