package com.ohgiraffers.springdata.menu.service;

import com.ohgiraffers.springdata.menu.dto.MenuDTO;
import com.ohgiraffers.springdata.menu.entity.Menu;
import com.ohgiraffers.springdata.menu.repository.CategoryRepository;
import com.ohgiraffers.springdata.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
//    private final CategoryRepository categoryRepository;

    public MenuService(MenuRepository menuRepository/*, CategoryRepository categoryRepository*/) {
        this.menuRepository = menuRepository;
        /*this.categoryRepository = categoryRepository;*/
    }

    public Menu findMenuByCode(int menuCode) {
        /* 방법 두가지*/
//        Menu menu = menuRepository.findById(menuCode).orElseThrow(IllegalArgumentException::new);
        Menu menu = menuRepository.findById(menuCode);
        return menu;
    }

    public List<Menu> findAllMenu() {

        List<Menu> menuList = menuRepository.findAll();
        return menuList;
    }

    @Transactional
    public int registMenu(Menu menu) {
        Menu result = menuRepository.save(menu);
        System.out.println(result);

        if (Objects.isNull(result)) {
            return 0;
        } else {
            return 1;
        }
    }

    public int updateMenu(Menu findmenu, Menu updaMenu) {
        if (Objects.isNull(updaMenu.getMenuName())) {
            findmenu.setMenuName(updaMenu.getMenuName());
            System.out.println("menu -> : " + findmenu.getMenuName());
        }
        Menu result = menuRepository.save(findmenu);

        if (Objects.isNull(result)) {
            return 0;
        } else {
            return 1;
        }
    }

    @Transactional
    public void deleteCode(int del) {
        menuRepository.deleteById(del);
        Menu menu = menuRepository.findById(del);
        System.out.println(menu);
    }
}
