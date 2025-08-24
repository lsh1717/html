package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import service.ReviewService;
import vo.Review;

@RestController
@RequestMapping("/admin/api/reviews")
public class AdminReviewRestController {

    @Autowired private ReviewService reviewService;

    /**
     * 관리자 리뷰 목록
     * 필터: keyword(통합검색), rating(정확히 일치), bookId, userId
     */
    @GetMapping
    public Map<String,Object> list(@RequestParam(defaultValue="1") int page,
                                   @RequestParam(defaultValue="10") int size,
                                   @RequestParam(required=false) String keyword,
                                   @RequestParam(required=false) Integer rating,
                                   @RequestParam(required=false) Long bookId,
                                   @RequestParam(required=false) Long userId) {

        int total = reviewService.adminCount(keyword, rating, bookId, userId);
        int totalPage = (int) Math.ceil(total / (double) size);
        if (page < 1) page = 1;
        if (totalPage > 0 && page > totalPage) page = totalPage;

        List<Review> items = reviewService.adminList(page, size, keyword, rating, bookId, userId);

        Map<String,Object> res = new HashMap<>();
        res.put("page", page);
        res.put("size", size);
        res.put("total", total);
        res.put("totalPage", Math.max(totalPage, 1));
        res.put("items", items);
        return res;
    }

    /** 단건 삭제 */
    @DeleteMapping("/{id}")
    public Map<String,Object> delete(@PathVariable("id") Long id){
        reviewService.delete(id);
        Map<String,Object> res = new HashMap<>();
        res.put("ok", true);
        res.put("deletedId", id);
        return res;
    }

    /**
     * 배치 삭제 (선택)
     * - 쿼리스트링: DELETE /admin/api/reviews?ids=1,2,3
     * - 또는 JSON 바디: { "ids":[1,2,3] }
     */
    @DeleteMapping
    public Map<String,Object> deleteBatch(@RequestParam(value="ids", required=false) List<Long> ids,
                                          @RequestBody(required=false) Map<String, List<Long>> body) {
        List<Long> payload = ids;
        if ((payload == null || payload.isEmpty()) && body != null) {
            payload = body.get("ids");
        }
        int deleted = reviewService.adminDeleteBatch(payload);
        Map<String,Object> res = new HashMap<>();
        res.put("deleted", deleted);
        return res;
    }
}