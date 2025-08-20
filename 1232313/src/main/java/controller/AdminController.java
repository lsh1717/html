package controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import vo.Order;

@Controller
public class AdminController {

    @RequestMapping("/admin/dashboard")
    public String dashboard(Model model) {
        // 예시 데이터
        int totalUsers = 120;
        int totalBooks = 450;
        int todayOrders = 15;
        int todayRevenue = 125000;

        List<Order> recentOrders = Arrays.asList(
            new Order(1, "user01", 32000),
            new Order(2, "user02", 48000)
        );

        String revenueLabels = "[\"3월\", \"4월\", \"5월\", \"6월\", \"7월\", \"8월\"]";
        String revenueData   = "[120000, 95000, 110000, 150000, 170000, 140000]";

        // 모델 속성
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalBooks", totalBooks);
        model.addAttribute("todayOrders", todayOrders);
        model.addAttribute("todayRevenue", todayRevenue);
        model.addAttribute("recentOrders", recentOrders);
        model.addAttribute("revenueLabels", revenueLabels);
        model.addAttribute("revenueData", revenueData);

        // ✅ contentPage에 상대경로만
        model.addAttribute("contentPage", "dashboard.jsp");

        return "admin/adminLayout"; // 레이아웃 JSP
    }
}