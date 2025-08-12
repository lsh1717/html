package vo;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Long bookId;               // PK
    private String title;              // 도서 제목
    private String author;             // 저자명
    private String description;        // 상세 설명(길이 무제한)
    private BigDecimal price;          // 판매가(0 이상)
    private Long stock;                // 재고 수량(음수 불가)
    private String cover_image;         // 표지 이미지 URL

}