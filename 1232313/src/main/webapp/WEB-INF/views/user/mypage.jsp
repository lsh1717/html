<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="orders-wrap">
  <h2 class="section-title">ORDERED</h2>

  <div class="order-card">
    <table class="order-table">
      <colgroup>
        <col style="width:160px" />
        <col style="width:88px"  />
        <col />
        <col style="width:160px" />
        <col style="width:72px"  />
        <col style="width:140px" />
      </colgroup>

      <thead>
        <tr>
          <th>주문번호</th>
          <th>IMAGE</th>
          <th>ITEM</th>
          <th>PRICE</th>
          <th>수량</th>
          <th>배송상태</th>
        </tr>
      </thead>

      <tbody>
      <c:forEach var="o" items="${orders}">
        <c:set var="rowspan" value="${fn:length(o.items)}"/>
        <c:forEach var="it" items="${o.items}" varStatus="st">
          <tr>
            <c:if test="${st.first}">
              <td class="order-id" rowspan="${rowspan}">
                <div class="odate"><fmt:formatDate value="${o.orderDate}" pattern="yyyy. MM. dd"/></div>
                <div class="ono">#${o.orderId}</div>
              </td>
            </c:if>

            <td class="imgcell">
              <c:choose>
                <c:when test="${not empty it.book.coverImage}">
                  <img src="${it.book.coverImage}" alt="${it.book.title}"/>
                </c:when>
                <c:otherwise>
                  <img src="<c:url value='/resources/img/noimg.png'/>" alt="no image"/>
                </c:otherwise>
              </c:choose>
            </td>

            <td class="itemname">
              <span class="title">${it.book.title}</span>
            </td>

            <td class="price">
              <strong><fmt:formatNumber value="${it.lineTotal}" pattern="#,###원"/></strong>
              <div class="mini text-muted">
                (<fmt:formatNumber value="${it.price}" pattern="#,###원"/> × ${it.quantity})
              </div>
            </td>

            <td class="qty">${it.quantity}</td>

            <c:if test="${st.first}">
              <td class="ship" rowspan="${rowspan}">
                <c:set var="code" value="${o.status}"/>
                <c:choose>
                  <c:when test="${code == 'PAID'}"><span class="chip chip-paid">결제 완료</span></c:when>
                  <c:when test="${code == 'SHIPPING'}"><span class="chip chip-ship">배송중</span></c:when>
                  <c:when test="${code == 'DELIVERED'}"><span class="chip chip-done">배송완료</span></c:when>
                  <c:when test="${code == 'CANCELLED'}"><span class="chip chip-cancel">취소</span></c:when>
                  <c:otherwise><span class="chip">확인중</span></c:otherwise>
                </c:choose>
              </td>
            </c:if>
          </tr>
        </c:forEach>
        <tr class="order-sep"><td colspan="6"></td></tr>
      </c:forEach>

      <c:if test="${empty orders}">
        <tr><td colspan="6" class="empty">주문내역이 없습니다.</td></tr>
      </c:if>
      </tbody>
    </table>
  </div>
</div>

<style>
  :root{
    --ink:#111827; --muted:#6b7280; --line:#e5e7eb; --card:#ffffff;
    --slate-50:#f8fafc; --slate-600:#475569;
    --blue-50:#eff6ff; --blue-600:#2563eb;
    --green-50:#ecfdf5; --green-600:#16a34a;
    --red-50:#fef2f2; --red-600:#dc2626;
  }

  /* 카드 느낌 강화(스크린샷 톤과 맞춤) */
  .order-table{ width:100%; border-collapse:collapse; table-layout:fixed; background:#fff;
    border:1px solid var(--line); border-radius:12px; overflow:hidden;
    box-shadow:0 12px 30px rgba(16,24,40,.06);
  }

  .order-table thead th{
    padding:14px 12px; background:#f4f6fb; color:#51607a;
    font-weight:700; font-size:.95rem; letter-spacing:.02em; text-align:left;
    border-bottom:1px solid var(--line);
  }
  .order-table tbody td{
    padding:18px 12px; border-bottom:1px solid var(--line);
    vertical-align:middle; color:var(--ink);
    background:#fff;
  }

  /* 열 정렬 */
  .order-table .price{ text-align:right; }
  .order-table .qty{ text-align:center; }
  .order-table .ship{ text-align:center; font-weight:600; }

  /* 이미지 */
  .order-table .imgcell img{
    width:64px; height:64px; object-fit:cover; border-radius:10px;
    border:1px solid var(--line); display:block;
  }

  /* 제목 말줄임 */
  .order-table .itemname{ overflow:hidden; text-overflow:ellipsis; white-space:nowrap; }

  /* 주문번호 셀 */
  .order-id .odate{ color:var(--muted); margin-bottom:6px; font-size:.95rem; }
  .order-id .ono{ font-weight:700; letter-spacing:.02em; }

  /* 구분선 */
  .order-sep td{ padding:0; border:none; height:10px; background:transparent; }

  /* ✅ 배송상태 chip — Bootstrap .badge 대신 별도 클래스로 (글자색 선명) */
  .chip{
    display:inline-block; padding:6px 12px; border-radius:999px; font-weight:700;
    border:1px solid transparent; font-size:.85rem; line-height:1;
  }
  .chip-paid   { background:var(--slate-50);  color:var(--slate-600);  border-color:#e2e8f0; }
  .chip-ship   { background:var(--blue-50);   color:var(--blue-600);   border-color:#cde1ff; }
  .chip-done   { background:var(--green-50);  color:var(--green-600);  border-color:#bfead3; }
  .chip-cancel { background:var(--red-50);    color:var(--red-600);    border-color:#fecaca; }

  .page-title{
    text-align:center; letter-spacing:.28em; font-weight:800; color:#0f172a;
    margin:28px 0 18px; font-size:28px;
  }
</style>