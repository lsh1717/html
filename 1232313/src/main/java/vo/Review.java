package vo;

import java.util.Date;
import lombok.Data;

@Data
public class Review {
    private Long reviewId;
    private Long bookId;
    private Long userId;
    private Integer rating;     // 1~5
    private String content;
    private Date createdAt;
    private String status;      // VISIBLE / HIDDEN

    // --- 관리자 화면 표시에만 쓰는 보조 필드 ---
    private String bookTitle;   // JOIN books.title
    private String userLoginId;  // JOIN members.login_id
}