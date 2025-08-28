package controller;

import java.text.SimpleDateFormat;
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
    @Autowired private BookService   bookService;
    @Autowired private OrderService  orderService;

    /**
     * 관리자 대시보드
     */
    @RequestMapping("/admin/dashboard")
    public String dashboard(Model model) throws Exception {

        // ===== KPI(없으면 0) =====
        int totalUsers   = safeInt(() -> memberService.countUsers());
        int totalBooks   = safeInt(() -> bookService.countBooks());
        int todayOrders  = safeInt(() -> orderService.countTodayOrders());
        int todayRevenue = safeInt(() -> orderService.sumTodayRevenue());

        // 출고 대기 = 결제완료(PAID), 배송 중 = SHIPPING
        int pendingCount  = safeInt(() -> orderService.countByStatus("PAID"));
        int shippingCount = safeInt(() -> orderService.countByStatus("SHIPPING"));

        // ===== 최근 주문 / 재고 임박 =====
        List<Orders> recentOrders = orEmpty(orderService.findRecent(5));
        List<Book>   lowStock     = orEmpty(bookService.findLowStockBooks(5, 5));

        // ===== 일자별 매출(최근 14일) =====
        // orderService.dailyRevenue(days) 는
        //   d(또는 date) = java.util.Date 또는 'YYYY-MM-DD' 문자열
        //   amount(또는 total/sum/value 등) = 매출합
        // 형태의 List<Map<String,Object>>를 반환한다고 가정
        int days = 14;
        List<Map<String,Object>> daily = orEmpty(orderService.dailyRevenue(days));
        List<String> dLabels = daily.stream().map(AdminController::labelFrom).collect(Collectors.toList());
        List<Integer> dData  = daily.stream().map(AdminController::valueFrom).collect(Collectors.toList());

        // ===== 주문 상태 통계 =====
        List<Map<String,Object>> s = orEmpty(orderService.statusCounts());
        List<String> sLabels = s.stream().map(AdminController::labelFrom).collect(Collectors.toList());
        List<Integer> sData  = s.stream().map(AdminController::valueFrom).collect(Collectors.toList());

       
        // ===== JSON 직렬화 (JSP에서 그대로 ${}로 출력) =====
        ObjectMapper om = new ObjectMapper();
        model.addAttribute("revenueLabels",  om.writeValueAsString(dLabels)); // 일자별
        model.addAttribute("revenueData",    om.writeValueAsString(dData));   // 일자별
        model.addAttribute("statusLabels",   om.writeValueAsString(sLabels));
        model.addAttribute("statusData",     om.writeValueAsString(sData));
       

        // ===== 숫자/목록 바인딩 =====
        model.addAttribute("totalUsers",    totalUsers);
        model.addAttribute("totalBooks",    totalBooks);
        model.addAttribute("todayOrders",   todayOrders);
        model.addAttribute("todayRevenue",  todayRevenue);
        model.addAttribute("recentOrders",  recentOrders);
        model.addAttribute("lowStockBooks", lowStock);

        // KPI(출고 대기/배송 중)
        model.addAttribute("pendingCount",  pendingCount);   // JSP에서 그대로 사용
        model.addAttribute("shippingCount", shippingCount);  // JSP에서 그대로 사용

        return "admin/dashboard"; // /WEB-INF/views/admin/dashboard.jsp
    }

    /* ==================== helpers ==================== */

    private static final SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * label 키 유연 매핑:
     *  - 일자(d, D, date, DATE) 또는
     *  - label/LABEL, category/CATEGORY, name/NAME, status/STATUS
     */
    private static String labelFrom(Map<String,Object> row) {
        if (row == null) return "";
        Object v = firstNonNull(row,
                "d","D","date","DATE",
                "label","LABEL",
                "category","CATEGORY",
                "name","NAME",
                "status","STATUS");
        if (v == null) return "";
        if (v instanceof Date) return DF.format((Date) v);
        return String.valueOf(v);
    }

    /**
     * 값 키 유연 매핑:
     *  - amount/AMOUNT, total_amount/TOTAL_AMOUNT, sum/SUM, revenue/REVENUE
     *  - value/VALUE, count/COUNT, total/TOTAL
     */
    private static Integer valueFrom(Map<String,Object> row) {
        if (row == null) return 0;
        Object v = firstNonNull(row,
                "amount","AMOUNT","total_amount","TOTAL_AMOUNT",
                "sum","SUM","revenue","REVENUE",
                "value","VALUE","count","COUNT","total","TOTAL");
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