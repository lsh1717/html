package vo;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 도서 엔터티 VO
 * - cover_image: DB/기존 JSP에서 사용하는 스네이크 케이스 필드
 * - getCoverImage()/setCoverImage(): 카멜 케이스 접근도 허용(신규 JSP/매퍼 호환)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Long bookId;          // PK
    private String title;         // 도서 제목
    private String author;        // 저자명
    private String description;   // 상세 설명
    private BigDecimal price;     // 판매가
    private Long stock;           // 재고 수량

    // 프로젝트 메모에 맞춘 기본 필드명(스네이크)
    private String cover_image;   // 표지 이미지 URL

    // === 호환 브릿지(카멜 케이스 접근 지원) ===
    public String getCoverImage() { return cover_image; }
    public void setCoverImage(String v) { this.cover_image = v; }
}