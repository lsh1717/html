package controller;

import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import service.BookService;
import service.MemberService;
import service.OrderService;
import vo.Book;
import vo.Orders;

@Controller
public class AdminController {

    @Autowired private MemberService memberService;
    @Autowired private BookService bookService;
    @Autowired private OrderService orderService;

    @RequestMapping("/admin/dashboard")
    public String dashboard(Model model) throws Exception {

        // === KPI (널 방어적으로 0 디폴트) ===
        int totalUsers   = safeInt(() -> memberService.countUsers());
        int totalBooks   = safeInt(() -> bookService.countBooks());
        int todayOrders  = safeInt(() -> orderService.countTodayOrders());
        int todayRevenue = safeInt(() -> orderService.sumTodayRevenue());

        // === 최근 주문 / 재고 임박 ===
        List<Orders> recentOrders = orEmpty(orderService.findRecent(5));
        List<Book>   lowStock     = orEmpty(bookService.findLowStockBooks(5, 5));

        // === 월별 매출 ===
        List<Map<String,Object>> m = orEmpty(orderService.monthlyRevenue(6));
        List<String> mLabels = m.stream()
                .map(AdminController::labelFrom)
                .collect(Collectors.toList());
        List<Integer> mData  = m.stream()
                .map(AdminController::valueFrom)
                .collect(Collectors.toList());

        // === 주문 상태 분포 ===
        List<Map<String,Object>> s = orEmpty(orderService.statusCounts());
        List<String> sLabels = s.stream().map(AdminController::labelFrom).collect(Collectors.toList());
        List<Integer> sData  = s.stream().map(AdminController::valueFrom).collect(Collectors.toList());

        // === 카테고리 분포 (BookMapper.categoryCounts: CATEGORY/TOTAL 로 옴) ===
        List<Map<String,Object>> c = orEmpty(bookService.categoryCounts());
        List<String> cLabels = c.stream().map(AdminController::labelFrom).collect(Collectors.toList());
        List<Integer> cData  = c.stream().map(AdminController::valueFrom).collect(Collectors.toList());

        // === JSON 직렬화 (JSP에서 그대로 ${}로 출력 가능) ===
        ObjectMapper om = new ObjectMapper();
        model.addAttribute("revenueLabels", om.writeValueAsString(mLabels));
        model.addAttribute("revenueData",   om.writeValueAsString(mData));
        model.addAttribute("statusLabels",  om.writeValueAsString(sLabels));
        model.addAttribute("statusData",    om.writeValueAsString(sData));
        model.addAttribute("categoryLabels",om.writeValueAsString(cLabels));
        model.addAttribute("categoryData",  om.writeValueAsString(cData));

        // === 뷰 모델 ===
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalBooks", totalBooks);
        model.addAttribute("todayOrders", todayOrders);
        model.addAttribute("todayRevenue", todayRevenue);
        model.addAttribute("recentOrders", recentOrders);
        model.addAttribute("lowStockBooks", lowStock);

        return "admin/dashboard"; // /WEB-INF/views/admin/dashboard.jsp
    }

    /* ==================== helpers ==================== */

    // label 키를 대/소문자/다른 이름까지 안전하게 뽑기
    private static String labelFrom(Map<String,Object> row) {
        if (row == null) return "";
        Object v = firstNonNull(row, "label", "LABEL", "category", "CATEGORY", "name", "NAME", "status", "STATUS");
        return String.valueOf(v != null ? v : "");
    }

    // value/count/total 등 어떤 키여도 안전하게 int 추출
    private static Integer valueFrom(Map<String,Object> row) {
        if (row == null) return 0;
        Object v = firstNonNull(row, "value", "VALUE", "count", "COUNT", "total", "TOTAL");
        if (v instanceof Number) return ((Number) v).intValue();
        if (v != null) {
            try { return Integer.parseInt(String.valueOf(v)); } catch (Exception ignore) {}
        }
        return 0;
    }

    private static Object firstNonNull(Map<String,Object> row, String... keys) {
        for (String k : keys) {
            if (row.containsKey(k) && row.get(k) != null) return row.get(k);
        }
        return null;
    }

    private static <T> List<T> orEmpty(List<T> list) {
        return (list == null) ? Collections.emptyList() : list;
    }

    private static int safeInt(IntSupplierThrows s) {
        try { return s.getAsInt(); } catch (Exception e) { return 0; }
    }

    @FunctionalInterface
    interface IntSupplierThrows { int getAsInt() throws Exception; }
}