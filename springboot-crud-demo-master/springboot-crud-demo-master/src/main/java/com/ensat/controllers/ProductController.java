package com.ensat.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ensat.entities.Product;
import com.ensat.services.EurekaService;
import com.ensat.services.ProductService;

/**
 * Product controller.
 */
@RestController
@RequestMapping("/crud")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private EurekaService eurekaService;

    /**
     * List all products along with Eureka IP address.
     *
     * @return ResponseEntity containing products and Eureka IP.
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> list() {
        Iterable<Product> products = productService.listAllProducts();
        String eurekaIp = eurekaService.getEurekaServerIp();

        // Create a map and put products and Eureka IP in it
        Map<String, Object> response = new HashMap<>();
        response.put("products", products);
        response.put("eurekaIp", eurekaIp);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public String showProduct(@PathVariable Integer id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("eurekaIp", eurekaService.getEurekaServerIp());
        return "productshow";
    }

    @PutMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("eurekaIp", eurekaService.getEurekaServerIp());
        return "productform";
    }

    @RequestMapping("/new")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("eurekaIp", eurekaService.getEurekaServerIp());
        return "productform";
    }

    @PostMapping
    public String saveProduct(Product product) {
        productService.saveProduct(product);
        return "redirect:/products/" + product.getId() + "?eurekaIp=" + eurekaService.getEurekaServerIp();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}