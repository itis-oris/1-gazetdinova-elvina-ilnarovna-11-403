<#import "layout/base.ftl" as layout>
<@layout.page title="Каталог туров">

    <section class="bt-hero">
        <div class="bt-hero-content">
            <h1 class="bt-hero-title">
                Планируй свои путешествия
            </h1>
            <p class="bt-hero-subtitle">
                Выбери свой тур и живи как main character ✨
            </p>

            <div class="bt-hero-actions">
                <a href="${request.contextPath}/tours"
                   class="bt-btn bt-btn-primary">
                    Смотреть туры
                </a>
                <#if user??>
                    <a href="${request.contextPath}/profile"
                       class="bt-btn bt-btn-outline">
                        Мой профиль
                    </a>
                <#else>
                    <a href="${request.contextPath}/register"
                       class="bt-btn bt-btn-ghost">
                        Создать аккаунт
                    </a>
                </#if>
            </div>
        </div>
    </section>

    <section class="bt-section">
        <div class="bt-section-header">
            <h2 class="bt-section-title">Доступные туры</h2>
            <p class="bt-section-subtitle">
                Выбирай, куда сегодня летим — Париж? Malibu? Токио?
            </p>
        </div>

        <div class="bt-tours-grid">
            <#if tours?has_content>
                <#list tours as tour>
                    <article class="bt-tour-card">
                        <div class="bt-tour-card-top">
                            <div class="bt-tour-destination">
                                ${tour.destination?html}
                            </div>
                            <h3 class="bt-tour-title">
                                ${tour.title?html}
                            </h3>
                            <p class="bt-tour-description">
                                ${tour.description?html}
                            </p>
                        </div>
                        <div class="bt-tour-meta">
                            <span class="bt-chip">${tour.durationDays} дней</span>
                            <span class="bt-chip">от ${tour.price} ₽</span>
                            <#if tour.tags?? && tour.tags?has_content>
                                <span class="bt-chip bt-chip-soft">
                                    ${tour.tags?html}
                                </span>
                            </#if>
                        </div>

                        <div class="bt-tour-actions">
                            <#if user??>
                                <form method="post" action="${request.contextPath}/booking" class="bt-inline-form">
                                    <input type="hidden" name="tourId" value="${tour.id}">
                                    <button type="submit" class="bt-btn bt-btn-primary bt-btn-sm">
                                        Забронировать
                                    </button>
                                </form>
                            <#else>
                                <a href="${request.contextPath}/login"
                                   class="bt-btn bt-btn-primary bt-btn-sm">
                                    Войти, чтобы забронировать
                                </a>
                            </#if>
                        </div>
                    </article>
                </#list>
            <#else>
                <p class="bt-empty">
                    Пока нет туров. 🥲
                </p>
            </#if>
        </div>
    </section>

</@layout.page>
