package domain;

import java.math.BigDecimal;
import java.util.Date;

public class Book {
    private Long   bookId;
    private String title;
    private String author;
    private String description;   // 상세보기에서만 사용
    private BigDecimal price;
    private Integer stock;        // 상세보기에서만 사용
    private String coverImage;       // 이미지 URL(문자열)

    public Book() {}

    // getters & setters
    public Long getBookId()            { return bookId; }
    public void setBookId(Long bookId){ this.bookId = bookId; }

    public String getTitle()           { return title; }
    public void setTitle(String title){ this.title = title; }

    public String getAuthor()          { return author; }
    public void setAuthor(String author){ this.author = author; }

    public String getDescription()     { return description; }
    public void setDescription(String description){ this.description = description; }

    public BigDecimal getPrice()       { return price; }
    public void setPrice(BigDecimal price){ this.price = price; }

    public Integer getStock()          { return stock; }
    public void setStock(Integer stock){ this.stock = stock; }

    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }
}
