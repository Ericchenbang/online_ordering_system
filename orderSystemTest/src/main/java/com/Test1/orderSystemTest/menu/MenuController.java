package com.Test1.orderSystemTest.menu;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders/menu")
public class MenuController {

    private final MenuRepository menuRepository;

    public MenuController(MenuRepository menuRepository){
        this.menuRepository = menuRepository;
    }

    @GetMapping("")
    List<Menu> findAll(){
        return menuRepository.findAll();
    }

    @GetMapping("{menuId}")
    Menu findById(@PathVariable Integer menuId){
        Optional<Menu> menu = menuRepository.findById(menuId);
        if (menu.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu item doesn't exist.");
        }
        return menu.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void addMenuItem(@RequestBody Menu menu){
        menuRepository.save(menu);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("{menuId}")
    void update(@RequestBody Menu menu, @PathVariable Integer menuId){

        menuRepository.save(menu);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{menuId}")
    void deleteById(@PathVariable Integer menuId){
        menuRepository.deleteById(menuId);
    }


}
