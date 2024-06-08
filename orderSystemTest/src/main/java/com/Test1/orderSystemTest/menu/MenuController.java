package com.Test1.orderSystemTest.menu;

import com.Test1.orderSystemTest.menu.JdbcClientMenuRepository;
import com.Test1.orderSystemTest.menu.Menu;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders/menu")
public class MenuController {

    private final JdbcClientMenuRepository menuRepository;
    public MenuController(JdbcClientMenuRepository menuRepository){
        this.menuRepository = menuRepository;
    }

    @GetMapping("")
    List<Menu> findAll(){
        return menuRepository.findAll();
    }

//    @GetMapping("/{category}")
//    List<Menu> finByCategory(@PathVariable String category){
//        return menuRepository.findByCategory(category);
//    }

    @GetMapping("/{name}")
    Integer findItemIdByName(@PathVariable String name){
        return menuRepository.findItemIdByName(name);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public Menu addMenuItem(@RequestBody Menu menu) {
        menuRepository.addMenuItem(menu);
        return menu;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@RequestBody Menu menu, @PathVariable Integer id){
        menuRepository.update(menu,id);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteMenuItem(@PathVariable int id) {
        menuRepository.deleteMenuItemById(id);
    }

}
