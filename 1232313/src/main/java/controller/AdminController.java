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

        // === KPI (�� ��������� 0 ����Ʈ) ===
        int totalUsers   = safeInt(() -> memberService.countUsers());
        int totalBooks   = safeInt(() -> bookService.countBooks());
        int todayOrders  = safeInt(() -> orderService.countTodayOrders());
        int todayRevenue = safeInt(() -> orderService.sumTodayRevenue());

        // === �ֱ� �ֹ� / ��� �ӹ� ===
        List<Orders> recentOrders = orEmpty(orderService.findRecent(5));
        List<Book>   lowStock     = orEmpty(bookService.findLowStockBooks(5, 5));

        // === ���� ���� ===
        List<Map<String,Object>> m = orEmpty(orderService.monthlyRevenue(6));
        List<String> mLabels = m.stream()
                .map(AdminController::labelFrom)
                .collect(Collectors.toList());
        List<Integer> mData  = m.stream()
                .map(AdminController::valueFrom)
                .collect(Collectors.toList());

        // === �ֹ� ���� ���� ===
        List<Map<String,Object>> s = orEmpty(orderService.statusCounts());
        List<String> sLabels = s.stream().map(AdminController::labelFrom).collect(Collectors.toList());
        List<Integer> sData  = s.stream().map(AdminController::valueFrom).collect(Collectors.toList());

        // === ī�װ� ���� (BookMapper.categoryCounts: CATEGORY/TOTAL �� ��) ===
        List<Map<String,Object>> c = orEmpty(bookService.categoryCounts());
        List<String> cLabels = c.stream().map(AdminController::labelFrom).collect(Collectors.toList());
        List<Integer> cData  = c.stream().map(AdminController::valueFrom).collect(Collectors.toList());

        // === JSON ����ȭ (JSP���� �״�� ${}�� ��� ����) ===
        ObjectMapper om = new ObjectMapper();
        model.addAttribute("revenueLabels", om.writeValueAsString(mLabels));
        model.addAttribute("revenueData",   om.writeValueAsString(mData));
        model.addAttribute("statusLabels",  om.writeValueAsString(sLabels));
        model.addAttribute("statusData",    om.writeValueAsString(sData));
        model.addAttribute("categoryLabels",om.writeValueAsString(cLabels));
        model.addAttribute("categoryData",  om.writeValueAsString(cData));

        // === �� �� ===
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalBooks", totalBooks);
        model.addAttribute("todayOrders", todayOrders);
        model.addAttribute("todayRevenue", todayRevenue);
        model.addAttribute("recentOrders", recentOrders);
        model.addAttribute("lowStockBooks", lowStock);

        return "admin/dashboard"; // /WEB-INF/views/admin/dashboard.jsp
    }

    /* ==================== helpers ==================== */

    // label Ű�� ��/�ҹ���/�ٸ� �̸����� �����ϰ� �̱�
    private static String labelFrom(Map<String,Object> row) {
        if (row == null) return "";
        Object v = firstNonNull(row, "label", "LABEL", "category", "CATEGORY", "name", "NAME", "status", "STATUS");
        return String.valueOf(v != null ? v : "");
    }

    // value/count/total �� � Ű���� �����ϰ� int ����
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