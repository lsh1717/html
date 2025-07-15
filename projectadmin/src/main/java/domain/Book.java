package domain;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Book {
    private int bookId;          // 도서 ID (기본키)
    private String title;        // 제목
    private String author;       // 저자
    private String description;  // 설명 (CLOB)
    private double price;        // 가격
    private int stock;           // 재고
    private byte[] coverImage;   // 표지 이미지 (BLOB → byte[]로 표현)
    private Date createdAt;      // 등록일
    private Date updatedAt;      // 수정일
}