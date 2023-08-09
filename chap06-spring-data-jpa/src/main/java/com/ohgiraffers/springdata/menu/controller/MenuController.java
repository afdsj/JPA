package com.ohgiraffers.springdata.menu.controller;

import com.ohgiraffers.springdata.menu.dto.MenuDTO;
import com.ohgiraffers.springdata.menu.entity.Menu;
import com.ohgiraffers.springdata.menu.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/menu") // 도메인을 의미
public class MenuController {

    private final MenuService menuService;

    /* 빈에서 서비스가 등록 될 수 있도록 설정*/
    public MenuController(MenuService menuService) {

        this.menuService = menuService;
    }
    /* Get : http 조회하는 메서드 */
    @GetMapping("/{menuCode}")
    public ResponseEntity<Object> findMenyByCode(@PathVariable int menuCode) {
        Menu menu = menuService.findMenuByCode(menuCode);

        if (Objects.isNull(menu)) {
            return ResponseEntity.status(404).body(new String("똑바로 입력해라"));
        }
        /* ok는 기본적으로 status가 생략되어 있는 상태, 200번 코드를 갖는다고 생각하기
         *  ok(menu)로 바로 값을 담아도 가능은 함*/
        return ResponseEntity.ok().body(menu);
    }

    @GetMapping("/list")
    public ResponseEntity<List<?>> findAllMenu() {
        List<Menu> menuList = menuService.findAllMenu();
        if (menuList.size() <= 0) {
            List<String> error = new ArrayList<>();
            error.add("String");
            return ResponseEntity.status(404).body(error);
        }

        return ResponseEntity.ok().body(menuList);
    }

    @PostMapping("/regist")
    public ResponseEntity<?> regist(Menu menu) {
        int result = menuService.registMenu(menu);

        return ResponseEntity.ok().body("성공");
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(Menu menu) {
        Menu findmenu = menuService.findMenuByCode(menu.getMenuCode());
        /* 조회 성공 시 : 엔티티가 존재함
         *  조회 실패 시 : 업데이트를 할 대상이 존재하지 않음 */
        if(Objects.isNull(findmenu)){
            return ResponseEntity.ok().body("데이터가 존재하지 않습니다");
        }
        int result = menuService.updateMenu(findmenu, menu);

        /* 스냅샷을 기준으로 전 : [0,0,0,0], 후 : [0,0,0,1] .save(id)
        *  -> 영속성 컨텍스트 : [0,0,0,1] -> DB에 반영됨
        * 영속성 컨텍스트에 없는 경우 영속화를 진행 후 .save() 호출 시 -> DB에 저장함 */
        if (result > 0) {
            return ResponseEntity.ok().body("수정 완료");
        } else {
            return ResponseEntity.status(400).body("실패");

        }
    }

    @DeleteMapping("/{delete}")
    public ResponseEntity<?> delete(@PathVariable int delete){
        menuService.deleteCode(delete);

        return ResponseEntity.ok().body("삭제 성공");
    }
}
