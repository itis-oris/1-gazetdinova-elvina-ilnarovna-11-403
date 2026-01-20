<#import "../layout/base.ftl" as layout>
<@layout.page title="Админка — туры">

    <section class="bt-section">
        <div class="bt-section-header bt-section-header-row">
            <div>
                <h1 class="bt-section-title">Управление турами</h1>
                <p class="bt-section-subtitle">
                    Здесь админ добавляет и редактирует туры, которые видят пользователи.
                </p>
            </div>
            <a href="${request.contextPath}/admin/tours/new"
               class="bt-btn bt-btn-primary">
                + Новый тур
            </a>
        </div>

        <#if tours?has_content>
            <table class="bt-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Название</th>
                    <th>Направление</th>
                    <th>Цена</th>
                    <th>Дней</th>
                    <th>Теги</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <#list tours as tour>
                    <tr>
                        <td>${tour.id}</td>
                        <td>${tour.title?html}</td>
                        <td>${tour.destination?html}</td>
                        <td>${tour.price}</td>
                        <td>${tour.durationDays}</td>
                        <td>${tour.tags?html!""}</td>
                        <td class="bt-table-actions">
                            <a href="${request.contextPath}/admin/tours/edit?id=${tour.id}"
                               class="bt-link">
                                Редактировать
                            </a>
                            <form method="post"
                                  action="${request.contextPath}/admin/tours"
                                  class="bt-inline-form">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${tour.id}">
                                <button type="submit"
                                        class="bt-link bt-link-danger"
                                        onclick="return confirm('Удалить тур «${tour.title?js_string}»?');">
                                    Удалить
                                </button>
                            </form>
                        </td>
                    </tr>
                </#list>
                </tbody>
            </table>
        <#else>
            <p class="bt-empty">
                Туров пока нет. Самое время добавить первый 💅
            </p>
        </#if>
    </section>

</@layout.page>
