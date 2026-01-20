<#import "layout/base.ftl" as layout>
<@layout.page title="Туры">

    <section class="bt-section">
        <h1 class="bt-section-title">🌍 ТУРЫ</h1>
        <p class="bt-section-subtitle">Выбери свой следующий приключение</p>

        <#if request.session.getAttribute("error")??>
            <div style="background: #f8d7da; color: #721c24; padding: 12px; border-radius: 8px; margin-bottom: 20px;">
                ⚠️ ${request.session.getAttribute("error")?html}
            </div>
            <#assign _ = request.session.removeAttribute("error")>
        </#if>

        <div class="bt-tours-grid">
            <#list tours as tour>
                <article class="bt-tour-card">
                    <#-- Картинка тура -->
                    <#if tour.imageUrl?? && tour.imageUrl?has_content>
                        <img class="bt-tour-image"
                             src="${request.contextPath}/static/img/tours/${tour.imageUrl?html}"
                             alt="${tour.title?html}"
                             style="width: 100%; height: 200px; object-fit: cover; border-radius: 8px 8px 0 0;">
                    <#else>
                        <div style="width: 100%; height: 200px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 8px 8px 0 0; display: flex; align-items: center; justify-content: center; color: white; font-size: 48px;">
                            🌴
                        </div>
                    </#if>

                    <div class="bt-tour-card-top">
                        <div class="bt-tour-destination">
                            📍 ${tour.destination}
                        </div>
                        <h3 class="bt-tour-title">
                            ${tour.title}
                        </h3>
                        <p class="bt-tour-description">
                            ${tour.description}
                        </p>
                    </div>

                    <div class="bt-tour-meta" style="padding: 12px; background: #f5f5f5; border-radius: 0 0 8px 8px; border-top: 1px solid #ddd;">
                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px;">
                        <span style="font-weight: bold; font-size: 18px; color: #667eea;">
                            💰 ${tour.price} ₽
                        </span>
                            <span style="background: #e3f2fd; color: #1565c0; padding: 4px 8px; border-radius: 4px; font-size: 12px;">
                            ⏱️ ${tour.durationDays} дней
                        </span>
                        </div>

                        <#if tour.tags?? && tour.tags?has_content>
                            <div style="margin-bottom: 12px;">
                                <#list tour.tags?split(",") as tag>
                                    <span style="display: inline-block; background: #fff9e6; color: #ff6d00; padding: 3px 6px; border-radius: 3px; font-size: 11px; margin-right: 4px; margin-bottom: 4px;">
                                    ${tag?trim}
                                </span>
                                </#list>
                            </div>
                        </#if>

                        <div style="display: flex; gap: 8px;">
                            <#if user??>
                                <form method="POST" action="${request.contextPath}/booking" style="flex: 1;">
                                    <input type="hidden" name="tourId" value="${tour.id}">
                                    <button type="submit" class="bt-btn bt-btn-primary" style="width: 100%;">
                                        Забронировать
                                    </button>
                                </form>
                            <#else>
                                <a href="${request.contextPath}/login" class="bt-btn bt-btn-primary" style="flex: 1; text-decoration: none; display: flex; align-items: center; justify-content: center;">
                                    Войти для брони
                                </a>
                            </#if>
                            <button class="bt-btn bt-btn-secondary" onclick="toggleReviews(${tour.id})" style="white-space: nowrap;">
                                Отзывы
                            </button>
                        </div>
                    </div>

                    <#-- Блок с отзывами (скрыто по умолчанию) -->
                    <div id="reviews-${tour.id}" style="display: none; padding: 12px; background: #fafafa; border-top: 1px solid #ddd;">
                        <h4 style="margin: 0 0 12px 0;">Отзывы о туре</h4>

                        <#if user??>
                            <form method="POST" action="${request.contextPath}/review" style="margin-bottom: 12px; padding: 12px; background: white; border-radius: 6px;">
                                <input type="hidden" name="tourId" value="${tour.id}">

                                <div style="margin-bottom: 8px;">
                                    <label for="rating-${tour.id}" style="font-size: 12px; font-weight: bold;">Оценка:</label>
                                    <select id="rating-${tour.id}" name="rating" style="width: 100%; padding: 6px; border: 1px solid #ddd; border-radius: 4px;">
                                        <option value="5">⭐⭐⭐⭐⭐ Отлично (5)</option>
                                        <option value="4">⭐⭐⭐⭐ Хорошо (4)</option>
                                        <option value="3">⭐⭐⭐ Нормально (3)</option>
                                        <option value="2">⭐⭐ Плохо (2)</option>
                                        <option value="1">⭐ Очень плохо (1)</option>
                                    </select>
                                </div>

                                <div style="margin-bottom: 8px;">
                                <textarea name="comment" placeholder="Напиши свой отзыв..."
                                          style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; font-family: inherit; font-size: 12px;"
                                          rows="2"></textarea>
                                </div>

                                <button type="submit" class="bt-btn bt-btn-primary" style="font-size: 12px;">
                                    Отправить отзыв
                                </button>
                            </form>
                        <#else>
                            <p style="font-size: 12px; color: #666;">
                                <a href="${request.contextPath}/login" class="bt-link">Войди</a>, чтобы оставить отзыв
                            </p>
                        </#if>
                    </div>
                </article>
            </#list>
        </div>
    </section>

    <script>
        function toggleReviews(tourId) {
            const reviewsBlock = document.getElementById('reviews-' + tourId);
            if (reviewsBlock.style.display === 'none') {
                reviewsBlock.style.display = 'block';
            } else {
                reviewsBlock.style.display = 'none';
            }
        }
    </script>

</@layout.page>
