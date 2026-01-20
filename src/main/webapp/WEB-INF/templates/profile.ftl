<#import "layout/base.ftl" as layout>
<@layout.page title="Мой профиль">

    <section class="bt-section">
        <h1 class="bt-section-title">МОЙ ПРОФИЛЬ</h1>
        <p class="bt-section-subtitle">Здесь живут твои данные и бронирования 💖</p>

<!-- тур увпешно забронирован -->
        <#if success??>
            <p class="bt-success" style="background: #d4edda; color: #155724; padding: 12px; border-radius: 8px; margin-bottom: 20px;">
                ✨ ${success}
            </p>
        </#if>

        <#if error??>
            <#if error == "already_booked">
                <p class="bt-error" style="background: #f8d7da; color: #721c24; padding: 12px; border-radius: 8px; margin-bottom: 20px;">
                    ⚠️ Вы уже забронировали этот тур! Один тур можно забронировать только один раз.
                </p>
            </#if>
        </#if>

        <!-- Информация о пользователе -->
        <div class="bt-card" style="margin-bottom: 24px;">
            <h3>👤 Личные данные</h3>
            <p><strong>Имя:</strong> ${user.firstName!""} ${user.lastName!""}</p>
            <p><strong>Email:</strong> ${user.email}</p>
            <p><strong>Роль:</strong> <#if user.role.name() == "ADMIN">🔑 Администратор<#else>👥 Пользователь</#if></p>
        </div>

        <!-- Мои бронирования -->
        <h2 class="bt-section-title" style="margin-top: 40px;">🎫 МОИ БРОНИРОВАНИЯ</h2>

        <#if bookings?? && bookings?size gt 0>
            <div class="bt-tours-grid">
                <#list bookings as item>
                    <#assign booking = item.booking>
                    <#assign tour = item.tour!"">
                    <#assign reviews = item.reviews!"">

                    <article class="bt-tour-card">
                        <#if tour != "" && tour.imageUrl?? && tour.imageUrl?has_content>
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
                            <#if tour != "">
                                <div class="bt-tour-destination">
                                    📍 ${tour.destination!"Неизвестно"}
                                </div>
                                <h3 class="bt-tour-title">
                                    ${tour.title!"Загрузка..."}
                                </h3>
                                <p class="bt-tour-description">
                                    ${(tour.description!"")!"Описание отсутствует"}
                                </p>
                            <#else>
                                <h3 class="bt-tour-title">Тур удалён</h3>
                                <p class="bt-tour-description">Информация о туре недоступна</p>
                            </#if>
                        </div>

                        <div class="bt-tour-meta" style="padding: 12px; background: #f5f5f5; border-radius: 0 0 8px 8px;">
                            <div style="display: flex; gap: 8px; margin-bottom: 12px; flex-wrap: wrap;">
                            <span class="bt-chip" style="display: inline-block; background: #e7f3ff; color: #0066cc; padding: 4px 8px; border-radius: 4px;">
                                Бронь #${booking.id}
                            </span>
                                <#if tour != "">
                                    <span class="bt-chip" style="display: inline-block; background: #fff3e0; color: #f57c00; padding: 4px 8px; border-radius: 4px;">
                                    ${tour.durationDays!0} дней
                                </span>
                                    <span class="bt-chip" style="display: inline-block; background: #f3e5f5; color: #7b1fa2; padding: 4px 8px; border-radius: 4px;">
                                    ${tour.price!0} ₽
                                </span>
                                </#if>
                            </div>

                            <div style="display: flex; gap: 8px;">
                                <button class="bt-btn bt-btn-secondary" onclick="toggleReviews('reviews-${booking.id}')" style="flex: 1; font-size: 12px;">
                                    📝 Отзывы
                                </button>
                                <#if tour != "">
                                    <button class="bt-btn bt-btn-secondary" onclick="toggleForm('form-${booking.id}')" style="flex: 1; font-size: 12px;">
                                        ✍️ Написать отзыв
                                    </button>
                                </#if>
                            </div>
                        </div>

                        <!-- Список отзывов -->
                        <div id="reviews-${booking.id}" style="display: none; padding: 12px; background: #fafafa; border-top: 1px solid #ddd; max-height: 300px; overflow-y: auto;">
                            <h4 style="margin: 0 0 12px 0; font-size: 14px;">⭐ Отзывы о туре</h4>

                            <#if reviews?? && reviews?size gt 0>
                                <#list reviews as review>
                                    <div style="background: white; padding: 10px; margin-bottom: 10px; border-radius: 4px; border-left: 3px solid #667eea;">
                                        <div style="display: flex; justify-content: space-between; margin-bottom: 6px; align-items: center;">
                                            <strong style="font-size: 12px;">👤 #${review.userId}</strong>
                                            <span style="color: #ff6d00; font-weight: bold;">
                                            <#list 1..review.rating as i>⭐</#list>
                                        </span>
                                        </div>
                                        <p style="font-size: 12px; color: #333; margin: 0; line-height: 1.4;">
                                            ${review.comment}
                                        </p>
                                    </div>
                                </#list>
                            <#else>
                                <p style="font-size: 12px; color: #999; text-align: center; margin: 0;">
                                    Отзывов нет. Будь первым! 😊
                                </p>
                            </#if>
                        </div>

                        <!-- Форма написания отзыва -->
                        <#if tour != "">
                            <div id="form-${booking.id}" style="display: none; padding: 12px; background: #f0f7ff; border-top: 1px solid #ddd;">
                                <h4 style="margin: 0 0 12px 0; font-size: 14px;">✍️ Напиши отзыв</h4>
                                <form method="POST" action="${request.contextPath}/review" style="display: flex; flex-direction: column; gap: 8px;">
                                    <input type="hidden" name="tourId" value="${tour.id}">

                                    <div>
                                        <label style="display: block; font-size: 12px; margin-bottom: 4px; font-weight: 500;">Оценка:</label>
                                        <select name="rating" style="width: 100%; padding: 6px; border: 1px solid #ddd; border-radius: 4px; font-size: 12px;">
                                            <option value="5">⭐⭐⭐⭐⭐ Отлично!</option>
                                            <option value="4">⭐⭐⭐⭐ Хорошо</option>
                                            <option value="3">⭐⭐⭐ Нормально</option>
                                            <option value="2">⭐⭐ Плохо</option>
                                            <option value="1">⭐ Ужасно</option>
                                        </select>
                                    </div>

                                    <div>
                                        <label style="display: block; font-size: 12px; margin-bottom: 4px; font-weight: 500;">Комментарий:</label>
                                        <textarea name="comment"
                                                  placeholder="Что тебе понравилось или не понравилось?"
                                                  style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; font-size: 12px; font-family: inherit; min-height: 60px; resize: vertical;"
                                                  required></textarea>
                                    </div>

                                    <div style="display: flex; gap: 8px;">
                                        <button type="submit" class="bt-btn bt-btn-primary" style="flex: 1; font-size: 12px; padding: 6px;">
                                            Отправить
                                        </button>
                                        <button type="button" class="bt-btn bt-btn-secondary" onclick="toggleForm('form-${booking.id}')" style="flex: 1; font-size: 12px; padding: 6px;">
                                            Отмена
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </#if>
                    </article>
                </#list>
            </div>
        <#else>
            <div style="text-align: center; padding: 40px; background: #f9f9f9; border-radius: 8px;">
                <p style="font-size: 18px; color: #666; margin-bottom: 20px;">
                    Пока нет бронирований. Самое время выбрать тур! 🌍
                </p>
                <a href="${request.contextPath}/tours" class="bt-btn bt-btn-primary" style="text-decoration: none; display: inline-block;">
                    Смотреть туры
                </a>
            </div>
        </#if>
    </section>

    <script>
        function toggleReviews(elementId) {
            const reviewsBlock = document.getElementById(elementId);
            if (reviewsBlock.style.display === 'none') {
                reviewsBlock.style.display = 'block';
            } else {
                reviewsBlock.style.display = 'none';
            }
        }

        function toggleForm(elementId) {
            const formBlock = document.getElementById(elementId);
            if (formBlock.style.display === 'none') {
                formBlock.style.display = 'block';
            } else {
                formBlock.style.display = 'none';
            }
        }
    </script>

</@layout.page>
