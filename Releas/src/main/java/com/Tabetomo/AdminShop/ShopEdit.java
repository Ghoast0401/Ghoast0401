package com.Tabetomo.AdminShop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Tabetomo.shop.GenreGroup;
import com.Tabetomo.shop.PhotoService;
import com.Tabetomo.shop.ShopEntity;
import com.Tabetomo.shop.ShopRepository;

@Controller
public class ShopEdit {

	@Autowired
		private AdminService shopUpdate;
	@Autowired
	private ShopRepository shopRepository;
	@Autowired
	private PhotoService photoService;

	@GetMapping("/admin/shops/{id}/edit")
	public String editForm(@PathVariable Long id, Model model) {

	    ShopEntity shop = shopRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("店舗が見つかりません"));

	    model.addAttribute("shopForm", shop);

	    return "shop_edit";
	}
	@PostMapping("/admin/shops/{id}/edit")
	public String update(@PathVariable Long id,
	                     @ModelAttribute("shopForm") ShopEntity shopForm,
	                     @RequestParam(value = "photos", required = false) List<MultipartFile> photos,
	                     Model model) {

	    shopUpdate.update(id, shopForm);

	    ShopEntity shop = shopRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("店舗が見つかりません"));
	    photoService.saveAll(photos, shop);

	    model.addAttribute("genreGroups", GenreGroup.values());
	    return "redirect:/detail/" + id;
	}
}