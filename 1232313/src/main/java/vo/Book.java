package vo;

import java.math.BigDecimal;

public class Book {
    private Long bookId;               // PK
    private String title;              // 도서 제목
    private String author;             // 저자명
    private String description;        // 상세 설명(길이 무제한)
    private BigDecimal price;          // 판매가(0 이상)
    private Long stock;                // 재고 수량(음수 불가)
    private String coverImage;         // 표지 이미지 URL

    public Book() {}

    // getters & setters
    public Long getBookId() {
        return bookId;
    }
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getStock() {
        return stock;
    }
    public void setStock(Long stock) {
        this.stock = stock;
    }

    public String getCoverImage() {
        return coverImage;
    }
    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }
}