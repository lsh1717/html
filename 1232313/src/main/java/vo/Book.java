package vo;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ���� ����Ƽ VO
 * - cover_image: DB/���� JSP���� ����ϴ� ������ũ ���̽� �ʵ�
 * - getCoverImage()/setCoverImage(): ī�� ���̽� ���ٵ� ���(�ű� JSP/���� ȣȯ)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Long bookId;          // PK
    private String title;         // ���� ����
    private String author;        // ���ڸ�
    private String description;   // �� ����
    private BigDecimal price;     // �ǸŰ�
    private Long stock;           // ��� ����

    // ������Ʈ �޸� ���� �⺻ �ʵ��(������ũ)
    private String cover_image;   // ǥ�� �̹��� URL

    // === ȣȯ �긴��(ī�� ���̽� ���� ����) ===
    public String getCoverImage() { return cover_image; }
    public void setCoverImage(String v) { this.cover_image = v; }
}