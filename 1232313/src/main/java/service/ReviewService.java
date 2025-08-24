package service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.ReviewMapper;
import vo.Review;

@Service
public class ReviewService {

    @Autowired private ReviewMapper reviewMapper;

    /* ========== 사용자 화면 ========== */

    /** 도서별 리뷰 목록 (1-base 페이지, size개) */
    public List<Review> listByBook(Long bookId, int page, int size){
        int p = Math.max(1, page);
        int s = Math.max(1, size);
        int start = (p - 1) * s + 1;
        int end   = p * s;
        return reviewMapper.listByBook(bookId, start, end);
    }

    /** 도서별 리뷰 총 개수 */
    public int countByBook(Long bookId){
        return reviewMapper.countByBook(bookId);
    }

    /** 도서별 평균 평점 (없으면 0.0) */
    public double avgRating(Long bookId){
        Double v = reviewMapper.avgRating(bookId);
        return v == null ? 0.0 : v;
        // 주의: 소수점 반올림/표시는 뷰에서 fmt:formatNumber 등으로 처리하세요.
    }

    /**
     * 별점 히스토그램 [★1..★5]
     * - 간단 구현: 최근 N개를 가져와 집계
     * - 데이터가 아주 많다면 Mapper에 집계 쿼리를 추가하는 것을 권장
     */
    public int[] ratingHistogram(Long bookId){
        int[] bins = new int[5]; // idx 0->★1, 4->★5
        // 한 번에 많이 읽고 집계 (필요시 조절)
        List<Review> chunk = reviewMapper.listByBook(bookId, 1, 10000);
        for (Review r : chunk) {
            Integer rt = r.getRating();
            if (rt != null && rt >= 1 && rt <= 5) bins[rt - 1]++;
        }
        return bins;
    }

    /** 구매 이력 체크 */
    public boolean hasPurchased(Long userId, Long bookId){
        return reviewMapper.hasPurchased(userId, bookId) > 0;
    }

    /** 이미 작성했는지 체크 */
    public boolean alreadyReviewed(Long userId, Long bookId){
        return reviewMapper.existsByUserAndBook(userId, bookId) > 0;
    }

    /** 리뷰 등록 */
    public void add(Review r){
        reviewMapper.insert(r);
    }

    /** 리뷰 삭제 (단건) */
    public void delete(Long reviewId){
        reviewMapper.delete(reviewId);
    }


    /* ========== 관리자 화면 ========== */

    /**
     * 관리자용 카운트
     * - keyword: 책제목/작성자ID/내용 통합 검색 (null이면 조건 제외)
     * - rating: 평점 정확히 일치 (null이면 조건 제외)
     * - bookId/userId: 식별자 필터 (null이면 조건 제외)
     */
    public int adminCount(String keyword, Integer rating, Long bookId, Long userId){
        return reviewMapper.adminCount(emptyToNull(keyword), rating, bookId, userId);
    }

    /**
     * 관리자용 목록
     * - ROW_NUMBER 페이징(start/end는 서비스에서 계산)
     * - 정렬: created_at DESC, review_id DESC (Mapper와 동일)
     */
    public List<Review> adminList(int page, int size,
                                  String keyword, Integer rating,
                                  Long bookId, Long userId){
        int p = Math.max(1, page);
        int s = Math.max(1, size);
        int start = (p - 1) * s + 1;
        int end   = p * s;
        return reviewMapper.adminList(start, end, emptyToNull(keyword), rating, bookId, userId);
    }

    /** 관리자 일괄 삭제 (ids 비거나 null이면 NO-OP) */
    public int adminDeleteBatch(List<Long> ids){
        if (ids == null || ids.isEmpty()) return 0;
        // 방어적으로 양수 ID만 남김
        List<Long> safe = new ArrayList<>();
        for (Long id : ids) if (id != null && id > 0) safe.add(id);
        if (safe.isEmpty()) return 0;
        return reviewMapper.deleteBatch(safe);
    }


    /* ========== util ========== */

    private String emptyToNull(String s){
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}