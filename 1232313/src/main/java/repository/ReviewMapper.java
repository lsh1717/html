package repository;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import vo.Review;

@Mapper
public interface ReviewMapper {

    /* ================= 사용자 화면 ================= */

    /**
     * 도서별 리뷰 목록 (ROW_NUMBER 페이징)
     */
    @Select({
      "SELECT * FROM (",
      "  SELECT",
      "    r.review_id   AS review_id,",
      "    r.book_id     AS book_id,",
      "    r.user_id     AS user_id,",
      "    r.rating      AS rating,",
      "    r.content     AS content,",
      "    r.created_at  AS created_at,",
      "    u.login_id    AS login_id,",
      "    b.title       AS title,",
      "    ROW_NUMBER() OVER (ORDER BY r.created_at DESC, r.review_id DESC) AS rn",
      "  FROM reviews r",
      "  JOIN users u ON u.user_id = r.user_id",
      "  JOIN books b ON b.book_id = r.book_id",
      "  WHERE r.book_id = #{bookId}",
      ")",
      "WHERE rn BETWEEN #{start} AND #{end}"
    })
    @Results(id = "ReviewMap", value = {
      @Result(property = "reviewId",    column = "review_id"),
      @Result(property = "bookId",      column = "book_id"),
      @Result(property = "userId",      column = "user_id"),
      @Result(property = "rating",      column = "rating"),
      @Result(property = "content",     column = "content"),
      @Result(property = "createdAt",   column = "created_at"),
      @Result(property = "userLoginId", column = "login_id"), // 별도 매핑 필요
      @Result(property = "bookTitle",   column = "title")     // vo.Review에 bookTitle 있으면 매핑
    })
    List<Review> listByBook(@Param("bookId") Long bookId,
                            @Param("start") int start,
                            @Param("end") int end);

    @Select("SELECT COUNT(*) FROM reviews WHERE book_id = #{bookId}")
    int countByBook(@Param("bookId") Long bookId);

    @Select("SELECT NVL(AVG(rating), 0) FROM reviews WHERE book_id = #{bookId}")
    Double avgRating(@Param("bookId") Long bookId);

    @Select({
      "SELECT COUNT(*)",
      "FROM order_items oi",
      "JOIN orders o ON o.order_id = oi.order_id",
      "WHERE o.user_id = #{userId}",
      "  AND oi.book_id = #{bookId}",
      "  AND NVL(o.status,'PAID') <> 'CANCELLED'"
    })
    int hasPurchased(@Param("userId") Long userId, @Param("bookId") Long bookId);

    @Select("SELECT COUNT(*) FROM reviews WHERE user_id = #{userId} AND book_id = #{bookId}")
    int existsByUserAndBook(@Param("userId") Long userId, @Param("bookId") Long bookId);

    @Insert({
      "INSERT INTO reviews (review_id, book_id, user_id, rating, content, created_at)",
      "VALUES (SEQ_REVIEWS.NEXTVAL, #{bookId}, #{userId}, #{rating}, #{content}, SYSDATE)"
    })
    void insert(Review r);

    @Delete("DELETE FROM reviews WHERE review_id = #{reviewId}")
    void delete(@Param("reviewId") Long reviewId);


    /* ================= 관리자 화면 ================= */

    /**
     * 관리자 목록 카운트 (동적 WHERE) — keyword가 null/빈문자면 WHERE에 포함하지 않음
     */
    @SelectProvider(type = ReviewSqlProvider.class, method = "adminCount")
    int adminCount(@Param("keyword") String keyword,
                   @Param("rating") Integer rating,
                   @Param("bookId") Long bookId,
                   @Param("userId") Long userId);

    /**
     * 관리자 목록 (ROW_NUMBER 페이징 + 동적 WHERE)
     */
    @SelectProvider(type = ReviewSqlProvider.class, method = "adminList")
    @ResultMap("ReviewMap")
    List<Review> adminList(@Param("start") int start,
                           @Param("end") int end,
                           @Param("keyword") String keyword,
                           @Param("rating") Integer rating,
                           @Param("bookId") Long bookId,
                           @Param("userId") Long userId);

    /**
     * 관리자 일괄 삭제 — ids 비면 NO-OP
     */
    @DeleteProvider(type = ReviewSqlProvider.class, method = "deleteBatch")
    int deleteBatch(@Param("ids") List<Long> ids);


    /* ========== 동적 SQL Provider (어노테이션 전용) ========== */
    class ReviewSqlProvider {

        // 공통 WHERE 구문 생성
        private static void appendAdminWhere(StringBuilder sb, Map<String, Object> p) {
            sb.append(" WHERE 1=1 ");

            Integer rating = (Integer) p.get("rating");
            Long bookId    = (Long)    p.get("bookId");
            Long userId    = (Long)    p.get("userId");
            String keyword = (String)  p.get("keyword");

            if (rating != null) sb.append(" AND r.rating = #{rating} ");
            if (bookId != null) sb.append(" AND r.book_id = #{bookId} ");
            if (userId != null) sb.append(" AND r.user_id = #{userId} ");

            if (keyword != null && !keyword.trim().isEmpty()) {
                // NULL/빈문자는 아예 조건에 포함하지 않음 → ORA-17004 방지
                sb.append(" AND (");
                sb.append("      LOWER(b.title)    LIKE '%' || LOWER(#{keyword}) || '%'");
                sb.append("   OR LOWER(u.login_id) LIKE '%' || LOWER(#{keyword}) || '%'");
                sb.append("   OR LOWER(r.content)  LIKE '%' || LOWER(#{keyword}) || '%'");
                sb.append(" )");
            }
        }

        /** 카운트 쿼리 */
        public static String adminCount(final Map<String, Object> p) {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT COUNT(*) ");
            sb.append("FROM reviews r ");
            sb.append("JOIN books b ON b.book_id = r.book_id ");
            sb.append("JOIN users u ON u.user_id = r.user_id ");
            appendAdminWhere(sb, p);
            return sb.toString();
        }

        /** 목록(ROW_NUMBER 페이징) */
        public static String adminList(final Map<String, Object> p) {
            StringBuilder inner = new StringBuilder();
            inner.append("SELECT ");
            inner.append("  r.review_id  AS review_id, ");
            inner.append("  r.book_id    AS book_id, ");
            inner.append("  r.user_id    AS user_id, ");
            inner.append("  r.rating     AS rating, ");
            inner.append("  r.content    AS content, ");
            inner.append("  r.created_at AS created_at, ");
            inner.append("  u.login_id   AS login_id, ");
            inner.append("  b.title      AS title, ");
            inner.append("  ROW_NUMBER() OVER (ORDER BY r.created_at DESC, r.review_id DESC) AS rn ");
            inner.append("FROM reviews r ");
            inner.append("JOIN books b ON b.book_id = r.book_id ");
            inner.append("JOIN users u ON u.user_id = r.user_id ");
            appendAdminWhere(inner, p);

            StringBuilder outer = new StringBuilder();
            outer.append("SELECT * FROM ( ");
            outer.append(inner);
            outer.append(") WHERE rn BETWEEN #{start} AND #{end}");

            return outer.toString();
        }

        /** 일괄 삭제 (ids 비면 무해 쿼리 반환) */
        @SuppressWarnings("unchecked")
        public static String deleteBatch(final Map<String, Object> p) {
            List<Long> ids = (List<Long>) p.get("ids");
            if (ids == null || ids.isEmpty()) {
                // 아무 것도 지우지 않음
                return "SELECT 1 FROM dual WHERE 1=0";
            }
            StringJoiner sj = new StringJoiner(",", "(", ")");
            for (int i = 0; i < ids.size(); i++) {
                sj.add("#{ids[" + i + "]}");
            }
            return "DELETE FROM reviews WHERE review_id IN " + sj;
        }
    }
}